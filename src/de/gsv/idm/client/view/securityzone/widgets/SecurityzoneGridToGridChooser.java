package de.gsv.idm.client.view.securityzone.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralRpcController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.securityzone.CreatedSecurityzoneEvent;
import de.gsv.idm.client.event.db.securityzone.DeletedSecurityzoneEvent;
import de.gsv.idm.client.event.db.securityzone.UpdatedSecurityzoneEvent;
import de.gsv.idm.client.view.properties.HasKeyAndName;
import de.gsv.idm.client.view.properties.SecurityzoneDTOProperties;
import de.gsv.idm.client.view.securityzone.SecurityzoneEditorWindow;
import de.gsv.idm.client.view.widgets.window.GridToGridRPCChooser;
import de.gsv.idm.shared.dto.AssettypeMeasureLinkDTO;
import de.gsv.idm.shared.dto.MeasureDTO;
import de.gsv.idm.shared.dto.SecurityzoneDTO;

public class SecurityzoneGridToGridChooser extends GridToGridRPCChooser<SecurityzoneDTO> {

	private ListStore<AssettypeMeasureLinkDTO> measureStore;
	private ListStore<SecurityzoneDTO> preSelectedStore;

	public SecurityzoneGridToGridChooser(GeneralRpcController<SecurityzoneDTO> rpcService,
	        ListStore<SecurityzoneDTO> securityzoneStore,
	        ListStore<AssettypeMeasureLinkDTO> listStore, HasKeyAndName<SecurityzoneDTO> props) {
		super(rpcService, securityzoneStore, props, -1);
		setEastPanelHeadingText("Ausgewählte Schutzzonen");
		setWestPanelHeadingText("Verfügbare Schutzzonen");
		setHeadingText("Schutzzonen bearbeiten");
		measureStore = listStore;
		preSelectedStore = securityzoneStore;
		bindEventBus();
		bindView();
	}

	public SecurityzoneGridToGridChooser(ListStore<SecurityzoneDTO> informationStore,
	        ListStore<AssettypeMeasureLinkDTO> listStore) {
		this(DBController.getInstance().getSecurityzoneController(), informationStore,
		        listStore, (SecurityzoneDTOProperties) GWT.create(SecurityzoneDTOProperties.class));
	}

	@Override
	public void callInitStores() {
		DBController.getInstance().getSecurityzoneController()
		        .getAll(new AsyncCallback<ArrayList<SecurityzoneDTO>>() {
			        public void onFailure(Throwable caught) {
			        	DBController.getLogger().log(Level.SEVERE,
				                "Error while calling SecurityzoneImpl.getAll");
			        }

			        public void onSuccess(ArrayList<SecurityzoneDTO> result) {
				        initStores(result);
			        }
		        });
	}

	private void bindEventBus() {
		eventBus.addHandler(CreatedSecurityzoneEvent.TYPE,
		        new GeneralEventHandler<CreatedSecurityzoneEvent>() {
			        public void onEvent(CreatedSecurityzoneEvent event) {
				        toChoose.getStore().add(event.getObject());
			        }
		        });

		eventBus.addHandler(UpdatedSecurityzoneEvent.TYPE,
		        new GeneralEventHandler<UpdatedSecurityzoneEvent>() {
			        public void onEvent(UpdatedSecurityzoneEvent event) {
				        if (toChoose.getStore().findModel(event.getObject()) != null) {
					        toChoose.getStore().update(event.getObject());
				        }
				        if (selected.getStore().findModel(event.getObject()) != null) {
					        selected.getStore().update(event.getObject());
				        }

			        }
		        });
		eventBus.addHandler(DeletedSecurityzoneEvent.TYPE,
		        new GeneralEventHandler<DeletedSecurityzoneEvent>() {
			        public void onEvent(DeletedSecurityzoneEvent event) {
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
		editObject.setText("Schutzzone bearbeiten");
		newObject.setText("Neue Schutzzone anlegen");
		verticalButtonBuffer.setWidth("245px");
		newObject.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				new SecurityzoneEditorWindow();
			}
		});

		editObject.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				List<SecurityzoneDTO> selectList = new ArrayList<SecurityzoneDTO>();
				selectList.addAll(toChoose.getSelectionModel().getSelectedItems());
				selectList.addAll(selected.getSelectionModel().getSelectedItems());
				if (selectList.size() == 1) {
					new SecurityzoneEditorWindow(selectList.get(0));
				}

			}
		});
	}

	@Override
	public void addSaveButtonOnSelect() {
		saveButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				List<SecurityzoneDTO> selected = new ArrayList<SecurityzoneDTO>(selectedStore
				        .getAll());

				List<SecurityzoneDTO> preSelectedList = new ArrayList<SecurityzoneDTO>(
				        preSelectedStore.getAll());
				List<SecurityzoneDTO> removedSecurityzones = new ArrayList<SecurityzoneDTO>(
				        preSelectedList);
				preSelectedList.retainAll(selected); // preSelected has now
				                                     // only Securityzones, that are
				                                     // still selected
				selected.removeAll(preSelectedList); // converted now has all
				                                     // new Securityzones
				removedSecurityzones.removeAll(preSelectedList); // All removed
				                                                 // Securityzones
				
				if(measureStore != null){
					List<AssettypeMeasureLinkDTO> currentMeasures = measureStore.getAll();
					for (SecurityzoneDTO newLink : selected) {
						for (MeasureDTO measure : newLink.getMeasures()) {
							AssettypeMeasureLinkDTO newMeasureLink = new AssettypeMeasureLinkDTO(
							        measure, false);
							if (currentMeasures.contains(newMeasureLink)) {
								currentMeasures.get(currentMeasures.indexOf(newMeasureLink))
								        .getLinkedSecurityzones().add(newLink);
							} else {
								newMeasureLink.getLinkedSecurityzones().add(newLink);
								measureStore.add(newMeasureLink);
							}
						}
					}

					for (SecurityzoneDTO oldLink : removedSecurityzones) {
						for (MeasureDTO measure : oldLink.getMeasures()) {
							AssettypeMeasureLinkDTO newMeasureLink = new AssettypeMeasureLinkDTO(
							        measure, false);
							if (currentMeasures.contains(newMeasureLink)) {
								AssettypeMeasureLinkDTO theLink = currentMeasures.get(currentMeasures
								        .indexOf(newMeasureLink));
								if (theLink.getLinkedSecurityzones().contains(oldLink)) {
									theLink.getLinkedSecurityzones().remove(oldLink);
								}
								if (theLink.getLinkedSecurityzones().size() < 1 && theLink.getLinkedModules().size() < 1) {
									measureStore.remove(newMeasureLink);
								}

							}
						}
					}
				}
				

				List<SecurityzoneDTO> toReplace = new ArrayList<SecurityzoneDTO>(selected);
				toReplace.addAll(preSelectedList);
				preSelectedStore.replaceAll(toReplace);

				hide();
			}
		});
	}
}
