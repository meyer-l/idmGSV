package de.gsv.idm.client.view.widgets.form;

import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.ComboBox;

public class ComboBoxWithMemory<T> extends ComboBox<T> {

	T previousValue = null;
	
	public ComboBoxWithMemory(ListStore<T> store, LabelProvider<? super T> labelProvider) {
		super(store, labelProvider);
	}
	
	
	
	public T getPreviousValue(){
		return previousValue;
	}

}
