package common.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {
	static FileInputStream fis = null;
	static Properties pro = null;

	public PropertiesReader(String filepath) {
		try {
			fis = new FileInputStream(filepath);
			pro = new Properties();
			pro.load(fis);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * @param filepath
	 * @return Properties 
	 */
	public static Properties getProperty(String filepath) {
		try {
			fis = new FileInputStream(filepath);
			pro = new Properties();
			pro.load(fis);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return pro;
	}

	/**
	 * @return Properties
	 */
	public Properties readPropertyFile() {
		return pro;
	}

	/**
	 * @param key
	 * @return value
	 */
	public String getPropValue(String key) {
		return pro.getProperty(key);
	}

}