package test;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.tesignature.TChunk2SignatureParser;
import test.tesignature.TContigwisePolynRepresentation;
import test.tesignature.TSignatureFrequencyEstimationFramework;

/**
 * Created by robertkofler on 8/25/15.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({test.data.TTEHierarchy.class, test.data.TTEFamilyShortCutTranslator.class,
		test.io.TSamReader.class,  test.io.TSamPairReader.class, test.io.TPpileupReader.class,
		test.data.TPpileupSampleSummary.class,test.data.TPpileupSite.class,  test.data.TPpileupSlidingWindow.class,
test.ppileup.TPpileupBuilder.class,test.data.TPpileupChunkReader.class,TChunk2SignatureParser.class,
TSignatureFrequencyEstimationFramework.class, TContigwisePolynRepresentation.class,test.tesignature.TInsertionSignature.class,
test.tesignature.TSignaturePairupFramework.class})
public class CoreTETestSuite {
}
