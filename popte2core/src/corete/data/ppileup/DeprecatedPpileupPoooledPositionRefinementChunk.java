package corete.data.ppileup;

import corete.data.SignatureDirection;
import corete.data.TEStrand;
import corete.data.tesignature.InsertionSignature;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by robertkofler on 9/4/15.
 */
public class DeprecatedPpileupPoooledPositionRefinementChunk {
	/*
	private final HashMap<Integer,PpileupSampleSummary> refinement;
	private final int chunkstart;
	private final int chunkend;
	private final String chromosome;
	public PpileupPoooledPositionRefinementChunk(PpileupChunk chunk)
	{
		chunkstart=chunk.getStartPosition();
		chunkend=chunk.getEndPosition();
		chromosome=chunk.getChromosome();
		refinement=chunk.getPooledTrack();
	}


	/**
	 * Iteratively scan for refined position of the given TE signature within the scanwindowsize
	 * Return the TE  signature when best signature converged to stable position
	 * @param sig
	 * @param windowsize
	 * @param scanwithinsize
	 * @return

	public InsertionSignature getIterativelyRefined(InsertionSignature sig, int windowsize, int scanwithinsize)
	{
		InsertionSignature prev=sig;
		InsertionSignature refined=getRefinedSignature(prev,windowsize,scanwithinsize);
		while(!prev.equals(refined))
		{
			prev=refined;
			refined=getRefinedSignature(prev,windowsize,scanwithinsize);
		}
		return refined;
	}


	/**
	 * Search for refined position of the given TE insertion within the scanwindowsize
	 * @param sig
	 * @param windowsize
	 * @param scanwithinsize
	 * @return

	public InsertionSignature getRefinedSignature(InsertionSignature sig, int windowsize, int scanwithinsize)
	{
		int scanstart=0;
		int scanend=0;
		if(sig.getSignatureDirection()== SignatureDirection.Forward)
		{

			scanstart=sig.position() -windowsize+1;
			scanend=sig.position();
		}
		else if(sig.getSignatureDirection()== SignatureDirection.Reverse)
		{
			scanstart=sig.position();
			scanend=sig.position()+windowsize-1;
		}
		else throw new IllegalArgumentException("Do not recognize strand "+sig.getSignatureDirection());

		if(scanstart<this.chunkstart)scanstart=chunkstart;
		if(scanend>this.chunkend)scanend=chunkend;
		return parseRefinedSignature(sig, windowsize, scanstart-scanwithinsize,scanend+scanwithinsize);
	}



	private InsertionSignature parseRefinedSignature(InsertionSignature sig, int windowsize, int scanstart, int scanend)
	{

		String family=sig.getTefamily();
		LinkedList<Integer> window=new LinkedList<Integer>();

		int lastMaxPosition=-1;
		double lastMaxSupport=0.0;
		double runningsum=0.0;

		for(int i=scanstart; i<=scanend; i++)
		{
			int tecount= this.refinement.get(i).getTEcount(family);
			runningsum+=tecount;
			window.add(tecount);
			if(window.size()>windowsize){
				int pop=window.remove(0);
				runningsum-=pop;
			}
			if(window.size()!=windowsize)continue;

			double av=runningsum/(double)windowsize;

				// larger than the mincount
			if(av>=lastMaxSupport)
			{
					// larger than the previous maximum count
				lastMaxSupport=av;
				lastMaxPosition=i;
			}
		}
		int start=lastMaxPosition-windowsize+1;
		InsertionSignature newsig=signify(start,lastMaxPosition,lastMaxSupport,sig);
		return newsig;
	}



	private InsertionSignature signify(int start,int end,double support,InsertionSignature sig)
	{
		String sampleid=sig.getPopid();
		// forward = is end
		// reverse = is start
		int position=end;
		SignatureDirection strand=sig.getSignatureDirection();
		if(strand== SignatureDirection.Reverse){
			position=start;

		}
		return new InsertionSignature(sampleid,sig.getChromosome(),strand,position,sig.getTefamily(), TEStrand.Unknown);
	}

*/
}


