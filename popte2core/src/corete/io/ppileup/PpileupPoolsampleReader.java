package corete.io.ppileup;

import corete.data.TEFamilyShortcutTranslator;
import corete.data.ppileup.PpileupPoolsampleFactory;
import corete.data.ppileup.PpileupSite;
import corete.data.stat.EssentialPpileupStats;

import java.util.LinkedList;

/**
 * Created by robertkofler on 9/3/15.
 */
public class PpileupPoolsampleReader implements IPpileupReader {
	private final PpileupReader reader;

	private LinkedList<PpileupSite> buffer;

	public PpileupPoolsampleReader(PpileupReader reader)   {
		this.reader=reader;
		this.buffer=new LinkedList<PpileupSite>();
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
	public void buffer(PpileupSite toBuffer)
	{
		buffer.add(toBuffer);
	}



	 @Override
	public PpileupSite next()
	{
	if(buffer.size()>0)return this.buffer.removeFirst();

	PpileupSite site=  reader.next();
	if(site==null) return null;
	PpileupSite pooledsite=	new PpileupPoolsampleFactory(site).getPooledsample();
	return pooledsite;
	}

}
