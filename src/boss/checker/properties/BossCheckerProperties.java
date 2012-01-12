package boss.checker.properties;

import java.util.Properties;

public class BossCheckerProperties {

	private static BossCheckerProperties instance;
	public static final String PROPERTIES_FILE_NAME = "bossChecker.properties";
	private Properties props;
	
	public static final String AUDIO_FILE_STRING = "audioFile";
	public static final String CHECKER_DELAY_STRING = "checkerDelay";
	public static final String SITE_URL = "siteURL";
	
	private BossCheckerProperties(){
		props = new Properties();

//		try {
//			props.load(new FileInputStream(PROPERTIES_FILE_NAME));

			props.put(BossCheckerProperties.CHECKER_DELAY_STRING, "1");
			props.put(BossCheckerProperties.AUDIO_FILE_STRING, "lets_rock.wav");
//			props.put(BossCheckerProperties.SITE_URL, "http://anonymouse.org/cgi-bin/anon-www.cgi/http://www.l2ouro.com/lineage2/grandboss.php");
			props.put(BossCheckerProperties.SITE_URL, "http://150.161.192.10/moises/tests/index.html");

//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
//	public static BossCheckerProperties getInstance(InputStream inputStream) {
//		if (instance == null) {
//			instance = new BossCheckerProperties(inputStream);
//		}
//		return instance;
//	}

//	public String getPropertie(String property) {
//		return props.getProperty(property);
//	}
//
//	public static BossCheckerProperties getInstance() {
//	if (instance == null) {
//		instance = new BossCheckerProperties();
//	}
//	return instance;
//	}
}
