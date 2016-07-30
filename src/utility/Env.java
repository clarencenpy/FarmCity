package utility;

import java.util.*;
import java.io.*;

/**
 * Static class to hold environment variables
 * Reads from the configuration.txt on initialisation
 */
public class Env {
	public static Properties properties = new Properties();
	public static String dataDir;

	static {
		try {
			properties.load(new FileInputStream("configuration.txt"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

        // since this is used very often
        dataDir = properties.getProperty("dataDirectory") + "/";

	}
}