package de.gsv.idm.client.view.securitylevelchange;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import de.gsv.idm.client.images.ImageResources;
import de.gsv.idm.client.presenter.securitylevelchange.SecurityLevelChangePresenter.SecurityLevelChangeDisplay;
import de.gsv.idm.client.view.general.GeneralEditGridView;
import de.gsv.idm.client.view.properties.SecurityLevelChangeDTOProperties;
import de.gsv.idm.client.view.widgets.securitylevelchange.SecurityLevelChangeCheckBoxCell;
import de.gsv.idm.shared.dto.SecurityLevelChangeDTO;

public class SecurityLevelChangeView extends GeneralEditGridView<SecurityLevelChangeDTO> implements
        SecurityLevelChangeDisplay {

	private TextButton reviewAllButton;

	public SecurityLevelChangeView() {
		this((SecurityLevelChangeDTOProperties) GWT.create(SecurityLevelChangeDTOProperties.class));
	}

	public SecurityLevelChangeView(SecurityLevelChangeDTOProperties properties) {
		super(properties, new StoreSortInfo<SecurityLevelChangeDTO>(
		        properties.valueProviderLabel(), SortDir.DESC));
		reviewAllButton = new TextButton();
		mainPanel.addButton(reviewAllButton);
		ImageResources imageBundler = GWT.create(ImageResources.class);
		mainPanel.getHeader().setIcon(imageBundler.help());
		mainPanel.getHeader().setToolTip(
		        "Änderungen am Schutzbedarf, die durch Berarbeitung"
		                + " des Informationsverbund entstanden sind.");
	}

	@Override
	protected ColumnModel<SecurityLevelChangeDTO> getColumnModel() {
		SecurityLevelChangeDTOProperties properties = GWT
		        .create(SecurityLevelChangeDTOProperties.class);
		List<ColumnConfig<SecurityLevelChangeDTO, ?>> cfgs = new ArrayList<ColumnConfig<SecurityLevelChangeDTO, ?>>();

		ColumnConfig<SecurityLevelChangeDTO, String> name = new ColumnConfig<SecurityLevelChangeDTO, String>(
		        properties.name(), 300);
		name.setHeader("Asset");
		cfgs.add(name);

		ColumnConfig<SecurityLevelChangeDTO, String> securityAssesment = new ColumnConfig<SecurityLevelChangeDTO, String>(
		        properties.securityAssesmentName(), 200);
		securityAssesment.setHeader("Schutzbedarf: Gesamt");
		cfgs.add(securityAssesment);

		ColumnConfig<SecurityLevelChangeDTO, String> availability = new ColumnConfig<SecurityLevelChangeDTO, String>(
		        properties.availabilityName(), 200);
		availability.setHeader("Schutzbedarf: Verfügbarkeit");
		cfgs.add(availability);

		ColumnConfig<SecurityLevelChangeDTO, String> confidentiality = new ColumnConfig<SecurityLevelChangeDTO, String>(
		        properties.confidentialityName(), 200);
		confidentiality.setHeader("Schutzbedarf: Vertraulichkeit");
		cfgs.add(confidentiality);

		ColumnConfig<SecurityLevelChangeDTO, String> integrity = new ColumnConfig<SecurityLevelChangeDTO, String>(
		        properties.integrityName(), 200);
		integrity.setHeader("Schutzbedarf: Integrität");
		cfgs.add(integrity);

		ColumnConfig<SecurityLevelChangeDTO, SecurityLevelChangeDTO> reviewed = new ColumnConfig<SecurityLevelChangeDTO, SecurityLevelChangeDTO>(
		        properties.instance(), 100);
		reviewed.setHeader("Überprüft?");
		reviewed.setCell(new SecurityLevelChangeCheckBoxCell());
		cfgs.add(reviewed);

		return new ColumnModel<SecurityLevelChangeDTO>(cfgs);
	}

	@Override
	public void setReviewAllButtonText(String text) {
		reviewAllButton.setText(text);
	}

	@Override
	public HasSelectHandlers getReviewAllButtonClick() {
		return reviewAllButton;
	}

}
