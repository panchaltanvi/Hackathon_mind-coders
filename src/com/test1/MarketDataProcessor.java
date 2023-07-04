package com.test1;

import java.io.IOException;

import hackathon.player.Main;

public class MarketDataProcessor 
{
    public void main(String[] args) throws IOException 
    {
        System.setProperty("debug", "true");
        System.setProperty("speed", "1.0");

        String[] newArgs = { "data/dataset.csv", "9011" };

        // Call the main method to capture the console output
        Main.main(newArgs);
    }
}

