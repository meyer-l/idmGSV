package de.gsv.idm.client.view;

import com.sencha.gxt.widget.core.client.info.DefaultInfoConfig;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.info.InfoConfig;

public class ExtendedDisplayInfo extends Info {
	
	public static void display(String title, String text) {
		InfoConfig config = new DefaultInfoConfig(title, text);
		config.setDisplay(4000);
	    display(config);
	  }
	
}
