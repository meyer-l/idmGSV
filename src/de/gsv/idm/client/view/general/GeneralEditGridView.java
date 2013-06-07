package de.gsv.idm.client.view.general;

import java.util.ArrayList;

import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;

import de.gsv.idm.client.presenter.general.GeneralEditGridPresenter.GeneralEditGridDisplay;
import de.gsv.idm.client.view.properties.HasKeyAndName;

public abstract class GeneralEditGridView<T> extends GeneralGridView<T> implements GeneralEditGridDisplay<T> {

	private TextButton resetButton;
	private TextButton saveButton;
	
	public GeneralEditGridView(HasKeyAndName<T> properties, StoreSortInfo<T> sortInfo) {
	    super(properties, sortInfo);
	    
	    resetButton = new TextButton();
		
		saveButton = new TextButton();
		addButtons();
    }

	protected void addButtons() {
		mainPanel.addButton(resetButton);
		mainPanel.addButton(saveButton);
    }

	public void setSaveButtonText(String text) {
		saveButton.setText(text);
	}

	public HasSelectHandlers getSaveButtonClick() {
		return saveButton;
	}

	public void setResetButtonText(String text) {
		resetButton.setText(text);
	}

	public HasSelectHandlers getResetButtonClick() {
		return resetButton;
	}

	@Override
	public ArrayList<T> getStoredItems() {
		return new ArrayList<T>(store.getAll());
	}

}
