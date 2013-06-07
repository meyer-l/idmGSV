package de.gsv.idm.client.presenter.informationdomain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.HasIcon;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tree.TreeSelectionModel;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.TransactionBufferEvent;
import de.gsv.idm.client.event.db.asset.CreatedAssetEvent;
import de.gsv.idm.client.event.db.asset.DeletedAssetEvent;
import de.gsv.idm.client.event.db.asset.UpdatedAssetEvent;
import de.gsv.idm.client.event.db.asset.link.CreatedAssetLinkEvent;
import de.gsv.idm.client.event.db.asset.link.DeletedAssetLinkEvent;
import de.gsv.idm.client.event.db.assettype.CreatedAssettypeEvent;
import de.gsv.idm.client.event.db.assettype.DeletedAssettypeEvent;
import de.gsv.idm.client.event.db.assettype.UpdatedAssettypeEvent;
import de.gsv.idm.client.event.db.securitylevelchange.AssetSecurityLevelChangedEvent;
import de.gsv.idm.client.images.ImageResources;
import de.gsv.idm.client.presenter.Presenter;
import de.gsv.idm.client.presenter.search.SearchPresenter;
import de.gsv.idm.client.presenter.securitylevelchange.data.SecurityLevelBundle;
import de.gsv.idm.client.transaction.DeleteTransaction;
import de.gsv.idm.client.view.informationdomain.InformationDomainTreeView;
import de.gsv.idm.client.view.informationdomain.asset.link.AssetLinkTreeToListChooser;
import de.gsv.idm.client.view.informationdomain.asset.widgets.AssetEditorWindow;
import de.gsv.idm.client.view.informationdomain.assettype.widgets.AssettypeEditorWindow;
import de.gsv.idm.client.view.properties.ObjectExchange;
import de.gsv.idm.client.view.search.SearchView;
import de.gsv.idm.client.view.search.SearchWindow;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssetLinkDTO;
import de.gsv.idm.shared.dto.AssettypeCategoryDTO;
import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.SecurityLevelDTO;

public class InformationDomainTreePresenter implements Presenter {

	public interface InformationDomainTreeDisplay {
		Widget asWidget();

		void setAssettypeTreePanelTitle(String text);

		void setAssettypePanelTitle(String text);

		void setAddAssettypeButtonText(String text);

		HasSelectHandlers getAddAssettypeButtonClick();

		void setRemoveAssettypeButtonText(String text);

		HasSelectHandlers getRemoveAssettypeButtonClick();

		void setRemoveAssettypeButtonEnabled(Boolean enabled);

		void setEditAssettypeButtonText(String text);

		HasSelectHandlers getEditAssettypeButtonClick();

		void setEditAssettypeButtonEnabled(Boolean enabled);

		void setSearchInformationDomainButtonText(String text);

		HasSelectHandlers getSearchInformationDomainButtonClick();

		void setAddAssettypeMenuText(String text);

		HasIcon getAddAssettypeMenuIcon();

		HasSelectionHandlers<Item> getAddAssettypeMenuClick();

		void setRemoveAssettypeMenuText(String text);

		HasIcon getRemoveAssettypeMenuIcon();

		HasSelectionHandlers<Item> getRemoveAssettypeMenuClick();

		void setAssettypeList(List<AssettypeDTO> result);

		void addAssettypeEntry(AssettypeDTO object);

		void updateAssettypeEntry(AssettypeDTO object);

		void removeAssettypeEntry(AssettypeDTO object);

		TreeSelectionModel<GeneralDTO<?>> getAssettypeSelectionModel();

		void setAssetTree(List<AssetDTO> result);

		void addAssetEntry(AssetDTO object, Integer tempId);

		void updateAssetEntry(AssetDTO object);

		void removeAssetEntry(AssetDTO object);

		public HasSelectHandlers getRemoveAssetButtonClick();

		public void setRemoveAssetButtonText(String text);

		public void setRemoveAssetButtonEnabled(boolean enabled);

		public HasSelectHandlers getEditAssetButtonClick();

		public void setEditAssetButtonEnabled(boolean enabled);

		public void setEditAssetButtonText(String text);

		public TreeSelectionModel<GeneralDTO<?>> getAssetTreeSelectionModel();

		public void expandAssettypeCategory(AssettypeCategoryDTO expandItem);

		public void expandAsset(GeneralDTO<?> expandItem);

		public HasSelectHandlers getAddAssetLinkButtonClick();

		public void setAddAssetLinkButtonText(String text);

		public void setAddAssetLinkButtonEnabled(Boolean enabled);

		public void removeAssetLink(AssetLinkDTO object);

		public void addAssetLink(AssetLinkDTO object);

		public TreeStore<GeneralDTO<?>> getAssetTreeStore();

		public TreeStore<GeneralDTO<?>> getAssettypeTreeStore();
	}

	interface ListDriver extends
	        SimpleBeanEditorDriver<ObjectExchange<AssettypeDTO>, InformationDomainTreeView> {
	}

	private ListDriver driver = GWT.create(ListDriver.class);
	private final ObjectExchange<AssettypeDTO> objectExchange;

	private final HandlerManager eventBus;
	private final DBController dbConnector;

	private final InformationDomainTreeDisplay display;
	private Integer domainId;
	private ImageResources imageBundler;

	public InformationDomainTreePresenter(InformationDomainTreeDisplay view, Integer domain_id) {
		this.dbConnector = DBController.getInstance();
		this.eventBus = dbConnector.getEventBus();

		display = view;
		this.domainId = domain_id;
		imageBundler = GWT.create(ImageResources.class);
		objectExchange = new ObjectExchange<AssettypeDTO>();
	}

	@Override
	public IsWidget go() {
		driver.initialize((InformationDomainTreeView) display);
		driver.edit(objectExchange);
		bindView();
		bindBus();
		return display.asWidget();
	}

	private void bindBus() {

		dbConnector.getAssettypeController().getAll(domainId,
		        new AsyncCallback<ArrayList<AssettypeDTO>>() {
			        public void onSuccess(ArrayList<AssettypeDTO> result) {
					        display.setAssettypeList(result);
			        }

			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(Level.SEVERE,
				                "Error while calling AssettypeImpl.getAll");
			        }
		        });

		eventBus.addHandler(CreatedAssettypeEvent.TYPE,
		        new GeneralEventHandler<CreatedAssettypeEvent>() {
			        public void onEvent(CreatedAssettypeEvent event) {
				        AssettypeDTO eventObject = event.getObject();
				        if (eventObject.getDomainId().equals(domainId)) {
					        display.addAssettypeEntry(eventObject);
				        }

			        }
		        });

		eventBus.addHandler(UpdatedAssettypeEvent.TYPE,
		        new GeneralEventHandler<UpdatedAssettypeEvent>() {
			        public void onEvent(UpdatedAssettypeEvent event) {
				        if (event.getObject().getDomainId().equals(domainId)) {
					        display.updateAssettypeEntry(event.getObject());
				        }

			        }
		        });

		eventBus.addHandler(DeletedAssettypeEvent.TYPE,
		        new GeneralEventHandler<DeletedAssettypeEvent>() {
			        public void onEvent(DeletedAssettypeEvent event) {
				        AssettypeDTO eventObject = event.getObject();
				        if (display.getAssettypeTreeStore().findModel(eventObject) != null) {
					        display.removeAssettypeEntry(event.getObject());
				        }

			        }
		        });

		eventBus.addHandler(CreatedAssetEvent.TYPE, new GeneralEventHandler<CreatedAssetEvent>() {
			public void onEvent(CreatedAssetEvent event) {
				if (event.getObject().getDomainId().equals(domainId)) {
					display.addAssetEntry(event.getObject(), event.getTempId());
				}

			}
		});

		eventBus.addHandler(UpdatedAssetEvent.TYPE, new GeneralEventHandler<UpdatedAssetEvent>() {
			public void onEvent(UpdatedAssetEvent event) {
				if (event.getObject().getDomainId().equals(domainId)) {
					display.updateAssetEntry(event.getObject());
				}

			}
		});

		eventBus.addHandler(DeletedAssetEvent.TYPE, new GeneralEventHandler<DeletedAssetEvent>() {
			public void onEvent(DeletedAssetEvent event) {
				AssetDTO eventObject = event.getObject();
				if (display.getAssetTreeStore().findModel(eventObject) != null) {
					display.removeAssetEntry(event.getObject());
				}

			}
		});

		eventBus.addHandler(CreatedAssetLinkEvent.TYPE,
		        new GeneralEventHandler<CreatedAssetLinkEvent>() {
			        public void onEvent(CreatedAssetLinkEvent event) {
				        if (event.getObject().getDomainId().equals(domainId)) {
					        display.addAssetLink(event.getObject());
				        }
			        }
		        });

		eventBus.addHandler(DeletedAssetLinkEvent.TYPE,
		        new GeneralEventHandler<DeletedAssetLinkEvent>() {
			        public void onEvent(DeletedAssetLinkEvent event) {
				        if (event.getObject().getDomainId().equals(domainId)) {
					        display.removeAssetLink(event.getObject());
				        }

			        }
		        });
	}

	private void bindView() {
		new DateField();
		display.setAssettypeTreePanelTitle("Asset-Grundtypen");
		display.setAssettypePanelTitle("Assets im Informationsverbund");
		display.setAddAssettypeButtonText(AssettypeDTO.getStaticClassName() + " hinzufügen");
		display.setAddAssettypeMenuText(AssettypeDTO.getStaticClassName() + " einfügen");
		display.setRemoveAssettypeButtonText(AssettypeDTO.getStaticClassName() + " löschen");
		display.setEditAssettypeButtonText(AssettypeDTO.getStaticClassName() + " bearbeiten");
		display.setSearchInformationDomainButtonText("Informationsverbund durchsuchen");
		display.setRemoveAssetButtonText("Asset löschen");
		display.setEditAssetButtonText("Asset bearbeiten");
		display.getAddAssettypeMenuIcon().setIcon(imageBundler.add());
		display.setRemoveAssettypeMenuText(AssettypeDTO.getStaticClassName() + " löschen");
		display.getRemoveAssettypeMenuIcon().setIcon(imageBundler.remove());
		display.getAddAssettypeButtonClick().addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				new AssettypeEditorWindow(domainId, AssettypeDTO.getStaticClassName()
				        + " erstellen");
			}
		});

		display.getAddAssettypeMenuClick().addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				new AssettypeEditorWindow(domainId, AssettypeDTO.getStaticClassName()
				        + " erstellen");
			}
		});

		display.getAssettypeSelectionModel().addSelectionChangedHandler(
		        new SelectionChangedHandler<GeneralDTO<?>>() {
			        public void onSelectionChanged(SelectionChangedEvent<GeneralDTO<?>> event) {
				        if (display.getAssettypeSelectionModel().getSelectedItem() instanceof AssettypeCategoryDTO) {
					        display.expandAssettypeCategory((AssettypeCategoryDTO) display
					                .getAssettypeSelectionModel().getSelectedItem());
				        } else if (display.getAssettypeSelectionModel().getSelectedItem() instanceof AssettypeDTO) {
					        if (display.getAssettypeSelectionModel().getSelectedItems().size() == 1) {
						        display.setEditAssettypeButtonEnabled(true);
					        } else {
						        display.setEditAssettypeButtonEnabled(false);
					        }

				        }

				        if (display.getAssettypeSelectionModel().getSelection().size() > 0) {
					        display.setRemoveAssettypeButtonEnabled(true);
				        } else {
					        display.setEditAssettypeButtonEnabled(false);
					        display.setRemoveAssettypeButtonEnabled(false);
				        }
				        for (GeneralDTO<?> selectedItem : display.getAssettypeSelectionModel()
				                .getSelectedItems()) {
					        if (selectedItem instanceof AssettypeCategoryDTO) {
						        display.setRemoveAssettypeButtonEnabled(false);
						        display.setEditAssettypeButtonEnabled(false);
					        }
				        }
			        }

		        });

		display.getRemoveAssettypeButtonClick().addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				if (display.getAssettypeSelectionModel().getSelectedItems().size() > 0) {
					for (GeneralDTO<?> toDeleteItem : display.getAssettypeSelectionModel()
					        .getSelectedItems()) {
						if (toDeleteItem instanceof AssettypeDTO) {
							final AssettypeDTO assettype = (AssettypeDTO) toDeleteItem;
							removeAssettypeRpcCall(assettype);
						}

					}

				}
			}

		});

		display.getAssetTreeSelectionModel().addSelectionChangedHandler(
		        new SelectionChangedHandler<GeneralDTO<?>>() {

			        public void onSelectionChanged(SelectionChangedEvent<GeneralDTO<?>> event) {
				        if (display.getAssetTreeSelectionModel().getSelectedItems().size() == 1) {
					        display.setEditAssetButtonEnabled(true);
					        display.setAddAssetLinkButtonEnabled(true);

				        } else {
					        display.setAddAssetLinkButtonEnabled(false);
					        display.setEditAssetButtonEnabled(false);
				        }

				        if (display.getAssetTreeSelectionModel().getSelectedItems().size() > 0) {
					        if (display.getAssetTreeSelectionModel().getSelectedItems().size() == 1
					                && display.getAssetTreeSelectionModel().getSelectedItems()
					                        .get(0) instanceof AssetLinkDTO) {
						        display.setRemoveAssetButtonEnabled(false);
						        display.setEditAssetButtonEnabled(false);
					        } else {
						        display.setRemoveAssetButtonEnabled(true);
					        }

					        display.expandAsset(display.getAssetTreeSelectionModel()
					                .getSelectedItem());
				        } else {
					        display.setRemoveAssetButtonEnabled(false);
				        }
			        }
		        });

		// Server sided deletes every dependent-association. But only
		// root-assoc can be restored
		display.getRemoveAssetButtonClick().addSelectHandler(new SelectHandler() {
			public void onSelect(SelectEvent event) {
				if (display.getAssetTreeSelectionModel().getSelectedItem() != null) {
					for (final GeneralDTO<?> toDeleteItem : display.getAssetTreeSelectionModel()
					        .getSelectedItems()) {
						if (toDeleteItem instanceof AssetDTO) {
							removeAssetRpcCall((AssetDTO) toDeleteItem);
						} else if (toDeleteItem instanceof AssetLinkDTO) {
							removeAssetLinkRpcCall((AssetLinkDTO) toDeleteItem);
						}
					}
				}
			}
		});

		dbConnector.getAssetController().getTreeRoot(domainId,
		        new AsyncCallback<ArrayList<AssetDTO>>() {
			        public void onSuccess(ArrayList<AssetDTO> result) {
				        display.setAssetTree(result);
			        }

			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(Level.SEVERE,
				                "Error while calling AssetImpl.getTreeRoot");
			        }
		        });

		display.getSearchInformationDomainButtonClick().addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				List<AssetDTO> rootAssets = new ArrayList<AssetDTO>();
				List<GeneralDTO<?>> tempList = display.getAssetTreeStore().getRootItems();
				for (GeneralDTO<?> item : tempList) {
					if (item instanceof AssetDTO) {
						rootAssets.add((AssetDTO) item);
					}
				}

				new SearchWindow(new SearchPresenter(new SearchView(), domainId, rootAssets),
				        "Informationsverbund durchsuchen");
			}

		});

		display.getRemoveAssettypeMenuClick().addSelectionHandler(new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				if (display.getAssettypeSelectionModel().getSelectedItem() != null
				        && display.getAssettypeSelectionModel().getSelectedItem() instanceof AssettypeDTO) {
					final AssettypeDTO assettype = (AssettypeDTO) display
					        .getAssettypeSelectionModel().getSelectedItem();
					removeAssettypeRpcCall(assettype);
				}
			}
		});

		display.getEditAssettypeButtonClick().addSelectHandler(new SelectHandler() {

			public void onSelect(SelectEvent event) {
				GeneralDTO<?> selectedItem = display.getAssettypeSelectionModel().getSelectedItem();
				if (selectedItem instanceof AssettypeDTO) {
					AssettypeDTO asset = (AssettypeDTO) selectedItem;
					new AssettypeEditorWindow(asset);
				}
			}
		});

		display.getEditAssetButtonClick().addSelectHandler(new SelectHandler() {

			public void onSelect(SelectEvent event) {
				GeneralDTO<?> asset = display.getAssetTreeSelectionModel().getSelectedItem();
				if (asset instanceof AssetDTO) {
					new AssetEditorWindow((AssetDTO) asset);
				}

			}
		});

		display.setAddAssetLinkButtonText("Asset-Verknüpfungen bearbeiten");
		display.getAddAssetLinkButtonClick().addSelectHandler(new SelectHandler() {

			public void onSelect(SelectEvent event) {
				List<GeneralDTO<?>> selectedAssetmodels = display.getAssetTreeSelectionModel()
				        .getSelectedItems();
				if (selectedAssetmodels.size() == 1
				        && selectedAssetmodels.get(0) instanceof AssetDTO) {
					if (selectedAssetmodels.get(0).getId() > 0) {
						new AssetLinkTreeToListChooser((AssetDTO) selectedAssetmodels.get(0));
					} else {
						MessageBox assetLinkError = new MessageBox(
						        "Asset-Verknüpfungen bearbeiten",
						        "Fehler beim berarbeiten der Asset-Verknüpfungen. Sie haben"
						                + " ein temporäres Asset ausgewält, bitte aktualisieren"
						                + " Sie den Informationsverbund");
						assetLinkError.setIcon(MessageBox.ICONS.error());
						assetLinkError.setWidth(500);
						assetLinkError.show();
					}

				}

				if (selectedAssetmodels.size() == 1
				        && selectedAssetmodels.get(0) instanceof AssetLinkDTO) {
					new AssetLinkTreeToListChooser(((AssetLinkDTO) selectedAssetmodels.get(0))
					        .getParent());
				}

			}

		});
	}

	protected void removeAssetLinkRpcCall(final AssetLinkDTO toDeleteItem) {
		final HashMap<Integer, SecurityLevelBundle> oldSecuriytLevels = new HashMap<Integer, SecurityLevelBundle>();
		for (AssetDTO asset : DBController.getInstance().getAssetController().getMapValues()) {
			oldSecuriytLevels.put(
			        asset.getId(),
			        new SecurityLevelBundle(asset.getCalculatedSecurityAssesment(), asset
			                .getCalculatedAvailability(), asset.getCalculatedConfidentiality(),
			                asset.getCalculatedIntegrity()));
		}

		eventBus.addHandler(DeletedAssetLinkEvent.TYPE,
		        new GeneralEventHandler<DeletedAssetLinkEvent>() {

			        @Override
			        public void onEvent(DeletedAssetLinkEvent event) {
				        if (event.getObject().getId().equals(toDeleteItem.getId())) {
					        checkForChanges(oldSecuriytLevels, null);
					        eventBus.removeHandler(DeletedAssetLinkEvent.TYPE, this);
				        }
			        }
		        });

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

	protected void removeAssetRpcCall(final AssetDTO toDeleteItem) {
		final HashMap<Integer, SecurityLevelBundle> oldSecuriytLevels = new HashMap<Integer, SecurityLevelBundle>();
		for (AssetDTO asset : DBController.getInstance().getAssetController().getMapValues()) {
			oldSecuriytLevels.put(
			        asset.getId(),
			        new SecurityLevelBundle(asset.getCalculatedSecurityAssesment(), asset
			                .getCalculatedAvailability(), asset.getCalculatedConfidentiality(),
			                asset.getCalculatedIntegrity()));
		}

		eventBus.addHandler(DeletedAssetEvent.TYPE, new GeneralEventHandler<DeletedAssetEvent>() {

			@Override
			public void onEvent(DeletedAssetEvent event) {
				if (event.getObject().getId().equals(toDeleteItem.getId())) {
					checkForChanges(oldSecuriytLevels, toDeleteItem);
					eventBus.removeHandler(DeletedAssetEvent.TYPE, this);
				}
			}
		});

		dbConnector.getAssetController().delete((AssetDTO) toDeleteItem,
		        new AsyncCallback<AssetDTO>() {
			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(
				                Level.SEVERE,
				                "Error while deleting " + toDeleteItem.getClassName() + " Id: "
				                        + toDeleteItem.getId());
			        }

			        public void onSuccess(AssetDTO result) {
				        String text = result.getClassName() + " " + result.getName()
				                + " wurde gelöscht.";
				        eventBus.fireEvent(new TransactionBufferEvent(
				                new DeleteTransaction<AssetDTO>(dbConnector.getAssetController(),
				                        result)));
				        Info.display("RPC:", text);
			        }
		        });
	}

	private void removeAssettypeRpcCall(final AssettypeDTO assettype) {
		final HashMap<Integer, SecurityLevelBundle> oldSecuriytLevels = new HashMap<Integer, SecurityLevelBundle>();
		for (AssetDTO asset : DBController.getInstance().getAssetController().getMapValues()) {
			oldSecuriytLevels.put(
			        asset.getId(),
			        new SecurityLevelBundle(asset.getCalculatedSecurityAssesment(), asset
			                .getCalculatedAvailability(), asset.getCalculatedConfidentiality(),
			                asset.getCalculatedIntegrity()));
		}
		eventBus.addHandler(DeletedAssettypeEvent.TYPE,
		        new GeneralEventHandler<DeletedAssettypeEvent>() {

			        @Override
			        public void onEvent(DeletedAssettypeEvent event) {
				        if (event.getObject().getId().equals(assettype.getId())) {
					        checkForChanges(oldSecuriytLevels, null);
					        eventBus.removeHandler(DeletedAssettypeEvent.TYPE, this);
				        }
			        }
		        });
		dbConnector.getAssettypeController().delete(assettype, new AsyncCallback<AssettypeDTO>() {
			public void onFailure(Throwable caught) {
				DBController.getLogger().log(
				        Level.SEVERE,
				        "Error while deleting " + assettype.getClassName() + " Id: "
				                + assettype.getId());
			}

			public void onSuccess(AssettypeDTO result) {
				String text = result.getClassName() + " " + result.getName() + " wurde gelöscht.";
				eventBus.fireEvent(new TransactionBufferEvent(new DeleteTransaction<AssettypeDTO>(
				        dbConnector.getAssettypeController(), result)));
				Info.display("RPC:", text);
			}
		});
	}

	private void checkForChanges(HashMap<Integer, SecurityLevelBundle> oldSecuriytLevelMap,
	        AssetDTO updatedAsset) {
		for (AssetDTO asset : DBController.getInstance().getAssetController().getMapValues()) {
			if (updatedAsset == null || !asset.equals(updatedAsset)) {
				SecurityLevelDTO newSecurityLevel = asset.getCalculatedSecurityAssesment();
				SecurityLevelBundle oldSecurityLevels = oldSecuriytLevelMap.get(asset.getId());
				if (!newSecurityLevel.getId().equals(
				        oldSecurityLevels.getOldSecurityAssesment().getId())) {
					eventBus.fireEvent(new AssetSecurityLevelChangedEvent(asset, oldSecurityLevels));
				}
			}

		}
	}
}
