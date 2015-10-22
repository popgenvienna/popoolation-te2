package test;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by robertkofler on 8/25/15.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({test.data.TTEHierarchy.class, test.data.TTEFamilyShortCutTranslator.class,
		test.io.TSamReader.class,  test.io.TSamPairReader.class, test.io.TPpileupReader.class,
		test.data.TPpileupSampleSummary.class,test.data.TPpileupSite.class,  test.data.TPpileupSlidingWindow.class,
test.ppileup.TPpileupBuilder.class})
public class CoreTETestSuite {
}
