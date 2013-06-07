package de.gsv.idm.client.view.informationdomain.assettype.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.view.properties.HasKeyAndName;
import de.gsv.idm.client.view.properties.ModuleDTOProperties;
import de.gsv.idm.client.view.widgets.window.GridToGridChooser;
import de.gsv.idm.shared.comperator.GSKComperator;
import de.gsv.idm.shared.dto.AssettypeMeasureLinkDTO;
import de.gsv.idm.shared.dto.AssettypeModuleLinkDTO;
import de.gsv.idm.shared.dto.MeasureLinkDTO;
import de.gsv.idm.shared.dto.ModuleDTO;

public class AssettypeModuleGridToGridChooser extends GridToGridChooser<ModuleDTO> {

	private ListStore<AssettypeModuleLinkDTO> preSelectedStore;
	private ListStore<AssettypeMeasureLinkDTO> measureStore;

	public AssettypeModuleGridToGridChooser(ListStore<AssettypeModuleLinkDTO> preSelectedStore,
	        HasKeyAndName<ModuleDTO> moduleProps, ListStore<AssettypeMeasureLinkDTO> measureStore) {
		super(new ListStore<ModuleDTO>(moduleProps.key()), moduleProps);
		this.preSelectedStore = preSelectedStore;
		this.measureStore = measureStore;

		final ArrayList<ModuleDTO> converted = new ArrayList<ModuleDTO>();
		for (AssettypeModuleLinkDTO pre : preSelectedStore.getAll()) {
			converted.add(pre.getModule());
		}

		selectedStore.getSortInfo().clear();
		selectedStore.addSortInfo(new StoreSortInfo<ModuleDTO>(moduleProps.name(),
		        new GSKComperator(), SortDir.ASC));
		toChooseStore.getSortInfo().clear();
		toChooseStore.addSortInfo(new StoreSortInfo<ModuleDTO>(moduleProps.name(),
		        new GSKComperator(), SortDir.ASC));
		toChooseStore.add(new ModuleDTO(-1, "... Bausteine werden vom Server geladen ..."));
		toChoose.disable();
		DBController.getInstance().getModuleController()
		        .getAll(new AsyncCallback<ArrayList<ModuleDTO>>() {

			        @Override
			        public void onSuccess(ArrayList<ModuleDTO> result) {
				        toChoose.enable();
				        result.removeAll(converted);
				        toChooseStore.replaceAll(result);
			        }

			        @Override
			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(Level.SEVERE,
				                "Error while calling ModulesImpl.getAll");
			        }

		        });

		selectedStore.replaceAll(converted);

		setEastPanelHeadingText("Ausgewählte Bausteine");
		setWestPanelHeadingText("Auswählbare Bausteine");

	}

	public AssettypeModuleGridToGridChooser(ListStore<AssettypeModuleLinkDTO> preSelectedStore,
	        ListStore<AssettypeMeasureLinkDTO> measureStore) {
		this(preSelectedStore, (ModuleDTOProperties) GWT.create(ModuleDTOProperties.class),
		        measureStore);
	}

	@Override
	public void addSaveButtonOnSelect() {
		saveButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				List<AssettypeModuleLinkDTO> converted = new ArrayList<AssettypeModuleLinkDTO>();
				for (ModuleDTO link : selectedStore.getAll()) {
					converted.add(new AssettypeModuleLinkDTO(link));
				}

				List<AssettypeModuleLinkDTO> preSelectedList = new ArrayList<AssettypeModuleLinkDTO>(
				        preSelectedStore.getAll());
				List<AssettypeModuleLinkDTO> removedModules = new ArrayList<AssettypeModuleLinkDTO>(
				        preSelectedList);
				preSelectedList.retainAll(converted); // preSelected has now
				                                      // only Modules, that are
				                                      // still selected
				converted.removeAll(preSelectedList); // converted now has all
				                                      // new Modules
				removedModules.removeAll(preSelectedList); // All removed
				                                           // modules
				List<AssettypeMeasureLinkDTO> currentMeasures = measureStore.getAll();
				for (AssettypeModuleLinkDTO newLink : converted) {
					for (MeasureLinkDTO measureLink : newLink.getModule().getMeasures()) {
						AssettypeMeasureLinkDTO newMeasureLink = new AssettypeMeasureLinkDTO(
						        measureLink.getMeasure(), false);
						if (currentMeasures.contains(newMeasureLink)) {
							currentMeasures.get(currentMeasures.indexOf(newMeasureLink))
							        .getLinkedModules().add(newLink.getModule());
						} else {
							newMeasureLink.getLinkedModules().add(newLink.getModule());
							measureStore.add(newMeasureLink);
						}
					}
				}

				for (AssettypeModuleLinkDTO oldLink : removedModules) {
					for (MeasureLinkDTO measureLink : oldLink.getModule().getMeasures()) {
						AssettypeMeasureLinkDTO newMeasureLink = new AssettypeMeasureLinkDTO(
						        measureLink.getMeasure(), false);
						if (currentMeasures.contains(newMeasureLink)) {
							AssettypeMeasureLinkDTO theLink = currentMeasures.get(currentMeasures
							        .indexOf(newMeasureLink));
							if (theLink.getLinkedModules().contains(oldLink.getModule())) {
								theLink.getLinkedModules().remove(oldLink.getModule());
							}
							if (theLink.getLinkedSecurityzones().size() < 1
							        && theLink.getLinkedModules().size() < 1
							        && !theLink.isManualAdd()) {
								measureStore.remove(newMeasureLink);
							}

						}
					}
				}

				List<AssettypeModuleLinkDTO> toReplace = new ArrayList<AssettypeModuleLinkDTO>(
				        converted);
				toReplace.addAll(preSelectedList);

				preSelectedStore.replaceAll(toReplace);

				hide();
			}
		});

	}

}
