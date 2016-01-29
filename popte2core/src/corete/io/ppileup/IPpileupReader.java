package corete.io.ppileup;

import corete.data.TEFamilyShortcutTranslator;
import corete.data.ppileup.PpileupSite;
import corete.data.stat.EssentialPpileupStats;

/**
 * Created by robertkofler on 9/3/15.
 */
public interface IPpileupReader {
	EssentialPpileupStats getEssentialPpileupStats();

	TEFamilyShortcutTranslator getTEFamilyShortcutTranslator();

	PpileupSite next();

	void buffer(PpileupSite tobuffer);
}
