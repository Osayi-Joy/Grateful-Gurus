package com.decagon.rewardyourteacher.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class Data {
    /**
     * the amount for a transaction
     */
    private BigDecimal amount;
    /**
     * the currency for the transaction
     */
    private String currency;
    /**
     * the date the transaction occurred
     */
    private String transaction_date;
    /**
     * status of transaction
     * if the transaction is successful, status = "success"
     */
    private String status;
    /**
     * the unique reference that identifies the transaction
     */
    private String reference;
    /**
     * the type of payStack account the transaction was made, could be "test" or "live"
     */
    private String domain;
    // private String metadata;
    /**
     * details about the transaction or why it failed
     */
    private String gateway_response;
    /**
     * message for invalid request
     */
    private String message;
    /**
     * the channel the transaction was made, could be "bank" or "card"
     */
    private String channel;
    /**
     * the ip address of the user performing the transaction
     */
    private String ip_address;
    /**
     *
     */
    private String fees;
    /**
     * the plan code if this transaction was made for a plan
     */
    private String plan;

    /**
     * the date the transaction was paid
     */
    private String paid_at;


    public BigDecimal getAmount() {
        return amount;
    }
}
