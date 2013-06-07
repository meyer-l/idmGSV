package de.gsv.idm.client.view.widgets.form;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;

public class ModuleStatusAssesmentBox extends SimpleComboBox<String> {
	
	public ModuleStatusAssesmentBox() {
		super(new StringLabelProvider<String>());
		add("in Betrieb");
		add("im Test");
		add("in Planung");
		setTypeAhead(true);
		setTriggerAction(TriggerAction.ALL);
	}


}
