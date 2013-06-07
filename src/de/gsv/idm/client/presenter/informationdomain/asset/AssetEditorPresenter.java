package de.gsv.idm.client.presenter.informationdomain.asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent.StoreDataChangeHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.asset.DeletedAssetEvent;
import de.gsv.idm.client.event.db.asset.UpdatedAssetEvent;
import de.gsv.idm.client.event.db.assettype.UpdatedAssettypeEvent;
import de.gsv.idm.client.event.db.employee.DeletedEmployeeEvent;
import de.gsv.idm.client.event.db.employee.UpdatedEmployeeEvent;
import de.gsv.idm.client.presenter.general.GeneralAssetCheckPresenter;
import de.gsv.idm.client.presenter.informationdomain.assettype.AssettypeFromAssetEditorPresenter;
import de.gsv.idm.client.presenter.securitylevelchange.data.SecurityLevelBundle;
import de.gsv.idm.client.view.employee.EmployeeEditorWindow;
import de.gsv.idm.client.view.employee.EmployeeGridToGridChooser;
import de.gsv.idm.client.view.informationdomain.asset.AssetEditor;
import de.gsv.idm.client.view.informationdomain.asset.link.AssetLinkTreeToListChooser;
import de.gsv.idm.client.view.informationdomain.asset.widgets.AssetEditorWindow;
import de.gsv.idm.client.view.informationdomain.asset.widgets.AssetMeasureGridToGridChooser;
import de.gsv.idm.client.view.informationdomain.assettype.AssettypeFromModelEditor;
import de.gsv.idm.client.view.informationdomain.assettype.widgets.AssettypeEditorWindow;
import de.gsv.idm.client.view.securityzone.SecurityzoneEditorWindow;
import de.gsv.idm.client.view.securityzone.widgets.SecurityzoneMeasuresDetailsWindow;
import de.gsv.idm.client.view.widgets.window.EditorWindow;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssetLinkDTO;
import de.gsv.idm.shared.dto.AssetMeasureLinkDTO;
import de.gsv.idm.shared.dto.AssetModuleLinkDTO;
import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.dto.AssettypeMeasureLinkDTO;
import de.gsv.idm.shared.dto.AssettypeModuleLinkDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;
import de.gsv.idm.shared.dto.GeneralDTO;

public class AssetEditorPresenter extends GeneralAssetCheckPresenter<AssetDTO> {

	public interface AssetEditorDisplay extends GeneralEditorDisplay<AssetDTO> {
		public HasSelectHandlers getEditAssetButtonClick();

		public void setEditAssetButtonText(String test);

		public void setComboBoxList(ArrayList<EmployeeDTO> data);

		HasSelectHandlers getAddEmployeeButtonClick();

		void setAddEmployeeButtonText(String text);

		ListStore<EmployeeDTO> getEmployeeStore();

		void setEmployeeLabel(String text);

		void setSecurityzoneMeasuresDetailsButtonText(String text);

		HasSelectHandlers getSecurityzoneMeasuresDetailsButtonClick();

		void setSecurityzoneMeasuresDetailsButtonEnabled(Boolean enabled);

		void setSecurityzoneMeasuresDetailsTooltip(String tooltip);

		void setAddMeasureButtonText(String text);

		HasSelectHandlers getAddMeasureButtonClick();

		public ListStore<AssetMeasureLinkDTO> getMeasuresStore();

		public ListStore<AssetMeasureLinkDTO> getInheritedStore();

		void setCalcualtedSecurityAssesmentTooltip(String tooltip);

		void setEditSecurityzoneButtonText(String text);

		HasSelectHandlers getEditSecurityzoneButtonClick();

		void setIdentifierToolTip(String text);

		void setAssettypeResponsiblePersonToolTip(String text);

		void setAvailabilityTooltip(String toolTip);

		void setConfidentialityTip(String toolTip);

		void setIntegrityTooltip(String toolTip);

		void setEmployeeListTooltip(String toolTip);

		void setAssignedSecurityzoneToolTip(String toolTip);

		void setModuleListTooltip(String toolTip);

		void setMeasureListTooltip(String toolTip);

		void setInheritedMeasureListTooltip(String toolTip);

		void setEditSecurityAssesmentSourceButtonText(String text);

		HasSelectHandlers getEditSecurityAssesmentSourceButtonClick();

		void setEditSecurityAssesmentSourceButtonEnabled(Boolean enabled);

		void setEditParentButtonText(String text);

		HasSelectHandlers getEditParentButtonClick();

		void setEditParentButtonEnabled(Boolean enabled);
		
		void setAssetLinkButtonText(String text);
		HasSelectHandlers getAssetLinkButtonClick();
	};

	interface ItemDriver extends SimpleBeanEditorDriver<AssetDTO, AssetEditor> {
	}

	protected ItemDriver itemDriver = GWT.create(ItemDriver.class);
	private Integer domainId;

	private final AssetEditorDisplay editorDisplay;

	private final DBController dbConnector;
	private final AssetEditorPresenter assetModelPresenter;

	public AssetEditorPresenter(AssetEditorDisplay editorDisplay, Integer domain_id) {
		super(DBController.getInstance().getAssetController(), editorDisplay);
		dbConnector = DBController.getInstance();
		this.domainId = domain_id;
		this.editorDisplay = editorDisplay;
		assetModelPresenter = this;
	}

	@Override
	public IsWidget go() {
		itemDriver.initialize((AssetEditor) editorDisplay);
		bindBus();
		bindView();
		return editorDisplay.asWidget();
	}

	private void bindBus() {
		dbConnector.getEventBus().addHandler(UpdatedAssettypeEvent.TYPE,
		        new GeneralEventHandler<UpdatedAssettypeEvent>() {
			        public void onEvent(UpdatedAssettypeEvent event) {
				        if (event.getObject().getId()
				                .equals(cachedEditObject.getAssettype().getId())) {
					        refreshAsset(event.getObject());
				        }
			        }
		        });

		eventBus.addHandler(UpdatedEmployeeEvent.TYPE,
		        new GeneralEventHandler<UpdatedEmployeeEvent>() {
			        public void onEvent(UpdatedEmployeeEvent event) {
				        if (editorDisplay.getEmployeeStore().findModel(event.getObject()) != null) {
					        editorDisplay.getEmployeeStore().update(event.getObject());
				        }
			        }
		        });
		eventBus.addHandler(DeletedEmployeeEvent.TYPE,
		        new GeneralEventHandler<DeletedEmployeeEvent>() {
			        public void onEvent(DeletedEmployeeEvent event) {
				        if (editorDisplay.getEmployeeStore().findModel(event.getObject()) != null) {
					        editorDisplay.getEmployeeStore().remove(event.getObject());
				        }
			        }
		        });
	}

	public void bindView() {
		dbConnector.getEmployeeController().getAll(domainId,
		        new AsyncCallback<ArrayList<EmployeeDTO>>() {

			        public void onSuccess(ArrayList<EmployeeDTO> result) {
				        editorDisplay.setComboBoxList(result);
			        }

			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(Level.SEVERE,
				                "Error while calling EmployeeImpl.getAll");
			        }

		        });

		editorDisplay.setEditAssetButtonText("Asset-Grundtyp editeren");
		editorDisplay.getEditAssetButtonClick().addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				new EditorWindow<AssettypeDTO>(new AssettypeFromAssetEditorPresenter(
				        new AssettypeFromModelEditor(), domainId, assetModelPresenter),
				        cachedEditObject.getAssettype(), "Asset-Grundtyp für Asset "
				                + cachedEditObject.getName() + " (Id:" + cachedEditObject.getId()
				                + ") bearbeiten");
			}
		});

		editorDisplay.setSecurityzoneMeasuresDetailsButtonText("Maßnahmen-Details der"
		        + " Schutzzone einsehen");
		editorDisplay.getSecurityzoneMeasuresDetailsButtonClick().addSelectHandler(
		        new SelectHandler() {

			        @Override
			        public void onSelect(SelectEvent event) {
				        new SecurityzoneMeasuresDetailsWindow(cachedEditObject);
			        }
		        });
		editorDisplay.setEditSecurityAssesmentSourceButtonText("Quelle des"
		        + " Schutzbedarfes bearbeiten");
		editorDisplay.getEditSecurityAssesmentSourceButtonClick().addSelectHandler(
		        new SelectHandler() {

			        @Override
			        public void onSelect(SelectEvent event) {
				        GeneralDTO<?> source = itemDriver.flush().getSecurityAssesmentSource();
				        if (source instanceof AssetDTO) {
					        new AssetEditorWindow((AssetDTO) source);
				        } else if (source instanceof AssettypeDTO) {
					        new AssettypeEditorWindow((AssettypeDTO) source);
				        } else if (source instanceof EmployeeDTO) {
					        new EmployeeEditorWindow((EmployeeDTO) source);
				        } else if (source instanceof AssetLinkDTO) {
					        AssetDTO asset = ((AssetLinkDTO) source).getAsset();
					        new AssetEditorWindow(asset);
				        }
			        }
		        });

		editorDisplay.setEditSecurityzoneButtonText("Schutzzone bearbeiten");
		editorDisplay.getEditSecurityzoneButtonClick().addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				new SecurityzoneEditorWindow(itemDriver.flush().getAssignedSecurityzone(),
				        "Schutzzone bearbeiten");
			}
		});

		editorDisplay.setEditParentButtonText("Vater-Asset bearbeiten");
		editorDisplay.getEditParentButtonClick().addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				AssetDTO parent = itemDriver.flush().getParent();
				if (parent != null) {
					new AssetEditorWindow(itemDriver.flush().getParent());
				}
			}
		});

		editorDisplay.setEmployeeLabel("Mitarbeiter");
		editorDisplay.setAddEmployeeButtonText("Mitarbeiter hinzufügen");
		editorDisplay.getAddEmployeeButtonClick().addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				new EmployeeGridToGridChooser(editorDisplay.getEmployeeStore(), domainId);
			}
		});

		editorDisplay.setAddMeasureButtonText("Maßnahme hinzufügen");
		editorDisplay.getAddMeasureButtonClick().addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				new AssetMeasureGridToGridChooser(editorDisplay.getMeasuresStore(), editorDisplay
				        .getInheritedStore());
			}
		});

		editorDisplay.setCalcualtedSecurityAssesmentTooltip("Ermittelter Gesamtschutzbedarf aus"
		        + " den verknüpften Mitarbeitern und untergeordneten Assets im"
		        + " Informationsverbund.");

		editorDisplay.setIdentifierToolTip("Bezeichner zur Identifikation des Assets. Wird zum"
		        + " auffinden von Änderungen in eingelesenen Telefonlisten verwendet.");

		editorDisplay.setAssettypeResponsiblePersonToolTip("Im Asset-Grundtyp eingetragender"
		        + " Verantworlticher.");

		editorDisplay.setAvailabilityTooltip("Aus den verknüpften Mitarbeitern abgeleiteter"
		        + " Schutzbedarf für die Verfügbarkeit.");

		editorDisplay.setConfidentialityTip("Aus den verknüpften Mitarbeitern abgeleiteter"
		        + " Schutzbedarf für die Vertraulichkeit.");

		editorDisplay.setIntegrityTooltip("Aus den verknüpften Mitarbeitern abgeleiteter"
		        + " Schutzbedarf für die Integrität.");

		editorDisplay.setEmployeeListTooltip("Mitarbeiter die dieses Asset zur Erledigung ihrer"
		        + " Aufgaben verwenden");

		editorDisplay.setAssignedSecurityzoneToolTip("Aus den Mitarbeitern abgeleitete Schutzzone"
		        + " für dieses Asset.");

		editorDisplay.setModuleListTooltip("Mit dem Asset-Grundtyp verknüpfte Bausteine.");

		editorDisplay.setMeasureListTooltip("Maßnahmen die für dieses Asset bereits umgesetzt"
		        + " wurden, oder Aufgrund der verknüpften Bausteine oder Mitarbeiter für das"
		        + " Asset umgesetzt werden müssen.");

		editorDisplay.getEmployeeStore().addStoreDataChangeHandler(
		        new StoreDataChangeHandler<EmployeeDTO>() {

			        @Override
			        public void onDataChange(StoreDataChangeEvent<EmployeeDTO> event) {
				        AssetDTO flushed = itemDriver.flush();
				        itemDriver.edit(flushed);
				        editorDisplay.setSecurityzoneMeasuresDetailsButtonEnabled(flushed
				                .getAssignedSecurityzone() != null);
				        editorDisplay.setEditSecurityAssesmentSourceButtonEnabled(!flushed
				                .getSecurityAssesmentSource().equals(flushed));
			        }
		        });
		editorDisplay.setAssetLinkButtonText("Asset-Verknüpfungen bearbeiten");
		editorDisplay.getAssetLinkButtonClick().addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				dbConnector.getAssetController().getObject(cachedEditObject.getId(), new AsyncCallback<AssetDTO>() {

					@Override
                    public void onFailure(Throwable caught) {
						DBController.getLogger().log(Level.SEVERE,
				                "Error while calling AssetImpl.getObject");
                    }

					@Override
                    public void onSuccess(AssetDTO result) {
	                    new AssetLinkTreeToListChooser(result);
                    }
				});
				
			}
		});

	}

	@Override
	public void doNew() {
		// not needed
	}

	@Override
	public void doSave() {
		AssetDTO flushedObject = itemDriver.flush();
		if (flushedObject.getAssettype().getResponsiblePerson() != null
		        && flushedObject.getAssettype().getResponsiblePerson().getId()
		                .equals(flushedObject.getResponsiblePerson().getId())) {
			flushedObject.setResponsiblePerson(null);
		}

		List<AssetModuleLinkDTO> newModuleLinks = new ArrayList<AssetModuleLinkDTO>();
		for (AssetModuleLinkDTO moduleLink : flushedObject.getModuleLinks()) {
			AssettypeModuleLinkDTO assetModuleLink = moduleLink.getAssettypeModuleLink();
			if (assetModuleLink.getResponsiblePerson() != null
			        && assetModuleLink.getResponsiblePerson().getId()
			                .equals(moduleLink.getResponsiblePerson().getId())) {
				moduleLink.setResponsiblePerson(null);
			}
			newModuleLinks.add(moduleLink);
		}
		flushedObject.setModuleLinks(newModuleLinks);

		List<AssetMeasureLinkDTO> newMeasureLinks = new ArrayList<AssetMeasureLinkDTO>();
		for (AssetMeasureLinkDTO measureLink : flushedObject.getMeasureLinks()) {
			AssettypeMeasureLinkDTO assettypeMeasureLink = measureLink.getAssettypeMeasureLink();
			if (assettypeMeasureLink != null) {

				if (assettypeMeasureLink.getResponsiblePerson() != null
				        && assettypeMeasureLink.getResponsiblePerson().getId()
				                .equals(measureLink.getResponsiblePerson().getId())) {
					measureLink.setResponsiblePerson(null);
				}
				if (assettypeMeasureLink.getCost() != null
				        && assettypeMeasureLink.getCost().equals(measureLink.getCost())) {
					measureLink.setCost(null);
				}
				if (assettypeMeasureLink.getStatus() != null
				        && assettypeMeasureLink.getStatus().equals(measureLink.getStatus())) {
					measureLink.setStatus(null);
				}
			}
			newMeasureLinks.add(measureLink);
		}
		flushedObject.setMeasureLinks(newMeasureLinks);
		if (flushedObject.getId() == null) {
			createObject(flushedObject);
		} else {
			updateObject(flushedObject, clonedEditObject);
		}
	}

	@Override
	public void doDelete() {
		final AssetDTO toDelete = itemDriver.flush();
		deleteObject(toDelete);
	}

	@Override
	public void doEdit(AssetDTO object) {
		resetScrollBar();
		clonedEditObject = object.clone();
		cachedEditObject = object.clone();
		itemDriver.edit(cachedEditObject);

		if (object.getParent() == null) {
			editorDisplay.setInheritedMeasureListTooltip("Maßnahmen die bereits durch ein "
			        + " übergeordnetes Asset umgesetzt wurden.");
		} else {
			editorDisplay.setInheritedMeasureListTooltip("Maßnahmen die bereits im"
			        + " übergeordneten Asset " + object.getParent().getName()
			        + " umgesetzt wurden.");
		}
		editorDisplay.setEditParentButtonEnabled(!(object.getParent() == null));

		editorDisplay.setEditSecurityAssesmentSourceButtonEnabled(!cachedEditObject
		        .getSecurityAssesmentSource().equals(cachedEditObject));

		editorDisplay.setSaveEnabled(true);
		editorDisplay.setDeleteEnabled(true);
		editorDisplay.setSaveAndNewVisibility(false);
		editorDisplay.setSecurityzoneMeasuresDetailsButtonEnabled(cachedEditObject
		        .getAssignedSecurityzone() != null);
	}

	public void refreshAsset(AssettypeDTO newAssettype) {
		cachedEditObject = itemDriver.flush();
		cachedEditObject.setAssettype(newAssettype);
		itemDriver.edit(cachedEditObject);
	}

	@Override
	protected void addUpdateEventHandler(
	        final HashMap<Integer, SecurityLevelBundle> oldSecuriytLevels) {
		eventBus.addHandler(UpdatedAssetEvent.TYPE, new GeneralEventHandler<UpdatedAssetEvent>() {

			@Override
			public void onEvent(UpdatedAssetEvent event) {
				if (event.getObject().getId().equals(cachedEditObject.getId())) {
					checkForChanges(oldSecuriytLevels, cachedEditObject);
					eventBus.removeHandler(UpdatedAssetEvent.TYPE, this);
				}
			}
		});
	}

	@Override
	protected void addDeleteEventHandler(
	        final HashMap<Integer, SecurityLevelBundle> oldSecuriytLevels, final Integer oldId) {
		eventBus.addHandler(DeletedAssetEvent.TYPE, new GeneralEventHandler<DeletedAssetEvent>() {

			@Override
			public void onEvent(DeletedAssetEvent event) {
				if (event.getObject().getId().equals(oldId)) {
					checkForChanges(oldSecuriytLevels, cachedEditObject);
					eventBus.removeHandler(DeletedAssetEvent.TYPE, this);
				}
			}
		});
	}

	@Override
    protected void fireDeletedEvent() {
	    
    }

	@Override
    protected AssetDTO getFlushedObject() {
	    return itemDriver.flush();
    }

	public boolean openChanges() {
		if(clonedEditObject != null){
			return itemDriver.isDirty();
		} else {
			return false;
		}
	    
    }
}
