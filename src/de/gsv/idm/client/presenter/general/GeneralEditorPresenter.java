package de.gsv.idm.client.presenter.general;

import java.util.logging.Level;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralRpcController;
import de.gsv.idm.client.event.db.TransactionBufferEvent;
import de.gsv.idm.client.presenter.Presenter;
import de.gsv.idm.client.transaction.CreateTransaction;
import de.gsv.idm.client.transaction.DeleteTransaction;
import de.gsv.idm.client.transaction.UpdateTransaction;
import de.gsv.idm.client.view.ExtendedDisplayInfo;
import de.gsv.idm.client.view.widgets.window.FormNotValidDialog;
import de.gsv.idm.shared.dto.GeneralDTO;

//TODO: handlen von eine domain create editor aus dem informationsverbund
public abstract class GeneralEditorPresenter<T extends GeneralDTO<T>> implements Presenter {

	public interface GeneralEditorDisplay<T> extends IsWidget {
		void setSaveText(String text);

		void setDeleteText(String text);

		void setEditHeading(String text);

		HasSelectHandlers getSaveClick();

		HasSelectHandlers getDeleteClick();

		void setDeleteEnabled(boolean enabled);

		void setSaveEnabled(boolean enabled);

		Integer getEditorHeight();

		boolean isFormValid();

		void setSaveAndNewText(String text);

		HasSelectHandlers getSaveAndNewClick();

		void setSaveAndNewEnabled(boolean enabled);

		void setSaveAndNewVisibility(boolean visible);

		void resetScrollBar();

		public void setEnabled(Boolean enabled);
	}

	protected final HandlerManager eventBus;
	protected final GeneralRpcController<T> generalRpcController;
	private GeneralEditorDisplay<T> generalEditorDisplay;
	protected T clonedEditObject;
	protected T cachedEditObject;
	protected T referenceObject;

	public GeneralEditorPresenter(GeneralRpcController<T> generalRpcController,
	        GeneralEditorDisplay<T> generalEditorDisplay) {
		this.generalRpcController = generalRpcController;
		this.eventBus = DBController.getInstance().getEventBus();
		this.generalEditorDisplay = generalEditorDisplay;
		generalBindView();
	}

	private void generalBindView() {
		generalEditorDisplay.setSaveText("Speichern");
		generalEditorDisplay.setDeleteText("Löschen");
		generalEditorDisplay.setSaveAndNewText("Speichern & Neu");

		generalEditorDisplay.getSaveClick().addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				if (generalEditorDisplay.isFormValid()) {
					generalEditorDisplay.setSaveAndNewEnabled(false);
					generalEditorDisplay.setSaveEnabled(false);
					doSave();

				} else {
					new FormNotValidDialog("Formulardaten nicht gültig",
					        "Es wurden nicht alle benötigten Formulardaten angegeben. Bitte überprüfen Sie die Eingaben.");
				}
			}
		});

		generalEditorDisplay.getDeleteClick().addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				doDelete();
			}
		});

		generalEditorDisplay.getSaveAndNewClick().addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				if (generalEditorDisplay.isFormValid()) {
					generalEditorDisplay.setSaveAndNewEnabled(false);
					generalEditorDisplay.setSaveEnabled(false);
					saveAndNew();
				} else {
					new FormNotValidDialog("Formulardaten nicht gültig",
					        "Es wurden nicht alle benötigten Formulardaten angegeben. Bitte überprüfen Sie die Eingaben.");
				}
			}

		});
	}

	private void saveAndNew() {
		final T flushedObject = getFlushedObject();
		if (flushedObject == null || flushedObject.getId() == null || flushedObject.getId() <= 0) {
			generalRpcController.create(flushedObject, new AsyncCallback<T>() {
				public void onSuccess(T result) {
					fireDeletedEvent();
					String text = result.getClassName() + " " + result.getLabel()
					        + " wurde erstellt. (ID: " + result.getId() + ")";
					eventBus.fireEvent(new TransactionBufferEvent(new CreateTransaction<T>(
					        generalRpcController, result)));
					ExtendedDisplayInfo.display("RPC:", text);
					doNew();
				}

				public void onFailure(Throwable caught) {
					DBController.getLogger().log(Level.SEVERE,
					        "Error while creating " + flushedObject.getClassName());
				}
			});
		} else {
			generalRpcController.update(flushedObject, new AsyncCallback<T>() {
				public void onSuccess(T result) {
					String text = result.getClassName() + " " + result.getLabel()
					        + " wurde aktualisiert. (ID: " + result.getId() + ")";
					TransactionBufferEvent event = new TransactionBufferEvent(
					        new UpdateTransaction<T>(generalRpcController, clonedEditObject,
					                result, text));
					eventBus.fireEvent(event);
					ExtendedDisplayInfo.display("RPC:", text);
					doNew();
				}

				public void onFailure(Throwable caught) {
					DBController.getLogger().log(
					        Level.SEVERE,
					        "Error while updating " + flushedObject.getClassName() + " Id: "
					                + flushedObject.getId());
				}
			});
		}
	}

	abstract protected T getFlushedObject();

	public T getEditObject() {
		return clonedEditObject;
	}

	public void createObject(final T object) {
		generalRpcController.create(object, new AsyncCallback<T>() {
			public void onSuccess(T result) {
				clonedEditObject = result;
				fireDeletedEvent();
				String text = result.getClassName() + " " + result.getLabel()
				        + " wurde erstellt. (ID: " + result.getId() + ")";
				eventBus.fireEvent(new TransactionBufferEvent(new CreateTransaction<T>(
				        generalRpcController, result)));
				ExtendedDisplayInfo.display("RPC:", text);
				edit(result);
			}

			public void onFailure(Throwable caught) {
				DBController.getLogger().log(Level.SEVERE,
				        "Error while creating " + object.getClassName());
			}
		});
	}

	abstract protected void fireDeletedEvent();

	public void updateObject(final T object, final T oldEditObject) {
		generalRpcController.update(object, new AsyncCallback<T>() {
			public void onSuccess(T result) {
				String text = result.getClassName() + " " + result.getLabel()
				        + " wurde aktualisiert. (ID: " + result.getId() + ")";
				TransactionBufferEvent event = new TransactionBufferEvent(new UpdateTransaction<T>(
				        generalRpcController, oldEditObject, result, text));
				eventBus.fireEvent(event);
				ExtendedDisplayInfo.display("RPC:", text);
			}

			public void onFailure(Throwable caught) {
				DBController.getLogger().log(Level.SEVERE,
				        "Error while updating " + object.getClassName() + " Id: " + object.getId());
			}
		});
	}

	public void deleteObject(final T object) {
		generalRpcController.delete(object, new AsyncCallback<T>() {
			public void onFailure(Throwable caught) {
				DBController.getLogger().log(Level.SEVERE,
				        "Error while deleting " + object.getClassName() + " Id: " + object.getId());
			}

			public void onSuccess(T result) {
				String text = result.getClassName() + " " + result.getLabel() + " wurde gelöscht.";
				eventBus.fireEvent(new TransactionBufferEvent(new DeleteTransaction<T>(
				        generalRpcController, object)));
				ExtendedDisplayInfo.display("RPC:", text);
			}
		});
	}

	public void edit(final T object) {
		if (object.getId() > 0) {
			generalRpcController.getObject(object.getId(), new AsyncCallback<T>() {
				@Override
				public void onFailure(Throwable caught) {
					DBController.getLogger().log(
					        Level.SEVERE,
					        "Error while fetching " + object.getClassName() + " Id: "
					                + object.getId());
				}

				@Override
				public void onSuccess(T result) {
					doEdit(result);
				}
			});
		} else {
			doEdit(object);
		}

	}

	public GeneralEditorDisplay<T> getDisplay() {
		return generalEditorDisplay;
	}

	public Integer getEditorHeight() {
		return generalEditorDisplay.getEditorHeight();
	}

	protected Integer createTempId() {
		Integer tempId = Random.nextInt();
		if (tempId > 1)
			tempId *= -1;
		// -1 is reserved for no Parent
		tempId -= 2;
		return tempId;
	}

	protected void resetScrollBar() {
		generalEditorDisplay.resetScrollBar();
	}

	abstract public void doNew();

	abstract public void doSave();

	abstract public void doDelete();

	abstract public void doEdit(T object);

	abstract public boolean openChanges();

}
