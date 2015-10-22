package corete.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by robertkofler on 8/18/15.
 * TODO make case sensitive
 */
public class TEFamilyShortcutTranslator {
	HashMap<String,String> full2short=null;
	HashMap<String,String> short2full=null;


	//region Constructors
	public static TEFamilyShortcutTranslator  getFull2ShortTranslator(HashMap<String, String> full2short)
	{
		return new TEFamilyShortcutTranslator(full2short);
	}

	public static TEFamilyShortcutTranslator  getShort2FullTranslator(HashMap<String, String> short2full)
	{
		return new TEFamilyShortcutTranslator(short2full,true);
	}


	private TEFamilyShortcutTranslator(HashMap<String, String> full2short)
	{
			this.full2short = new HashMap<String, String>(full2short);
			this.short2full = swapKeyValue(full2short);
			if(!areShortcutsLowercase()) throw new IllegalArgumentException("All shortcuts must be lowercase");
	}


	private TEFamilyShortcutTranslator(HashMap<String, String> short2full,boolean tmp)
	{
		this.short2full = new HashMap<String, String>(short2full);
		this.full2short = swapKeyValue(short2full);
		if(!areShortcutsLowercase()) throw new IllegalArgumentException("All shortcuts must be lowercase");
	}

	/**
	 * Test if all shortcuts are lower-case
	 * @return
	 */
	private boolean areShortcutsLowercase()
	{
		if(short2full==null) throw new IllegalArgumentException("Shortcuts not set");

		for(String shortcut:this.short2full.keySet())
		{
			if(!shortcut.toLowerCase().equals(shortcut)) return false;
		}
		return true;
	}
	/**
	 * Reverse the hashmap
	 * use for example if the key is the shortcut and the value the full-length name of a TE insertion
	 * @param hm
	 * @return
	 */
	private  HashMap<String,String> swapKeyValue(HashMap<String,String> hm)
	{
		HashMap<String,String> toret=new HashMap<String,String>();
		for(Map.Entry<String,String> e: hm.entrySet())
		{
			if(toret.containsKey(e.getValue()))throw new IllegalArgumentException("Error in TE shortcut; Entry "+e.getValue()+" is present mulitple times");
			toret.put(e.getValue(),e.getKey());
		}
		return toret;
	}
	//endregion


	/**
	 * Get shortcut for forward insertions (UPPER-CASE)
	 * @param fullName
	 * @return
	 */
	public String getShortcutFwd(String fullName)
	{
		if(!full2short.containsKey(fullName)) throw new IllegalArgumentException("Can not find entry for full-name "+fullName);
		String sc=full2short.get(fullName);
		return  sc.toUpperCase();
	}

	/**
	 * Get shortcut for reverse insertions (LOWER-CASE)
	 * @param fullName
	 * @return
	 */
	public String getShortcutRev(String fullName)
	{
		if(!full2short.containsKey(fullName)) throw new IllegalArgumentException("Can not find entry for full-name "+fullName);
		String sc=full2short.get(fullName);
		assert(sc.toLowerCase().equals(sc));   // lower case conversion should be redundant; all shortcuts in lower case
		return  sc;
	}



	/**
	 * Translates shortcut to the Family name of TE insertion
	 * Case insensitive, as all shortcuts are converted to lowercase
	 * @param shortcut
	 * @return
	 */
	public String getFamilyname(String shortcut)
	{
		String lcs=shortcut.toLowerCase();
		if(!short2full.containsKey(lcs)) throw new IllegalArgumentException("Can not find entry for shortcut "+lcs);
		return short2full.get(lcs);
	}


	/**
	 * Returns the direction of the TE insertions signature
	 * @param shortcut
	 * @return
	 */
	public SignatureDirection getSignatureDirection(String shortcut) {
		String lcs = shortcut.toLowerCase();
		if (!short2full.containsKey(lcs)) throw new IllegalArgumentException("Can not find entry for shortcut " + lcs);
		SignatureDirection strand = SignatureDirection.Forward;
		if (shortcut.toLowerCase().equals(shortcut)) {
			strand = SignatureDirection.Reverse;
		}
		return strand;
	}





	public HashMap<String,String> getFull2short()
	{
		return new HashMap<String,String>(this.full2short);
	}


}
