package de.gsv.idm.client.view.widgets.form;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.tips.QuickTip;

import de.gsv.idm.client.images.ImageResources;
import de.gsv.idm.shared.dto.GeneralDTO;

public abstract class GridList<T extends GeneralDTO<T>> extends FieldLabel {

	// XXX: refactor gridlist, so that it has an abstract get ColumnModel
	// method;
	private TextButton addButton;
	protected ListStore<T> store;
	protected Grid<T> grid;

	public GridList(ListStore<T> store, ValueProvider<T, String> sortPropety) {
		this.store = store;
		store.addSortInfo(new StoreSortInfo<T>(sortPropety, SortDir.ASC));
	}

	protected void buildView(ColumnModel<T> columnModel) {
		FramedPanel gridPanel = new FramedPanel();
		VerticalLayoutContainer verticalLayout = new VerticalLayoutContainer();
		verticalLayout.add(new FilterWidget<T>(store), new VerticalLayoutData(1, -1));
		gridPanel.setHeaderVisible(false);
		gridPanel.setWidth("400px");
		grid = new Grid<T>(store, columnModel);
		new QuickTip(grid);
		grid.setBorders(true);
		grid.getView().setForceFit(true);
		grid.setWidth(400);
		grid.setHeight(200);
		grid.addCellDoubleClickHandler(getCellDoubleClickHandler());
		verticalLayout.add(grid, new VerticalLayoutData(1, -1));
		gridPanel.add(verticalLayout);
		addButton = new TextButton();
		addButton.setStyleName("gridListButton");
		ImageResources bundler = GWT.create(ImageResources.class);
		addButton.setIcon(bundler.add());
		gridPanel.addButton(addButton);
		setWidget(gridPanel);
	}

	public HasSelectHandlers getAddButtonClick() {
		return addButton;
	}

	public void setAddButtonText(String text) {
		addButton.setText(text);
	}

	public void setAddButtonVisibility(boolean b) {
		addButton.setVisible(b);
	}

	protected abstract CellDoubleClickHandler getCellDoubleClickHandler();

	public void setAddButtonEnabled(Boolean enabled) {
		addButton.setEnabled(enabled);
    }

}
