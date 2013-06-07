package de.gsv.idm.client.presenter.informationdomain.assettype;

import java.util.ArrayList;
import java.util.logging.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.ListStore;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.assettype.CreatedAssettypeEvent;
import de.gsv.idm.client.event.db.assettype.DeletedAssettypeEvent;
import de.gsv.idm.client.event.db.assettype.UpdatedAssettypeEvent;
import de.gsv.idm.client.view.properties.AssettypeDTOProperties;
import de.gsv.idm.client.view.widgets.window.GridToGridRPCChooser;
import de.gsv.idm.shared.dto.AssettypeDTO;

public class AssettypeGridToGridChooser extends GridToGridRPCChooser<AssettypeDTO> {

	public AssettypeGridToGridChooser(ListStore<AssettypeDTO> informationStore, Integer domain_id) {
		super(DBController.getInstance().getAssettypeController(), informationStore,
		        (AssettypeDTOProperties) GWT.create(AssettypeDTOProperties.class), domain_id);
		setEastPanelHeadingText("Ausgewählte Asset-Grundtypen");
		setWestPanelHeadingText("Verfügbare Asset-Grundtypen");
		setHeadingText("Asset-Grundtypen hinzufügen");

		bindEventBus();
		bindView();
	}

	private void bindView() {
		editObject.hide();
		newObject.hide();
	}

	private void bindEventBus() {
		eventBus.addHandler(CreatedAssettypeEvent.TYPE,
		        new GeneralEventHandler<CreatedAssettypeEvent>() {
			        public void onEvent(CreatedAssettypeEvent event) {
				        if (event.getObject().getDomainId().equals(domainId)) {
					        toChoose.getStore().add(event.getObject());
				        }
			        }
		        });

		eventBus.addHandler(UpdatedAssettypeEvent.TYPE,
		        new GeneralEventHandler<UpdatedAssettypeEvent>() {
			        public void onEvent(UpdatedAssettypeEvent event) {
				        if (toChoose.getStore().findModel(event.getObject()) != null) {
					        toChoose.getStore().update(event.getObject());
				        }
				        if (selected.getStore().findModel(event.getObject()) != null) {
					        selected.getStore().update(event.getObject());
				        }

			        }
		        });
		eventBus.addHandler(DeletedAssettypeEvent.TYPE,
		        new GeneralEventHandler<DeletedAssettypeEvent>() {
			        public void onEvent(DeletedAssettypeEvent event) {
				        if (toChoose.getStore().findModel(event.getObject()) != null) {
					        toChoose.getStore().remove(event.getObject());
				        }
				        if (selected.getStore().findModel(event.getObject()) != null) {
					        selected.getStore().remove(event.getObject());
				        }
			        }
		        });
	}

	@Override
	public void callInitStores() {
		DBController.getInstance().getAssettypeController()
		        .getAll(domainId, new AsyncCallback<ArrayList<AssettypeDTO>>() {
			        public void onFailure(Throwable caught) {
			        	DBController.getLogger().log(Level.SEVERE, "Error while calling AssettypeImpl.getAll");
			        }

			        public void onSuccess(ArrayList<AssettypeDTO> result) {
				        initStores(result);
			        }
		        });
	}

}
