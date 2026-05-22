package com.example.BankingSystem.transaction;


import java.math.BigDecimal;

public class TransferRequest {

    private Long senderAccountId;
    private Long receiverAccountId;
    private BigDecimal amount;

    public TransferRequest() {
    }

    public TransferRequest(Long senderAccountId,
                           Long receiverAccountId,
                           BigDecimal amount) {

        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.amount = amount;
    }

    public Long getSenderAccountId() {
        return senderAccountId;
    }

    public Long getReceiverAccountId() {
        return receiverAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setSenderAccountId(Long senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public void setReceiverAccountId(Long receiverAccountId) {
        this.receiverAccountId = receiverAccountId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
