package corete.io.ppileup;

import corete.data.ppileup.PpileupChunk;
import corete.data.ppileup.PpileupSite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 9/1/15.
 */
public class PpileupChunkReader {
	private final IPpileupReader pr;
	private final int scanwindowsize;
	private final ArrayList<Integer> windowsizes;
	private final int chunkdistance;
	private final int mincount;
	private final HashSet<String> processedChromosomes;
	private final Logger logger;


	public PpileupChunkReader(IPpileupReader pr, int mincount, ArrayList<Integer> windowsizes, int chunkdistance, Logger logger ){
		this.pr=pr;
		this.mincount=mincount;

		this.windowsizes=windowsizes;
		this.chunkdistance=chunkdistance;
		this.logger=logger;
		this.scanwindowsize=  getScanwindowsize(windowsizes);
		this.processedChromosomes = new HashSet<String>();
	}

	private int getScanwindowsize(ArrayList<Integer> windowsizes)
	{
		if(windowsizes.size()<1) throw new IllegalArgumentException("Invalid window size");
		int toret=0;
		for(int i:this.windowsizes)
		{
			if(i>toret)toret=i;
		}
		return toret;
	}



	private PpileupSite site;
	private void bufferSite(PpileupSite tobuffer)
	{
		assert(tobuffer!=null);
		this.site=tobuffer;
	}

	private PpileupSite nextSite()
	{
		if(this.site!=null)
		{
			PpileupSite toret=this.site;
			this.site=null;
			return toret;
		}
		else
		{
			return this.pr.next();
		}
	}



	public PpileupChunk next() {

		// initialize the harvest
		ArrayList<PpileupSite> toChunk = new ArrayList<PpileupSite>();
		PpileupSlidingWindow slider = new PpileupSlidingWindow(this.scanwindowsize);

		String activeChr = null;
		boolean startHarvest = false;
		int endPositionLastValidWindow = 0;

		PpileupSite site = null;
		while ((site = this.nextSite()) != null) {
			if (activeChr == null)
			{
				activeChr = site.getChromosome();
			}
			// log if a new chromosome is processed
			if(!processedChromosomes.contains(activeChr)){ this.logger.info("Reading chromosomal chunks; Processing chromosome "+activeChr); processedChromosomes.add(activeChr);}

				if (!activeChr.equals(site.getChromosome())) {
					if (startHarvest) {
						// new chromosome with started harvest; return the chunk
						this.bufferSite(site);
						return chunkify(toChunk, slider);
					} else {
						// new chromosome without started harvest; RESET
						slider = new PpileupSlidingWindow(this.scanwindowsize);
						toChunk = new ArrayList<PpileupSite>();
						endPositionLastValidWindow = 0;
						activeChr = site.getChromosome();
						// continue with normal routine
					}
				}

				// add the site
				ArrayList<PpileupSite> sites=slider.addSite(site);
				if(startHarvest)
				{
					toChunk.addAll(sites);
					if(slider.averageMaxTEsupport()>=(double)this.mincount) endPositionLastValidWindow=slider.getEndPosition();
					if((slider.getStartPosition()-endPositionLastValidWindow) > this.chunkdistance) {
							this.bufferSite(site);
							return chunkify(toChunk, null);
					}
				}
				else {
					// if harvest has not started just ignore the returned sites;
					if (slider.averageMaxTEsupport() >= (double) this.mincount) {
						startHarvest = true;
						endPositionLastValidWindow=slider.getEndPosition();
						}
				}
			}


		if(startHarvest) return chunkify(toChunk,slider);
		else return null;
	}

	private PpileupChunk chunkify(ArrayList<PpileupSite> sites, PpileupSlidingWindow window)
	{
		ArrayList<PpileupSite> tochunk=new ArrayList<PpileupSite>(sites);
		if(window!=null)tochunk.addAll(window.flushWindow());
		PpileupChunk chunk= new PpileupChunk(tochunk);
		this.logger.fine("Read chunk on "+chunk.getChromosome()+"; start "+chunk.getStartPosition()+"; end "+chunk.getEndPosition());
		return chunk;
	}





}
