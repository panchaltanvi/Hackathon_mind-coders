package com.test3;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class IV_Calculation 
{
    public static void main(String[] args) throws CsvValidationException, IOException 
    {
        // Path to your CSV file
        String csvFilePath = "data/Mod_Output.csv";
        String outputCsvFilePath = "data/Mod_Output_IV.csv";

        String[] values;
        try (CSVReader csvReader = new CSVReader(new FileReader(csvFilePath));
        	CSVWriter csvWriter = new CSVWriter(new FileWriter(outputCsvFilePath))) { 
        {
            String[] parameters1 = csvReader.readNext(); // Read the first row containing parameter names

            String[] values1 = csvReader.readNext(); // Read the second row containing parameter values
            while ((values1 = csvReader.readNext()) != null) 
            {
                String MarketData = values1[0];
                double LTP = Double.parseDouble(values1[1]);
                double LTQ = Double.parseDouble(values1[2]);
                double TotalTradedVolume = Double.parseDouble(values1[3]);
                double BestBid = Double.parseDouble(values1[4]);
                double BestAsk = Double.parseDouble(values1[5]);
                double BestBidQty = Double.parseDouble(values1[6]);
                double BestAskQty = Double.parseDouble(values1[7]);
                double OpenInterest = Double.parseDouble(values1[8]);
                String TimeStamp = values1[9];
                int Sequence = Integer.parseInt(values1[10]);
                double PrevClosePrice = Double.parseDouble(values1[11]);
                double PrevOpenInterest = Double.parseDouble(values1[12]);

                // Additional required inputs
                double StrikePrice = BestAsk; // Assuming the best ask price is the strike price
                double RiskFreeRate = 0.05; // Assuming a fixed risk-free interest rate
                double TimeToExpiration = getTimeToExpiration(TimeStamp);

                // Option-specific data
                double CurrentPrice = LTP;
                double OptionPrice = (BestBid + BestAsk) / 2.0;

                // Calculate implied volatility
                double IV = calculateImpliedVolatility(CurrentPrice, StrikePrice, RiskFreeRate, TimeToExpiration, OptionPrice);

                System.out.println("Implied Volatility: " + IV);
                String[] outputValues = addColumn(values1, String.valueOf(IV)); // Add IV value to the row
                csvWriter.writeNext(outputValues);
            }
        }
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    private static double calculateImpliedVolatility(double CurrentPrice, double StrikePrice, double RiskFreeRate, double TimeToExpiration, double OptionPrice) 
    {
    	double IV = 1.0;
        double Accuracy = 0.0001;
        int MaxIterations = 100;
        double Guess = 0.5; // Initial guess for implied volatility

        for (int i = 0; i < MaxIterations; i++) 
        {
            double price = calculateOptionPrice(CurrentPrice, StrikePrice, RiskFreeRate, TimeToExpiration, Guess);
            double diff = price - OptionPrice;
            if (Math.abs(diff) < Accuracy) 
            {
                IV = Guess;
                break;
            }
            double vega = calculateVega(CurrentPrice, StrikePrice, RiskFreeRate, TimeToExpiration, Guess);
            Guess = Guess - diff/vega;
        }
        return IV;
    }
    private static double calculateOptionPrice(double CurrentPrice, double StrikePrice, double RiskFreeRate, double TimeToExpiration, double Volatility) 
    {
        double d1 = (Math.log(CurrentPrice/StrikePrice) + (RiskFreeRate + Math.pow(Volatility, 2)/2)*TimeToExpiration)/(Volatility*Math.sqrt(TimeToExpiration));
        double d2 = d1 - Volatility * Math.sqrt(TimeToExpiration);

        double CumulativeProbabilityD1 = cumulativeNormalDistribution(d1);
        double CumulativeProbabilityD2 = cumulativeNormalDistribution(d2);

        double callOptionPrice = CurrentPrice*CumulativeProbabilityD1 - StrikePrice*Math.exp(-RiskFreeRate*TimeToExpiration)*CumulativeProbabilityD2;
        return callOptionPrice;
    }
    private static double calculateVega(double currentPrice, double strikePrice, double riskFreeRate, double timeToExpiration, double volatility) 
    {
        double d1 = (Math.log(currentPrice/strikePrice) + (riskFreeRate + Math.pow(volatility,2)/2)*timeToExpiration)/(volatility*Math.sqrt(timeToExpiration));
        double vega = currentPrice*Math.sqrt(timeToExpiration)*cumulativeNormalDistribution(d1);
        return vega;
    }
    private static double cumulativeNormalDistribution(double x) 
    {
        double t = 1.0/(1.0 + 0.2316419*Math.abs(x));
        double d = 0.3989423*Math.exp(-x*x/2.0);
        double probability = d*t*(0.3193815 + t*(-0.3565638 + t*(1.781478 + t*(-1.821256 + t*1.330274))));
        if (x > 0) 
        {
            probability = 1 - probability;
        }
        return probability;
    }
    private static double getTimeToExpiration(String timeStamp) 
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss zzz yyyy");
        LocalDateTime expirationDateTime = LocalDateTime.parse(timeStamp, formatter);
        LocalDateTime currentDateTime = LocalDateTime.now();
        long hoursToExpiration = currentDateTime.until(expirationDateTime,ChronoUnit.HOURS);
        double timeToExpiration = hoursToExpiration/8760.0; // Assuming 1 year = 8760 hours
        return timeToExpiration;
    }
    private static String[] addColumn(String[] row, String columnValue) 
    {
        String[] newRow = new String[row.length + 1];
        System.arraycopy(row, 0, newRow, 0, row.length);
        newRow[row.length] = columnValue;
        return newRow;
    }
}




