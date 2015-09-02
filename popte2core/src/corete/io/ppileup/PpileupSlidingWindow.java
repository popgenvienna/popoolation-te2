package corete.io.ppileup;

import corete.data.ppileup.PpileupSite;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by robertkofler on 9/2/15.
 */
public class PpileupSlidingWindow {
	private final int targetSize;
	private LinkedList<PpileupSite> sites;
	private int summaxtesupport;
	public PpileupSlidingWindow(int targetSize)
	{
		this.targetSize=targetSize;
		this.sites=new LinkedList<PpileupSite>();
		summaxtesupport=0;
	}


	public ArrayList<PpileupSite> addSite(PpileupSite site)

	{

		ArrayList<PpileupSite> toret=new ArrayList<PpileupSite>();
		//      ***
		//		456
		//		6-3+1=4
		int targetStart=site.getPosition()-targetSize+1;
		sites.add(site);
		summaxtesupport+=site.getMaxTESupport_SampleSpecific();
		while(sites.size()>0 && sites.peekFirst().getPosition()<targetStart)
		{
			PpileupSite s=sites.remove(0);
			toret.add(s);
			summaxtesupport-=s.getMaxTESupport_SampleSpecific();

		}
		return toret;
	}

	public double averageMaxTEsupport()
	{
		double toret=((double)this.summaxtesupport)/((double)this.targetSize);
		return toret;
	}

	/**
	 * Return the remaining content of the window
	 * and reset state of the windo
	 * @return
	 */
	public ArrayList<PpileupSite> flushWindow()
	{
		ArrayList<PpileupSite> toret=new ArrayList<PpileupSite>(this.sites);
		this.summaxtesupport=0;
		this.sites=new LinkedList<PpileupSite>();
		return toret;

	}

	public int getStartPosition()
	{
		return this.sites.getFirst().getPosition();
	}
	public int getEndPosition()
	{
		return this.sites.getLast().getPosition();
	}






}
