package corete.data.ppileup;

import corete.data.TEFamilyShortcutTranslator;
import corete.data.hier.TEHierarchy;
import corete.data.stat.ISDSummary;
import corete.io.PpileupWriter;
import corete.io.SamPairReader;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 8/21/15.
 */
public class PpileupMultipopBuilder {
	private final TEFamilyShortcutTranslator tetranslator;
	private final ISDSummary isdSummary;
	private final ArrayList<String> inputFilesNames;
	private final ArrayList<String> rcSorting;

	private final PpileupWriter writer;
	private final Logger logger;

	public PpileupMultipopBuilder(TEFamilyShortcutTranslator tetranslator, ISDSummary isdSummary, ArrayList<String> inputFileNames, TEHierarchy hier, int minMapqual, int srmd, PpileupWriter writer, Logger logger)
	{
		this.tetranslator=tetranslator;
		this.isdSummary=isdSummary;
		this.inputFiles=inputFiles;
		this.writer=writer;
		this.logger=logger;
		ArrayList<SamPairReader> input=new ArrayList<SamPairReader>();
		for(String s: this.inputFiles)   {
			input.add(new SamPairReader(input,hier,srmd,this.logger));
		}


	}




}
