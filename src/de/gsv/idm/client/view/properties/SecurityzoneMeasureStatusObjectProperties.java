package de.gsv.idm.client.view.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.gsv.idm.client.view.securityzone.widgets.data.SecurityzoneMeasureStatusObject;

public interface SecurityzoneMeasureStatusObjectProperties extends
        PropertyAccess<SecurityzoneMeasureStatusObject> {
	@Path("id")
	ModelKeyProvider<SecurityzoneMeasureStatusObject> key();

	ValueProvider<SecurityzoneMeasureStatusObject, String> name();

	ValueProvider<SecurityzoneMeasureStatusObject, String> status();

	ValueProvider<SecurityzoneMeasureStatusObject, Boolean> inherited();

}
