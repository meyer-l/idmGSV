package de.gsv.idm.client.presenter.changeevent;

import java.util.List;
import java.util.logging.Level;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.change.event.UpdatedChangeEventEvent;
import de.gsv.idm.shared.dto.ChangeEventDTO;

public class ManualChangesPresenter extends EditableChangesPresenter {

	public interface ManualChangesDisplay extends EditableChangesDisplay {
		void setAppliedTooltip(String tooltip);
	}

	private ManualChangesDisplay display;
	private final Integer domainId;

	public ManualChangesPresenter(ManualChangesDisplay view, Integer domainId) {
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
				        if (changeEvent.isManual()) {
					        display.addItem(changeEvent);
				        } else {
					        display.removeItem(changeEvent);
				        }
			        }
		        });
	}

	@Override
	protected void setStore() {
		dbConnector.getChangeEventController().getAllManual(domainId,
		        new AsyncCallback<List<ChangeEventDTO>>() {

			        @Override
			        public void onSuccess(List<ChangeEventDTO> result) {
				        display.setStore(result);
			        }

			        @Override
			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(Level.SEVERE,
				                "Error while calling ChangeEventsImpl.getAllManual");
			        }
		        });
	}

	@Override
	protected void individualBindView() {
		display.setHeaderText("Manuell umzusetzende Änderungen");
		display.setAppliedTooltip("Die Änderung wurde erfolgreich manuell"
		        + " in den Informationsverbund eingearbeitet.");
	}

}
