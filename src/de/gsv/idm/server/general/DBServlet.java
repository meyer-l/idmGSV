package de.gsv.idm.server.general;

import java.util.Properties;

import org.javalite.activejdbc.Base;

import de.gsv.idm.server.DBPropertiesProvider;
import de.novanic.eventservice.service.RemoteEventServiceServlet;

@SuppressWarnings("serial")
public class DBServlet extends RemoteEventServiceServlet {

	protected void buildConnection() {
		Properties dbProperties = DBPropertiesProvider.getProperties(getServletContext());
		if (!Base.hasConnection()) {
			Base.open(dbProperties.getProperty("driver"), dbProperties.getProperty("url"),
			        dbProperties.getProperty("user"), dbProperties.getProperty("password"));
		}

	}

	protected void closeConnection() {
		if (Base.hasConnection()) {
			Base.close();
		}
	}

}
