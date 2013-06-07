package de.gsv.idm.client.presenter.general;

import java.util.ArrayList;

import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.gsv.idm.shared.dto.GeneralDTO;

public abstract class GeneralEditGridPresenter<T extends GeneralDTO<T>> extends GeneralGridPresenter<T> {

	public interface GeneralEditGridDisplay<T> extends GeneralGridDisplay<T> {

		void setSaveButtonText(String text);

		HasSelectHandlers getSaveButtonClick();

		void setResetButtonText(String text);

		HasSelectHandlers getResetButtonClick();

		ArrayList<T> getStoredItems();
	}

	private GeneralEditGridDisplay<T> display;

	public GeneralEditGridPresenter(GeneralEditGridDisplay<T> view) {
		super(view);
		display = view;
	}

	@Override
	protected void bindView() {
		display.setSaveButtonText("Änderungen speichern");

		display.getSaveButtonClick().addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				doSaveButtonAction();			}

			
		});

		display.setResetButtonText("Alle Änderungen zurücksetzen");
		display.getResetButtonClick().addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				setStore();
			}
		});
		setStore();
		individualBindView();
	}
	
	public void refreshView(){
		display.refreshGrid();
	}

	protected abstract void setStore();

	protected abstract void individualBindView();
	
	protected abstract void doSaveButtonAction();

}
