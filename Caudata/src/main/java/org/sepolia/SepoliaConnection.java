package org.sepolia;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.core.methods.response.EthBlockNumber;

import java.io.IOException;
import java.math.BigInteger;

public class SepoliaConnection
{
    private static Web3j web3;

    public SepoliaConnection(String sepoliaUrl)
    {
        web3 = Web3j.build(new HttpService(sepoliaUrl));
    }

    public String getClientVersion() throws IOException
    {

        Web3ClientVersion clientVersion = web3.web3ClientVersion().send();
        System.out.println("Client Version: " + clientVersion.getWeb3ClientVersion());
        return clientVersion.getWeb3ClientVersion();
    }

    public String getLatestBlock() throws IOException
    {

        EthBlockNumber blockNumber = web3.ethBlockNumber().send();
        System.out.println("Latest Block: " + blockNumber.getBlockNumber());
        return blockNumber.getBlockNumber().toString();
    }
}