package corete.io;

import corete.data.TEFamilyShortcutTranslator;
import corete.io.Parser.PpileupHeaderParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Read shortcuts from a file; must be unzipped
 */
public class TEFamilyShortcutReader {
	private final String inputFile;
	private  BufferedReader br;
	private final Logger logger;

	public TEFamilyShortcutReader(String inputFile, Logger logger)
	{
		this.inputFile=inputFile;
		this.logger=logger;
		try
		{
			this.br=new BufferedReader(new FileReader(inputFile));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		this.logger.info("Reading TE Shortcuts from file (must be unzipped) "+ inputFile);
	}

	public TEFamilyShortcutTranslator getShortcutTranslator()
	{
		ArrayList<String> lines=new ArrayList<String>();
		String line=null;
		try {
			while ((line = br.readLine()) != null) {
				if(!line.startsWith("@"))break;
			lines.add(line);
			}
			this.br.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		PpileupHeaderParser pphp=new PpileupHeaderParser(lines);
		return pphp.getTEFamilyShortcutTranslator();
	}


}
