package de.gsv.idm.client.view.widgets.form;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;

import de.gsv.idm.shared.dto.MeasureStatusObject;

public class MeasureStatusAssesmentBoxCell extends ComboBoxCell<String> {

	public MeasureStatusAssesmentBoxCell() {
		super(new ListStore<String>(new ModelKeyProvider<String>() {
			@Override
			public String getKey(String item) {
				return item;
			}
		}), new LabelProvider<String>() {
			@Override
			public String getLabel(String item) {
				return item;
			}
		});
		store.add(MeasureStatusObject.getNotProcessed());
		store.add("Entbehrlich");
		store.add("Ja");
		store.add("Teilweise");
		store.add(MeasureStatusObject.getNotImplemented());
		setTypeAhead(true);
		setTriggerAction(TriggerAction.ALL);
	}
	
	
}
