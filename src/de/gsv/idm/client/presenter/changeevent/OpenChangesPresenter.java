package de.gsv.idm.client.presenter.changeevent;

import java.util.List;
import java.util.logging.Level;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.change.event.CreatedChangeEventEvent;
import de.gsv.idm.client.event.db.change.event.UpdatedChangeEventEvent;
import de.gsv.idm.client.view.widgets.upload.TelefonListUploadWindow;
import de.gsv.idm.shared.dto.ChangeEventDTO;

public class OpenChangesPresenter extends EditableChangesPresenter {

	public interface OpenChangesDisplay extends EditableChangesDisplay {
		public void setUploadTelefonlistButtonText(String text);
		public HasSelectHandlers getUploadTelefonlistButtonClick();
	}

	private OpenChangesDisplay display;
	private final Integer domainId;

	public OpenChangesPresenter(OpenChangesDisplay view, Integer domainId) {
		super(view, domainId);
		display = view;
		this.domainId = domainId;
	}

	protected void bindBus() {

		eventBus.addHandler(UpdatedChangeEventEvent.TYPE,
		        new GeneralEventHandler<UpdatedChangeEventEvent>() {

			        @Override
			        public void onEvent(UpdatedChangeEventEvent event) {

				        ChangeEventDTO changeEvent = event.getObject();
				        if (!changeEvent.notProcessed()) {
					        display.removeItem(changeEvent);
				        } else {
				        	display.addItem(changeEvent);
				        }
			        }
		        });

		eventBus.addHandler(CreatedChangeEventEvent.TYPE,
		        new GeneralEventHandler<CreatedChangeEventEvent>() {

			        @Override
			        public void onEvent(CreatedChangeEventEvent event) {
				        ChangeEventDTO changeEvent = event.getObject();
				        display.addItem(changeEvent);
			        }
		        });

	}

	@Override
	protected void setStore() {
		dbConnector.getChangeEventController().getAllNotProcessed(domainId,
		        new AsyncCallback<List<ChangeEventDTO>>() {

			        @Override
			        public void onSuccess(List<ChangeEventDTO> result) {
				        display.setStore(result);
			        }

			        @Override
			        public void onFailure(Throwable caught) {
			        	DBController.getLogger().log(Level.SEVERE, "Error while calling ChangeEventsImpl.getAllNotProcessed");
			        }
		        });
	}

	@Override
	protected void individualBindView() {
		display.setHeaderText("Offene Änderungen");
		display.setUploadTelefonlistButtonText("Telefonliste einlesen");
		display.getUploadTelefonlistButtonClick().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				new TelefonListUploadWindow(domainId, dbConnector.getSessionId());
			}
		});
	}

}
