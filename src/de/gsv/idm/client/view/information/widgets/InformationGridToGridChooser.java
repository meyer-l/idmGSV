package de.gsv.idm.client.view.information.widgets;

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
import de.gsv.idm.client.event.db.information.CreatedInformationEvent;
import de.gsv.idm.client.event.db.information.DeletedInformationEvent;
import de.gsv.idm.client.event.db.information.UpdatedInformationEvent;
import de.gsv.idm.client.view.information.InformationEditorWindow;
import de.gsv.idm.client.view.properties.HasKeyAndName;
import de.gsv.idm.client.view.properties.InformationDTOProperties;
import de.gsv.idm.client.view.widgets.window.GridToGridRPCChooser;
import de.gsv.idm.shared.dto.InformationDTO;

public class InformationGridToGridChooser extends GridToGridRPCChooser<InformationDTO> {

	@SuppressWarnings("unchecked")
	public InformationGridToGridChooser(final ListStore<InformationDTO> informationStore,
	        Integer domain_id) {
		super(DBController.getInstance().getInformationController(), informationStore,
		        (HasKeyAndName<InformationDTO>) GWT.create(InformationDTOProperties.class),
		        domain_id);

		setEastPanelHeadingText("Ausgewählte Informationen");
		setWestPanelHeadingText("Verfügbare Informationen");
		setHeadingText("Informationen hinzufügen");
		bindEventBus();
		bindView();
	}

	public void callInitStores() {
		DBController.getInstance().getInformationController()
		        .getAll(domainId, new AsyncCallback<ArrayList<InformationDTO>>() {
			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(Level.SEVERE,
				                "Error while calling InformationImpl.getAll");
			        }

			        public void onSuccess(ArrayList<InformationDTO> result) {
				        initStores(result);
			        }
		        });
	}

	private void bindEventBus() {
		eventBus.addHandler(CreatedInformationEvent.TYPE,
		        new GeneralEventHandler<CreatedInformationEvent>() {
			        public void onEvent(CreatedInformationEvent event) {
				        if (event.getObject().getDomainId().equals(domainId)) {
					        toChoose.getStore().add(event.getObject());
				        }

			        }
		        });

		eventBus.addHandler(UpdatedInformationEvent.TYPE,
		        new GeneralEventHandler<UpdatedInformationEvent>() {
			        public void onEvent(UpdatedInformationEvent event) {
				        if (toChoose.getStore().findModel(event.getObject()) != null) {
					        toChoose.getStore().update(event.getObject());
				        }
				        if (selected.getStore().findModel(event.getObject()) != null) {
					        selected.getStore().update(event.getObject());
				        }
			        }
		        });

		eventBus.addHandler(DeletedInformationEvent.TYPE,
		        new GeneralEventHandler<DeletedInformationEvent>() {
			        public void onEvent(DeletedInformationEvent event) {
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
		editObject.setText("Information bearbeiten");
		newObject.setText("Neue Information anlegen");
		newObject.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				new InformationEditorWindow(domainId);
			}
		});

		editObject.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				List<InformationDTO> selectList = new ArrayList<InformationDTO>();
				selectList.addAll(toChoose.getSelectionModel().getSelectedItems());
				selectList.addAll(selected.getSelectionModel().getSelectedItems());
				if (selectList.size() == 1) {
					new InformationEditorWindow(domainId, selectList.get(0));
				}

			}
		});
	}
}
