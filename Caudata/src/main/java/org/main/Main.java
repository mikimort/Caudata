package org.main;

import org.sepolia.SepoliaConnection;

import java.io.IOException;

public class Main
{
    public static void main(String [] args) throws IOException
    {
        SepoliaConnection sepoliaConnection = new SepoliaConnection("https://sepolia.infura.io/v3/29863256897141bf8003236221994040");
        sepoliaConnection.getClientVersion();
        sepoliaConnection.getLatestBlock();
    }
}
