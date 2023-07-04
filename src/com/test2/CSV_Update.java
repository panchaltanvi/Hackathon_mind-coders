package com.test2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CSV_Update 
{
    public void main(String[] args) 
    {
        String csvFilePath = "data/Output.csv";
        String modifiedCsvFilePath = "data/Mod_Output.csv";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
            String line;
            StringBuilder modifiedData = new StringBuilder();
            String[] columnNames = {"Market Data", "LTP", "LTQ", "Total Traded Volume", "Best Bid", "Best Ask", "Best Bid QTY", "Best Ask QTY", "Open Interest", "TIME STAMP", "SEQUENCE", "Prev Close Price", "Prev Open Interest"};
            String headerRow = String.join(",", columnNames);
            modifiedData.append(headerRow).append("\n");
            while ((line = reader.readLine()) != null) 
            {
            	String modifiedLine = modifyCSVLine(line);
                modifiedData.append(modifiedLine).append("\n");
                }
            reader.close();
            BufferedWriter writer = new BufferedWriter(new FileWriter(modifiedCsvFilePath));
            writer.write(modifiedData.toString());
            writer.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
	private static String modifyCSVLine(String line) 
    {
        String[] columns = line.split(",");
        String firstColumn = columns[0].substring(columns[0].indexOf("'") + 1, columns[0].lastIndexOf("'"));
        columns[0] = firstColumn;
        String secondColumn = columns[1].substring(columns[1].indexOf('=') + 1);
        columns[1] = secondColumn;
        String thirdColumn = columns[2].substring(columns[2].indexOf('=') + 1);
        columns[2] = thirdColumn;
        String fourthColumn = columns[3].substring(columns[3].indexOf('=') + 1);
        columns[3] = fourthColumn;
        String fifthColumn = columns[4].substring(columns[4].indexOf('=') + 1);
        columns[4] = fifthColumn;
        String sixthColumn = columns[5].substring(columns[5].indexOf('=') + 1);
        columns[5] = sixthColumn;
        String seventhColumn = columns[6].substring(columns[6].indexOf('=') + 1);
        columns[6] = seventhColumn;
        String eightColumn = columns[7].substring(columns[7].indexOf('=') + 1);
        columns[7] = eightColumn;
        String ninthColumn = columns[8].substring(columns[8].indexOf('=') + 1);
        columns[8] = ninthColumn;
        String tenthColumn = columns[9].substring(columns[9].indexOf('=') + 1);
        columns[9] = tenthColumn;
        String eleventhColumn = columns[10].substring(columns[10].indexOf('=') + 1);
        columns[10] = eleventhColumn;
        String twelvethColumn = columns[11].substring(columns[11].indexOf('=') + 1);
        columns[11] = twelvethColumn;
        String thirteenthColumn = columns[12].substring(columns[12].indexOf('=') + 1);
        columns[12] = thirteenthColumn.replace("}", "");
        String modifiedLine = String.join(",", columns);

        return modifiedLine;
    }
}

