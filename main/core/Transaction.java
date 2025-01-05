package main.core;

import main.util.CryptoUtil;
import java.security.*;
import java.time.Instant;

public class Transaction {
    private String transactionId;
    private PublicKey sender;
    private PublicKey recipient;
    private double amount;
    private byte[] signature;
    private long timestamp;

    public Transaction(PublicKey from, PublicKey to, double amount) {
        this.sender = from;
        this.recipient = to;
        this.amount = amount;
        this.timestamp = Instant.now().getEpochSecond();
        this.transactionId = calculateHash();
    }

    private String calculateHash() {
        return CryptoUtil.sha256(
                CryptoUtil.getStringFromKey(sender) +
                        CryptoUtil.getStringFromKey(recipient) +
                        amount + timestamp);
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = CryptoUtil.getStringFromKey(sender) +
                CryptoUtil.getStringFromKey(recipient) +
                amount;
        signature = CryptoUtil.applyECDSASig(privateKey, data);
    }

    public boolean verifySignature() {
        String data = CryptoUtil.getStringFromKey(sender) +
                CryptoUtil.getStringFromKey(recipient) +
                amount;
        return CryptoUtil.verifyECDSASig(sender, data, signature);
    }

    public boolean processTransaction() {
        if (!verifySignature()) {
            System.out.println("Transaction signature failed to verify");
            return false;
        }

        // Additional transaction processing logic can be added here
        // For example, checking if sender has sufficient balance

        return true;
    }

    // Getters
    public String getTransactionId() {
        return transactionId;
    }

    public PublicKey getSender() {
        return sender;
    }

    public PublicKey getRecipient() {
        return recipient;
    }

    public double getAmount() {
        return amount;
    }

    public byte[] getSignature() {
        return signature;
    }
}