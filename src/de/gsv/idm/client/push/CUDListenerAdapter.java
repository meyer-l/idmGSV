package de.gsv.idm.client.push;

import java.util.List;
import java.util.logging.Level;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralRpcController;
import de.gsv.idm.client.event.db.ReceivedPushEvent;
import de.gsv.idm.client.event.db.pushevents.CreatedPushEventInformation;
import de.gsv.idm.client.event.db.pushevents.DeletedPushEventInformation;
import de.gsv.idm.client.event.db.pushevents.UpdatedPushEventInformation;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.push.event.PushEvent;
import de.novanic.eventservice.client.event.Event;

public abstract class CUDListenerAdapter<T extends PushEvent<T, V>, V extends GeneralDTO<V>>
        implements CUDListener<T> {

	protected final HandlerManager eventBus;
	protected final GeneralRpcController<V> modelController;

	public CUDListenerAdapter(GeneralRpcController<V> modelController) {
		this.eventBus = DBController.getInstance().getEventBus();
		this.modelController = modelController;
	}

	@SuppressWarnings("unchecked")
	public void apply(Event anEvent) {
		if (anEvent instanceof PushEvent) {
			((PushEvent<T, V>) anEvent).call(this);
		}
	}

	public void onCreated(final T createdEvent) {
		modelController.getObject(createdEvent.getObject().getId(), new AsyncCallback<V>() {

			@Override
			public void onFailure(Throwable caught) {
				DBController.getLogger().log(
				        Level.SEVERE,
				        "Error while calling getObject for "
				                + createdEvent.getObject().getClassName() + " (ID: "
				                + createdEvent.getObject().getId() + ")");
			}

			@Override
			public void onSuccess(V result) {
				createdEvent.setObject(modelController.putInMap(result));
				fireCreatedEvent(createdEvent);
				DBController.getLogger().log(Level.INFO,
				        "Created " + result.getClassName() + "(Id: " + result + ")");
				eventBus.fireEvent(new ReceivedPushEvent(new CreatedPushEventInformation(result)));
			}
		});

	}

	public void onDeleted(T deletedEvent) {
		modelController.removeFromMap(deletedEvent.getObject());
		fireDeletedEvent(deletedEvent);
		DBController.getLogger().log(
		        Level.INFO,
		        "Deleted " + deletedEvent.getObject().getClassName() + "(Id: "
		                + deletedEvent.getObject().getId() + ")");
		eventBus.fireEvent(new ReceivedPushEvent(new DeletedPushEventInformation(deletedEvent
		        .getObject())));
	}

	public void onUpdated(final T updatedEvent) {
		DBController.getLogger().log(
		        Level.INFO,
		        "Updated " + updatedEvent.getObject().getClassName() + "(ID: "
		                + updatedEvent.getObject().getId() + ")");

		modelController.getAssociatedObjects(updatedEvent.getObject().getId(),
		        new AsyncCallback<List<GeneralDTO<?>>>() {
			        @Override
			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(
				                Level.SEVERE,
				                "Error while fetching AssociatedObjects for "
				                        + updatedEvent.getObject().getClassName() + " (ID: "
				                        + updatedEvent.getObject().getId() + ")");
			        }

			        @Override
			        public void onSuccess(List<GeneralDTO<?>> result) {
			        	new AssociatedObjectsProcessor(result);
			        }
		        });

		eventBus.fireEvent(new ReceivedPushEvent(new UpdatedPushEventInformation(updatedEvent
		        .getObject())));
	}

	protected abstract void fireCreatedEvent(T createdObject);

	protected abstract void fireDeletedEvent(T deletedObject);

	public abstract void fireUpdatedEvent(V updatedObject);

}
