package com.decagon.rewardyourteacher.services;


import com.decagon.rewardyourteacher.dto.EmailResponse;
import com.decagon.rewardyourteacher.utils.EmailDetails;

public interface EmailService {

    EmailResponse sendSimpleMail(EmailDetails details, String email);
    EmailResponse sendMailWithAttachment(EmailDetails details, String email);
}
