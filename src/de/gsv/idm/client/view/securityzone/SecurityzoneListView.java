package de.gsv.idm.client.view.securityzone;

import com.google.gwt.cell.client.IconCellDecorator;
import com.google.gwt.core.client.GWT;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;

import de.gsv.idm.client.images.ImageResources;
import de.gsv.idm.client.presenter.securityzone.SecurityzoneListPresenter.SecurityzoneDisplay;
import de.gsv.idm.client.view.general.GeneralListView;
import de.gsv.idm.client.view.properties.SecurityzoneDTOProperties;
import de.gsv.idm.shared.dto.SecurityzoneDTO;

public class SecurityzoneListView extends GeneralListView<SecurityzoneDTO, SecurityzoneDisplay>
        implements SecurityzoneDisplay {

	public SecurityzoneListView() {
		super(0.8747);
		addButtons();
		objects.getStore().getSortInfo().clear();
		SecurityzoneDTOProperties prop = GWT.create(SecurityzoneDTOProperties.class);
		objects.getStore().addSortInfo(
		        new StoreSortInfo<SecurityzoneDTO>(prop.orderNumber(), SortDir.ASC));
		ImageResources imageBundler = GWT.create(ImageResources.class);
		IconCellDecorator<String> icon = new IconCellDecorator<String>(imageBundler.arrowDown(),
		        list.getCell());
		list.setCell(icon);

	}

}
