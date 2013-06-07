package de.gsv.idm.client.presenter.informationdomain.assettype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.assettype.DeletedAssettypeEvent;
import de.gsv.idm.client.event.db.assettype.UpdatedAssettypeEvent;
import de.gsv.idm.client.presenter.general.GeneralAssetCheckPresenter;
import de.gsv.idm.client.presenter.helper.ImageHelper;
import de.gsv.idm.client.presenter.informationdomain.assettype.category.AssettypeCategoryEditorPresenter;
import de.gsv.idm.client.presenter.securitylevelchange.data.SecurityLevelBundle;
import de.gsv.idm.client.view.informationdomain.assettype.AssettypeEditor;
import de.gsv.idm.client.view.informationdomain.assettype.category.AssettypeCategoryEditor;
import de.gsv.idm.client.view.informationdomain.assettype.widgets.AssettypeMeasureGridToGridChooser;
import de.gsv.idm.client.view.informationdomain.assettype.widgets.AssettypeModuleGridToGridChooser;
import de.gsv.idm.client.view.widgets.window.ChooseImageWindow;
import de.gsv.idm.client.view.widgets.window.EditorWindow;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssettypeCategoryDTO;
import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.dto.AssettypeMeasureLinkDTO;
import de.gsv.idm.shared.dto.AssettypeModuleLinkDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;

public class AssettypeEditorPresenter extends GeneralAssetCheckPresenter<AssettypeDTO> {

	public interface AssettypeEditorDisplay extends GeneralEditorDisplay<AssettypeDTO> {
		public HasChangeHandlers getItAssetsCheckBoxClick();

		public void setItAssetsFieldsEnabled(Boolean bool);

		public HasChangeHandlers getManuelSecurityAssesmentCheckBoxClick();

		public void setSecurityAssesmentFieldsEnabled(Boolean bool);

		void setComboBoxList(ArrayList<EmployeeDTO> data);

		public HasSelectHandlers getAddAssetModuleLinkClick();

		void setAddAssetModuleLinkText(String text);

		ListStore<AssettypeModuleLinkDTO> getModulesListStore();

		public HasSelectHandlers getAddAssetMeasureLinkClick();

		void setAddAssetMeasureLinkText(String text);

		ListStore<AssettypeMeasureLinkDTO> getMeasuresListStore();

		void setAssettypeCategoryList(List<AssettypeCategoryDTO> treeCategories);

		void setChooseIconButtonText(String text);

		HasSelectHandlers getChooseIconButtonClick();

		Image getImage();

		String getIconName();

		void setImageResource(ImageResource image);

		void setChooseIconButtonToolTip(String toolTip);

		void setPropergateSecurityAssesmentToolTip(String toolTip);

		void setManualSecurityAssesmentToolTip(String toolTip);

		void setEditCategoryButtonText(String text);

		HasSelectHandlers getEditCategoryButtonClick();

		void setAssetsMeasuresContainerToolTip(String text);

		void setPersonalDataTooltip(String toolTip);

		public void resetEditor();
	};

	interface ItemDriver extends SimpleBeanEditorDriver<AssettypeDTO, AssettypeEditor> {
	}

	protected ItemDriver itemDriver = GWT.create(ItemDriver.class);
	private Integer domainId;

	protected final AssettypeEditorDisplay editorDisplay;

	private final DBController dbConnector;

	public AssettypeEditorPresenter(AssettypeEditorDisplay editorDisplay, Integer domain_id) {
		super(DBController.getInstance().getAssettypeController(), editorDisplay);
		dbConnector = DBController.getInstance();
		this.domainId = domain_id;
		this.editorDisplay = editorDisplay;
	}

	@Override
	public IsWidget go() {
		itemDriver.initialize((AssettypeEditor) editorDisplay);
		bindView();
		return editorDisplay.asWidget();
	}

	public void bindView() {

		editorDisplay.setEditCategoryButtonText("Kategorie bearbeiten");
		editorDisplay.getEditCategoryButtonClick().addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				new EditorWindow<AssettypeCategoryDTO>(new AssettypeCategoryEditorPresenter(
				        new AssettypeCategoryEditor()), itemDriver.flush().getCategory());
			}
		});

		editorDisplay.setChooseIconButtonText("Icon auswählen");
		editorDisplay.getChooseIconButtonClick().addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				new ChooseImageWindow(editorDisplay.getImage());
			}
		});

		editorDisplay.getItAssetsCheckBoxClick().addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				CheckBox check = (CheckBox) event.getSource();
				editorDisplay.setItAssetsFieldsEnabled(check.getValue());
			}
		});

		editorDisplay.getManuelSecurityAssesmentCheckBoxClick().addChangeHandler(
		        new ChangeHandler() {
			        public void onChange(ChangeEvent event) {
				        CheckBox check = (CheckBox) event.getSource();
				        if (check.getValue()) {
					        editorDisplay.setSecurityAssesmentFieldsEnabled(true);
				        } else {
					        editorDisplay.setSecurityAssesmentFieldsEnabled(false);
				        }
			        }
		        });

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

		editorDisplay.setAddAssetModuleLinkText("Verlinkte Bausteine bearbeiten");
		editorDisplay.getAddAssetModuleLinkClick().addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				new AssettypeModuleGridToGridChooser(editorDisplay.getModulesListStore(),
				        editorDisplay.getMeasuresListStore());
			}
		});

		editorDisplay.setAddAssetMeasureLinkText("Verlinkte Maßnahmen bearbeiten");
		editorDisplay.getAddAssetMeasureLinkClick().addSelectHandler(new SelectHandler() {

			public void onSelect(SelectEvent event) {
				new AssettypeMeasureGridToGridChooser(editorDisplay.getMeasuresListStore());

			}
		});
		dbConnector.getAssettypeCategoryController().getAll(
		        new AsyncCallback<ArrayList<AssettypeCategoryDTO>>() {

			        @Override
			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(Level.SEVERE,
				                "Error while calling AssetCategoriesImpl.getAll");
			        }

			        @Override
			        public void onSuccess(ArrayList<AssettypeCategoryDTO> result) {
				        editorDisplay.setAssettypeCategoryList(result);
			        }
		        });
		editorDisplay.setPersonalDataTooltip("Bei Personbezug wird für das Schutzziel"
		        + " Vertraulichkeit immer der Wert 'Sehr Hoch' propagiert.");
		editorDisplay.setChooseIconButtonToolTip("Das Icon wird zur Darstellung von"
		        + " Asset-Grundtypen im Informationsverbund verwendet, wenn kein Icon ausgewählt"
		        + " wurde, wird das Icon der Asset-Grundtyp-Kategorie verwendet.");
		editorDisplay.setManualSecurityAssesmentToolTip("Wenn der Schutzbedarf für Assets von"
		        + " diesem Typ nicht über die Verknüpfung von Mitarbeitern zugewiesen werden kann,"
		        + " kann der Schutzbedarf auch manuell festgelegt werden.");

	}

	@Override
	public void doNew() {
		AssettypeDTO assettype = new AssettypeDTO(domainId);
		assettype.setName("Neuer Asset-Grundtyp");
		doEdit(assettype);
	}

	@Override
	public void doSave() {
		AssettypeDTO flushedObject = itemDriver.flush();
		flushedObject.setIconName(editorDisplay.getIconName());
		if (clonedEditObject.getId() == null) {
			createObject(flushedObject);
		} else {
			updateObject(flushedObject, clonedEditObject.clone());
		}
	}

	@Override
	public void doDelete() {
		final AssettypeDTO toDelete = itemDriver.flush();
		deleteObject(toDelete);
	}

	@Override
	public void doEdit(AssettypeDTO object) {
		clonedEditObject = object.clone();
		itemDriver.edit(object.clone());
		if (object.getIconName() != null && !object.getIconName().equals("")) {
			editorDisplay.setImageResource(ImageHelper.getImageResourceFromIconName(object
			        .getIconName()));
		}
		editorDisplay.setPropergateSecurityAssesmentToolTip(getDynamicPropergateToolTip(object));

		editorDisplay.setAssetsMeasuresContainerToolTip("Vererbung: Durch Vererbung wird"
		        + " der Umsetzungsgrad von Instanzen an untergeordnete Assets weitergegeben."
		        + getDynamicInheritToolTip(object));
		editorDisplay.setSaveEnabled(true);
		editorDisplay.setSaveAndNewEnabled(true);
		editorDisplay.setItAssetsFieldsEnabled(object.isItAsset());
		editorDisplay.setSecurityAssesmentFieldsEnabled(object.isManuelSecurityAssesment());
		if(object.getId() == null || object.getId() < 0 ){
			editorDisplay.resetEditor();
		}
	}

	private String getDynamicPropergateToolTip(AssettypeDTO object) {
		String dynamicToolTipText = "";
		Integer numberOfExampleAssets = 4;
		List<AssetDTO> assetsWithParent = new ArrayList<AssetDTO>();
		for (AssetDTO asset : object.getLinkedAssets()) {
			if (asset.getParent() != null) {
				assetsWithParent.add(asset);
			}
		}
		if (assetsWithParent.size() > 0) {
			List<AssetDTO> toolTipAssets = new ArrayList<AssetDTO>();
			for (AssetDTO asset : assetsWithParent) {
				if (asset.getIdentifier() != null && !asset.getIdentifier().isEmpty()
				        && toolTipAssets.size() < numberOfExampleAssets) {
					toolTipAssets.add(asset);
				}
			}

			if (toolTipAssets.size() < numberOfExampleAssets) {
				for (AssetDTO asset : assetsWithParent) {
					if (toolTipAssets.size() < numberOfExampleAssets) {
						toolTipAssets.add(asset);
					}
				}
			}
			dynamicToolTipText += "Festlegen, ob der Schutzbedarf in"
			        + " Instanzen an übergeordnete Assets, wie z.B. ";
			String assetList = "";
			for (int i = 0; i < toolTipAssets.size(); i++) {
				if (i != 0) {
					if (i == toolTipAssets.size()) {
						assetList += " oder ";
					} else {
						assetList += ", ";
					}
				}
				assetList += toolTipAssets.get(i).getName();
			}

			dynamicToolTipText += assetList + " weitergegeben wird.";
		} else {
			dynamicToolTipText = "Festlegen, ob der Schutzbedarf in"
			        + " Instanzen an übergeordnete Assets weitergegeben wird.";
		}

		return dynamicToolTipText;
	}

	private String getDynamicInheritToolTip(AssettypeDTO object) {
		String dynamicToolTipText = "";
		Integer numberOfExampleAssets = 2;
		List<AssetDTO> assetsWithChildren = new ArrayList<AssetDTO>();
		for (AssetDTO asset : object.getLinkedAssets()) {
			if (asset.getChildren().size() > 0) {
				assetsWithChildren.add(asset);
			}
		}
		if (assetsWithChildren.size() > 0) {
			List<AssetDTO> toolTipAssets = new ArrayList<AssetDTO>();
			for (AssetDTO asset : assetsWithChildren) {
				if (asset.getIdentifier() != null && !asset.getIdentifier().isEmpty()
				        && toolTipAssets.size() < numberOfExampleAssets) {
					toolTipAssets.add(asset);
				}
			}
			if (toolTipAssets.isEmpty()) {
				dynamicToolTipText += " Von der Umsetzung von vererbten Maßnahme würden z.B.";
				Integer childAssetCounter = 0;
				String childNameList = "";
				for (AssetDTO asset : assetsWithChildren) {
					for (AssetDTO child : asset.getChildren()) {
						if (childAssetCounter < 5) {
							if (!childNameList.isEmpty()) {
								childNameList += ", ";
							}
							childNameList += child.getName();
							childAssetCounter++;
						} else {
							childNameList += ", u.w.";
						}

					}
				}
				if (childAssetCounter == 1) {
					dynamicToolTipText += " das Asset ";
				} else {
					dynamicToolTipText += " die Assets ";
				}
				dynamicToolTipText += childNameList + " profitieren.";
			} else {
				for (AssetDTO asset : toolTipAssets) {
					if (dynamicToolTipText.isEmpty()) {
						dynamicToolTipText += "Bei";
					} else {
						dynamicToolTipText += "und bei";
					}
					dynamicToolTipText += asset.getName() + getAssetChildrenList(asset)
					        + " von der Umsetzung einer vererbten Maßnahme profitieren.";
				}
			}
		}

		return dynamicToolTipText;
	}

	private String getAssetChildrenList(AssetDTO asset) {
		String childrenText = "";
		if (asset.getChildren().size() == 1) {
			childrenText += "würde ";
		} else {
			childrenText += "würden ";
		}
		Integer childCounter = 0;
		String childrenNames = "";
		for (AssetDTO child : asset.getChildren()) {
			if (childCounter < 3) {
				if (!childrenNames.isEmpty()) {
					childrenNames += ", ";
				}
				childrenNames += child.getName();
				childCounter++;
			} else {
				childrenNames += ", u.w";
				break;
			}
		}
		childrenText += childrenNames;
		return childrenText;
	}

	@Override
	protected void addUpdateEventHandler(
	        final HashMap<Integer, SecurityLevelBundle> oldSecuriytLevels) {
		eventBus.addHandler(UpdatedAssettypeEvent.TYPE,
		        new GeneralEventHandler<UpdatedAssettypeEvent>() {

			        @Override
			        public void onEvent(UpdatedAssettypeEvent event) {
				        checkForChanges(oldSecuriytLevels);
				        eventBus.removeHandler(UpdatedAssettypeEvent.TYPE, this);
			        }
		        });
	}

	@Override
	protected void addDeleteEventHandler(
	        final HashMap<Integer, SecurityLevelBundle> oldSecuriytLevels, final Integer oldId) {
		eventBus.addHandler(DeletedAssettypeEvent.TYPE,
		        new GeneralEventHandler<DeletedAssettypeEvent>() {

			        @Override
			        public void onEvent(DeletedAssettypeEvent event) {
				        if (event.getObject().getId().equals(oldId)) {
					        checkForChanges(oldSecuriytLevels);
					        eventBus.removeHandler(DeletedAssettypeEvent.TYPE, this);
				        }
			        }
		        });
	}

	@Override
    protected void fireDeletedEvent() {
	    
    }

	@Override
    protected AssettypeDTO getFlushedObject() {
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
