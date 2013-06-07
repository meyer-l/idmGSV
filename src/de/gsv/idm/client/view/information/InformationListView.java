package de.gsv.idm.client.view.information;

import de.gsv.idm.client.presenter.information.InformationListPresenter.InformationListDisplay;
import de.gsv.idm.client.view.general.GeneralListView;
import de.gsv.idm.shared.dto.InformationDTO;

public class InformationListView extends GeneralListView<InformationDTO, InformationListDisplay> implements InformationListDisplay {
	
	public InformationListView() {
		super(0.6);
	    addButtons();
	}

}
