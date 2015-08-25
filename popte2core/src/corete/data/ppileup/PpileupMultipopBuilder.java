package corete.data.ppileup;

import corete.data.TEFamilyShortcutTranslator;
import corete.data.hier.TEHierarchy;
import corete.data.stat.ISDSummary;
import corete.io.PpileupWriter;
import corete.io.SamPairReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 8/21/15.
 */
public class PpileupMultipopBuilder {
	private final TEFamilyShortcutTranslator tetranslator;
	private final ISDSummary isdSummary;
	private final ArrayList<String> inputFileNames;
	private final ArrayList<String> refChrSorting;
	private final HashMap<String,Integer> lastPositions;
	private final TEHierarchy hier;
	private final int minMapQual;
	private final int srmd;
	private final PpileupWriter writer;
	private final Logger logger;
	private final int maxWorkDist;

	private ArrayList<PpileupBuilder> builders;

	public PpileupMultipopBuilder(TEFamilyShortcutTranslator tetranslator, ISDSummary isdSummary, ArrayList<String> inputFileNames,
								  ArrayList<String> refChrSorting, HashMap<String,Integer> lastPositions, TEHierarchy hier, int minMapQual,
								  int srmd, PpileupWriter writer, Logger logger)
	{
		this.tetranslator=tetranslator;
		this.isdSummary=isdSummary;
		this.inputFileNames=inputFileNames;
		this.refChrSorting=refChrSorting;
		this.lastPositions=lastPositions;
		this.hier=hier;
		this.minMapQual=minMapQual;
		this.srmd=srmd;
		this.writer=writer;
		this.logger=logger;

		ArrayList<PpileupBuilder> tmpBuilders=new ArrayList<PpileupBuilder>();
		int maxdist=0;
		for(int i=0; i<this.inputFileNames.size(); i++)
		{
			String fileName=this.inputFileNames.get(i);
			int workDist=this.isdSummary.getMean(i);
			if(workDist>maxdist) maxdist=workDist;
			int maxDist=this.isdSummary.getUpperQuantil(i);
			SamPairReader spr=new SamPairReader(fileName,hier,srmd,logger);

			PpileupBuilder build=new PpileupBuilder(minMapQual,workDist,maxDist,spr,tetranslator);
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
					for(PpileupBuilder b:this.builders)
					{
						pps.add(b.getSite(pos));
					}
					this.writer.writeEntry(chr,pos,pps);
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
					  while(doneuntil>=pos)
					  {
						  builder.addRead();
					  }
				  }
		}







}
