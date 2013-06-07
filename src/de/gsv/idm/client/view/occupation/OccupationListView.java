package de.gsv.idm.client.view.occupation;

import de.gsv.idm.client.presenter.occupation.OccupationListPresenter.OccupationListDisplay;
import de.gsv.idm.client.view.general.GeneralListView;
import de.gsv.idm.shared.dto.OccupationDTO;

public class OccupationListView extends GeneralListView<OccupationDTO, OccupationListDisplay> implements OccupationListDisplay {
		
	public OccupationListView() {
		super(0.6);	
	    addButtons();
	}
}
