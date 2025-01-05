# Java Blockchain Implementation 🔗

A simple yet functional blockchain implementation in Java that demonstrates the core concepts of blockchain technology. This project serves as both an educational resource and a foundation for understanding how blockchains work.

---

## 🌟 Overview

This blockchain implementation showcases the fundamental workings of cryptocurrency transactions and blockchain technology. The project includes essential features such as:

- **Block creation and mining**
- **Transaction processing with digital signatures**
- **Proof-of-work consensus mechanism**
- **Wallet balance tracking**
- **Chain validation**
- **Cryptographic security features**

---

## 🧱 Key Components

### Blocks  
Each block in the chain contains:  
- Transactions  
- Timestamp  
- Hash of the previous block  
- Hash of the current block  
- Nonce (used in mining)  
- Merkle root (for transaction verification)  

### Transactions  
Transactions include:  
- Sender and recipient public keys  
- Amount being transferred  
- Digital signature for authenticity  
- Transaction ID  

### Mining  
- Uses **SHA-256 proof-of-work** algorithm  
- Adjustable mining difficulty  
- Mining rewards for block creators  

### Security Features  
- **Public/private key cryptography** for secure transactions  
- **Digital signatures** for transaction verification  
- **SHA-256 hashing** for block integrity  
- **Merkle trees** for efficient transaction verification  

---

## 💻 Example Usage  

```java
// Initialize the blockchain
Blockchain blockchain = new Blockchain();

// Generate wallets (key pairs)
KeyPair aliceWallet = CryptoUtil.generateKeyPair();
KeyPair bobWallet = CryptoUtil.generateKeyPair();
KeyPair minerWallet = CryptoUtil.generateKeyPair();

// Create and sign a transaction
Transaction transaction = new Transaction(aliceWallet.getPublic(), bobWallet.getPublic(), 50.0);
transaction.generateSignature(aliceWallet.getPrivate());

// Add the transaction to the blockchain and mine pending transactions
blockchain.addTransaction(transaction);
blockchain.minePendingTransactions(minerWallet.getPublic());

// Display blockchain data
System.out.println(blockchain);
