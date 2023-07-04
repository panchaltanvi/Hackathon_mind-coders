package com.test2;

import java.io.IOException;

public class Process 
{
    void updateData(String[] args) throws IOException 
    {
        CSV_Update Test_UP = new CSV_Update();
        Test_UP.main(args);
    }
    public static void main(String[] args) throws IOException, InterruptedException 
    {
        Process main = new Process();
        while (true) 
        {
            Thread updateDataThread = new Thread(() -> 
            {
                try 
                {
                    main.updateData(args);
                } 
                catch (IOException e) 
                {
                    e.printStackTrace();
                }
            });
            updateDataThread.start();
            updateDataThread.join(); // Wait for the thread to complete
            Thread.sleep(60000);
        }
    }
}
