package corete.io.ppileup;

import corete.data.TEFamilyShortcutTranslator;
import corete.data.ppileup.PpileupSiteLightwight;
import corete.data.stat.EssentialPpileupStats;

/**
 * Created by robertkofler on 8/31/15.
 */
public interface IPpileupLightwightReader {
	public TEFamilyShortcutTranslator getTEFamilyShortcutTranslator();

	public EssentialPpileupStats getEssentialPpileupStats();

	public PpileupSiteLightwight next();
}
