package de.gsv.idm.client.view.general;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.ListViewSelectionModel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;

import de.gsv.idm.client.images.ImageResources;
import de.gsv.idm.client.presenter.general.GeneralListPresenter.GeneralListDisplay;
import de.gsv.idm.client.view.properties.GeneralDTOProperties;
import de.gsv.idm.client.view.properties.ObjectExchange;
import de.gsv.idm.client.view.widgets.cell.ToolTipTextCell;
import de.gsv.idm.client.view.widgets.form.FilterWidget;
import de.gsv.idm.shared.comperator.LowerCaseStringComperator;
import de.gsv.idm.shared.dto.GeneralDTO;

//XXX: Generalize a GeneralDomainListEditView
public class GeneralListView<T extends GeneralDTO<T>, V extends GeneralListDisplay<T>> extends
        GeneralView implements Editor<ObjectExchange<T>>, GeneralListDisplay<T> {

	protected ListView<T, String> list;
	public ListStoreEditor<T> objects;

	protected ContentPanel listPanel;
	protected BorderLayoutContainer mainPanel;
	@Ignore
	protected TextButton addButton;

	public GeneralListView(Double height) {
		mainPanel = new BorderLayoutContainer();
		mainPanel.setHeight((int) (CLIENT_HEIGHT * height));
		mainPanel.setWidth(CLIENT_WIDTH);

		listPanel = new ContentPanel();
		addButton = new TextButton();
		ImageResources bundler = GWT.create(ImageResources.class);
		addButton.setIcon(bundler.add());

		mainPanel.setWestWidget(listPanel, new BorderLayoutData(CLIENT_WIDTH * 0.2));
		GeneralDTOProperties props = GWT.create(GeneralDTOProperties.class);
		list = new ListView<T, String>(new ListStore<T>(props.key()), props.valueProviderLabel());
		list.addDomHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				GeneralListView.this.onDoubleClick();
			}
			
		}, DoubleClickEvent.getType());
		list.setCell(new ToolTipTextCell());
		VerticalLayoutContainer listPanelVerticalContainer = new VerticalLayoutContainer();

		listPanelVerticalContainer.add(new FilterWidget<T>(list.getStore()),
		        new VerticalLayoutData(1, -1));
		listPanelVerticalContainer.add(list, new VerticalLayoutData(1, 1));
		listPanel.add(listPanelVerticalContainer);
		objects = new ListStoreEditor<T>(list.getStore());
		objects.getStore().addSortInfo(
		        new StoreSortInfo<T>(props.valueProviderLabel(), new LowerCaseStringComperator(),
		                SortDir.ASC));
	}

	protected void addButtons() {
		listPanel.addButton(addButton);
	}

	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public void setEditContainer(IsWidget container) {
		mainPanel.setCenterWidget(container);
	}

	@Override
	public void setAddText(String text) {
		addButton.setText(text);
	}

	@Override
	public HasSelectHandlers getAddClick() {
		return addButton;
	}

	@Override
	public void setListHeading(String text) {
		listPanel.setHeadingText(text);
	}

	public void addListEntry(T object) {
		objects.getStore().add(object);
	}

	public void updateListEntry(T object) {
		objects.getStore().applySort(false);
		list.refresh();
	}

	public void removeListEntry(T object) {
		if (objects.getStore().findModel(object) != null) {
			objects.getStore().remove(
			        objects.getStore().findModelWithKey(object.getId().toString()));
		}
	}

	public ListViewSelectionModel<T> getSelectionModel() {
		return list.getSelectionModel();
	}

	public void setList(ArrayList<T> data) {
		list.enable();
		objects.getStore().replaceAll(data);
	}

	public T getSelected() {
		return list.getSelectionModel().getSelectedItem();
	}

	public void setSelected(Integer id) {
		list.getSelectionModel().getSelectedItems().clear();
		list.getSelectionModel().select(objects.getStore().findModelWithKey(id.toString()), false);
	}

	@Override
	public ListStore<T> getStore() {
		return list.getStore();
	}

	@Override
	public void addLoadEntry(T entry) {
		list.getStore().add(entry);
		list.disable();
	}

	@Override
	public void setEditContainerVisible(Boolean visible) {
		mainPanel.getCenterWidget().setVisible(visible);
	}

	@Override
	public boolean getEditContainerVisibility() {
		return mainPanel.getCenterWidget().isVisible();
	}
	
	protected void onDoubleClick() {
        
    }

}