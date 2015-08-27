package test;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by robertkofler on 8/25/15.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({test.data.TTEHierarchy.class, test.data.TTEFamilyShortCutTranslator.class,
		test.io.TSamReader.class,  test.io.TSamPairReader.class,
test.ppileup.TPpileupBuilder.class})
public class CoreTETestSuite {
}
