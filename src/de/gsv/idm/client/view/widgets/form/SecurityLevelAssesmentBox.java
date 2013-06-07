package de.gsv.idm.client.view.widgets.form;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.ComboBox;

import de.gsv.idm.client.view.properties.SecurityLevelDTOProperties;
import de.gsv.idm.shared.dto.SecurityLevelDTO;

public class SecurityLevelAssesmentBox extends ComboBox<SecurityLevelDTO> {

	private static SecurityLevelDTOProperties properties = GWT.create(SecurityLevelDTOProperties.class);

	public SecurityLevelAssesmentBox(ListStore<SecurityLevelDTO> store,
	        LabelProvider<SecurityLevelDTO> labelProvider) {
		super(store, labelProvider);
		store.add(SecurityLevelDTO.getSecurityLevel(1));
		store.add(SecurityLevelDTO.getSecurityLevel(2));
		store.add(SecurityLevelDTO.getSecurityLevel(3));
		setForceSelection(true);
		setTypeAhead(true);
		setTriggerAction(TriggerAction.ALL);
	}

	public SecurityLevelAssesmentBox() {
		this(new ListStore<SecurityLevelDTO>(properties.key()), properties.comboLabel());
	}

}
