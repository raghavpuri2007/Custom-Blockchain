package main.core;

import java.util.ArrayList;
import java.util.List;
import java.security.PublicKey;

public class Blockchain {
    private List<Block> chain;
    private int difficulty;
    private List<Transaction> pendingTransactions;

    public Blockchain() {
        chain = new ArrayList<>();
        pendingTransactions = new ArrayList<>();
        difficulty = 4;
        // Create genesis block
        createGenesisBlock();
    }

    private void createGenesisBlock() {
        Block genesisBlock = new Block("0");
        genesisBlock.mineBlock();
        chain.add(genesisBlock);
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public void addBlock(Block newBlock) {
        newBlock.mineBlock();
        chain.add(newBlock);
    }

    public void addTransaction(Transaction transaction) {
        if (transaction.processTransaction()) {
            pendingTransactions.add(transaction);
            System.out.println("Transaction added to pending transactions");
        }
    }

    public void minePendingTransactions(PublicKey minerAddress) {
        Block block = new Block(getLatestBlock().getHash());

        for (Transaction transaction : pendingTransactions) {
            block.addTransaction(transaction);
        }

        block.mineBlock();
        System.out.println("Block mined!");
        chain.add(block);

        // Clear pending transactions and reward miner
        pendingTransactions = new ArrayList<>();
        // Add mining reward transaction
        addTransaction(new Transaction(null, minerAddress, 10)); // 10 coins reward
    }

    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            // Validate current block's hash
            if (!currentBlock.isHashValid()) {
                System.out.println("Invalid hash for block " + i);
                return false;
            }

            // Validate chain connection
            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                System.out.println("Chain break between blocks " + (i - 1) + " and " + i);
                return false;
            }

            // Validate transactions in block
            if (!currentBlock.validateTransactions()) {
                System.out.println("Invalid transactions in block " + i);
                return false;
            }
        }
        return true;
    }

    public double getBalance(PublicKey address) {
        double balance = 0;

        for (Block block : chain) {
            for (Transaction transaction : block.getTransactions()) {
                if (transaction.getSender() != null &&
                        transaction.getSender().equals(address)) {
                    balance -= transaction.getAmount();
                }

                if (transaction.getRecipient().equals(address)) {
                    balance += transaction.getAmount();
                }
            }
        }

        return balance;
    }

    // Getters
    public List<Block> getChain() {
        return chain;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public List<Transaction> getPendingTransactions() {
        return pendingTransactions;
    }
}