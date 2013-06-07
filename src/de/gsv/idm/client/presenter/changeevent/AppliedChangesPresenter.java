package de.gsv.idm.client.presenter.changeevent;

import java.util.List;
import java.util.logging.Level;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.change.event.UpdatedChangeEventEvent;
import de.gsv.idm.client.presenter.general.GeneralGridPresenter;
import de.gsv.idm.shared.dto.ChangeEventDTO;

public class AppliedChangesPresenter extends GeneralGridPresenter<ChangeEventDTO> {

	public interface AppliedChangesDisplay extends GeneralGridDisplay<ChangeEventDTO> {

	}

	private AppliedChangesDisplay display;
	private final Integer domainId;

	public AppliedChangesPresenter(AppliedChangesDisplay view, Integer domainId) {
		super(view);
		display = view;
		this.domainId = domainId;
	}

	protected void bindBus() {

		eventBus.addHandler(UpdatedChangeEventEvent.TYPE,
		        new GeneralEventHandler<UpdatedChangeEventEvent>() {

			        @Override
			        public void onEvent(UpdatedChangeEventEvent event) {

				        ChangeEventDTO changeEvent = event.getObject();
				        if (changeEvent.getApplied()) {
					        display.addItem(changeEvent);
				        } else {
				        	display.removeItem(changeEvent);
				        }
			        }
		        });
	}

	protected void bindView() {
		display.setHeaderText("Umgesetzte Änderungen");

		dbConnector.getChangeEventController().getAllApplied(domainId,
		        new AsyncCallback<List<ChangeEventDTO>>() {

			        @Override
			        public void onSuccess(List<ChangeEventDTO> result) {
				        display.setStore(result);
			        }

			        @Override
			        public void onFailure(Throwable caught) {
			        	DBController.getLogger().log(Level.SEVERE, "Error while calling ChangeEventsImpl.getAllApplied");
			        }
		        });

	}

	public void refreshView() {
	   display.refreshGrid();
    }

}
