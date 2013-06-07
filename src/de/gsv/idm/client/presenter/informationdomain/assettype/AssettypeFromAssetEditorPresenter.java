package de.gsv.idm.client.presenter.informationdomain.assettype;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.info.Info;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.db.TransactionBufferEvent;
import de.gsv.idm.client.presenter.helper.ImageHelper;
import de.gsv.idm.client.presenter.informationdomain.asset.AssetEditorPresenter;
import de.gsv.idm.client.transaction.CreateTransaction;
import de.gsv.idm.client.transaction.UpdateTransaction;
import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.dto.AssettypeMeasureLinkDTO;
import de.gsv.idm.shared.dto.AssettypeModuleLinkDTO;

public class AssettypeFromAssetEditorPresenter extends AssettypeEditorPresenter {

	public interface AssettypeFromAssetEditorDisplay extends AssettypeEditorDisplay {
		public HasSelectHandlers getSaveNewAssetClick();

		void setSaveNewAssetText(String text);
		void setSaveNewAssetToolTip(String text);
	};

	private AssettypeFromAssetEditorDisplay assettypeFromAssetEditorDisplay;
	private final AssetEditorPresenter assetPresenter;

	public AssettypeFromAssetEditorPresenter(AssettypeFromAssetEditorDisplay editorDisplay,
	        Integer domain_id, AssetEditorPresenter assetPresenter
	       ) {
		super(editorDisplay, domain_id);
		assettypeFromAssetEditorDisplay = editorDisplay;
		this.assetPresenter = assetPresenter;
	}

	@Override
	public void bindView() {
		super.bindView();
		assettypeFromAssetEditorDisplay.setSaveNewAssetText("Als neues Asset speichern");
		assettypeFromAssetEditorDisplay.setSaveNewAssetToolTip("Vorgenommende Änderungen am Bausteinen und Maßnahmen im Asset-Typ werden erst nach dem abspeichern des Assets im Editor angezeigt.");
		assettypeFromAssetEditorDisplay.getSaveNewAssetClick().addSelectHandler(new SelectHandler() {

			public void onSelect(SelectEvent event) {
				AssettypeDTO flushedObject = itemDriver.flush();
				flushedObject.setId(null);
				flushedObject.getMeasures();
				List<AssettypeModuleLinkDTO> clonedModules = new ArrayList<AssettypeModuleLinkDTO>();
				for (AssettypeModuleLinkDTO moduleLink : flushedObject.getModules()) {
					AssettypeModuleLinkDTO moduleClone = moduleLink.clone();
					moduleClone.setId(null);
					clonedModules.add(moduleClone);
				}
				flushedObject.setModules(clonedModules);
				List<AssettypeMeasureLinkDTO> clonedMeasures = new ArrayList<AssettypeMeasureLinkDTO>();
				for (AssettypeMeasureLinkDTO measureLink : flushedObject.getMeasures()) {
					AssettypeMeasureLinkDTO measureClone = measureLink.clone();
					measureClone.setId(null);
					clonedMeasures.add(measureClone);
				}
				flushedObject.setMeasures(clonedMeasures);
				createObject(flushedObject);
			}
		});
	}

	@Override
	public void doEdit(AssettypeDTO object) {
		clonedEditObject = object.clone();
		cachedEditObject = object.clone();
		itemDriver.edit(cachedEditObject);
		if (object.getIconName() != null && !object.getIconName().equals("")) {
			assettypeFromAssetEditorDisplay.setImageResource(ImageHelper.getImageResourceFromIconName(object
			        .getIconName()));
		}
		assettypeFromAssetEditorDisplay.setSaveEnabled(true);
		assettypeFromAssetEditorDisplay.setItAssetsFieldsEnabled(object.isItAsset());
		assettypeFromAssetEditorDisplay.setSecurityAssesmentFieldsEnabled(object
		        .isManuelSecurityAssesment());
	}

	@Override
	public void createObject(final AssettypeDTO object) {
		object.setIconName(editorDisplay.getIconName());
		generalRpcController.create(object, new AsyncCallback<AssettypeDTO>() {
			public void onSuccess(AssettypeDTO result) {
				String text = result.getClassName() + " " + result.getName()
				        + " wurde erstellt. (ID: " + result.getId() + ")";
				eventBus.fireEvent(new TransactionBufferEvent(new CreateTransaction<AssettypeDTO>(
				        generalRpcController, result)));
				Info.display("RPC:", text);
				edit(result);
				assetPresenter.refreshAsset(result);
			}

			public void onFailure(Throwable caught) {
				DBController.getLogger().log(Level.SEVERE, "Error while creating "+ object.getClassName());
			}
		});
	}

	public void updateObject(final AssettypeDTO object, final AssettypeDTO oldEditObject) {
		generalRpcController.update(object, new AsyncCallback<AssettypeDTO>() {
			public void onSuccess(AssettypeDTO result) {
				String text = result.getClassName() + " wurde aktualisiert. (ID: " + result.getId()
				        + ")";
				eventBus.fireEvent(new TransactionBufferEvent(new UpdateTransaction<AssettypeDTO>(
				        generalRpcController, oldEditObject, result, text)));
				Info.display("RPC:", text);
			}

			public void onFailure(Throwable caught) {
				DBController.getLogger().log(Level.SEVERE, "Error while updating "+ object.getClassName() + " Id:" +object.getId());
			}
		});
	}
}
