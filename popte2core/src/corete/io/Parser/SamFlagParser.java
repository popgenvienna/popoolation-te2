package corete.io.Parser;

/**
 * Created by robertkofler on 8/17/15.
 */
public class SamFlagParser {

	private static final int revcomplement=0x10;
	private static final int unmapped=0x4;
	private static final int mateunmapped=0x8;
	private static final int pairedinsequencing=0x1;

	public static boolean isForwardStrand(int samflag)
	{

		if((samflag & revcomplement)>0 )
		{
			return false;
		}
		else
		{
			return true;
		}

	}

	public static boolean isUnmapped(int samflag)
	{

		if((samflag & unmapped)>0 )
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	public static boolean isUnmappedMate(int samflag)
	{

		if((samflag & mateunmapped)>0 )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static boolean isPairedInSequencing(int samflag)
	{

		if((samflag & pairedinsequencing)>0 )
		{
			return true;
		}
		else
		{
			return false;
		}

	}

}
