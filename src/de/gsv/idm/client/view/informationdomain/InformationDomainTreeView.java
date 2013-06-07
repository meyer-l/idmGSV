package de.gsv.idm.client.view.informationdomain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.IconProvider;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.HasIcon;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.TreeSelectionModel;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.images.ImageResources;
import de.gsv.idm.client.presenter.helper.ImageHelper;
import de.gsv.idm.client.presenter.informationdomain.InformationDomainTreePresenter.InformationDomainTreeDisplay;
import de.gsv.idm.client.view.informationdomain.asset.link.AssetLinkTreeToListChooser;
import de.gsv.idm.client.view.informationdomain.asset.widgets.AssetEditorWindow;
import de.gsv.idm.client.view.informationdomain.assettype.widgets.AssettypeEditorWindow;
import de.gsv.idm.client.view.properties.GeneralDTOProperties;
import de.gsv.idm.client.view.properties.ObjectExchange;
import de.gsv.idm.client.view.tree.AssetDragSource;
import de.gsv.idm.client.view.tree.AssetDropTarget;
import de.gsv.idm.client.view.tree.AssettypeTreeDrag;
import de.gsv.idm.client.view.widgets.form.FilterWidget;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssetLinkDTO;
import de.gsv.idm.shared.dto.AssettypeCategoryDTO;
import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.dto.GeneralDTO;

//FIXME: neu speichern von asset-grundtypen aus asset ansicht überprüfen
//FIXME: Kontextmenü für trees
//FIXME: measures von securityzones werden nicht uer assets angelegt
//FIXME: scutzzone eintrag wird nicht immer aktualisiert

//TODO: alles in blau ist schlecht fuer den kontrast
//TODO: Buttons mit horizontallayoutoanels anordnen
//TODO: MassUpdates durch AssetLinks sind unperformant
//TODO: suche: assetgrundtyp liste zu tree umwandeln

//TODO: hotkeys zu besseren benutzung, insbesondere esc zum schließen von EditorWindows

public class InformationDomainTreeView implements InformationDomainTreeDisplay,
        Editor<ObjectExchange<AssettypeDTO>> {
	static final int CLIENT_WIDTH = Window.getClientWidth();
	static final int CLIENT_HEIGHT = Window.getClientHeight();

	Tree<GeneralDTO<?>, ?> assettypeTree;
	TreeStore<GeneralDTO<?>> objects;

	TreeStore<GeneralDTO<?>> assetStore;
	Tree<GeneralDTO<?>, String> informationDomain;

	protected ContentPanel listPanel;
	protected ContentPanel modelPanel;
	protected BorderLayoutContainer mainPanel;
	private TextButton addAssetButton = new TextButton();
	private TextButton removeAssetButton = new TextButton();
	private TextButton editAssetButton = new TextButton();
	private MenuItem insertChildAssetMenu = new MenuItem();
	private MenuItem removeAssetMenu = new MenuItem();

	private TextButton removeAssetmodelButton = new TextButton();
	private TextButton editAssetmodelButton = new TextButton();
	private TextButton addAssetmodelLinkButton = new TextButton();
	private TextButton searchInformationDomainButton = new TextButton();

	private final DBController dbConnector;
	private ImageResources imageBundler;

	FilterWidget<GeneralDTO<?>> assettypeFilterWidget;
	FilterWidget<GeneralDTO<?>> assetFilterWidget;

	public InformationDomainTreeView() {
		this.dbConnector = DBController.getInstance();
		imageBundler = GWT.create(ImageResources.class);

		mainPanel = new BorderLayoutContainer();
		mainPanel.setHeight((int) (CLIENT_HEIGHT * 0.6));
		mainPanel.setWidth(CLIENT_WIDTH);

		listPanel = new ContentPanel();
		modelPanel = new ContentPanel();
		mainPanel.setWestWidget(listPanel, new BorderLayoutData(CLIENT_WIDTH * 0.3));
		mainPanel.setCenterWidget(modelPanel);
		GeneralDTOProperties generalProps = GWT.create(GeneralDTOProperties.class);
		objects = new TreeStore<GeneralDTO<?>>(generalProps.treeKey());
		objects.add(new AssettypeCategoryDTO(-1,
		        "... Asset-Grundtypen werden vom Server geladen ...", "none"));
		assettypeFilterWidget = new FilterWidget<GeneralDTO<?>>(objects);
		assettypeTree = new Tree<GeneralDTO<?>, String>(objects, generalProps.valueProviderLabel()) {
			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONDBLCLICK == event.getTypeInt()) {
					GeneralDTO<?> node = getSelectionModel().getSelectedItem();
					if (node != null && node instanceof AssettypeDTO) {
						AssettypeDTO assettype = (AssettypeDTO) node;
						new AssettypeEditorWindow(assettype);

					}
				}
				super.onBrowserEvent(event);
			}
		};
		assettypeTree.disable();
		assettypeTree.setIconProvider(new IconProvider<GeneralDTO<?>>() {
			@Override
			public ImageResource getIcon(GeneralDTO<?> model) {
				if (model != null) {
					AssettypeDTO assettype = null;
					if (model instanceof AssettypeDTO) {
						assettype = (AssettypeDTO) model;
					}

					if (assettype != null) {
						return ImageHelper.getImageResourceFromIconName((assettype)
						        .getTreeIconName());
					}
				}
				return ImageHelper.getImageResourceFromIconName("");
			}

		});

		new AssettypeTreeDrag(assettypeTree);
		Menu contextMenu = new Menu();
		contextMenu.add(insertChildAssetMenu);
		contextMenu.add(removeAssetMenu);
		assettypeTree.setContextMenu(contextMenu);
		VerticalLayoutContainer listPanelVerticalContainer = new VerticalLayoutContainer();
		listPanel.add(listPanelVerticalContainer);

		listPanelVerticalContainer.add(assettypeFilterWidget, new VerticalLayoutData(1, -1));
		listPanelVerticalContainer.add(assettypeTree, new VerticalLayoutData(1, 1));
		removeAssetButton.setEnabled(false);		
		editAssetButton.setEnabled(false);		
		listPanel.addButton(removeAssetButton);
		listPanel.addButton(editAssetButton);
		listPanel.addButton(addAssetButton);
		removeAssetButton.setIcon(imageBundler.remove());
		editAssetButton.setIcon(imageBundler.edit());
		addAssetButton.setIcon(imageBundler.add());

		assetStore = new TreeStore<GeneralDTO<?>>(generalProps.treeKey());
		assetStore.add(new AssetDTO(-1, -1, new AssettypeDTO(-1,
		        "... Assets werden vom Server geladen")));
		objects.addSortInfo(new StoreSortInfo<GeneralDTO<?>>(generalProps.valueProviderLabel(),
		        SortDir.ASC));
		assetStore.addSortInfo(new StoreSortInfo<GeneralDTO<?>>(generalProps.valueProviderLabel(),
		        SortDir.ASC));

		List<TreeStore.TreeNode<GeneralDTO<?>>> modelList = new ArrayList<TreeStore.TreeNode<GeneralDTO<?>>>();
		assetStore.addSubTree(0, modelList);
		informationDomain = new Tree<GeneralDTO<?>, String>(assetStore,
		        generalProps.valueProviderLabel()) {

			@Override
			protected void onDoubleClick(Event event) {
				GeneralDTO<?> node = getSelectionModel().getSelectedItem();
				if (node != null) {
					AssetDTO asset = null;
					if (node instanceof AssetDTO) {
						asset = (AssetDTO) node;
					} else if (node instanceof AssetLinkDTO) {
						new AssetLinkTreeToListChooser(((AssetLinkDTO) node).getParent());
					}
					if (asset != null) {
						new AssetEditorWindow(asset);
					}

				}
				super.onDoubleClick(event);
			}

		};
		informationDomain.disable();
		informationDomain.setIconProvider(new IconProvider<GeneralDTO<?>>() {
			@Override
			public ImageResource getIcon(GeneralDTO<?> model) {
				if (model != null) {
					AssetDTO asset = null;
					if (model instanceof AssetDTO) {
						asset = (AssetDTO) model;
					} else if (model instanceof AssetLinkDTO) {
						asset = ((AssetLinkDTO) model).getAsset();
					}
					if (asset != null) {
						if (asset.isSecurityAssesmentValid()) {
							return ImageHelper.getImageResourceFromIconName((asset)
							        .getTreeIconName());
						} else {
							return ImageHelper.getErrorImageResourceFromIconName((asset)
							        .getTreeIconName());
						}
					}
				}
				return imageBundler.folder();
			}

		});
		new AssetDragSource(informationDomain);
		final AssetDropTarget modelDrop = new AssetDropTarget(informationDomain,
		        dbConnector.getEventBus(), dbConnector.getAssetController());
		modelDrop.setAllowSelfAsSource(true);
		modelDrop.setAllowDropOnLeaf(true);
		VerticalLayoutContainer modelPanelVerticalContainer = new VerticalLayoutContainer();
		modelPanel.add(modelPanelVerticalContainer);

		assetFilterWidget = new FilterWidget<GeneralDTO<?>>(assetStore);
		assetFilterWidget.getFilter().clear();
		modelPanelVerticalContainer.add(assetFilterWidget, new VerticalLayoutData(1, -1));
		modelPanelVerticalContainer.add(informationDomain, new VerticalLayoutData(1, 1));

		removeAssetmodelButton.setEnabled(false);
		editAssetmodelButton.setEnabled(false);
		addAssetmodelLinkButton.setEnabled(false);

		modelPanel.getHeader().setIcon(imageBundler.help());
		modelPanel.getHeader().setToolTip(
		        "Asset-Grundtypen können durch"
		                + " Drag & Drop im Informationsverbund instanziiert werden."
		                + " Der Informationsverbund wird in einer Baumstruktur verwaltet.");
		modelPanel.addButton(searchInformationDomainButton);
		modelPanel.addButton(removeAssetmodelButton);
		modelPanel.addButton(editAssetmodelButton);
		modelPanel.addButton(addAssetmodelLinkButton);
		searchInformationDomainButton.setIcon(imageBundler.folderSearch());
		removeAssetmodelButton.setIcon(imageBundler.remove());
		editAssetmodelButton.setIcon(imageBundler.edit());
		addAssetmodelLinkButton.setIcon(imageBundler.addLink());
	}

	@Override
	public void setAssettypeTreePanelTitle(String text) {
		listPanel.setHeadingText(text);
	}

	@Override
	public void setAssettypePanelTitle(String text) {
		modelPanel.setHeadingText(text);
	}

	public HasSelectHandlers getAddAssettypeButtonClick() {
		return addAssetButton;
	}

	public void setAddAssettypeButtonText(String text) {
		addAssetButton.setText(text);
	}

	public HasSelectHandlers getRemoveAssettypeButtonClick() {
		return removeAssetButton;
	}

	public void setRemoveAssettypeButtonText(String text) {
		removeAssetButton.setText(text);
	}

	public void setRemoveAssettypeButtonEnabled(Boolean enabled) {
		removeAssetButton.setEnabled(enabled);
	}

	public HasSelectHandlers getEditAssettypeButtonClick() {
		return editAssetButton;
	}

	public void setEditAssettypeButtonText(String text) {
		editAssetButton.setText(text);
	}

	public void setEditAssettypeButtonEnabled(boolean enabled) {
		editAssetButton.setEnabled(enabled);
	}

	@Override
	public void setAssettypeList(List<AssettypeDTO> result) {
		objects.clear();
		for (AssettypeDTO asset : result) {
			addAssettypeToTree(asset);
		}
		assettypeTree.enable();
	}

	public HasSelectHandlers getSearchInformationDomainButtonClick() {
		return searchInformationDomainButton;
	}

	public void setSearchInformationDomainButtonText(String text) {
		searchInformationDomainButton.setText(text);
	}

	public HasSelectHandlers getRemoveAssetButtonClick() {
		return removeAssetmodelButton;
	}

	public void setRemoveAssetButtonText(String text) {
		removeAssetmodelButton.setText(text);
	}

	public HasSelectHandlers getEditAssetButtonClick() {
		return editAssetmodelButton;
	}

	public void setEditAssetButtonText(String text) {
		editAssetmodelButton.setText(text);
	}

	public void setEditAssetButtonEnabled(boolean enabled) {
		editAssetmodelButton.setEnabled(enabled);
	}

	@Override
	public void setAssetTree(List<AssetDTO> result) {
		assetStore.clear();
		for (AssetDTO model : result) {
			assetStore.add(model);
			processAssetDTOChildren(model);
		}
		// XXX: Remove when treeStore add-bug while fitering is fixed. Maybe
		// that is fixed with gxt 3.03 ...
		refreshTreeEntries(informationDomain);
		informationDomain.enable();
	}

	private void processAssetDTOChildren(AssetDTO parent) {
		for (AssetDTO child : parent.getChildren()) {
			assetStore.add(parent, child);
			processAssetDTOChildren(child);
		}
		for (AssetLinkDTO linkedChild : parent.getLinkedAssets()) {
			assetStore.add(parent, linkedChild);
		}
	}

	@Override
	public void addAssettypeEntry(AssettypeDTO object) {
		addAssettypeToTree(object);
		assettypeTree.setExpanded(object, true);
	}

	private void addAssettypeToTree(AssettypeDTO asset) {
		AssettypeCategoryDTO treeCat = getAssetTreeCategory(asset.getCategory());
		GeneralDTO<?> model = objects.findModel(treeCat);
		if (model == null) {
			objects.add(treeCat);
		}
		if (objects.findModel(asset) == null) {
			objects.add(treeCat, asset);
		}
		// XXX: Remove when treeStore add-bug while fitering is fixed. Maybe
		// that is fixed with gxt 3.03 ...
		refreshTreeEntries(assettypeTree);

	}

	// need to save the tree state, because the filter event collapses the tree
	// and there seems to be no alternative event to refresh the tree
	private void refreshTreeEntries(Tree<GeneralDTO<?>, ?> tree) {
		HashMap<String, Boolean> expandStates = new HashMap<String, Boolean>();
		for (GeneralDTO<?> treeItem : tree.getStore().getAll()) {
			if (tree.findNode(treeItem) != null) {
				expandStates.put(treeItem.getTreeKey(), tree.isExpanded(treeItem));
			}
		}

		tree.getStore().fireEvent(new StoreFilterEvent<GeneralDTO<?>>());

		for (Entry<String, Boolean> entry : expandStates.entrySet()) {
			GeneralDTO<?> treeItem = tree.getStore().findModelWithKey(entry.getKey());
			if (treeItem != null && tree.findNode(treeItem) != null) {
				tree.setExpanded(treeItem, entry.getValue());
			}
		}
	}

	public void updateAssettypeEntry(AssettypeDTO object) {
		if (objects.findModel(object) != null) {
			GeneralDTO<?> parent = assettypeTree.getStore().getParent(object);
			AssettypeCategoryDTO treeCat = getAssetTreeCategory(object.getCategory());
			assettypeTree.getStore().remove(object);
			if (parent != null && parent instanceof AssettypeCategoryDTO && !parent.equals(treeCat)) {
				treeCategoryValid((AssettypeCategoryDTO) parent);
			}
			GeneralDTO<?> model = objects.findModel(treeCat);
			if (model == null) {
				objects.add(treeCat);
			}
			// Always re-adding to ensure order
			assettypeTree.getStore().add(treeCat, object);
			// XXX: Remove when treeStore add-bug while fitering is fixed. Maybe
			// that is fixed with gxt 3.03 ...
			refreshTreeEntries(assettypeTree);
			assettypeTree.setExpanded(object, true);
		}

	}

	@Override
	public void removeAssettypeEntry(AssettypeDTO object) {
		GeneralDTO<?> storedAssetType = objects.findModel(object);
		if (storedAssetType != null) {
			objects.remove(storedAssetType);
		}
		treeCategoryValid(getAssetTreeCategory(object.getCategory()));

	}

	private void treeCategoryValid(AssettypeCategoryDTO category) {
		GeneralDTO<?> storedCategory = objects.findModel(category);
		if (storedCategory != null) {
			if (objects.getAllChildren(storedCategory).size() == 0) {
				objects.remove(storedCategory);
			}
		}

	}

	@Override
	public TreeSelectionModel<GeneralDTO<?>> getAssettypeSelectionModel() {
		return assettypeTree.getSelectionModel();
	}

	@Override
	public TreeSelectionModel<GeneralDTO<?>> getAssetTreeSelectionModel() {
		return informationDomain.getSelectionModel();
	}

	@Override
	public void addAssetEntry(AssetDTO object, Integer tempId) {
		AssetDTO dummyAsset = new AssetDTO();
		dummyAsset.setId(tempId);
		GeneralDTO<?> tempObject = assetStore.findModel(dummyAsset);

		if (tempObject != null) {
			assetStore.remove(tempObject);
		}

		if (object.getParent() != null) {
			GeneralDTO<?> parent = assetStore.findModel(object.getParent());
			if (parent != null && parent instanceof AssetDTO) {
				assetStore.add(parent, object);
				refreshAssetTree((AssetDTO) parent);
			}
		} else {
			assetStore.add(object);
		}
		// XXX: Remove when treeStore add-bug while fitering is fixed. Maybe
		// that is fixed with gxt 3.03 ...
		refreshTreeEntries(informationDomain);
		informationDomain.setExpanded(object, true);

	}

	public void updateAssetEntry(AssetDTO object) {
		if (object.getParent() != null) {
			GeneralDTO<?> generalModel = assetStore.findModel(object);
			if (generalModel instanceof AssetDTO) {
				AssetDTO storedModel = (AssetDTO) generalModel;

				if (object.getParent().equals(storedModel.getParent())) {
					GeneralDTO<?> generalParent = assetStore.getParent(object);
					if (generalParent != null && generalParent instanceof AssetDTO) {
						refreshAssetTree((AssetDTO) storedModel);
					}
				} else {
					GeneralDTO<?> newParent = assetStore.findModel(object.getParent());
					if (newParent != null && newParent instanceof AssetDTO) {
						GeneralDTO<?> generalStoredModel = assetStore.getParent(storedModel);
						if (generalStoredModel != null && generalStoredModel instanceof AssetDTO)
							refreshAssetTree((AssetDTO) generalStoredModel);
						assetStore.remove(storedModel);
						assetStore.add(newParent, object);
						refreshAssetTree((AssetDTO) newParent);
					}
				}
			}
		} else {
			if (assetStore.findModel(object) != null) {
				informationDomain.refresh(object);
			}
		}
		// XXX: Remove when treeStore add-bug while fitering is fixed. Maybe
		// that is fixed with gxt 3.03 ...
		refreshTreeEntries(informationDomain);
	}

	public void refreshAssetTree(AssetDTO leaf) {
		for (GeneralDTO<?> asset : assetStore.getAll()) {
			informationDomain.refresh(asset);
		}
	}

	public void removeAssetEntry(AssetDTO object) {
		if (assetStore.findModel(object) != null && object.getParent() != null) {
			GeneralDTO<?> parent = assetStore.getParent(object);
			if (parent != null && parent instanceof AssetDTO) {
				refreshAssetTree((AssetDTO) parent);
			}
		}
		GeneralDTO<?> storeObject = assetStore.findModel(object);
		if (storeObject != null) {
			assetStore.remove(storeObject);
		}
	}

	public void removeAssetLink(AssetLinkDTO object) {
		if (assetStore.findModel(object) != null) {
			if (object.getParent() != null) {
				GeneralDTO<?> parent = assetStore.getParent(object);
				if (parent != null && parent instanceof AssetDTO) {
					refreshAssetTree((AssetDTO) parent);
				}
			}

			assetStore.remove(assetStore.findModel(object));
		}
	}

	public void addAssetLink(AssetLinkDTO object) {
		if (object.getParent() != null) {
			GeneralDTO<?> parent = assetStore.findModel(object.getParent());
			if (parent != null && parent instanceof AssetDTO) {
				assetStore.add(parent, object);
				refreshAssetTree((AssetDTO) parent);
			}
		}

		if (object.getAsset() != null) {
			GeneralDTO<?> asset = assetStore.findModel(object.getAsset());
			if (asset != null && asset instanceof AssetDTO) {
				refreshAssetTree((AssetDTO) asset);
			}
		}

		informationDomain.setExpanded(object, true);
		// XXX: Remove when treeStore add-bug while fitering is fixed. Maybe
		// that is fixed with gxt 3.03 ...
		refreshTreeEntries(informationDomain);
	}

	@Override
	public void setAddAssettypeMenuText(String text) {
		insertChildAssetMenu.setText(text);
	}

	@Override
	public HasIcon getAddAssettypeMenuIcon() {
		return insertChildAssetMenu;
	}

	@Override
	public HasSelectionHandlers<Item> getAddAssettypeMenuClick() {
		return insertChildAssetMenu;
	}

	@Override
	public void setRemoveAssettypeMenuText(String text) {
		removeAssetMenu.setText(text);
	}

	@Override
	public HasIcon getRemoveAssettypeMenuIcon() {
		return removeAssetMenu;
	}

	@Override
	public HasSelectionHandlers<Item> getRemoveAssettypeMenuClick() {
		return removeAssetMenu;
	}

	@Override
	public void setEditAssettypeButtonEnabled(Boolean enabled) {
		editAssetButton.setEnabled(enabled);

	}

	@Override
	public void setRemoveAssetButtonEnabled(boolean enabled) {
		removeAssetmodelButton.setEnabled(enabled);
	}

	private AssettypeCategoryDTO getAssetTreeCategory(AssettypeCategoryDTO category) {
		if (category == null) {
			category = new AssettypeCategoryDTO(-1, "Sonstige", "");
		}
		return category;
	}

	@Override
	public void expandAssettypeCategory(AssettypeCategoryDTO expandItem) {
		assettypeTree.setExpanded(expandItem, true);
	}

	@Override
	public void expandAsset(GeneralDTO<?> expandItem) {
		if (expandItem != null) {
			GeneralDTO<?> storedModel = assetStore.findModel(expandItem);
			if (storedModel != null) {
				informationDomain.setExpanded(storedModel, true);
			}
		}

	}

	@Override
	public HasSelectHandlers getAddAssetLinkButtonClick() {
		return addAssetmodelLinkButton;
	}

	@Override
	public void setAddAssetLinkButtonText(String text) {
		addAssetmodelLinkButton.setText(text);
	}

	@Override
	public void setAddAssetLinkButtonEnabled(Boolean enabled) {
		addAssetmodelLinkButton.setEnabled(enabled);
	}

	@Override
	public TreeStore<GeneralDTO<?>> getAssetTreeStore() {
		return assetStore;
	}

	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public TreeStore<GeneralDTO<?>> getAssettypeTreeStore() {
		return objects;
	}

}
