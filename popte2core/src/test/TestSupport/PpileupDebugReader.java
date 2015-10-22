package test.TestSupport;

import corete.data.TEFamilyShortcutTranslator;
import corete.data.ppileup.PpileupSite;
import corete.data.stat.EssentialPpileupStats;
import corete.io.ppileup.IPpileupReader;

import java.util.LinkedList;

/**
 * Created by robertkofler on 9/4/15.
 */
public class PpileupDebugReader implements IPpileupReader {
	private final LinkedList<String> filecontent;

	public PpileupDebugReader(String toparse)
	{
		String[] sp= toparse.split("\\n");
		LinkedList<String> tmp=new LinkedList<String>();
		for(String s:sp){tmp.add(s);}
		filecontent=tmp;
	}

	@Override
	public EssentialPpileupStats getEssentialPpileupStats() {
		return null;
	}

	@Override
	public TEFamilyShortcutTranslator getTEFamilyShortcutTranslator() {
		return null;
	}

	@Override
	public PpileupSite next() {
		if(filecontent.size()<1)return null;
		return PpileupTestSupport.ppileupSiteFactory(filecontent.remove(0));
	}
}
