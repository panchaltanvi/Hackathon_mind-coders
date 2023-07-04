package com.test1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Output_To_DB 
{
    public void main(String[] args) throws FileNotFoundException 
    {
    	BufferedReader br = new BufferedReader(new FileReader("data/Output.csv"));
        Connection jdbc_crsr = null;
        try {
            // Establish a connection to the database
            jdbc_crsr=DriverManager.getConnection("jdbc:sqlite:My_DB.db");

            // Prepare the SQL statement
            PreparedStatement stmnt=jdbc_crsr.prepareStatement("INSERT INTO My_Table (Symbol, LTP, LTQ, Total_Traded_Volume, Best_Bid, Best_Ask, Best_Bid_QTY, Best_Ask_QTY, Open_Int, TM_STMP, SQNC, Prev_Close_Price, Prev_Open_Int) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            // Assuming br is a BufferedReader object used to read input data
            String line;
            while ((line=br.readLine())!=null) 
            {
                String[] table=line.split(",");
                // Set values to replace question marks in prepared statement
                stmnt.setInt(1,Integer.parseInt(table[0]));
                stmnt.setString(2,table[1]);
                stmnt.setString(3,table[2]);
                // Execute the SQL statement to insert data into the table
                stmnt.executeUpdate();
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            try 
            {
                // Close the resources
                if (jdbc_crsr!= null) 
                {
                	jdbc_crsr.close();
                }
            } 
            catch (SQLException e) 
            {
                e.printStackTrace();
            }
        }
    }
}
