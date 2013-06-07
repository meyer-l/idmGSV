package de.gsv.idm.client.view.gsk.widgets;

import java.util.ArrayList;
import java.util.logging.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralRpcController;
import de.gsv.idm.client.view.properties.MeasureDTOProperties;
import de.gsv.idm.client.view.widgets.window.GridToGridRPCChooser;
import de.gsv.idm.shared.comperator.GSKComperator;
import de.gsv.idm.shared.dto.MeasureDTO;

public class MeasureGridToGridChooser extends GridToGridRPCChooser<MeasureDTO> {

	private final ListStore<MeasureDTO> inheritedStore;

	public MeasureGridToGridChooser(GeneralRpcController<MeasureDTO> rpcService,
	        ListStore<MeasureDTO> store, ListStore<MeasureDTO> inheritedStore,
	        MeasureDTOProperties props) {
		super(rpcService, store, props, -1);
		selectedStore.getSortInfo().clear();
		selectedStore.addSortInfo(new StoreSortInfo<MeasureDTO>(props.name(), new GSKComperator(),
		        SortDir.ASC));
		toChooseStore.getSortInfo().clear();
		toChooseStore.addSortInfo(new StoreSortInfo<MeasureDTO>(props.name(), new GSKComperator(),
		        SortDir.ASC));	
		toChooseStore.add(new MeasureDTO(-1,"... Maßnahmen werden vom Server geladen ..."));
		toChoose.disable();
		this.inheritedStore = inheritedStore;
		hideRPCButtons();
		setEastPanelHeadingText("Ausgewählte Maßnahmen");
		setWestPanelHeadingText("Verfügbare Maßnahmen");
		setHeadingText("Maßnahmen-Verknüpfungen bearbeiten");
	}

	public void callInitStores() {
		DBController.getInstance().getMeasureController()
		        .getAll(new AsyncCallback<ArrayList<MeasureDTO>>() {
			        public void onFailure(Throwable caught) {
			        	DBController.getLogger().log(Level.SEVERE, "Error while calling MeasuresImpl.getAll");
			        }

			        public void onSuccess(ArrayList<MeasureDTO> result) {
			        	toChoose.enable();
			        	toChooseStore.clear();
				        result.removeAll(inheritedStore.getAll());
				        initStores(result);
			        }
		        });
	}

	public MeasureGridToGridChooser(ListStore<MeasureDTO> store,
	        ListStore<MeasureDTO> inheritedStore) {
		this(DBController.getInstance().getMeasureController(), store, inheritedStore,
		        (MeasureDTOProperties) GWT.create(MeasureDTOProperties.class));
	}

	public MeasureGridToGridChooser(ListStore<MeasureDTO> store) {
		this(store, (MeasureDTOProperties) GWT.create(MeasureDTOProperties.class));
	}

	public MeasureGridToGridChooser(ListStore<MeasureDTO> store, MeasureDTOProperties properties) {
		this(DBController.getInstance().getMeasureController(), store,
		        new ListStore<MeasureDTO>(properties.key()), (MeasureDTOProperties) GWT
		                .create(MeasureDTOProperties.class));
	}

}
