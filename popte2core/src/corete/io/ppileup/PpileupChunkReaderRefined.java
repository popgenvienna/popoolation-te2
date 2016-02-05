package corete.io.ppileup;

import corete.data.ppileup.PpileupChunk;
import corete.data.ppileup.PpileupSite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 9/1/15.
 */
public class PpileupChunkReaderRefined {
	private final IPpileupReader pr;
	private final int minwindow;
	private final int maxwindow;

	private final ArrayList<Integer> windowsizes;
	private final int chunkdistance;
	private final double mincount;
	private final HashSet<String> processedChromosomes;
	private final Logger logger;


	public PpileupChunkReaderRefined(IPpileupReader pr, double mincount, ArrayList<Integer> windowsizes, int chunkdistance, Logger logger){
		this.pr=pr;
		this.mincount=mincount;

		this.windowsizes=windowsizes;
		this.chunkdistance=chunkdistance;
		this.logger=logger;
		this.minwindow=  getMinwindow(windowsizes);
		this.maxwindow=getMaxwindow(windowsizes);
		if(chunkdistance<2*this.maxwindow)throw new IllegalArgumentException("Chunkdistance ("+this.chunkdistance +") must be larger than two times the window-size ("+this.maxwindow+")");
		this.processedChromosomes = new HashSet<String>();
	}

	private int getMaxwindow(ArrayList<Integer> windowsizes)
	{
		if(windowsizes.size()<1) throw new IllegalArgumentException("Invalid window size");
		int toret=0;

		for(int i:this.windowsizes)
		{

			if(i>toret)toret=i;
		}
		return toret;
	}


	private int getMinwindow(ArrayList<Integer> windowsizes)
	{
		if(windowsizes.size()<1) throw new IllegalArgumentException("Invalid window size");
		Integer toret=null;

		for(int i:this.windowsizes)
		{
			if(toret==null)toret=i;
			if(i<toret)toret=i;
		}
		return toret;
	}



	public PpileupChunk next() {

		// initialize the harvest

		PpileupSlidingWindow slider = new PpileupSlidingWindow(this.minwindow);

		String activeChr = null;
		boolean startHarvest = false;
		int startHarvestPosition=0;
		int endPositionLastValidWindow = 0;
		ArrayList<PpileupSite> chunk=new ArrayList<PpileupSite>();

		PpileupSite site = null;
		while ((site = this.pr.next()) != null) {
			if (activeChr == null)
			{
				activeChr = site.getChromosome();
			}
			// log if a new chromosome is processed
			if(!processedChromosomes.contains(activeChr)){ this.logger.info("Reading chromosomal chunks; Processing chromosome "+activeChr); processedChromosomes.add(activeChr);}

				if (!activeChr.equals(site.getChromosome())) {
					if (startHarvest) {
						// new chromosome with started harvest; return the chunk
						this.pr.buffer(site);
						return chunkify(chunk, startHarvestPosition - this.maxwindow, chunk.get(chunk.size() - 1).getPosition());
					} else {
						// new chromosome without started harvest; RESET
						slider = new PpileupSlidingWindow(this.minwindow);
						chunk = new ArrayList<PpileupSite>();
						endPositionLastValidWindow = 0;
						activeChr = site.getChromosome();
						// continue with normal routine
					}
				}
				chunk.add(site);
				slider.addSite(site);

				if(startHarvest)
				{

					if((site.getPosition()-endPositionLastValidWindow) > this.chunkdistance) {

							return chunkify(chunk, startHarvestPosition-this.maxwindow, endPositionLastValidWindow+this.maxwindow);
					}
					else if(site.getMaxTESupport()>=(double)this.mincount) endPositionLastValidWindow=site.getPosition();
				}
				else {
					// if harvest has not started just ignore the returned sites;
						if (slider.averageMaxTEsupport() >= (double) this.mincount) {
							startHarvest = true;
							startHarvestPosition=slider.getStartPosition();
							endPositionLastValidWindow=slider.getEndPosition();
						}
				}
			}


		if(startHarvest) return chunkify(chunk, startHarvestPosition-this.maxwindow, chunk.get(chunk.size() - 1).getPosition());
		else return null;
	}





	private PpileupChunk chunkify(ArrayList<PpileupSite> sites, int startChunk, int endChunk)
	{
		ArrayList<PpileupSite> tochunk=new ArrayList<PpileupSite>();
		for(PpileupSite p: sites){
			if(p.getPosition()<startChunk) continue;
			if(p.getPosition()>endChunk)
			{
				this.pr.buffer(p);
			}
			else{
				tochunk.add(p);
			}
		}


		PpileupChunk chunk= new PpileupChunk(tochunk);
		this.logger.fine("Read chunk on "+chunk.getChromosome()+"; start "+chunk.getStartPosition()+"; end "+chunk.getEndPosition());
		return chunk;
	}





}
