package corete.io;

import corete.data.hier.HierarchyEntry;
import corete.data.hier.TEHierarchy;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 6/29/15.
 */
public class HierarchyReader {
	private  BufferedReader bf;
	private final Logger logger;
	private final String hierFile;

	public HierarchyReader(String hierarchyFile, Logger logger)
	{
		this.hierFile=hierarchyFile;
		this.logger=logger;
		try{
			bf=new BufferedReader(new FileReader(hierarchyFile));
		}
		catch(FileNotFoundException e)
		{
		   	e.printStackTrace();
			System.exit(0);
		}
	}

	public TEHierarchy getHierarchy()
	{
		HashMap<String,HierarchyEntry> teh=new HashMap<String, HierarchyEntry>();
		logger.info(String.format("Started reading TE hierarchy from file: %s", this.hierFile));

		int entries=0;
		HashSet<String> uniqueIDs=new HashSet<String>();
		try
		{
			HierIndizes hi = parseHeader(bf.readLine());
			String line=null;
			while((line=bf.readLine())!=null)
			{
				String[] ts=line.split("\\t");
				String id= ts[hi.id];
				if(uniqueIDs.contains(id)) throw new IllegalArgumentException(String.format("Invalid TE hierarchy - id %s is present multiple times",id));
				HierarchyEntry he=new HierarchyEntry(id,ts[hi.family],ts[hi.order]);
				teh.put(id,he);
				uniqueIDs.add(id);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}

		TEHierarchy th= new TEHierarchy(teh);
		logger.info(String.format("Finished reading TE hierarchy"));
		logger.info(String.format("Read %d ids, %d families, %d orders",th.countIDs(),th.countFamilies(),th.countOrders()));
		return th;

	}


	private class HierIndizes
	{
		public int id=0;
		public int family=0;
		public int order=0;

	}

	private HierIndizes parseHeader(String header)
	{
		HierIndizes hi =new HierIndizes();
		String[] parts=header.split("\\t");
		boolean ids=false;
		boolean family=false;
		boolean order=false;

		for(int i=0; i<parts.length; i++)
		{
			if(parts[i].equals("id"))
			{
				hi.id = i;
				ids=true;
				this.logger.fine(String.format("Column of 'id' is %d",i));
			}
			else if(parts[i].equals("family"))
			{

				hi.family=i;
				family=true;
				this.logger.fine(String.format("Column of 'family' is %d",i));
			}
			else if(parts[i].equals("order"))
			{
				hi.order=i;
				order=true;
				this.logger.fine(String.format("Column of 'family' is %d",i));
			}
		}
		if(!ids) throw new IllegalArgumentException("Hierarchy file invalid; no id column found");
		if(!family) throw new IllegalArgumentException("Hierarchy file invalid; no family column found");
		if(!order) throw new IllegalArgumentException("Hierarchy file invalid; no order column found");
		return hi;
	}




}
