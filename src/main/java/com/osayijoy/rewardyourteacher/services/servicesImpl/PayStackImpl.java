package com.decagon.rewardyourteacher.services.servicesImpl;

import com.decagon.rewardyourteacher.dto.PayStackPaymentRequest;
import com.decagon.rewardyourteacher.dto.PayStackResponse;
import com.decagon.rewardyourteacher.entity.User;
import com.decagon.rewardyourteacher.enums.TransactionType;
import com.decagon.rewardyourteacher.exceptions.CustomException;
import com.decagon.rewardyourteacher.services.EmailService;
import com.decagon.rewardyourteacher.services.PayStackService;
import com.decagon.rewardyourteacher.services.WalletService;
import com.decagon.rewardyourteacher.utils.AuthDetails;
import com.decagon.rewardyourteacher.utils.EmailDetails;
import com.decagon.rewardyourteacher.utils.VerifyTransactionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.HashMap;

@AllArgsConstructor
@Service
@NoArgsConstructor
public class PayStackImpl implements PayStackService {

    @Value("${secret.key}")
    private String PAY_STACK_SECRET_KEY;


    @Value("${paystack.url}")
    private String PAY_STACK_BASE_URL;

    @Value("${paystack.verification.url}")
    private String PAY_STACK_VERIFY_URL;

    private AuthDetails authDetails;

    @Autowired
    private EmailService emailService;

    @Autowired
    WalletService walletService;

//    @Autowired
//    private EmailService emailService;
    private HashMap<String, String> paymentTracker;

    @Autowired
    public PayStackImpl(AuthDetails authDetails) {
        this.authDetails = authDetails;
    }

    @Override
    public PayStackResponse initTransaction(Principal principal, PayStackPaymentRequest request) throws Exception {
        PayStackResponse initializeTransactionResponse;

        if (request.getAmount() <= 0) {
            throw new CustomException("Deposit must be greater than zero");
        }

        User user = authDetails.getAuthorizedUser(principal);

        request.setEmail(user.getEmail());
        try {
            Gson gson = new Gson();
            StringEntity postingString = new StringEntity(gson.toJson(request));
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(PAY_STACK_BASE_URL);
            post.setEntity(postingString);
            post.addHeader("Content-type", "application/json");
            post.addHeader("Authorization", "Bearer " + PAY_STACK_SECRET_KEY);
            StringBuilder result = new StringBuilder();
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }

            } else {
                throw new AuthenticationException("Error Occurred while initializing transaction");
            }
            ObjectMapper mapper = new ObjectMapper();

            initializeTransactionResponse = mapper.readValue(result.toString(), PayStackResponse.class);
        } catch (Exception ex) {
            throw new Exception("Failure initializing payStack transaction");
        }

        if (paymentTracker == null) {
            paymentTracker = new HashMap<>();
        }

        String reference = initializeTransactionResponse.getData().getReference();
        paymentTracker.put(reference, user.getEmail());

        String email = user.getEmail();
        String msgBody = "Dear " + user.getLastName() + " " + user.getFirstName() +  ", your Verification code is \n" +
                reference;
        String mailSubject = "Verification code";

        emailService.sendSimpleMail(new EmailDetails(email, msgBody, mailSubject), email);
        return initializeTransactionResponse;
    }

    @Override
    public VerifyTransactionResponse verifyPayment(String reference) {

        String email = paymentTracker.get(reference);

        if (email == null) {
            throw new CustomException("Invalid payment reference");
        }

        VerifyTransactionResponse payStackResponse;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(PAY_STACK_VERIFY_URL + reference);
            request.addHeader("Content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + PAY_STACK_SECRET_KEY);
            StringBuilder result = new StringBuilder();
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }

            } else {
                throw new CustomException("Error Occurred while connecting to PayStack URL");
            }
            ObjectMapper mapper = new ObjectMapper();

            payStackResponse = mapper.readValue(result.toString(), VerifyTransactionResponse.class);

            walletService.fundWallet(email, payStackResponse, "My new wallet",
                    "Wallet funded from payStack");

            paymentTracker.remove(reference);

        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
        return payStackResponse;
    }
}
