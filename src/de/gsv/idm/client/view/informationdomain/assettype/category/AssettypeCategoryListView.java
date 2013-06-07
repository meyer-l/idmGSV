package de.gsv.idm.client.view.informationdomain.assettype.category;

import de.gsv.idm.client.presenter.informationdomain.assettype.category.AssettypeCategoryListPresenter.AssettypeCategoryDisplay;
import de.gsv.idm.client.view.general.GeneralListView;
import de.gsv.idm.shared.dto.AssettypeCategoryDTO;

public class AssettypeCategoryListView extends
        GeneralListView<AssettypeCategoryDTO, AssettypeCategoryDisplay> implements AssettypeCategoryDisplay {

	public AssettypeCategoryListView() {
		super(0.8747);
	    addButtons();
    }

}
