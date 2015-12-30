package corete.data.tesignature;

import corete.data.TEFamilyShortcutTranslator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 12/30/15.
 */
public class RefinementChunk2SignatureParser {
	private final ArrayList<SignatureRangeInfo> ranges;
	private final SampleChunk2SignatureParser refineParser;
	private final String chromosome;
	private final int start;
	private final int end;
	private final int windowsize;
	private final int valleysize;
	private final TEFamilyShortcutTranslator translator;
	private  Logger logger;


	public RefinementChunk2SignatureParser(SampleChunk2SignatureParser refineParser, ArrayList<SignatureRangeInfo> ranges, String chromosome, int start, int end, int windowsize, int valleysize, TEFamilyShortcutTranslator translator, Logger logger)
	{
		this.refineParser=refineParser;
		this.ranges=new ArrayList<SignatureRangeInfo>(ranges);
		this.chromosome=chromosome;
		this.start=start;
		this.end=end;
		this.windowsize=windowsize;
		this.valleysize=valleysize;
		this.translator=translator;
		this.logger=logger;
	}



	public ArrayList<InsertionSignature> getSignatures()
	{
		HashSet<InsertionSignature> uniqsig=new HashSet<InsertionSignature>();
		this.logger.info("Starting to refine positions of "+this.ranges.size()+ " signatures of TE insertions");

		int countRefined=0;
		int countNotrefined=0;
		for(SignatureRangeInfo sri:this.ranges)
		{
			InsertionSignature sig=sri.getSignature();
			String shortcut =sig.getShortcut(this.translator);
			ArrayList<SignatureRangeInfo> candidates = this.refineParser.getRanges(shortcut,0,this.windowsize,this.valleysize,sri.getAverageScore());
			if(candidates.size()==0) throw new IllegalArgumentException("No signature for refinement found for "+sig.toString()+
					"\n"+shortcut+" "+this.windowsize+" "+this.valleysize+ " " +sri.getMinBorderScore());
			boolean found=false;
			for(SignatureRangeInfo cri: candidates)
			{

				if(cri.getRangeStart() <= sig.getStart() && sig.getEnd()<= cri.getRangeEnd())
				{
					uniqsig.add(cri.getSignature());
					found=true;
					countRefined++;
					break;
				}
			}

			if(!found)
			{
				throw new IllegalArgumentException("Could not refine  "+sig.toString()+
						"\n"+shortcut+" "+this.windowsize+" "+this.valleysize+ " " +sri.getMinBorderScore());
			}

		}

		this.logger.info("Refined positions of "+countRefined+ " signatures of TE insertions");
		this.logger.info("Final number of TE insertions "+uniqsig.size());


		return new ArrayList<InsertionSignature>(uniqsig);
	}


}
