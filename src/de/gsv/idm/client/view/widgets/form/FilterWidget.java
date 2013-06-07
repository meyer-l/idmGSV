package de.gsv.idm.client.view.widgets.form;

import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;

import de.gsv.idm.shared.dto.HasName;

public class FilterWidget<T extends HasName> extends FramedPanel {
	
	StoreFilterField<T> filter;
	public FilterWidget(Store<T> store){
		filter = new StoreFilterField<T>() {
			@Override
			protected boolean doSelect(Store<T> store, T parent,
			        T item, String filter) {

				String name = item.getName();
				name = name.toLowerCase();
				if (name.contains(filter.toLowerCase())) {
					return true;
				}
				return false;
			}
		};
		filter.bind(store);
		FieldLabel label = new FieldLabel(filter, "Filter");
		label.setLabelWidth(30);
		add(label);
		setHeaderVisible(false);
	}
	
	public StoreFilterField<T> getFilter(){
		return filter;
	}

}
