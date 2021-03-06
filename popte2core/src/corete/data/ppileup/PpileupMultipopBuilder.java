package corete.data.ppileup;

import corete.data.TEFamilyShortcutTranslator;
import corete.data.hier.TEHierarchy;
import corete.data.stat.EssentialPpileupStats;
import corete.io.ISamPairReader;
import corete.io.ppileup.PpileupWriter;
import corete.io.SamPairReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 8/21/15.
 */
public class PpileupMultipopBuilder {
	private final TEFamilyShortcutTranslator tetranslator;
	//private final ArrayList<String> inputFileNames;
	private final ArrayList<String> refChrSorting;
	private final HashMap<String,Integer> lastPositions;
	private final TEHierarchy hier;
	private final EssentialPpileupStats estats;
	private final PpileupWriter writer;
	private final Logger logger;
	private final int maxWorkDist;
	private final boolean extendClipped;
	private final int srmd;

	private ArrayList<PpileupBuilder> builders;

	public PpileupMultipopBuilder(TEFamilyShortcutTranslator tetranslator, EssentialPpileupStats estats, int srmd, ArrayList<ISamPairReader> sprs,
								  ArrayList<String> refChrSorting, HashMap<String,Integer> lastPositions, TEHierarchy hier, boolean extendClipped,
							 		PpileupWriter writer, Logger logger)
	{
		this.tetranslator=tetranslator;
		this.estats=estats;

		this.refChrSorting=refChrSorting;
		this.lastPositions=lastPositions;
		this.hier=hier;
		this.srmd=srmd;

		this.writer=writer;
		this.logger=logger;

		this.extendClipped=extendClipped;

		ArrayList<PpileupBuilder> tmpBuilders=new ArrayList<PpileupBuilder>();
		int maxdist=0;
		for(int i=0; i<sprs.size(); i++)
		{

			int workDist=this.estats.getInnerDistance(i);
			if(workDist>maxdist)maxdist=workDist;
			ISamPairReader spr=sprs.get(i);
			PpileupBuilder build=new PpileupBuilder(estats.getMinMapQual(),workDist,srmd,spr,tetranslator,logger);
			tmpBuilders.add(build);
		}
		this.builders=tmpBuilders;
		this.maxWorkDist=maxdist;

		}





		public void buildPpileup() {


			for(String chr: this.refChrSorting)
			{
				this.logger.info("Building ppileup for chromosome "+chr);
				spool2Chromosome(chr);       // spool all samples to the same reference chromosome
				for(int pos=1;pos<=this.lastPositions.get(chr)+maxWorkDist;pos++)
				{
					//spool all samples to the same position
					spool2Position(pos);

					// write the position
					ArrayList<String> pps=new ArrayList<String>();
					boolean allNull=true;
					for(PpileupBuilder b:this.builders)
					{
						String site=b.getSite(pos);
						if(site!=null)allNull=false;
						else site="";
						pps.add(site);
					}
					if(!allNull)this.writer.writeEntry(chr,pos,"",pps);
				}
			}
			this.logger.info("Finished writing ppileup");
			this.writer.close();
		}
		private void spool2Chromosome(String chr)
		{
			for(PpileupBuilder builder:this.builders)
			{
				String activechr=builder.switchChromosome();
				while(!activechr.equals((chr)))
				{
					activechr=builder.switchChromosome();
				}

			}
			return;
		}

		private void spool2Position(int pos)
		{
			      for(PpileupBuilder builder:this.builders)
				  {
					  int doneuntil=builder.doneUntil();
					  while(doneuntil<=pos)
					  {
						  builder.addRead();
						  doneuntil=builder.doneUntil();
					  }
				  }
		}







}
