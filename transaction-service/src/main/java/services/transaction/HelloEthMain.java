package services.transaction;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.ExecutionException;

public class HelloEthMain {

    private static String WEB3_ADDRESS = "HTTP://127.0.0.1:7545";

    private static String fromAddress = "0x2815693Df74DCDf31b11D05F6fa0243e40139097";
    private static String toAddress = "0x75D6c523107AC20351A9Bb55dCbe7e277f661318";
    private static Credentials fromCredentials = Credentials.create("45503b4549ce1bcc5fe8622370994132da1b9458bc4d795410fd0ee0d6aaceb2");

    public static void main(String[] args) {
        System.out.println("start hello eth");

        Web3j web3j = Web3j.build(new HttpService(WEB3_ADDRESS));

        // 查询 ETH 余额
        // https://www.jianshu.com/p/d6dc974d9748

        EthGetBalance ethGetBalance = null;
        try {
            ethGetBalance = web3j.ethGetBalance(
                    fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        BigInteger balance = ethGetBalance.getBalance();
        System.out.println("eth get balance is " + balance);

        // 以太坊 转账
        // https://www.jianshu.com/p/8ae984e6bafc
        EthGetTransactionCount ethGetTransactionCount = null;
        try {
            ethGetTransactionCount = web3j.ethGetTransactionCount(
                    fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce,
                Convert.toWei("18", Convert.Unit.GWEI).toBigInteger(),
                Convert.toWei("45000", Convert.Unit.WEI).toBigInteger(),
                toAddress,
                Convert.toWei("0.20210305", Convert.Unit.ETHER).toBigInteger()
        );
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, fromCredentials);
        String hexValue = Numeric.toHexString(signedMessage);

        EthSendTransaction ethSendTransaction = null;
        try {
            ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (ethSendTransaction.hasError()) {
                System.out.println("transfer error:" + ethSendTransaction.getError().getMessage());
            } else {
                String transactionHash = ethSendTransaction.getTransactionHash();
                System.out.println("Transfer transactionHash:" + transactionHash);
            }

    }
}
