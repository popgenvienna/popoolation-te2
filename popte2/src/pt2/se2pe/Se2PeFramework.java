package pt2.se2pe;

import corete.data.SamRecord;
import corete.io.AutoDetectSamBamReader;
import corete.io.BestHitSamBamReader;
import corete.io.BufferSamBamReader;
import htsjdk.samtools.*;
import htsjdk.samtools.fastq.FastqReader;
import htsjdk.samtools.fastq.FastqRecord;
import pt2.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by robertkofler on 10/29/15.
 */
public class Se2PeFramework {
	private final String fastq1;
	private final String fastq2;
	private final String bam1;
	private final String bam2;
	private final String output;
	private final boolean sort;
	private final boolean index;
	private final int maxAlignmentsSA;
	private final Logger logger;

	public Se2PeFramework(String fastq1, String fastq2, String bam1, String bam2, String output,
						  boolean sort, boolean index, int maxAlignmentsSA, Logger logger) {
		this.fastq1 = fastq1;
		this.fastq2 = fastq2;
		this.bam1 = bam1;
		this.bam2 = bam2;
		this.output = output;
		this.sort = sort;
		this.index = index;
		this.maxAlignmentsSA = maxAlignmentsSA;
		this.logger = logger;

		if (!new File(fastq1).exists()) throw new IllegalArgumentException("Input file does not exist: " + fastq1);
		if (!new File(fastq2).exists()) throw new IllegalArgumentException("Input file does not exist: " + fastq2);
		if (!new File(bam1).exists()) throw new IllegalArgumentException("Input file does not exist: " + fastq1);
		if (!new File(bam2).exists()) throw new IllegalArgumentException("Input file does not exist: " + fastq2);


		try {
			new FileWriter(output);
		} catch (IOException e) {
			throw new IllegalArgumentException("Can not create output file:" + output);
		}


	}


	public void run() {
		FastqReader fr1 = new FastqReader(new File(fastq1));
		FastqReader fr2 = new FastqReader(new File(fastq2));
		BufferSamBamReader b1 = new BufferSamBamReader(new BestHitSamBamReader(new AutoDetectSamBamReader(this.bam1, this.logger)));
		BufferSamBamReader b2 = new BufferSamBamReader(new BestHitSamBamReader(new AutoDetectSamBamReader(this.bam2, this.logger)));
		SAMFileHeader header =getFileHeader();
		SAMFileWriter output = getSamWriter(header);
		Pop2FileWriter writer=new Pop2FileWriter(header,output,logger);


		while (fr1.hasNext() && fr2.hasNext()) {
			// read the two pairs
			FastqRecord record1 = fr1.next();
			FastqRecord record2 = fr2.next();
			String readname1 = record1.getReadHeader().replaceAll("/[12]$", "");
			String readname2 = record2.getReadHeader().replaceAll("/[12]$", "");
			if (!readname1.equals(readname2))
				throw new IllegalArgumentException("Invalid readnames for fastq reads " + readname1 + "  " + readname2);
			SamRecord s1 = b1.next();
			SamRecord s2 = b2.next();
			if(s1!=null)
			{
				String name = s1.getReadname().replaceAll("/[12]$", "");
				if(!name.equals(readname1)){b1.buffer(s1); s1=null;}      // if readname does not match, buffer the thing
			}
			if(s2!=null)
			{
				String name = s2.getReadname().replaceAll("/[12]$", "");
				if(!name.equals(readname1)){b2.buffer(s2); s2=null;}     // if readname does not match buffer the thing
			}
			writer.write(record1,record2,s1,s2,readname1);
		}


		writer.close();


	}

	public SAMFileHeader getFileHeader()
	{
		SamReader sr = SamReaderFactory.makeDefault().validationStringency(ValidationStringency.SILENT).open(new File(this.bam1));
		 SAMFileHeader header= sr.getFileHeader();
		try {
			sr.close();
		}catch(IOException e)
		{
			System.exit(1);
		}
		return header;

	}



	public SAMFileWriter getSamWriter(SAMFileHeader header) {

		// Check if files exists and exit otherwise
		// Add the program to the header
		SAMProgramRecord programRecord = new SAMProgramRecord("popte2");
		programRecord.setProgramName("popte2");
		programRecord.setProgramVersion(Main.getVersionNumber());
		header.addProgramRecord(programRecord);
		// Create the output factory
		SAMFileWriterFactory factory = new SAMFileWriterFactory();
		// Log the index fine

		boolean sortlocal=this.sort;
		if (index) {
			logger.fine("Output will be indexed. Switching to 'sort' mode if not provided");
			sortlocal = true;
		}

		if (sortlocal) {
			// Change the sort ordering and log
			header.setSortOrder(SAMFileHeader.SortOrder.coordinate);
			logger.fine("Output will be sorted on the fly");
		} else {
			// Add unsorted ordering and log
			header.setSortOrder(SAMFileHeader.SortOrder.unsorted);
			logger.fine("Output file will be unsorted");
		}
		// set the index creation
		factory.setCreateIndex(index);
		// Open the output. Note: I use the htsjdk and not a wrapper because it could index and sort on the fly
		logger.fine("Creating output file");
		return  factory.makeSAMOrBAMWriter(header, false, new File(this.output));
	}



}
