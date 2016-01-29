package test.TestSupport;

import corete.data.TEFamilyShortcutTranslator;
import corete.data.ppileup.PpileupSite;
import corete.data.stat.EssentialPpileupStats;
import corete.io.Parser.PpileupHeaderParser;
import corete.io.ppileup.IPpileupReader;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by robertkofler on 9/4/15.
 */
public class PpileupDebugReader implements IPpileupReader {
	private final LinkedList<String> filecontent;
	private LinkedList<PpileupSite> buffer;
	private final PpileupHeaderParser headerParser;

	/**
	 * Quick way to provide multiple pileup sites
	 *
	 * eg.: "2L\t10\tcom\t. 10 r 10\t. 15 i 10\t . 0 t 30"
	 * eg.: "2L\t11\tcom\t. 10 r 10\t. 15 i 10\t . 0 t 30"
	 *
	 * @param toparse
	 */
	public PpileupDebugReader(String toparse)
	{
		String[] sp= toparse.split("\\n");
		LinkedList<String> tmp=new LinkedList<String>();
		ArrayList<String> header=new ArrayList<String>();
		for(String s:sp){
			if(s.startsWith("@")) header.add(s);
			else tmp.add(s);
		}
		headerParser=new PpileupHeaderParser(header);
		filecontent=tmp;
		this.buffer=new LinkedList<PpileupSite>();
	}

	@Override
	public EssentialPpileupStats getEssentialPpileupStats() {
		return null;
	}

	@Override
	public TEFamilyShortcutTranslator getTEFamilyShortcutTranslator() {
		return headerParser.getTEFamilyShortcutTranslator();
	}

	@Override
	public void buffer(PpileupSite toBuffer)
	{
		this.buffer.add(toBuffer);
	}


	@Override
	public PpileupSite next() {
		if(this.buffer.size()>1)return this.buffer.removeFirst();

		if(filecontent.size()<1)return null;
		return PpileupTestSupport.ppileupSiteFactory(filecontent.remove(0));
	}
}
