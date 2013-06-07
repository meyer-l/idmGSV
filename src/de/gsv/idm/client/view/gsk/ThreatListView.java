package de.gsv.idm.client.view.gsk;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;

import de.gsv.idm.client.presenter.gsk.ThreatListPresenter.ThreatsListDisplay;
import de.gsv.idm.client.view.general.GeneralListView;
import de.gsv.idm.client.view.properties.ThreatDTOProperties;
import de.gsv.idm.shared.comperator.GSKComperator;
import de.gsv.idm.shared.dto.ThreatDTO;

public class ThreatListView extends GeneralListView<ThreatDTO, ThreatsListDisplay> implements
        ThreatsListDisplay {

	public ThreatListView() {
		super(0.8747);
		objects.getStore().getSortInfo().clear();
		ThreatDTOProperties props = GWT.create(ThreatDTOProperties.class);
		objects.getStore().addSortInfo(
		        new StoreSortInfo<ThreatDTO>(props.name(), new GSKComperator(), SortDir.ASC));
	}

}
