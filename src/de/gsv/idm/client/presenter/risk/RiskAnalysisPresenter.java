package de.gsv.idm.client.presenter.risk;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.asset.CreatedAssetEvent;
import de.gsv.idm.client.event.db.asset.DeletedAssetEvent;
import de.gsv.idm.client.event.db.asset.UpdatedAssetEvent;
import de.gsv.idm.client.presenter.general.GeneralGridPresenter;
import de.gsv.idm.client.presenter.informationdomain.asset.AssetEditorPresenter;
import de.gsv.idm.client.view.informationdomain.asset.AssetEditor;
import de.gsv.idm.client.view.widgets.window.EditorWindow;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.SecurityLevelDTO;

public class RiskAnalysisPresenter extends GeneralGridPresenter<AssetDTO> {

	public interface RiskAnalysisDisplay extends GeneralGridDisplay<AssetDTO> {
		void setCsvExportButtonText(String text);

		HasSelectHandlers getCsvExportButtonClick();
	}

	private final RiskAnalysisDisplay display;
	private final Integer domainId;

	public RiskAnalysisPresenter(RiskAnalysisDisplay view, Integer domainId) {
		super(view);
		display = view;
		this.domainId = domainId;
	}

	@Override
	protected void bindBus() {
		eventBus.addHandler(DeletedAssetEvent.TYPE, new GeneralEventHandler<DeletedAssetEvent>() {

			@Override
			public void onEvent(DeletedAssetEvent event) {
				display.removeItem(event.getObject());
			}
		});

		eventBus.addHandler(UpdatedAssetEvent.TYPE, new GeneralEventHandler<UpdatedAssetEvent>() {

			@Override
			public void onEvent(UpdatedAssetEvent event) {

				AssetDTO asset = event.getObject();
				if (asset.getCalculatedSecurityAssesment().getId() > SecurityLevelDTO
				        .getDefaultSecurityLevel().getId()) {
					display.addItem(asset);
				} else {
					display.removeItem(event.getObject());
				}
			}
		});

		eventBus.addHandler(CreatedAssetEvent.TYPE, new GeneralEventHandler<CreatedAssetEvent>() {

			@Override
			public void onEvent(CreatedAssetEvent event) {
				AssetDTO asset = event.getObject();
				if (asset.getCalculatedSecurityAssesment().getId() > SecurityLevelDTO
				        .getDefaultSecurityLevel().getId()) {
					display.addItem(asset);
				}
			}
		});

	}

	@Override
	protected void bindView() {
		display.setHeaderText("Risikoanalyse");

		display.addDoubleClickHandler(new CellDoubleClickHandler() {

			@Override
			public void onCellClick(CellDoubleClickEvent event) {
				AssetDTO asset = display.getGridSelectionModel().getSelectedItem();
				new EditorWindow<AssetDTO>(new AssetEditorPresenter(new AssetEditor(), domainId),
				        asset, "Asset bearbeiten");
			}
		});

		display.setCsvExportButtonText("Als CSV exportieren");
		display.getCsvExportButtonClick().addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, GWT
				        .getModuleBaseURL()
				        + "csvExportProvider?domainId="
				        + domainId
				        + "&sessionId=" + dbConnector.getUser().getSessionId());
				Window.open(builder.getUrl(), "_blank", "");
			}
		});
	}

	public void refreshView() {
		dbConnector.getAssetController().getAll(domainId, new AsyncCallback<ArrayList<AssetDTO>>() {

			@Override
			public void onSuccess(ArrayList<AssetDTO> result) {
				List<AssetDTO> riskAnalysisAssets = new ArrayList<AssetDTO>();
				for (AssetDTO asset : result) {
					SecurityLevelDTO sec = asset.getCalculatedSecurityAssesment();
					if (sec.getId() > SecurityLevelDTO.getDefaultSecurityLevel().getId()) {
						riskAnalysisAssets.add(asset);
					}
				}
				display.setStore(riskAnalysisAssets);
			}

			@Override
			public void onFailure(Throwable caught) {
				DBController.getLogger().log(Level.SEVERE,
				        "Error while fetching Assets with high SecurityAssesment");
			}
		});
	}

}