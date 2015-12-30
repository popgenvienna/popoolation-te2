package corete.data.tesignature;

import corete.data.TEFamilyShortcutTranslator;

import java.util.ArrayList;
import java.util.HashSet;

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


	public RefinementChunk2SignatureParser(SampleChunk2SignatureParser refineParser, ArrayList<SignatureRangeInfo> ranges, String chromosome, int start, int end, int windowsize, int valleysize, TEFamilyShortcutTranslator translator)
	{
		this.refineParser=refineParser;
		this.ranges=new ArrayList<SignatureRangeInfo>(ranges);
		this.chromosome=chromosome;
		this.start=start;
		this.end=end;
		this.windowsize=windowsize;
		this.valleysize=valleysize;
		this.translator=translator;
	}



	public ArrayList<InsertionSignature> getSignatures()
	{
		HashSet<InsertionSignature> uniqsig=new HashSet<InsertionSignature>();

		for(SignatureRangeInfo sri:this.ranges)
		{
			InsertionSignature sig=sri.getSignature();
			String shortcut =sig.getShortcut(this.translator);
			ArrayList<SignatureRangeInfo> candidates = this.refineParser.getRanges(shortcut,0,this.windowsize,this.valleysize,sri.getMinBorderScore());
			if(candidates.size()==0) throw new IllegalArgumentException("No signature for refinement found for "+sig.toString());
			for(SignatureRangeInfo cri: candidates)
			{
				if(cri.getRangeStart() <= sig.getStart() && sig.getEnd()<= cri.getRangeEnd())
				{
					uniqsig.add(cri.getSignature());
					break;
				}
			}

		}

		return new ArrayList<InsertionSignature>(uniqsig);
	}


}
