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
import de.gsv.idm.client.view.properties.MeasureDTOProperties;
import de.gsv.idm.client.view.widgets.window.GridToGridChooser;
import de.gsv.idm.shared.comperator.GSKComperator;
import de.gsv.idm.shared.dto.AssettypeMeasureLinkDTO;
import de.gsv.idm.shared.dto.MeasureDTO;

public class AssettypeMeasureGridToGridChooser extends GridToGridChooser<MeasureDTO> {

	private ListStore<AssettypeMeasureLinkDTO> preSelectedStore;

	public AssettypeMeasureGridToGridChooser(ListStore<AssettypeMeasureLinkDTO> preSelectedStore,
	        HasKeyAndName<MeasureDTO> props) {
		super(new ListStore<MeasureDTO>(props.key()), props);
		this.preSelectedStore = preSelectedStore;

		final ArrayList<MeasureDTO> converted = new ArrayList<MeasureDTO>();
		for (AssettypeMeasureLinkDTO pre : preSelectedStore.getAll()) {
			converted.add(pre.getMeasure());
		}

		selectedStore.getSortInfo().clear();
		selectedStore.addSortInfo(new StoreSortInfo<MeasureDTO>(props.name(), new GSKComperator(),
		        SortDir.ASC));
		toChooseStore.getSortInfo().clear();
		toChooseStore.addSortInfo(new StoreSortInfo<MeasureDTO>(props.name(), new GSKComperator(),
		        SortDir.ASC));

		toChooseStore.add(new MeasureDTO(-1, "... Maßnahmen werden vom Server geladen ..."));
		toChoose.disable();
		DBController.getInstance().getMeasureController()
		        .getAll(new AsyncCallback<ArrayList<MeasureDTO>>() {

			        @Override
			        public void onSuccess(ArrayList<MeasureDTO> result) {
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

		setEastPanelHeadingText("Ausgewählte Maßnahmen");
		setWestPanelHeadingText("Auswählbare Maßnahmen");

	}

	public AssettypeMeasureGridToGridChooser(ListStore<AssettypeMeasureLinkDTO> preSelectedStore) {
		this(preSelectedStore, (MeasureDTOProperties) GWT.create(MeasureDTOProperties.class));
	}

	@Override
	public void addSaveButtonOnSelect() {
		saveButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				List<AssettypeMeasureLinkDTO> converted = new ArrayList<AssettypeMeasureLinkDTO>();
				for (MeasureDTO link : selectedStore.getAll()) {
					converted.add(new AssettypeMeasureLinkDTO(link, true));
				}
				List<AssettypeMeasureLinkDTO> preSelectedList = new ArrayList<AssettypeMeasureLinkDTO>(
				        preSelectedStore.getAll());
				preSelectedList.retainAll(converted);
				converted.removeAll(preSelectedList);
				List<AssettypeMeasureLinkDTO> toReplace = new ArrayList<AssettypeMeasureLinkDTO>(
				        converted);
				toReplace.addAll(preSelectedList);

				preSelectedStore.replaceAll(toReplace);
				hide();
			}
		});

	}

}
