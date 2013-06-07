package de.gsv.idm.client.view.changeevent;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;

import de.gsv.idm.client.presenter.changeevent.NotNeededChangesPresenter.NotNeededDisplay;
import de.gsv.idm.client.view.properties.ChangeEventDTOProperties;
import de.gsv.idm.shared.dto.ChangeEventDTO;

public class NotNeededChangesView extends EditableChangesView implements NotNeededDisplay {

	public NotNeededChangesView() {
		this((ChangeEventDTOProperties) GWT.create(ChangeEventDTOProperties.class));
	}

	public NotNeededChangesView(ChangeEventDTOProperties create) {
		this(create, new StoreSortInfo<ChangeEventDTO>(create.date(), SortDir.DESC));
	}

	public NotNeededChangesView(ChangeEventDTOProperties properties,
	        StoreSortInfo<ChangeEventDTO> sortInfo) {
		super(properties, sortInfo);
	}

}
