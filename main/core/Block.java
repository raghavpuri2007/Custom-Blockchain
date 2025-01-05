package main.core;

import main.util.CryptoUtil;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Block {
    private String hash;
    private String previousHash;
    private List<Transaction> transactions;
    private long timestamp;
    private int nonce;
    private String merkleRoot;
    private int difficulty;

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timestamp = Instant.now().getEpochSecond();
        this.transactions = new ArrayList<>();
        this.nonce = 0;
        this.difficulty = 4; // Number of leading zeros required in hash
        this.hash = calculateHash();
    }

    public String calculateHash() {
        return CryptoUtil.sha256(
                previousHash +
                        timestamp +
                        nonce +
                        merkleRoot);
    }

    public void mineBlock() {
        merkleRoot = CryptoUtil.getMerkleRoot(transactions);
        String target = new String(new char[difficulty]).replace('\0', '0');

        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }

        System.out.println("Block mined: " + hash);
    }

    public boolean addTransaction(Transaction transaction) {
        if (transaction == null)
            return false;

        if (!previousHash.equals("0")) {
            if (!transaction.processTransaction()) {
                System.out.println("Transaction failed to process");
                return false;
            }
        }

        transactions.add(transaction);
        System.out.println("Transaction added to block");
        return true;
    }

    // Getters
    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getNonce() {
        return nonce;
    }

    // Validation methods
    public boolean isHashValid() {
        return hash.equals(calculateHash());
    }

    public boolean validateTransactions() {
        for (Transaction transaction : transactions) {
            if (!transaction.verifySignature()) {
                return false;
            }
        }
        return true;
    }
}