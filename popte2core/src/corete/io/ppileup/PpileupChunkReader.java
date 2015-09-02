package corete.io.ppileup;

import corete.data.ppileup.PpileupChunk;
import corete.data.ppileup.PpileupSite;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 9/1/15.
 */
public class PpileupChunkReader {
	private final PpileupReader pr;
	private final int scanwindowsize;
	private final int chunkdistance;
	private final int mincount;
	private final Logger logger;


	public PpileupChunkReader(PpileupReader pr, int mincount, int scanwindowsize, int chunkdistance, Logger logger ){
		this.pr=pr;
		this.mincount=mincount;
		this.scanwindowsize=scanwindowsize;
		this.chunkdistance=chunkdistance;
		this.logger=logger;
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
			if (activeChr == null) activeChr = site.getChromosome();
			if (!activeChr.equals(site.getChromosome())) {
				this.logger.info("Scanning for TE signatures; Using chromosome "+site.getChromosome());
				if (startHarvest) {
					// new chromosome with started harvest; return the chunk
					this.bufferSite(site);
					return chunkify(toChunk,slider);
				}
				else {
					// new chromosome without started harvest; just reset
					slider=new PpileupSlidingWindow(this.scanwindowsize);
					toChunk=new ArrayList<PpileupSite>();
					endPositionLastValidWindow=0;
					activeChr=site.getChromosome();
				}

				// add the site
				ArrayList<PpileupSite> sites=slider.addSite(site);
				if(startHarvest)
				{
					toChunk.addAll(sites);
					if(slider.averageMaxTEsupport()>(double)this.mincount) endPositionLastValidWindow=slider.getEndPosition();
					else if(slider.getEndPosition()-endPositionLastValidWindow>this.chunkdistance) return chunkify(toChunk,slider);
				}
				else
				{
					// if harvest has not started just ignore the returned sites;
					if(slider.averageMaxTEsupport()> (double)this.mincount) startHarvest=true;
				}



			}



		}

		if(startHarvest) return chunkify(toChunk,slider);
		else return null;
	}

	private PpileupChunk chunkify(ArrayList<PpileupSite> sites, PpileupSlidingWindow window)
	{
		ArrayList<PpileupSite> tochunk=new ArrayList<PpileupSite>(sites);
		tochunk.addAll(window.flushWindow());
		PpileupChunk chunk= new PpileupChunk(tochunk);
		this.logger.fine("Read chunk on "+chunk.);
		return chunk;
	}





}
