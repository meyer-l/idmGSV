package de.gsv.idm.client.view.informationdomain.asset.link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.cell.core.client.ButtonCell.IconAlign;
import com.sencha.gxt.data.shared.IconProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent.DndDragStartHandler;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent.DndDropHandler;
import com.sencha.gxt.dnd.core.client.ListViewDragSource;
import com.sencha.gxt.dnd.core.client.ListViewDropTarget;
import com.sencha.gxt.dnd.core.client.TreeDragSource;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.TransactionBufferEvent;
import de.gsv.idm.client.event.db.asset.CreatedAssetEvent;
import de.gsv.idm.client.event.db.asset.DeletedAssetEvent;
import de.gsv.idm.client.event.db.asset.UpdatedAssetEvent;
import de.gsv.idm.client.event.db.asset.link.CreatedAssetLinkEvent;
import de.gsv.idm.client.event.db.asset.link.DeletedAssetLinkEvent;
import de.gsv.idm.client.event.db.assettype.CreatedAssettypeEvent;
import de.gsv.idm.client.event.db.securitylevelchange.AssetSecurityLevelChangedEvent;
import de.gsv.idm.client.images.ImageResources;
import de.gsv.idm.client.presenter.helper.ImageHelper;
import de.gsv.idm.client.presenter.securitylevelchange.data.SecurityLevelBundle;
import de.gsv.idm.client.transaction.CreateTransaction;
import de.gsv.idm.client.transaction.DeleteTransaction;
import de.gsv.idm.client.view.properties.GeneralDTOProperties;
import de.gsv.idm.client.view.widgets.form.FilterWidget;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssetLinkDTO;
import de.gsv.idm.shared.dto.AssettypeCategoryDTO;
import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.SecurityLevelDTO;

//TODO: Added von Links durch assetcategory wenn bereits ein teil verlinkt ist
public class AssetLinkTreeToListChooser extends Window {

	private AssetDTO asset;

	private TextButton addListEntriesButton;
	private TextButton removeListEntriesButton;
	private TextButton cancelButton;
	private TextButton saveButton;
	private ContentPanel westPanel;
	private ContentPanel eastPanel;
	private FlowPanel verticalButtonBuffer;
	private ImageResources imageBundler = GWT.create(ImageResources.class);

	protected TreeStore<GeneralDTO<?>> toChooseStore;
	protected Tree<GeneralDTO<?>, String> toChoose;
	protected ListViewDropTarget<GeneralDTO<?>> toChooseDrag;

	protected ListStore<AssetLinkDTO> selectedStore;
	protected ListView<AssetLinkDTO, String> selected;
	protected ListViewDropTarget<AssetLinkDTO> selectedDropTarget;

	private final DBController dbConnector;
	private final HandlerManager eventBus;

	public AssetLinkTreeToListChooser(AssetDTO toLink) {
		dbConnector = DBController.getInstance();
		eventBus = dbConnector.getEventBus();
		asset = toLink;
		setBodyBorder(false);
		setWidth(800);
		setHeight(600);

		GeneralDTOProperties props = GWT.create(GeneralDTOProperties.class);
		selectedStore = new ListStore<AssetLinkDTO>(props.key());
		selected = new ListView<AssetLinkDTO, String>(selectedStore, props.valueProviderLabel());

		toChooseStore = new TreeStore<GeneralDTO<?>>(props.treeKey());
		toChooseStore.addSortInfo(new StoreSortInfo<GeneralDTO<?>>(props.valueProviderLabel(),
		        SortDir.ASC));

		toChoose = new Tree<GeneralDTO<?>, String>(toChooseStore, props.treeToListLabel());
		toChoose.setIconProvider(new IconProvider<GeneralDTO<?>>() {
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
		toChoose.setIconProvider(new IconProvider<GeneralDTO<?>>() {
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
		toChooseStore.add(new AssettypeDTO(-1, "Assets werden vom Server geladen"));
		selectedStore
		        .add(new AssetLinkDTO(-1, new AssetDTO(), new AssetDTO(-1, -1, new AssettypeDTO(-1,
		                "... Verknüpfungen mit Assets werden vom Server geladen ..."))));
		toChoose.disable();
		selected.disable();
		dbConnector.getAssetController().getAll(asset.getDomainId(),
		        new AsyncCallback<ArrayList<AssetDTO>>() {

			        @Override
			        public void onSuccess(ArrayList<AssetDTO> result) {
				        toChooseStore.clear();
				        selectedStore.clear();
				        for (AssetDTO addAsset : result) {
					        if (!addAsset.equals(asset)) {
						        addAssetToChooseStore(addAsset);
					        }
				        }

				        for (AssetDTO child : asset.getChildren()) {
					        removeAssetFromChooseStore(child);
				        }

				        for (AssetLinkDTO link : asset.getLinkedAssets()) {
					        removeAssetFromChooseStore(link.getAsset());
					        selectedStore.add(link);
				        }
				        toChoose.enable();
				        selected.enable();
			        }

			        @Override
			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(Level.SEVERE,
				                "Error while calling AssetImpl.getAll");
			        }
		        });

		selectedStore.addSortInfo(new StoreSortInfo<AssetLinkDTO>(props.valueProviderLabel(),
		        SortDir.ASC));

		TreeDragSource<GeneralDTO<?>> chooseDrag = new TreeDragSource<GeneralDTO<?>>(toChoose) {
			@Override
			protected void onDragDrop(DndDropEvent event) {
				super.onDragDrop(event);
				for (GeneralDTO<?> rootItem : toChooseStore.getRootItems()) {
					List<GeneralDTO<?>> children = toChooseStore.getChildren(rootItem);
					for (GeneralDTO<?> child : children) {
						List<GeneralDTO<?>> rootChildren = toChooseStore.getChildren(child);
						if (rootChildren.size() < 1) {
							toChooseStore.remove(child);
						}
					}
					List<GeneralDTO<?>> afterChildPurge = toChooseStore.getChildren(rootItem);
					if (afterChildPurge.size() < 1) {
						toChooseStore.remove(rootItem);
					}
				}
			}

		};
		chooseDrag.addDragStartHandler(new DndDragStartHandler() {

			@Override
			public void onDragStart(DndDragStartEvent event) {
				GeneralDTO<?> sel = toChoose.getSelectionModel().getSelectedItem();
				if (sel != null
				        && (sel instanceof AssettypeCategoryDTO || sel instanceof AssettypeDTO)) {
					event.setCancelled(true);
					event.getStatusProxy().setStatus(false);
				}

			}
		});

		new ListViewDragSource<AssetLinkDTO>(selected);
		AssetLinkTreeDropTarget treeDrop = new AssetLinkTreeDropTarget(toChoose);
		treeDrop.setAllowDropOnLeaf(false);
		treeDrop.addDropHandler(new DndDropHandler() {

			@Override
			public void onDrop(DndDropEvent event) {
				HashMap<String, Boolean> expandStates = new HashMap<String, Boolean>();
				for (GeneralDTO<?> treeItem : toChoose.getStore().getAll()) {
					if (toChoose.findNode(treeItem) != null) {
						expandStates.put(treeItem.getTreeKey(), toChoose.isExpanded(treeItem));
					}
				}

				toChoose.getStore().fireEvent(new StoreFilterEvent<GeneralDTO<?>>());

				for (Entry<String, Boolean> entry : expandStates.entrySet()) {
					GeneralDTO<?> treeItem = toChoose.getStore().findModelWithKey(entry.getKey());
					if (treeItem != null && toChoose.findNode(treeItem) != null) {
						toChoose.setExpanded(treeItem, entry.getValue());
					}
				}
				// XXX: Remove when treeStore add-bug while fitering is fixed.
				// Maybe that is fixed with gxt 3.03 ...

			}
		});

		new AssetLinkListDropTarget(selected, asset);

		setSelectionModels();

		BorderLayoutContainer con = new BorderLayoutContainer();

		westPanel = new ContentPanel();
		westPanel.getHeader().setIcon(imageBundler.help());
		westPanel.getHeader().setToolTip(
		        "Assets im Informationsverbund mit denen" + " das Asset " + asset.getName()
		                + " noch nicht in Verbindung steht. Die Assets sind"
		                + " anhand ihreren Asset-Grundtypen sortiert.");
		VerticalLayoutContainer westPanelVerticalContainer = new VerticalLayoutContainer();
		westPanel.add(westPanelVerticalContainer);
		westPanelVerticalContainer.add(new FilterWidget<GeneralDTO<?>>(toChooseStore),
		        new VerticalLayoutData(1, -1));
		westPanelVerticalContainer.add(toChoose, new VerticalLayoutData(1, 1));
		con.setWestWidget(westPanel, new BorderLayoutData(340));
		eastPanel = new ContentPanel();
		eastPanel.getHeader().setIcon(imageBundler.help());
		eastPanel.getHeader().setToolTip(
		        "Bestehende Verbindungen des Assets " + asset.getName()
		                + " zu anderen Assets im Informationsverbund");
		VerticalLayoutContainer eastPanelVerticalContainer = new VerticalLayoutContainer();
		eastPanel.add(eastPanelVerticalContainer);
		eastPanelVerticalContainer.add(new FilterWidget<AssetLinkDTO>(selectedStore),
		        new VerticalLayoutData(1, -1));
		eastPanelVerticalContainer.add(selected, new VerticalLayoutData(1, 1));
		con.setEastWidget(eastPanel, new BorderLayoutData(340));

		addListEntriesButton = new TextButton();
		addListEntriesButton.setText("Hinzufügen");
		addListEntriesButton.setIcon(imageBundler.arrowRight());
		addListEntriesButton.setIconAlign(IconAlign.RIGHT);
		FlowPanel addPanel = new FlowPanel();
		addPanel.add(addListEntriesButton);

		removeListEntriesButton = new TextButton();
		removeListEntriesButton.setText("Entfernen");
		removeListEntriesButton.setIcon(imageBundler.arrowLeft());
		removeListEntriesButton.setWidth("85px");
		FlowPanel removePanel = new FlowPanel();
		removePanel.add(removeListEntriesButton);

		FlowPanel buttonPanel = new FlowPanel();
		buttonPanel.add(addPanel);
		buttonPanel.add(removePanel);

		CenterLayoutContainer centerPanel = new CenterLayoutContainer();
		centerPanel.add(buttonPanel);
		con.setCenterWidget(centerPanel);

		add(con);
		verticalButtonBuffer = new FlowPanel();
		verticalButtonBuffer.setWidth("270px");
		cancelButton = new TextButton("Abbrechen");
		saveButton = new TextButton("Ok");

		addButtons();
		bindEventBus();
		bindGeneralView();
		show();
	}

	private void setSelectionModels() {
		selected.getSelectionModel().addSelectionChangedHandler(
		        new SelectionChangedHandler<AssetLinkDTO>() {
			        @Override
			        public void onSelectionChanged(SelectionChangedEvent<AssetLinkDTO> event) {
				        if (event.getSelection().size() > 0) {
					        toChoose.getSelectionModel().deselectAll();
				        }
			        }
		        });

		toChoose.getSelectionModel().addSelectionChangedHandler(
		        new SelectionChangedHandler<GeneralDTO<?>>() {
			        @Override
			        public void onSelectionChanged(SelectionChangedEvent<GeneralDTO<?>> event) {
				        if (event.getSelection().size() > 0) {
					        selected.getSelectionModel().deselectAll();
				        }
				        if (toChoose.getSelectionModel().getSelectedItem() instanceof AssettypeCategoryDTO) {
					        toChoose.setExpanded(toChoose.getSelectionModel().getSelectedItem(),
					                true);
				        }
			        }
		        });
	}

	private void addButtons() {
		addButton(verticalButtonBuffer);
		addButton(cancelButton);
		addButton(saveButton);
	}

	private void bindEventBus() {
		eventBus.addHandler(CreatedAssetEvent.TYPE, new GeneralEventHandler<CreatedAssetEvent>() {

			public void onEvent(CreatedAssetEvent event) {
				addAssetToChooseStore(event.getObject());
			}
		});
		eventBus.addHandler(DeletedAssetEvent.TYPE, new GeneralEventHandler<DeletedAssetEvent>() {

			public void onEvent(DeletedAssetEvent event) {
				removeAssetFromChooseStore(event.getObject());
			}
		});

		eventBus.addHandler(CreatedAssettypeEvent.TYPE,
		        new GeneralEventHandler<CreatedAssettypeEvent>() {
			        public void onEvent(CreatedAssettypeEvent event) {
				        addAssettype(event.getObject());
			        }
		        });

		eventBus.addHandler(CreatedAssetLinkEvent.TYPE,
		        new GeneralEventHandler<CreatedAssetLinkEvent>() {
			        public void onEvent(CreatedAssetLinkEvent event) {
				        AssetLinkDTO eventObject = event.getObject();
				        if (asset.equals(eventObject.getParent())) {
					        GeneralDTO<?> modelInChooseStore = toChooseStore.findModel(eventObject
					                .getAsset());
					        if (modelInChooseStore != null
					                && modelInChooseStore instanceof AssetDTO) {
						        removeAssetFromChooseStore((AssetDTO) modelInChooseStore);
					        }

					        AssetLinkDTO modelInSelectStore = selectedStore.findModel(eventObject);
					        if (modelInSelectStore == null) {
						        selectedStore.add(event.getObject());
					        }
				        }

			        }
		        });

		eventBus.addHandler(DeletedAssetLinkEvent.TYPE,
		        new GeneralEventHandler<DeletedAssetLinkEvent>() {
			        public void onEvent(DeletedAssetLinkEvent event) {
				        AssetLinkDTO eventObject = event.getObject();
				        if (asset.equals(eventObject.getParent())) {
					        GeneralDTO<?> modelInChooseStore = toChooseStore.findModel(eventObject
					                .getAsset());
					        if (modelInChooseStore == null) {
						        addAssetToChooseStore(eventObject.getAsset());
					        }

					        AssetLinkDTO modelInSelectStore = selectedStore.findModel(eventObject);
					        if (modelInSelectStore != null) {
						        selectedStore.remove(modelInSelectStore);
					        }
				        }

			        }
		        });

	}

	protected void addAssettype(AssettypeDTO object) {
		if (toChooseStore.findModel(object) == null) {
			AssettypeCategoryDTO treeCat = getAssetTreeCategory(object.getCategory());
			GeneralDTO<?> model = toChooseStore.findModel(treeCat);
			if (model == null) {
				toChooseStore.add(treeCat);
			}
			if (treeCat != null) {
				toChooseStore.add(treeCat, object);
			}
		}

	}

	private AssettypeCategoryDTO getAssetTreeCategory(AssettypeCategoryDTO category) {
		if (category == null) {
			category = new AssettypeCategoryDTO(-1, "Sonstige");
		}
		return category;
	}

	private void bindGeneralView() {
		setHeadingText("Verknüpfungen für " + asset.getLabel() + " bearbeiten");
		westPanel.setHeadingText("Assets im Informationsverbund");
		eastPanel.setHeadingText("Bestehende Verknüpfungen zu Assets");

		cancelButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				hide();
			}
		});

		addSaveButtonOnSelect();

		addListEntriesButton.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				List<GeneralDTO<?>> selection = toChoose.getSelectionModel().getSelectedItems();
				for (GeneralDTO<?> selected : selection) {
					if (selected instanceof AssetDTO) {
						selectedStore.add(new AssetLinkDTO(asset.getDomainId(), asset,
						        (AssetDTO) selected));
					}
				}
				for (GeneralDTO<?> i : selection) {
					if (i instanceof AssetDTO) {
						toChooseStore.remove(i);
					}
				}

				for (GeneralDTO<?> rootItem : toChooseStore.getRootItems()) {
					List<GeneralDTO<?>> children = toChooseStore.getChildren(rootItem);
					for (GeneralDTO<?> child : children) {
						List<GeneralDTO<?>> rootChildren = toChooseStore.getChildren(child);
						if (rootChildren.size() < 1) {
							toChooseStore.remove(child);
						}
					}
					List<GeneralDTO<?>> afterChildPurge = toChooseStore.getChildren(rootItem);
					if (afterChildPurge.size() < 1) {
						toChooseStore.remove(rootItem);
					}
				}
			}
		});
		removeListEntriesButton.addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				List<AssetLinkDTO> selection = selected.getSelectionModel().getSelectedItems();
				for (AssetLinkDTO selected : selection) {
					addAssetToChooseStore(selected.getAsset());
				}
				for (AssetLinkDTO i : selection) {
					selectedStore.remove(i);
				}
			}
		});

	}

	private void addSaveButtonOnSelect() {
		saveButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				List<AssetLinkDTO> oldLinks = asset.getLinkedAssets();
				List<AssetLinkDTO> currentLinks = selectedStore.getAll();

				List<AssetLinkDTO> toAddList = new ArrayList<AssetLinkDTO>(currentLinks);
				toAddList.removeAll(oldLinks);
				for (final AssetLinkDTO toAdd : toAddList) {
					createAssetLinkRpcCall(toAdd);
				}

				List<AssetLinkDTO> toRemoveList = new ArrayList<AssetLinkDTO>(oldLinks);
				toRemoveList.removeAll(currentLinks);
				for (final AssetLinkDTO toRemove : toRemoveList) {
					removeAssetLinkRpcCall(toRemove);
				}

				hide();
			}
		});
	}

	private void addAssetToChooseStore(AssetDTO addAsset) {
		if (addAsset.getId() > 0) {
			if (addAsset.getAssettype() == null) {
				return;
			}

			if (toChooseStore.findModel(addAsset) == null) {
				GeneralDTO<?> storedModel = toChooseStore.findModel(addAsset.getAssettype());
				if (storedModel == null) {
					addAssettype(addAsset.getAssettype());
					toChooseStore.add(addAsset.getAssettype(), addAsset);
				} else {
					toChooseStore.add(storedModel, addAsset);
				}
			}
		}
		// XXX: Remove when treeStore add-bug while fitering is fixed. Maybe
		// that is fixed with gxt 3.03 ...
		toChooseStore.fireEvent(new StoreFilterEvent<GeneralDTO<?>>());
	}

	private void removeAssetFromChooseStore(AssetDTO addAsset) {
		GeneralDTO<?> linkAsset = toChooseStore.findModel(addAsset);
		if (linkAsset != null) {
			GeneralDTO<?> parentAsset = toChooseStore.getParent(linkAsset);
			toChooseStore.remove(linkAsset);
			checkParentsChilds(parentAsset);

		}
	}

	void checkParentsChilds(GeneralDTO<?> parent) {
		if (parent != null) {
			List<GeneralDTO<?>> children = toChooseStore.getChildren(parent);
			if (children == null || children.size() == 0) {
				GeneralDTO<?> parentsParent = toChooseStore.getParent(parent);
				toChooseStore.remove(parent);
				checkParentsChilds(parentsParent);
			}
		}
	}

	protected void createAssetLinkRpcCall(final AssetLinkDTO toAdd) {
		handleChange(toAdd);

		dbConnector.getAssetLinkController().create(toAdd, new AsyncCallback<AssetLinkDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				DBController.getLogger().log(Level.SEVERE,
				        "Error while creating " + toAdd.getClassName());
			}

			@Override
			public void onSuccess(AssetLinkDTO result) {
				eventBus.fireEvent(new TransactionBufferEvent(new CreateTransaction<AssetLinkDTO>(
				        dbConnector.getAssetLinkController(), toAdd)));
			}
		});
	}

	protected void removeAssetLinkRpcCall(final AssetLinkDTO toDeleteItem) {
		handleChange(toDeleteItem);

		dbConnector.getAssetLinkController().delete((AssetLinkDTO) toDeleteItem,
		        new AsyncCallback<AssetLinkDTO>() {
			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(
				                Level.SEVERE,
				                "Error while deleting " + toDeleteItem.getClassName() + " Id: "
				                        + toDeleteItem.getId());
			        }

			        public void onSuccess(AssetLinkDTO result) {
				        String text = result.getClassName() + " " + result.getName()
				                + " wurde gelöscht.";
				        eventBus.fireEvent(new TransactionBufferEvent(
				                new DeleteTransaction<AssetLinkDTO>(dbConnector
				                        .getAssetLinkController(), result)));
				        Info.display("RPC:", text);
			        }
		        });

	}

	private void handleChange(final AssetLinkDTO assetLink) {
		final HashMap<Integer, SecurityLevelBundle> oldSecuriytLevels = new HashMap<Integer, SecurityLevelBundle>();
		for (AssetDTO asset : DBController.getInstance().getAssetController().getMapValues()) {
			oldSecuriytLevels.put(
			        asset.getId(),
			        new SecurityLevelBundle(asset.getCalculatedSecurityAssesment(), asset
			                .getCalculatedAvailability(), asset.getCalculatedConfidentiality(),
			                asset.getCalculatedIntegrity()));
		}

		eventBus.addHandler(UpdatedAssetEvent.TYPE, new GeneralEventHandler<UpdatedAssetEvent>() {

			@Override
			public void onEvent(UpdatedAssetEvent event) {
				if (event.getObject().getId().equals(assetLink.getParent().getId())) {
					checkForChanges(oldSecuriytLevels, null);
					eventBus.removeHandler(UpdatedAssetEvent.TYPE, this);
				}
			}
		});

		eventBus.addHandler(UpdatedAssetEvent.TYPE, new GeneralEventHandler<UpdatedAssetEvent>() {

			@Override
			public void onEvent(UpdatedAssetEvent event) {
				if (event.getObject().getId().equals(assetLink.getAsset().getId())) {
					checkForChanges(oldSecuriytLevels, null);
					eventBus.removeHandler(UpdatedAssetEvent.TYPE, this);
				}
			}
		});
	}

	private void checkForChanges(HashMap<Integer, SecurityLevelBundle> oldSecuriytLevels,
	        AssetDTO updatedAsset) {
		for (AssetDTO asset : DBController.getInstance().getAssetController().getMapValues()) {
			if (updatedAsset == null || !asset.equals(updatedAsset)) {
				SecurityLevelDTO newSecurityLevel = asset.getCalculatedSecurityAssesment();
				SecurityLevelBundle oldSecurityLevel = oldSecuriytLevels.get(asset.getId());
				if (!newSecurityLevel.getId().equals(
				        oldSecurityLevel.getOldSecurityAssesment().getId())) {
					eventBus.fireEvent(new AssetSecurityLevelChangedEvent(asset, oldSecurityLevel));
				}
			}

		}
	}

}
