package de.gsv.idm.client.view.widgets.window;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralRpcController;
import de.gsv.idm.client.event.db.TransactionBufferEvent;
import de.gsv.idm.client.transaction.DeleteTransaction;
import de.gsv.idm.client.view.properties.HasKeyAndName;
import de.gsv.idm.shared.dto.GeneralDTO;

public abstract class GridToGridRPCChooser<T extends GeneralDTO<T>> extends GridToGridChooser<T> {

	private TextButton deleteObject;
	protected Integer domainId;
	protected TextButton editObject;
	protected TextButton newObject;

	private final GeneralRpcController<T> generalRpcController;
	protected final HandlerManager eventBus;

	public GridToGridRPCChooser(GeneralRpcController<T> rpcService,
	        final ListStore<T> informationStore, HasKeyAndName<T> props, Integer domain_id) {
		super(informationStore, props);
		this.generalRpcController = rpcService;
		this.eventBus = DBController.getInstance().getEventBus();
		this.domainId = domain_id;
		callInitStores();
	}

	@Override
	protected void setSelectionModels() {
		selected.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<T>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<T> event) {
				if (event.getSelection().size() > 0) {
					if (event.getSelection().size() == 1) {
						editObject.setEnabled(true);
					} else {
						editObject.setEnabled(false);
					}
					deleteObject.setEnabled(true);
					toChoose.getSelectionModel().deselectAll();
				} else {
					editObject.setEnabled(false);
					deleteObject.setEnabled(false);
				}
			}
		});

		toChoose.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<T>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<T> event) {
				if (event.getSelection().size() > 0) {
					if (event.getSelection().size() == 1) {
						editObject.setEnabled(true);
					} else {
						editObject.setEnabled(false);
					}
					deleteObject.setEnabled(true);
					selected.getSelectionModel().deselectAll();
				} else {
					editObject.setEnabled(false);
					deleteObject.setEnabled(false);
				}
			}
		});
	}

	@Override
	protected void addButtons() {
		deleteObject = new TextButton("Löschen");
		deleteObject.setEnabled(false);
		editObject = new TextButton();
		editObject.setEnabled(false);
		newObject = new TextButton();
		addButton(deleteObject);
		addButton(editObject);
		addButton(newObject);
		super.addButtons();
	}

	@Override
	protected void bindGeneralView() {
		super.bindGeneralView();
		deleteObject.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				List<T> deleteItems = new ArrayList<T>();
				deleteItems.addAll(toChoose.getSelectionModel().getSelectedItems());
				deleteItems.addAll(selected.getSelectionModel().getSelectedItems());
				for (final T item : deleteItems) {
					generalRpcController.delete(item, new AsyncCallback<T>() {
						public void onFailure(Throwable caught) {
							DBController.getLogger().log(Level.SEVERE,
					                "Error while calling delete for " + item.getClassName() + "Id: " + item.getId());
						}

						public void onSuccess(T result) {
							String text = result.getClassName() + " " + result.getName()
							        + " wurde gelöscht.";
							eventBus.fireEvent(new TransactionBufferEvent(new DeleteTransaction<T>(
							        generalRpcController, result)));
							Info.display("RPC:", text);
						}
					});
				}
			}
		});
	}

	protected void addSaveButtonOnSelect() {
		saveButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				@SuppressWarnings({ "rawtypes", "unchecked" })
                ArrayList<T> newList = new ArrayList(selectedStore.getAll());
				generalStore.replaceAll(newList);
				for (final T item : generalStore.getAll()) {
					if (item.isSlim()) {
						generalRpcController.getObject(item.getId(), new AsyncCallback<T>() {
							
							public void onFailure(Throwable caught) {
								DBController.getLogger().log(Level.SEVERE,
						                "Error while calling getObject for " + item.getClassName() + "Id: " + item.getId());
							}
							public void onSuccess(T result) {
								generalStore.update(result);
							}
						});
					}
				}
				hide();
			}
		});
	}

	protected void initStores(List<T> result) {
		result.removeAll(generalStore.getAll());
		toChooseStore.replaceAll(result);
	}
	
	public abstract void callInitStores();

	public void setDeleteObjectButtonText(String text) {
		deleteObject.setText(text);
	}

	public void setEditObjectButtonText(String text) {
		editObject.setText(text);
	}

	public void setNewObjectButtonText(String text) {
		newObject.setText(text);
	}
	
	public void hideRPCButtons(){
		deleteObject.hide();
		editObject.hide();
		newObject.hide();
	}
}
