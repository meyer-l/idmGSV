package de.gsv.idm.client.view.widgets.window;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.cell.core.client.ButtonCell.IconAlign;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.dnd.core.client.ListViewDragSource;
import com.sencha.gxt.dnd.core.client.ListViewDropTarget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

import de.gsv.idm.client.images.ImageResources;
import de.gsv.idm.client.view.properties.HasKeyAndName;
import de.gsv.idm.client.view.widgets.cell.ToolTipTextCell;
import de.gsv.idm.client.view.widgets.form.FilterWidget;
import de.gsv.idm.shared.dto.GeneralDTO;

public class GridToGridChooser<T extends GeneralDTO<T>> extends Window {

	protected TextButton addListEntriesButton;
	protected TextButton removeListEntriesButton;
	protected TextButton cancelButton;
	protected TextButton saveButton;
	private ContentPanel westPanel;
	private ContentPanel eastPanel;
	protected FlowPanel verticalButtonBuffer;
	private ImageResources imageBundler = GWT.create(ImageResources.class);

	protected ListStore<T> generalStore;

	protected ListStore<T> toChooseStore;
	protected ListView<T, String> toChoose;
	protected ListViewDropTarget<T> toChooseDrag;

	protected ListStore<T> selectedStore;
	protected ListView<T, String> selected;
	protected ListViewDropTarget<T> selectedDropTarget;

	public GridToGridChooser(final ListStore<T> informationStore, HasKeyAndName<T> props) {
		this.generalStore = informationStore;

		setBodyBorder(false);
		setWidth(800);
		setHeight(600);

		selectedStore = new ListStore<T>(props.key());
		selected = new ListView<T, String>(selectedStore, props.valueProviderLabel());
		selected.setCell(new ToolTipTextCell());

		toChooseStore = new ListStore<T>(props.key());
		toChoose = new ListView<T, String>(toChooseStore, props.valueProviderLabel());
		toChoose.setCell(new ToolTipTextCell());
		toChooseStore.addSortInfo(new StoreSortInfo<T>(props.valueProviderLabel(), SortDir.ASC));

		selectedStore.replaceAll(informationStore.getAll());
		selectedStore.addSortInfo(new StoreSortInfo<T>(props.valueProviderLabel(), SortDir.ASC));

		new ListViewDragSource<T>(toChoose);
		new ListViewDragSource<T>(selected);

		new ListViewDropTarget<T>(toChoose);
		new ListViewDropTarget<T>(selected);

		setSelectionModels();

		BorderLayoutContainer con = new BorderLayoutContainer();

		westPanel = new ContentPanel();
		VerticalLayoutContainer westPanelVerticalContainer = new VerticalLayoutContainer();
		westPanel.add(westPanelVerticalContainer);
		westPanelVerticalContainer.add(new FilterWidget<T>(toChooseStore), new VerticalLayoutData(
		        1, -1));
		westPanelVerticalContainer.add(toChoose, new VerticalLayoutData(1, 1));
		con.setWestWidget(westPanel, new BorderLayoutData(340));
		eastPanel = new ContentPanel();
		VerticalLayoutContainer eastPanelVerticalContainer = new VerticalLayoutContainer();
		eastPanel.add(eastPanelVerticalContainer);
		eastPanelVerticalContainer.add(new FilterWidget<T>(selectedStore), new VerticalLayoutData(
		        1, -1));
		eastPanelVerticalContainer.add(selected, new VerticalLayoutData(1, 1));
		con.setEastWidget(eastPanel, new BorderLayoutData(340));

		addListEntriesButton = new TextButton();
		addListEntriesButton.setText("Hinzufügen");
		addListEntriesButton.setIcon(imageBundler.arrowRight());
		addListEntriesButton.setIconAlign(IconAlign.RIGHT);
		FlowPanel addPanel = new FlowPanel();
		addPanel.add(addListEntriesButton);

		removeListEntriesButton = new TextButton();
		removeListEntriesButton.setText("Entfernen");
		removeListEntriesButton.setIcon(imageBundler.arrowLeft());
		removeListEntriesButton.setWidth("85px");
		FlowPanel removePanel = new FlowPanel();
		removePanel.add(removeListEntriesButton);

		FlowPanel buttonPanel = new FlowPanel();
		buttonPanel.add(addPanel);
		buttonPanel.add(removePanel);

		CenterLayoutContainer centerPanel = new CenterLayoutContainer();
		centerPanel.add(buttonPanel);
		con.setCenterWidget(centerPanel);

		add(con);
		verticalButtonBuffer = new FlowPanel();
		verticalButtonBuffer.setWidth("270px");
		cancelButton = new TextButton("Abbrechen");
		saveButton = new TextButton("Ok");

		addButtons();
		bindGeneralView();
		show();
	}

	protected void setSelectionModels() {
		selected.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<T>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<T> event) {
				if (event.getSelection().size() > 0) {
					toChoose.getSelectionModel().deselectAll();
				}
			}
		});

		toChoose.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<T>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<T> event) {
				if (event.getSelection().size() > 0) {
					selected.getSelectionModel().deselectAll();
				}
			}
		});
	}

	protected void addButtons() {
		addButton(verticalButtonBuffer);
		addButton(cancelButton);
		addButton(saveButton);
	}

	protected void bindGeneralView() {
		cancelButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				hide();
			}
		});
		addSaveButtonOnSelect();

		addListEntriesButton.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				List<T> selected = toChoose.getSelectionModel().getSelectedItems();
				selectedStore.addAll(selected);
				for (T i : selected) {
					toChooseStore.remove(i);
				}
			}
		});
		removeListEntriesButton.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				List<T> selection = selected.getSelectionModel().getSelectedItems();
				toChooseStore.addAll(selection);
				for (T i : selection) {
					selectedStore.remove(i);
				}
			}
		});

	}

	protected void addSaveButtonOnSelect() {
		saveButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				generalStore.replaceAll(selectedStore.getAll());
				hide();
			}
		});
	}

	public void setEastPanelHeadingText(String text) {
		eastPanel.setHeadingText(text);
	}

	public void setWestPanelHeadingText(String text) {
		westPanel.setHeadingText(text);
	}

	public HasSelectHandlers getAddListEntriesClick() {
		return addListEntriesButton;
	}

	public HasSelectHandlers getRemoveListEntriesClick() {
		return removeListEntriesButton;
	}

}