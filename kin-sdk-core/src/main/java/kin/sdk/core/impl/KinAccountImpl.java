package kin.sdk.core.impl;

import org.ethereum.geth.Account;
import org.ethereum.geth.KeyStore;

import java.math.BigDecimal;

import kin.sdk.core.Balance;
import kin.sdk.core.KinAccount;
import kin.sdk.core.TransactionId;
import kin.sdk.core.ethereum.EthClientWrapper;
import kin.sdk.core.exception.InsufficientBalanceException;
import kin.sdk.core.exception.OperationFailedException;
import kin.sdk.core.exception.PassphraseException;
import kin.sdk.core.mock.MockBalance;


/**
 * Project - Kin SDK
 * Created by Oren Zakay on 02/11/2017.
 */
public class KinAccountImpl extends AbstractKinAccount {

    private final long PROCESSING_DURATION = 3000;

    private KeyStore keyStore;
    private EthClientWrapper ethClient;
    private Account account;

    /**
     * Creates a new {@link Account}.
     *
     * @param ethClientWrapper that will be use to call to Kin smart-contract.
     * @param passphrase       that will be used to store the account private key securely.
     * @throws Exception if go-ethereum was unable to generate the account
     *                   (unable to generate new key or store the key).
     */
    public KinAccountImpl(EthClientWrapper ethClientWrapper, String passphrase) throws Exception {
        this.keyStore = ethClientWrapper.getKeyStore();
        this.account = keyStore.newAccount(passphrase);
        this.ethClient = ethClientWrapper;
    }

    /**
     * Creates a {@link KinAccount} from existing {@link Account}
     *
     * @param ethClientWrapper that will be use to call to Kin smart-contract.
     * @param account          the existing Account.
     */
    public KinAccountImpl(EthClientWrapper ethClientWrapper, Account account) {
        this.keyStore = ethClientWrapper.getKeyStore();
        this.account = account;
        this.ethClient = ethClientWrapper;
    }

    @Override
    public String getPublicAddress() {
        return account.getAddress().getHex();
    }

    @Override
    public String getPrivateKey(String passphrase) throws PassphraseException {
        return "0xMock01ForPrivateKey";
    }

    @Override
    public TransactionId sendTransactionSync(String publicAddress, String passphrase, BigDecimal amount)
            throws InsufficientBalanceException, OperationFailedException, PassphraseException {
        return ethClient.sendTransaction(account, publicAddress, passphrase, amount);
    }

    @Override
    public Balance getBalanceSync() throws OperationFailedException {
        try {
            Thread.sleep(PROCESSING_DURATION);
            return ethClient.getBalance();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new OperationFailedException("Failed - could not get balance");
        }
    }

    @Override
    public Balance getPendingBalanceSync() throws OperationFailedException {
        try {
            Thread.sleep(PROCESSING_DURATION);
            return new MockBalance();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new OperationFailedException("Failed - could not get pending balance");
        }
    }
}