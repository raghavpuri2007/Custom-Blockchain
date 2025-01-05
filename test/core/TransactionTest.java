package test.core;

import org.junit.Before;
import org.junit.Test;
import java.security.KeyPair;

import main.core.Transaction;
import main.util.CryptoUtil;
import static org.junit.Assert.*;

public class TransactionTest {
    private Transaction transaction;
    private KeyPair senderKeys;
    private KeyPair recipientKeys;

    @Before
    public void setUp() {
        senderKeys = CryptoUtil.generateKeyPair();
        recipientKeys = CryptoUtil.generateKeyPair();
        transaction = new Transaction(
                senderKeys.getPublic(),
                recipientKeys.getPublic(),
                100.0);
    }

    @Test
    public void testTransactionCreation() {
        assertNotNull("Transaction ID should not be null",
                transaction.getTransactionId());
        assertEquals("Sender should match",
                senderKeys.getPublic(), transaction.getSender());
        assertEquals("Recipient should match",
                recipientKeys.getPublic(), transaction.getRecipient());
        assertEquals("Amount should match",
                100.0, transaction.getAmount(), 0.001);
    }

    @Test
    public void testSignatureGeneration() {
        transaction.generateSignature(senderKeys.getPrivate());
        assertNotNull("Signature should not be null",
                transaction.getSignature());
    }

    @Test
    public void testValidSignature() {
        transaction.generateSignature(senderKeys.getPrivate());
        assertTrue("Signature should be valid",
                transaction.verifySignature());
    }

    @Test
    public void testInvalidSignature() {
        // Generate signature with wrong private key
        KeyPair wrongKeys = CryptoUtil.generateKeyPair();
        transaction.generateSignature(wrongKeys.getPrivate());
        assertFalse("Signature should be invalid with wrong key",
                transaction.verifySignature());
    }

    @Test
    public void testProcessTransaction() {
        transaction.generateSignature(senderKeys.getPrivate());
        assertTrue("Valid transaction should process successfully",
                transaction.processTransaction());
    }

    @Test
    public void testProcessUnsignedTransaction() {
        assertFalse("Unsigned transaction should not process",
                transaction.processTransaction());
    }

    @Test
    public void testUniqueTransactionIds() {
        Transaction transaction2 = new Transaction(
                senderKeys.getPublic(),
                recipientKeys.getPublic(),
                100.0);
        assertNotEquals("Different transactions should have unique IDs",
                transaction.getTransactionId(), transaction2.getTransactionId());
    }
}