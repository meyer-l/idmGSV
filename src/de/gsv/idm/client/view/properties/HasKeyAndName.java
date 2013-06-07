package de.gsv.idm.client.view.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface HasKeyAndName<T> extends PropertyAccess<T> {
	
	@Path("id")
	ModelKeyProvider<T> key();
	@Path("label")
	ValueProvider<T, String> valueProviderLabel();
	LabelProvider<T> label();	
	ValueProvider<T, String> name();
}
