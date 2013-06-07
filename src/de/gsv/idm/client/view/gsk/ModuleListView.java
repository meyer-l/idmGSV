package de.gsv.idm.client.view.gsk;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;

import de.gsv.idm.client.presenter.gsk.ModuleListPresenter.ModulesListDisplay;
import de.gsv.idm.client.view.general.GeneralListView;
import de.gsv.idm.client.view.properties.ModuleDTOProperties;
import de.gsv.idm.shared.comperator.GSKComperator;
import de.gsv.idm.shared.dto.ModuleDTO;

public class ModuleListView extends GeneralListView<ModuleDTO, ModulesListDisplay> implements
        ModulesListDisplay {

	public ModuleListView() {
		super(0.8747);
		objects.getStore().getSortInfo().clear();
		ModuleDTOProperties props = GWT.create(ModuleDTOProperties.class);
		objects.getStore().addSortInfo(
		        new StoreSortInfo<ModuleDTO>(props.name(), new GSKComperator(), SortDir.ASC));
	}
}
