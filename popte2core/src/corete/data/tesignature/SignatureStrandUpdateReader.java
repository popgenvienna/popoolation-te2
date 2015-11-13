package corete.data.tesignature;

import corete.data.SamPair;
import corete.data.SamPairType;
import corete.data.SignatureDirection;
import corete.data.TEStrand;
import corete.data.hier.TEHierarchy;
import corete.io.SamPairReader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by robertkofler on 11/10/15.
 */
public class SignatureStrandUpdateReader {
	private SamPairReader reader;
	private ArrayList<SignatureStrandUpdateBuilder> builders;
	private int medianDistance;
	private TEHierarchy hier;

	public SignatureStrandUpdateReader(SamPairReader reader, TEHierarchy hier,int medianDistance, ArrayList<SignatureStrandUpdateBuilder> builder)
	{
		this.reader=reader;
		this.hier=hier;
		this.medianDistance=medianDistance;
		this.builders=new ArrayList<SignatureStrandUpdateBuilder>(builder);
	}


	public void updateSignatures()
	{
		String activeChr=null;
		HashMap<Integer,ArrayList<SignatureStrandUpdateBuilder>> chrSpecBuilders=new HashMap<Integer,ArrayList<SignatureStrandUpdateBuilder>>();

		while(reader.hasNext())
		{
			SamPair sp=reader.next();
			// skip everything that is not a TE insert
			if(sp.getSamPairType()!= SamPairType.TEInsert) continue;
			if(activeChr==null || (!activeChr.equals(sp.getFirstRead().getRefchr())))
			{
				activeChr=sp.getFirstRead().getRefchr();
				chrSpecBuilders=getChromosomeSpecificBuilders(activeChr);
			}

			int start;
			int end;
			String fam=sp.getFamily();
			SignatureDirection sd=SignatureDirection.Forward;
			if(!sp.getFirstRead().isForwardStrand())  sd=SignatureDirection.Reverse;

			if(sd==SignatureDirection.Forward)
			{
				start=sp.getFirstRead().getEnd()+1;
				end=sp.getFirstRead().getEnd()+this.medianDistance;
			}
			else
			{
				start=sp.getFirstRead().getStart()-this.medianDistance;
				end=sp.getFirstRead().getStart()-1;
			}

			for(int i=start; i<=end; i++)
			{
				if(chrSpecBuilders.containsKey(i))
				{
					ArrayList<SignatureStrandUpdateBuilder> ssubs=chrSpecBuilders.get(i);
					for(SignatureStrandUpdateBuilder sb:ssubs)
					{
						assert(activeChr.equals(sb.getRefChr()));
						if(!sb.getFamily().equals(fam)) continue;
						if(sb.getDirection()!=sd)continue;

						TEStrand str=sp.getTEStrand();
						if(str==TEStrand.Minus) sb.incrementMinus();
						else if(str==TEStrand.Plus) sb.incrementPlus();
						else throw new IllegalArgumentException("invalid strand "+str);

						// so now it should be on the same chromosome, same direction, same position and same family...
						// updateBuilder(sb, sd, sp.getFirstRead().isMateForwardStrand());
					}
				}
			}





		}

	}


	private void updateBuilder(SignatureStrandUpdateBuilder builder, SignatureDirection refChr, boolean teMateIsForward)
	{
		if(refChr == SignatureDirection.Forward)
		{
			if(teMateIsForward)
			{
				// signature forward and mate is forward; TE => MINUS
				builder.incrementMinus();

			}
			else
			{
				// signature forward and mate is reverse => plus strand TE
				   builder.incrementPlus();
			}

		}
		else if(refChr==SignatureDirection.Reverse)
		{

			if(teMateIsForward)
			{
				// signature reverse and mate is forward; TE => plus
				builder.incrementPlus();
			}
			else
			{
				// signature revere and mate is reverse => minus strand TE
				builder.incrementMinus();
			}

		}
		else throw new IllegalArgumentException("Invalid");
	}




	private HashMap<Integer,ArrayList<SignatureStrandUpdateBuilder>> getChromosomeSpecificBuilders(String chr)
	{
		HashMap<Integer,ArrayList<SignatureStrandUpdateBuilder>> toret =new HashMap<Integer,ArrayList<SignatureStrandUpdateBuilder>>();
		for(SignatureStrandUpdateBuilder b:this.builders)
		{
			   if(!b.getRefChr().equals(chr))  continue;

			toret.putIfAbsent(b.getPosition(),new ArrayList<SignatureStrandUpdateBuilder>());
			toret.get(b.getPosition()).add(b);
		}
		return toret;
	}





}
