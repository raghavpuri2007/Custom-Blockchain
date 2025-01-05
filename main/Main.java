package com.blockchain;

import com.blockchain.core.Block;
import com.blockchain.core.Blockchain;
import com.blockchain.core.Transaction;
import com.blockchain.util.CryptoUtil;
import java.security.KeyPair;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Main {
    public static void main(String[] args) {
        // Setup Bouncy Castle as a Security Provider
        Security.addProvider(new BouncyCastleProvider());

        try {
            // Create the blockchain
            Blockchain blockchain = new Blockchain();

            // Generate some keypairs for testing
            KeyPair minerKeys = CryptoUtil.generateKeyPair();
            KeyPair aliceKeys = CryptoUtil.generateKeyPair();
            KeyPair bobKeys = CryptoUtil.generateKeyPair();

            System.out.println("Creating and processing transactions...\n");

            // Create some test transactions
            Transaction transaction1 = new Transaction(aliceKeys.getPublic(), bobKeys.getPublic(), 50.0);
            transaction1.generateSignature(aliceKeys.getPrivate());
            blockchain.addTransaction(transaction1);

            System.out.println("Mining first block...");
            blockchain.minePendingTransactions(minerKeys.getPublic());

            // Create more transactions
            Transaction transaction2 = new Transaction(bobKeys.getPublic(), aliceKeys.getPublic(), 30.0);
            transaction2.generateSignature(bobKeys.getPrivate());
            blockchain.addTransaction(transaction2);

            System.out.println("Mining second block...");
            blockchain.minePendingTransactions(minerKeys.getPublic());

            // Display chain information
            System.out.println("\nBlockchain status:");
            System.out.println("Is chain valid? " + blockchain.isChainValid());
            System.out.println("Chain length: " + blockchain.getChain().size());

            // Display balances
            System.out.println("\nFinal balances:");
            System.out.println("Alice's balance: " + blockchain.getBalance(aliceKeys.getPublic()));
            System.out.println("Bob's balance: " + blockchain.getBalance(bobKeys.getPublic()));
            System.out.println("Miner's balance: " + blockchain.getBalance(minerKeys.getPublic()));

            // Display detailed block information
            System.out.println("\nDetailed block information:");
            for (Block block : blockchain.getChain()) {
                System.out.println("\nBlock Hash: " + block.getHash());
                System.out.println("Previous Hash: " + block.getPreviousHash());
                System.out.println("Nonce: " + block.getNonce());
                System.out.println("Timestamp: " + block.getTimestamp());
                System.out.println("Number of transactions: " + block.getTransactions().size());
            }
        } catch (Exception e) {
            System.out.println("Error initializing blockchain: " + e.getMessage());
            e.printStackTrace();
        }
    }
}