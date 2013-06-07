package de.gsv.idm.client.view.occupation.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.occupation.CreatedOccupationEvent;
import de.gsv.idm.client.event.db.occupation.DeletedOccupationEvent;
import de.gsv.idm.client.event.db.occupation.UpdatedOccupationEvent;
import de.gsv.idm.client.view.occupation.OccupationEditorWindow;
import de.gsv.idm.client.view.properties.HasKeyAndName;
import de.gsv.idm.client.view.properties.OccupationDTOProperties;
import de.gsv.idm.client.view.widgets.window.GridToGridRPCChooser;
import de.gsv.idm.shared.dto.OccupationDTO;

public class OccupationGridToGridChooser extends GridToGridRPCChooser<OccupationDTO> {

	@SuppressWarnings("unchecked")
	public OccupationGridToGridChooser(ListStore<OccupationDTO> occupationStore, Integer domain_id) {
		super(DBController.getInstance().getOccupationController(), occupationStore,
		        (HasKeyAndName<OccupationDTO>) GWT.create(OccupationDTOProperties.class), domain_id);
		setEastPanelHeadingText("Ausgewählte Dienstposten");
		setWestPanelHeadingText("Verfügbare Dienstposten");
		setHeadingText("Dienstposten hinzufügen");
		
		bindEventBus();
		bindView();
	}
	
	public void callInitStores(){
		 DBController.getInstance().getOccupationController().getAll(domainId,
		        new AsyncCallback<ArrayList<OccupationDTO>>() {
			        public void onFailure(Throwable caught) {
			        	 DBController.getLogger().log(Level.SEVERE,
					                "Error while calling OccupationImpl.getAll");
			        }

			        public void onSuccess(ArrayList<OccupationDTO> result) {
				        initStores(result);
			        }
		        });
	}

	private void bindEventBus() {
		eventBus.addHandler(CreatedOccupationEvent.TYPE,
		        new GeneralEventHandler<CreatedOccupationEvent>() {
			        public void onEvent(CreatedOccupationEvent event) {
			        	if(event.getObject().getDomainId().equals(domainId)){
			        		toChoose.getStore().add(event.getObject());
			        	}				        
			        }
		        });

		eventBus.addHandler(UpdatedOccupationEvent.TYPE,
		        new GeneralEventHandler<UpdatedOccupationEvent>() {
			        public void onEvent(UpdatedOccupationEvent event) {
				        if (toChoose.getStore().findModel(event.getObject()) != null) {
					        toChoose.getStore().update(event.getObject());
				        }
				        if (selected.getStore().findModel(event.getObject()) != null) {
					        selected.getStore().update(event.getObject());
				        }

			        }
		        });
		eventBus.addHandler(DeletedOccupationEvent.TYPE,
		        new GeneralEventHandler<DeletedOccupationEvent>() {
			        public void onEvent(DeletedOccupationEvent event) {
				        if (toChoose.getStore().findModel(event.getObject()) != null) {
					        toChoose.getStore().remove(event.getObject());
				        }
				        if (selected.getStore().findModel(event.getObject()) != null) {
					        selected.getStore().remove(event.getObject());
				        }
			        }
		        });
	}

	private void bindView() {
		editObject.setText("Dienstposten bearbeiten");
		newObject.setText("Neuen Dienstposten anlegen");
		verticalButtonBuffer.setWidth("245px");
		newObject.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				new OccupationEditorWindow(domainId);
			}
		});

		editObject.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				List<OccupationDTO> selectList = new ArrayList<OccupationDTO>();
				selectList.addAll(toChoose.getSelectionModel().getSelectedItems());
				selectList.addAll(selected.getSelectionModel().getSelectedItems());
				if (selectList.size() == 1) {
					new OccupationEditorWindow(domainId, selectList.get(0));
				}

			}
		});
	}

}
