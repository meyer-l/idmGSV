package de.gsv.idm.client.presenter.general;

import java.util.ArrayList;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ListViewSelectionModel;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.presenter.ListPresenter;
import de.gsv.idm.client.presenter.general.GeneralEditorPresenter.GeneralEditorDisplay;
import de.gsv.idm.client.view.properties.ObjectExchange;
import de.gsv.idm.shared.dto.GeneralDTO;

public abstract class GeneralListPresenter<T extends GeneralDTO<T>> implements ListPresenter {

	public abstract interface GeneralListDisplay<T> extends IsWidget {
		void setEditContainer(IsWidget container);
		
		void setEditContainerVisible(Boolean visible);

		void setAddText(String text);

		HasSelectHandlers getAddClick();

		void setListHeading(String text);

		ListStore<T> getStore();

		void setList(ArrayList<T> result);

		void addListEntry(T object);

		void updateListEntry(T object);

		void removeListEntry(T object);

		ListViewSelectionModel<T> getSelectionModel();

		T getSelected();

		void setSelected(Integer id);
		void addLoadEntry(T entry);

		boolean getEditContainerVisibility();
	}

	protected final HandlerManager eventBus;

	protected ObjectExchange<T> objectExchange;
	private final GeneralListDisplay<T> generalDisplay;
	private GeneralEditorDisplay<T> generalEditorDisplay;
	private GeneralEditorPresenter<T> generalEditorPresenter;

	public GeneralListPresenter(GeneralListDisplay<T> display) {
		eventBus = DBController.getInstance().getEventBus();

		generalDisplay = display;
		objectExchange = new ObjectExchange<T>();
	}

	protected void setEditor(GeneralEditorPresenter<T> generalEditorPresenter) {
		this.generalEditorPresenter = generalEditorPresenter;
		this.generalEditorDisplay = generalEditorPresenter.getDisplay();
	}

	protected void bindGeneralView() {
		generalDisplay.getAddClick().addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				generalDisplay.getSelectionModel().deselectAll();
				generalEditorPresenter.doNew();
			}
		});

		bindSelectionModel();
		fillList();
	}

	protected void bindSelectionModel() {
		generalDisplay.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		generalDisplay.getSelectionModel().addSelectionChangedHandler(
		        new SelectionChangedHandler<T>() {
			        @Override
			        public void onSelectionChanged(SelectionChangedEvent<T> event) {
				        if (event.getSelection().size() > 0) {
				        	final T object = event.getSelection().get(0);
				        	if (generalDisplay.getEditContainerVisibility() && generalEditorPresenter.openChanges()) {
						        final MessageBox messageBox = new MessageBox("Änderungen verwerfen?",
						                "Sie haben ungespeicherte Änderungen, wollen sie die Änderungen speichern?");
						        messageBox.getButtonBar().clear();
						        TextButton saveButton = new TextButton("Speichern");
						        saveButton.addSelectHandler(new SelectHandler() {

							        @Override
							        public void onSelect(SelectEvent event) {
							        	generalEditorPresenter.doSave();
							        	generalEditorPresenter.edit(object);
							        	generalDisplay.setEditContainerVisible(true);
								        messageBox.hide();
							        }
						        });
						        TextButton abortButton = new TextButton("Verwerfen");
						        abortButton.addSelectHandler(new SelectHandler() {

							        @Override
							        public void onSelect(SelectEvent event) {
							        	generalEditorPresenter.edit(object);
							        	generalDisplay.setEditContainerVisible(true);
								        messageBox.hide();
							        }
						        });
						        messageBox.addButton(abortButton);
						        messageBox.addButton(saveButton);
						        messageBox.show();
					        } else {
					        	generalEditorPresenter.edit(object);
					        	generalEditorDisplay.setEnabled(true);
					        	generalDisplay.setEditContainerVisible(true);
					        }
				        } else {
				        	generalEditorDisplay.setSaveEnabled(false);
					        generalEditorDisplay.setDeleteEnabled(false);
					        generalEditorDisplay.setSaveAndNewEnabled(false);
					        generalDisplay.setEditContainerVisible(false);
				        }
			        }
		        });
	}

	abstract protected void fillList();

}
