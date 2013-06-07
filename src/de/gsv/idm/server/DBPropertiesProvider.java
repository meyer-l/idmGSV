package de.gsv.idm.server;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;

public class DBPropertiesProvider {
	static Properties dbProperties;

	public static Properties getProperties(ServletContext servletContext) {
		if (dbProperties == null) {
			try {
				dbProperties = new Properties();

				BufferedInputStream stream = new BufferedInputStream(new FileInputStream(
				        servletContext.getRealPath("WEB-INF") + "/db.properties"));
				dbProperties.load(stream);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dbProperties;
	}
}
