package org.raports;

import org.model.BlockData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class RaportCreator
{
    public void exportToCSV(String fileName, List<BlockData> blocks)
    {
        try (PrintWriter writer = new PrintWriter(new File(fileName)))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("BlockNumber,Timestamp,Hash,TxCount\n");

            for (BlockData block : blocks)
            {
                sb.append(block.getBlockNumber()).append(",");
                long seconds = block.getTimestamp().longValue();
                String date = java.time.LocalDateTime.ofInstant(java.time.Instant.ofEpochSecond(seconds), java.time.ZoneId.systemDefault()).toString();
                sb.append(date).append(",");
                sb.append(block.getBlockHash()).append(",");
                sb.append(block.getTransactionCount()).append("\n");
            }

            writer.write(sb.toString());
            System.out.println("Raport został wygenerowany: " + fileName);
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Błąd podczas tworzenia pliku: " + e.getMessage());
        }
    }
}
