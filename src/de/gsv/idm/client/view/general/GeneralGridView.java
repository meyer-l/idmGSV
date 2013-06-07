package de.gsv.idm.client.view.general;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;

import de.gsv.idm.client.presenter.general.GeneralGridPresenter.GeneralGridDisplay;
import de.gsv.idm.client.view.properties.HasKeyAndName;

public abstract class GeneralGridView<T> extends GeneralView implements GeneralGridDisplay<T> {

	//XXX: Add ListDriver
	protected ContentPanel mainPanel;
	protected ListStore<T> store;
	protected Grid<T> grid;
	
	protected ColumnModel<T> columnModel = null;

	public GeneralGridView(HasKeyAndName<T> properties,
	        StoreSortInfo<T> sortInfo) {
		mainPanel = new ContentPanel();
		mainPanel.setWidth(CLIENT_WIDTH - 5);
		mainPanel.setBorders(false);

		store = new ListStore<T>(properties.key());
		if (sortInfo != null) {
			store.addSortInfo(sortInfo);
		}

		grid = new Grid<T>(store, getColumnModelInstance());
		grid.getView().setForceFit(true);

		VerticalLayoutContainer vertical = new VerticalLayoutContainer();
		vertical.add(grid, new VerticalLayoutData(1, 1));
		mainPanel.add(vertical);
	}
	
	protected ColumnModel<T> getColumnModelInstance(){
		if(columnModel == null){
			columnModel = getColumnModel();
		}
		return columnModel;
	}

	protected abstract ColumnModel<T> getColumnModel();

	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public void setStore(List<T> assets) {
		grid.enable();
		store.replaceAll(assets);
	}

	@Override
	public void setHeaderText(String text) {
		mainPanel.setHeadingText(text);
	}

	@Override
	public void addItem(T item) {
		if (store.findModel(item) == null) {
			store.add(item);
		}
	}

	@Override
	public void removeItem(T item) {
		if (store.findModel(item) != null) {
			store.remove(item);
		}
	}
	
	@Override
	public void addDoubleClickHandler(CellDoubleClickHandler handler) {
		grid.addCellDoubleClickHandler(handler);
	}

	public GridSelectionModel<T> getGridSelectionModel() {		
		return grid.getSelectionModel();
	}
	
	public void refreshGrid(){
		grid.getView().refresh(true);
	}
	
	
}