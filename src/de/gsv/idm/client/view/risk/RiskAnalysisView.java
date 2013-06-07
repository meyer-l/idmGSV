package de.gsv.idm.client.view.risk;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.GroupingView;

import de.gsv.idm.client.presenter.risk.RiskAnalysisPresenter.RiskAnalysisDisplay;
import de.gsv.idm.client.view.general.GeneralGridView;
import de.gsv.idm.client.view.properties.AssetDTOProperties;
import de.gsv.idm.shared.comperator.LowerCaseStringComperator;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.dto.SecurityLevelDTO;

public class RiskAnalysisView extends GeneralGridView<AssetDTO> implements RiskAnalysisDisplay {

	TextButton csvExportButton;

	public RiskAnalysisView() {
		this((AssetDTOProperties) GWT.create(AssetDTOProperties.class));
	}

	public RiskAnalysisView(AssetDTOProperties create) {
		this(create, new StoreSortInfo<AssetDTO>(create.name(), new LowerCaseStringComperator(),
		        SortDir.ASC));
	}

	public RiskAnalysisView(AssetDTOProperties properties, StoreSortInfo<AssetDTO> sortInfo) {
		super(properties, sortInfo);

		final GroupingView<AssetDTO> view = new GroupingView<AssetDTO>();
		view.setShowGroupedColumn(false);
		view.setForceFit(true);
		grid.setView(view);
		view.groupBy(grid.getColumnModel().getColumn(0));
		csvExportButton = new TextButton();
		mainPanel.addButton(csvExportButton);
		store.add(new AssetDTO(-1,-1, new AssettypeDTO(-1, "... Assets für Risikoanalyse werden vom Server geladen ...")));
		grid.disable();
	}

	@Override
	protected ColumnModel<AssetDTO> getColumnModel() {
		AssetDTOProperties properties = GWT.create(AssetDTOProperties.class);

		List<ColumnConfig<AssetDTO, ?>> cfgs = new ArrayList<ColumnConfig<AssetDTO, ?>>();

		ColumnConfig<AssetDTO, AssettypeDTO> assetType = new ColumnConfig<AssetDTO, AssettypeDTO>(
		        properties.assettype());
		assetType.setHeader("Grundtyp");

		cfgs.add(assetType);

		ColumnConfig<AssetDTO, String> name = new ColumnConfig<AssetDTO, String>(properties.name(),300);
		name.setHeader("Name");
		cfgs.add(name);

		ColumnConfig<AssetDTO, SecurityLevelDTO> summary = new ColumnConfig<AssetDTO, SecurityLevelDTO>(
		        properties.calculatedSecurityAssesment(), 200);
		summary.setHeader("Schutzbedarf: Gesamt");
		cfgs.add(summary);
		
		ColumnConfig<AssetDTO, SecurityLevelDTO> confidentiality = new ColumnConfig<AssetDTO, SecurityLevelDTO>(
		        properties.calculatedConfidentiality(), 200);
		confidentiality.setHeader("Schutzbedarf: Vertraulichkeit");
		cfgs.add(confidentiality);

		ColumnConfig<AssetDTO, SecurityLevelDTO> integrity = new ColumnConfig<AssetDTO, SecurityLevelDTO>(
		        properties.calculatedIntegrity(), 200);
		integrity.setHeader("Schutzbedarf: Integrität");
		cfgs.add(integrity);

		ColumnConfig<AssetDTO, SecurityLevelDTO> availability = new ColumnConfig<AssetDTO, SecurityLevelDTO>(
		        properties.calculatedAvailability(), 200);
		availability.setHeader("Schutzbedarf: Verfügbarkeit");
		cfgs.add(availability);

		return new ColumnModel<AssetDTO>(cfgs);
	}

	@Override
	public void setCsvExportButtonText(String text) {
		csvExportButton.setText(text);
	}

	@Override
	public HasSelectHandlers getCsvExportButtonClick() {
		return csvExportButton;
	}
}
