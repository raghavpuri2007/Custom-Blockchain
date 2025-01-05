package test.core;

import org.junit.Before;
import org.junit.Test;
import java.security.KeyPair;

import main.core.Blockchain;
import main.util.CryptoUtil;
import static org.junit.Assert.*;

public class BlockchainTest {
    private Blockchain blockchain;
    private KeyPair minerKeys;
    private KeyPair senderKeys;
    private KeyPair recipientKeys;

    @Before
    public void setUp() {
        blockchain = new Blockchain();
        minerKeys = CryptoUtil.generateKeyPair();
        senderKeys = CryptoUtil.generateKeyPair();
        recipientKeys = CryptoUtil.generateKeyPair();
    }

    @Test
    public void testGenesisBlockCreation() {
        assertNotNull("Blockchain should be initialized with genesis block",
                blockchain.getLatestBlock());
        assertEquals("Chain should have length 1 after initialization",
                1, blockchain.getChain().size());
    }

    @Test
    public void testAddBlock() {
        Block newBlock = new Block(blockchain.getLatestBlock().getHash());
        blockchain.addBlock(newBlock);

        assertEquals("Chain length should be 2 after adding block",
                2, blockchain.getChain().size());
        assertEquals("New block should be the latest block",
                newBlock.getHash(), blockchain.getLatestBlock().getHash());
    }

    @Test
    public void testAddTransaction() {
        Transaction transaction = new Transaction(
                senderKeys.getPublic(),
                recipientKeys.getPublic(),
                100.0);
        transaction.generateSignature(senderKeys.getPrivate());

        blockchain.addTransaction(transaction);
        assertTrue("Transaction should be in pending transactions",
                blockchain.getPendingTransactions().contains(transaction));
    }

    @Test
    public void testMinePendingTransactions() {
        Transaction transaction = new Transaction(
                senderKeys.getPublic(),
                recipientKeys.getPublic(),
                100.0);
        transaction.generateSignature(senderKeys.getPrivate());
        blockchain.addTransaction(transaction);

        blockchain.minePendingTransactions(minerKeys.getPublic());

        assertEquals("Chain should have new block after mining",
                2, blockchain.getChain().size());
        assertTrue("Pending transactions should be empty after mining",
                blockchain.getPendingTransactions().size() <= 1); // Only mining reward transaction
    }

    @Test
    public void testChainValidity() {
        // Add some blocks and transactions
        Transaction transaction = new Transaction(
                senderKeys.getPublic(),
                recipientKeys.getPublic(),
                100.0);
        transaction.generateSignature(senderKeys.getPrivate());
        blockchain.addTransaction(transaction);
        blockchain.minePendingTransactions(minerKeys.getPublic());

        assertTrue("Blockchain should be valid", blockchain.isChainValid());
    }

    @Test
    public void testGetBalance() {
        // Send 100 coins from sender to recipient
        Transaction transaction = new Transaction(
                senderKeys.getPublic(),
                recipientKeys.getPublic(),
                100.0);
        transaction.generateSignature(senderKeys.getPrivate());
        blockchain.addTransaction(transaction);
        blockchain.minePendingTransactions(minerKeys.getPublic());

        // Mine pending transactions to process mining reward
        blockchain.minePendingTransactions(minerKeys.getPublic());

        assertEquals("Recipient balance should be 100",
                100.0, blockchain.getBalance(recipientKeys.getPublic()), 0.001);
        assertEquals("Sender balance should be -100",
                -100.0, blockchain.getBalance(senderKeys.getPublic()), 0.001);
        assertEquals("Miner should have mining reward",
                10.0, blockchain.getBalance(minerKeys.getPublic()), 0.001);
    }
}