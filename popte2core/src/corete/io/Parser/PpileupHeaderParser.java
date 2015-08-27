package corete.io.Parser;

import corete.data.TEFamilyShortcutTranslator;
import corete.data.ppileup.PpileupeHeaderSymbols;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by robertkofler on 8/27/15.
 */
public class PpileupHeaderParser {
	private final HashMap<String, ArrayList<String>> abbrv2entries;

	public PpileupHeaderParser(ArrayList<String> header) {
		HashMap<String, ArrayList<String>> tostore = new HashMap<String, ArrayList<String>>();
		for (String s : header) {
			if (!s.startsWith("@")) throw new IllegalArgumentException("Header must start with @");
			String[] a = s.split("\\t");
			String key = a[0];
			tostore.putIfAbsent(key, new ArrayList<String>());
			tostore.get(key).add(s);
		}
		//this.header=new ArrayList<String>()
		this.abbrv2entries = tostore;
	}


	public TEFamilyShortcutTranslator getTEFamilyShortcutTranslator() {
		if (!abbrv2entries.containsKey(PpileupeHeaderSymbols.SHORTCUT))
			throw new IllegalArgumentException("Header does not contain shortcuts for TEs");

		HashMap<String, String> s2f = new HashMap<String, String>();
		HashSet<String> pastFam = new HashSet<String>();

		for (String s : this.abbrv2entries.get(PpileupeHeaderSymbols.SHORTCUT)) {
			String[] a = s.split("\\t");
			//@SC     yp      gypsy
			//@SC     mc      McClintock
			String sc = a[1];
			String fam = a[2];
			if (sc.toLowerCase().equals(sc.toUpperCase()))
				throw new IllegalArgumentException("Invalid shortcut; must be able to build lower as well as upper case version of shortcut\n");
			sc = sc.toLowerCase();
			if (s2f.containsKey(sc))
				throw new IllegalArgumentException("Shortcut " + sc + " is present multiple times in header");
			if (pastFam.contains(fam))
				throw new IllegalArgumentException("Family " + fam + " is present multiple times in header");
			s2f.put(sc, fam);
		}

		return TEFamilyShortcutTranslator.getShort2FullTranslator(s2f);
	}

	public ArrayList<Integer> getInnerDistances() {
		if (!abbrv2entries.containsKey(PpileupeHeaderSymbols.DEFAULTINNERDISTANCE))
			throw new IllegalArgumentException("Header does not contain shortcut for inner distances");
		HashMap<Integer, Integer> id = new HashMap<Integer, Integer>();
		int max = 0;

		for (String s : this.abbrv2entries.get(PpileupeHeaderSymbols.DEFAULTINNERDISTANCE)) {
			String[] a = s.split("\\t");
			//@ID     1       69
			//@ID     2       128
			//@ID     3       60
			int index = Integer.parseInt(a[1]);
			if (index < 1) throw new IllegalArgumentException("Index of inner distance must not be smaller than 1");

			int innerDistance = Integer.parseInt(a[2]);
			id.put(index, innerDistance);
			if (index > max) max = index;
		}

		ArrayList<Integer> toret = new ArrayList<Integer>();
		for (int i = 1; i <= max; i++) {
			if (!id.containsKey(i)) throw new IllegalArgumentException("Did not find Inner Distance for sample " + i);
			toret.add(id.get(i));
		}

		return toret;


	}

	public int getMappingQuality()
	{
		if (!abbrv2entries.containsKey(PpileupeHeaderSymbols.MAPPINGQUALITY))
			throw new IllegalArgumentException("Header does not contain shortcut for inner distances");
		ArrayList<String> tw=this.abbrv2entries.get(PpileupeHeaderSymbols.MAPPINGQUALITY);
		if(tw.size()!=1) throw new IllegalArgumentException("Invalid header; only a single mapping quality entry is allowed");
		String[] s=tw.get(0).split("\\t");
		// @MQ     15
		int mq=Integer.parseInt(s[1]);
		return mq;
	}

	public int getStructuralRearangementMinDistance()
	{
		if (!abbrv2entries.containsKey(PpileupeHeaderSymbols.STRUCTREARMINDIST))
			throw new IllegalArgumentException("Header does not contain shortcut for structural rearrangement minimum distances");
		ArrayList<String> tw=this.abbrv2entries.get(PpileupeHeaderSymbols.STRUCTREARMINDIST);
		if(tw.size()!=1) throw new IllegalArgumentException("Invalid header; only a single entry for structural rearrangement minimum distance is allowed");
		String[] s=tw.get(0).split("\\t");
		// @SR     10000
		int mq=Integer.parseInt(s[1]);
		return mq;
	}

	public String getVersionNumber()
	{
		if (!abbrv2entries.containsKey(PpileupeHeaderSymbols.VERSIONNUMBER))
			throw new IllegalArgumentException("Header does not contain shortcut for version number");
		ArrayList<String> tw=this.abbrv2entries.get(PpileupeHeaderSymbols.VERSIONNUMBER);
		if(tw.size()!=1) throw new IllegalArgumentException("Invalid header; only a single entry for version number is allowed");
		String[] s=tw.get(0).split("\\t");
		// @SR     10000

		return s[1];
	}

}

