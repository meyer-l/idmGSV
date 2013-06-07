package de.gsv.idm.client.view.informationdomain.asset.widgets;

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
import de.gsv.idm.shared.dto.AssetMeasureLinkDTO;
import de.gsv.idm.shared.dto.MeasureDTO;

public class AssetMeasureGridToGridChooser extends GridToGridChooser<MeasureDTO> {

	private ListStore<AssetMeasureLinkDTO> preSelectedStore;
	private ArrayList<AssetMeasureLinkDTO> notRemovables;

	public AssetMeasureGridToGridChooser(ListStore<AssetMeasureLinkDTO> preSelectedStore,
	        ListStore<AssetMeasureLinkDTO> inheritedStore,
	        HasKeyAndName<MeasureDTO> props) {
		super(new ListStore<MeasureDTO>(props.key()), props);
		this.preSelectedStore = preSelectedStore;

		final ArrayList<MeasureDTO> converted = new ArrayList<MeasureDTO>();
		notRemovables = new ArrayList<AssetMeasureLinkDTO>();
		for (AssetMeasureLinkDTO pre : preSelectedStore.getAll()) {
			if (!pre.getNotRemovable()) {
				converted.add(pre.getMeasure());
			} else {
				notRemovables.add(pre);
			}
		}

		ArrayList<MeasureDTO> convertedNotRemovable = new ArrayList<MeasureDTO>();
		for (AssetMeasureLinkDTO notRemovable : notRemovables) {
			convertedNotRemovable.add(notRemovable.getMeasure());
		}

		final ArrayList<MeasureDTO> convertedInherited = new ArrayList<MeasureDTO>();
		for (AssetMeasureLinkDTO pre : inheritedStore.getAll()) {
			convertedInherited.add(pre.getMeasure());
		}

		selectedStore.getSortInfo().clear();
		selectedStore.addSortInfo(new StoreSortInfo<MeasureDTO>(props.name(), new GSKComperator(),
		        SortDir.ASC));
		toChooseStore.getSortInfo().clear();
		toChooseStore.addSortInfo(new StoreSortInfo<MeasureDTO>(props.name(), new GSKComperator(),
		        SortDir.ASC));
		toChooseStore.add(new MeasureDTO(-1, "... Maßnahmen werden vom Server geladen ..."));
		toChoose.disable();
		DBController.getInstance().getMeasureController().getAll(new AsyncCallback<ArrayList<MeasureDTO>>() {
			
			@Override
			public void onSuccess(ArrayList<MeasureDTO> result) {
				toChooseStore.clear();
				result.removeAll(converted);
				result.removeAll(convertedInherited);
				result.removeAll(notRemovables);
				toChooseStore.replaceAll(result);
				toChoose.enable();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				DBController.getLogger().log(Level.SEVERE,
		                "Error while calling MeasureImpl.getAll");
			}
		});
		
		selectedStore.replaceAll(converted);

		setEastPanelHeadingText("Ausgewählte Maßnahmen");
		setWestPanelHeadingText("Auswählbare Maßnahmen");

	}

	public AssetMeasureGridToGridChooser(ListStore<AssetMeasureLinkDTO> preSelectedStore,
	        ListStore<AssetMeasureLinkDTO> inheritedStore) {
		this(preSelectedStore, inheritedStore, (MeasureDTOProperties) GWT
		        .create(MeasureDTOProperties.class));
	}

	@Override
	public void addSaveButtonOnSelect() {
		saveButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				List<AssetMeasureLinkDTO> converted = new ArrayList<AssetMeasureLinkDTO>();
				for (MeasureDTO link : selectedStore.getAll()) {
					converted.add(new AssetMeasureLinkDTO(link));
				}
				converted.addAll(notRemovables);
				List<AssetMeasureLinkDTO> preSelectedList = new ArrayList<AssetMeasureLinkDTO>(
				        preSelectedStore.getAll());
				preSelectedList.retainAll(converted);
				converted.removeAll(preSelectedList);
				List<AssetMeasureLinkDTO> toReplace = new ArrayList<AssetMeasureLinkDTO>(converted);
				toReplace.addAll(preSelectedList);

				preSelectedStore.replaceAll(toReplace);
				hide();
			}
		});

	}

}
