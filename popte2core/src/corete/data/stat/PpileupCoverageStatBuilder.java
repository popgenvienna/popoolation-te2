package corete.data.stat;

import corete.data.ppileup.PpileupSiteLightwight;
import corete.io.ppileup.IPpileupLightwightReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 8/31/15.
 */
public class PpileupCoverageStatBuilder {
	private final IPpileupLightwightReader lwr;
	private HashMap<Integer,Integer> jointAnalysis;
	private ArrayList<HashMap<Integer,Integer>> sepAnalysis;
	private int totalSites;
	private int maxcoverage=0;
	private Logger logger;

	public PpileupCoverageStatBuilder(IPpileupLightwightReader lwr, Logger logger)
	{
		    this.lwr=lwr;
		this.logger=logger;
			computeStat();
	}




	private void computeStat()
	{
		this.logger.info("Start building coverage statistics");
		HashMap<Integer,Integer> joint=new HashMap<Integer,Integer>();
		ArrayList<HashMap<Integer,Integer>> sep=new ArrayList<HashMap<Integer,Integer>>();


		PpileupSiteLightwight l=null;
		int counter=0;
		while((l=lwr.next())!=null)
		{
			if(counter==0)
			{
				for(int i=0; i<l.size(); i++) sep.add(new HashMap<Integer,Integer>());
			}

			Integer min=null;
			for(int i=0; i<l.size(); i++)
			{
				int cov=l.getCoverage(i);
				if(cov>maxcoverage)maxcoverage=cov;  // overall maximum coverage over all samples and sites
				if(min==null) min=cov;               // min coverage over all samples but per site
				if(cov<min) min=cov;
				sep.get(i).putIfAbsent(cov,0);                //set per default to zero than increment by one
				sep.get(i).put(cov, sep.get(i).get(cov)+1); // key value; key=cov, value=old+1
			}

			joint.putIfAbsent(min,0);
			joint.put(min,joint.get(min)+1);
			counter++;
		}

		this.totalSites=counter;
		this.jointAnalysis=joint;
		this.sepAnalysis=sep;
		this.logger.info("Done building coverage statistics");

	}

	public int getTotalsites() {return this.totalSites;}

	public int getSamplecount(){return this.sepAnalysis.size();}

	public int getMaxcoverage(){return this.maxcoverage;}

	public HashMap<Integer,Integer> getCoverageStatistics(int index){
		return new HashMap<Integer,Integer>(this.sepAnalysis.get(index));
	}

	public HashMap<Integer,Integer> getJointCoverageStatistics()
	{
		return new HashMap<Integer,Integer>(this.jointAnalysis);
	}



}
