package org.sepolia;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.core.methods.response.EthBlockNumber;

import java.io.IOException;
import java.math.BigInteger;

public class SepoliaConnection
{
    private static Web3j web3j;

    public SepoliaConnection(String sepoliaUrl)
    {
        web3j = Web3j.build(new HttpService(sepoliaUrl));
    }

    public String getClientVersion()
    {

        try
        {
            Web3ClientVersion clientVersion = web3j.web3ClientVersion().send();
            return clientVersion.getWeb3ClientVersion();
        }
        catch (IOException e)
        {
            return e.getMessage();
        }
    }

    public String getLatestBlock()
    {
        try
        {
            EthBlockNumber blockNumber = web3j.ethBlockNumber().send();
            return blockNumber.getBlockNumber().toString();
        }
        catch (IOException e)
        {
            return e.getMessage();
        }
    }

    public Web3j getWeb3j()
    {
        return web3j;
    }
}