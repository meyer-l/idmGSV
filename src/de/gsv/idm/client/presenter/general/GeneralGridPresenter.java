package de.gsv.idm.client.presenter.general;

import java.util.List;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.presenter.Presenter;
import de.gsv.idm.shared.dto.GeneralDTO;

public abstract class GeneralGridPresenter<T extends GeneralDTO<T>> implements Presenter {

	protected final HandlerManager eventBus;
	protected final DBController dbConnector;
	private final GeneralGridDisplay<T> generalDisplay;

	public abstract interface GeneralGridDisplay<T> extends IsWidget {
		void setStore(List<T> items);

		void setHeaderText(String text);

		void addItem(T item);

		void removeItem(T item);

		void addDoubleClickHandler(CellDoubleClickHandler handler);

		GridSelectionModel<T> getGridSelectionModel();

		void refreshGrid();

	}

	public GeneralGridPresenter(GeneralGridDisplay<T> view) {
		dbConnector = DBController.getInstance();
		eventBus = dbConnector.getEventBus();
		generalDisplay = view;
	}

	@Override
	public IsWidget go() {
		bindView();
		bindBus();
		return generalDisplay.asWidget();
	}

	

	protected abstract void bindBus();

	protected abstract void bindView();

}
