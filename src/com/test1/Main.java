package com.test1;

import java.io.IOException;

public class Main 
{
    void Get_Data(String[] args) throws IOException 
    {
        MarketDataProcessor Test_MDP = new MarketDataProcessor();
        Test_MDP.main(args);
    }
    public static void main(String[] args) throws IOException, InterruptedException 
    {
        Main main = new Main();

        Thread getDataThread = new Thread(() -> 
        {
            try 
            {
                main.Get_Data(args);
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        });
        getDataThread.start();
    }           
}
