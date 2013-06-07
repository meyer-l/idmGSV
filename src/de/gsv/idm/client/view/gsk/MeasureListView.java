package de.gsv.idm.client.view.gsk;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;

import de.gsv.idm.client.presenter.gsk.MeasureListPresenter.MeasuresListDisplay;
import de.gsv.idm.client.view.general.GeneralListView;
import de.gsv.idm.client.view.properties.MeasureDTOProperties;
import de.gsv.idm.shared.comperator.GSKComperator;
import de.gsv.idm.shared.dto.MeasureDTO;

public class MeasureListView extends GeneralListView<MeasureDTO, MeasuresListDisplay> implements
        MeasuresListDisplay {

	public MeasureListView() {
		super(0.8747);
		objects.getStore().getSortInfo().clear();
		MeasureDTOProperties props = GWT.create(MeasureDTOProperties.class);
		objects.getStore().addSortInfo(
		        new StoreSortInfo<MeasureDTO>(props.name(), new GSKComperator(), SortDir.ASC));

	}

}
