package org.access;

import org.model.BlockData;

import java.math.BigInteger;
import java.util.List;

public class BlockPoller
{
    private final BlockFetcher blockFetcher;
    private final int intervalSeconds;

    private volatile boolean running = true;

    public BlockPoller(BlockFetcher blockFetcher, int intervalSeconds)
    {
        this.blockFetcher = blockFetcher;
        this.intervalSeconds = intervalSeconds;
    }

    public void stop()
    {
        running = false;
    }

    public void start()
    {
        System.out.printf("Uruchomiono pulling (co %d s). ", intervalSeconds);
        BigInteger lastKnownBlock = null;

        while(running)
        {
            try
            {
                BigInteger latest = blockFetcher.getLatestBlockNumber();

                if(lastKnownBlock == null)
                {
                    lastKnownBlock = latest;
                    System.out.printf("Punkt startowy: blok #%s%n", latest);

                }
                else if(latest.compareTo(lastKnownBlock) > 0)
                {
                    //Są nowe bloki
                    BigInteger from = lastKnownBlock.add(BigInteger.ONE);
                    List<BlockData> newBlocks = blockFetcher.fetchBlockRange(from, latest);

                    System.out.printf("%n[ + ] %d nowy/nowych bloków: %n", newBlocks.size());

                    lastKnownBlock = latest;
                }
                else
                {
                    System.out.printf("Brak nowych bloków (ostatni: #%s)%n", latest);
                }
                Thread.sleep(intervalSeconds * 1000L);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
            catch(Exception e)
            {
                System.err.println("Błąd pollingu: " + e.getMessage());
                try
                {
                    Thread.sleep(intervalSeconds * 1000L);
                }
                catch (InterruptedException es)
                {
                    Thread.currentThread().interrupt();
                }
            }
        }
        System.out.println("Polling zatrzymany.");
    }
}
