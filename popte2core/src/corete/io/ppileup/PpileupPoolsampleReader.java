package corete.io.ppileup;

import corete.data.TEFamilyShortcutTranslator;
import corete.data.ppileup.PpileupPoolsampleFactory;
import corete.data.ppileup.PpileupSite;
import corete.data.stat.EssentialPpileupStats;

/**
 * Created by robertkofler on 9/3/15.
 */
public class PpileupPoolsampleReader implements IPpileupReader {
	private final PpileupReader reader;

	public PpileupPoolsampleReader(PpileupReader reader)   {
		this.reader=reader;
	}

	@Override
	public EssentialPpileupStats getEssentialPpileupStats() {
		return reader.getEssentialPpileupStats();
	}

	@Override
	public TEFamilyShortcutTranslator getTEFamilyShortcutTranslator() {
		return reader.getTEFamilyShortcutTranslator();
	}


	 @Override
	public PpileupSite next()
	{
	PpileupSite site=  reader.next();
	if(site==null) return null;
	PpileupSite pooledsite=	new PpileupPoolsampleFactory(site).getPooledsample();
	return pooledsite;
	}

}
