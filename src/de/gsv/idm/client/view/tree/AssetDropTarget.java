/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package de.gsv.idm.client.view.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.TreeDropTarget;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.TreeNode;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.service.AssetRpcController;
import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.TransactionBufferEvent;
import de.gsv.idm.client.event.db.asset.CreatedAssetEvent;
import de.gsv.idm.client.event.db.asset.UpdatedAssetEvent;
import de.gsv.idm.client.event.db.securitylevelchange.AssetSecurityLevelChangedEvent;
import de.gsv.idm.client.presenter.securitylevelchange.data.SecurityLevelBundle;
import de.gsv.idm.client.transaction.CreateTransaction;
import de.gsv.idm.client.transaction.UpdateTransaction;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.SecurityLevelDTO;

public class AssetDropTarget extends TreeDropTarget<GeneralDTO<?>> {

	private final HandlerManager eventBus;
	private final AssetRpcController assetService;

	public AssetDropTarget(Tree<GeneralDTO<?>, ?> tree, HandlerManager eventBus,
	        AssetRpcController assetModelService) {
		super(tree);
		this.eventBus = eventBus;
		this.assetService = assetModelService;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void appendModel(GeneralDTO<?> p, List<?> items, int index) {
		AssetDTO parent = null;
		if (p != null && p instanceof AssetDTO) {
			parent = (AssetDTO) p;
		}
		if (items.size() == 0)
			return;
		if (items.get(0) instanceof TreeStore.TreeNode) {
			List<TreeStore.TreeNode<GeneralDTO<?>>> nodes = (List<TreeStore.TreeNode<GeneralDTO<?>>>) items;

			processAppendTree(parent, nodes);
			if (parent == null) {
				getWidget().getStore().addSubTree(index, nodes);
			} else {
				getWidget().getStore().addSubTree(parent, index, nodes);
			}
		} else {
			List<GeneralDTO<?>> models = (List<GeneralDTO<?>>) items;
			processAppendList(parent, models);
			if (parent == null) {
				getWidget().getStore().insert(index, models);
			} else {
				getWidget().getStore().insert(parent, index, models);
			}
		}
	}

	private void processAppendTree(AssetDTO parent, List<TreeStore.TreeNode<GeneralDTO<?>>> nodes) {
		for (TreeStore.TreeNode<GeneralDTO<?>> node : nodes) {
			if (node.getData() instanceof AssetDTO) {
				callRPC(parent, (AssetDTO) node.getData());
			}
			processChildrenForAppendTree(node);
		}
	}

	private void processChildrenForAppendTree(TreeStore.TreeNode<GeneralDTO<?>> parent) {
		for (TreeStore.TreeNode<GeneralDTO<?>> child : parent.getChildren()) {

			if (child.getData().getId() < -1) {
				if (parent.getData() instanceof AssetDTO && child.getData() instanceof AssetDTO) {
					callRPC((AssetDTO) parent.getData(), (AssetDTO) child.getData());
				}
				processChildrenForAppendTree(child);
			}
		}
	}

	private void processAppendList(AssetDTO parent, List<GeneralDTO<?>> models) {
		for (GeneralDTO<?> model : models) {
			if (model instanceof AssetDTO) {
				callRPC(parent, (AssetDTO) model);
			}
		}
	}

	public void callRPC(AssetDTO parent, final AssetDTO model) {
		final AssetDTO modelClone = model.clone();
		final HashMap<Integer, SecurityLevelBundle> oldSecuriytLevelMap = new HashMap<Integer, SecurityLevelBundle>();
		for (AssetDTO asset : DBController.getInstance().getAssetController().getMapValues()) {
			oldSecuriytLevelMap.put(
			        asset.getId(),
			        new SecurityLevelBundle(asset.getCalculatedSecurityAssesment(), asset
			                .getCalculatedAvailability(), asset.getCalculatedConfidentiality(),
			                asset.getCalculatedIntegrity()));
		}
		if (model != null) {
			if (parent != null) {
				model.setParent(parent);
			} else {
				model.setParent(null);
			}
			if (model.getId() > 0) {
				eventBus.addHandler(UpdatedAssetEvent.TYPE,
				        new GeneralEventHandler<UpdatedAssetEvent>() {

					        @Override
					        public void onEvent(UpdatedAssetEvent event) {
						        if (event.getObject().getId().equals(model.getId())) {
							        checkForChanges(oldSecuriytLevelMap, model);
							        eventBus.removeHandler(UpdatedAssetEvent.TYPE, this);
						        }
					        }
				        });
				assetService.update(model, new AsyncCallback<AssetDTO>() {
					public void onSuccess(AssetDTO result) {
						String text = result.getClassName() + " " + result.getName()
						        + " wurde aktualisiert. (ID: " + result.getId() + ")";
						eventBus.fireEvent(new TransactionBufferEvent(
						        new UpdateTransaction<AssetDTO>(assetService, modelClone, result,
						                text)));
						Info.display("RPC:", text);
					}

					public void onFailure(Throwable caught) {
						DBController.getLogger().log(Level.SEVERE,
						        "Error while calling AssetImpl.update, Id: " + model.getId());
					}
				});
			} else {
				eventBus.addHandler(CreatedAssetEvent.TYPE,
				        new GeneralEventHandler<CreatedAssetEvent>() {

					        @Override
					        public void onEvent(CreatedAssetEvent event) {
						        if (event.getTempId().equals(model.getId())) {
							        checkForChanges(oldSecuriytLevelMap, event.getObject());
							        eventBus.removeHandler(CreatedAssetEvent.TYPE, this);
						        }
					        }
				        });
				assetService.create(model, new AsyncCallback<AssetDTO>() {
					public void onSuccess(AssetDTO result) {
						String text = result.getClassName() + " " + result.getName()
						        + " wurde erstellt. (ID: " + result.getId() + ")";
						eventBus.fireEvent(new TransactionBufferEvent(
						        new CreateTransaction<AssetDTO>(assetService, result)));
						Info.display("RPC:", text);
					}

					public void onFailure(Throwable caught) {
						DBController.getLogger().log(Level.SEVERE,
						        "Error while calling AssetImpl.create");
					}
				});
			}
		}
	}

	protected void checkForChanges(HashMap<Integer, SecurityLevelBundle> oldSecuriytLevelMap,
	        AssetDTO model) {
		for (AssetDTO asset : DBController.getInstance().getAssetController().getMapValues()) {
			if (model == null || !asset.equals(model)) {
				SecurityLevelDTO newSecurityLevel = asset.getCalculatedSecurityAssesment();
				SecurityLevelBundle oldSecurityLevels = oldSecuriytLevelMap.get(asset.getId());
				if (newSecurityLevel != null
				        && newSecurityLevel.getId() != null
				        && oldSecurityLevels != null
				        && oldSecurityLevels.getOldSecurityAssesment() != null
				        && oldSecurityLevels.getOldSecurityAssesment().getId() != null
				        && !newSecurityLevel.getId().equals(
				                oldSecurityLevels.getOldSecurityAssesment().getId())) {
					eventBus.fireEvent(new AssetSecurityLevelChangedEvent(asset, oldSecurityLevels));
				}
			}
		}
	}

	// This is GXT3 Code, I only added RPC-DataHandling
	protected void handleAppendDrop(DndDropEvent event, TreeNode<GeneralDTO<?>> item) {
		List<?> models = (List<?>) event.getData();
		if (models.size() > 0 && models.get(0) instanceof AssettypeDTO) {
			@SuppressWarnings("unchecked")
			List<AssettypeDTO> assets = (List<AssettypeDTO>) models;
			List<AssetDTO> newModels = new ArrayList<AssetDTO>();
			for (AssettypeDTO asset : assets) {
				Integer tempId = createTempId();
				newModels.add(new AssetDTO(tempId, asset.getDomainId(), asset));
			}
			models = newModels;

		} else if (models.get(0) instanceof TreeStore.TreeNode
		        && ((TreeStore.TreeNode<?>) models.get(0)).getData() instanceof AssettypeDTO) {
			@SuppressWarnings("unchecked")
			List<TreeStore.TreeNode<AssettypeDTO>> nodes = (List<TreeStore.TreeNode<AssettypeDTO>>) models;
			List<AssetDTO> newModels = new ArrayList<AssetDTO>();
			for (TreeStore.TreeNode<AssettypeDTO> node : nodes) {
				Integer tempId = createTempId();
				newModels.add(new AssetDTO(tempId, node.getData().getDomainId(), node.getData()));
			}
			models = newModels;
		} else if (event.getDragEndEvent().getNativeEvent().getCtrlKey()
		        && models.get(0) instanceof TreeStore.TreeNode) {
			@SuppressWarnings("unchecked")
			List<TreeStore.TreeNode<GeneralDTO<?>>> nodes = (List<TreeStore.TreeNode<GeneralDTO<?>>>) models;
			for (TreeStore.TreeNode<GeneralDTO<?>> node : nodes) {
				if (node.getData() instanceof AssetDTO) {
					AssetDTO model = (AssetDTO) node.getData();
					model.setId(createTempId());
					processChildrenForCopy(node);
				}

			}
		}
		if (models.size() > 0) {
			GeneralDTO<?> p = null;
			if (item != null) {
				p = item.getModel();
				appendModel(p, models, getWidget().getStore().getChildCount(item.getModel()));
			} else {
				appendModel(p, models, getWidget().getStore().getRootItems().size());
			}

		}
	}

	private void processChildrenForCopy(TreeStore.TreeNode<GeneralDTO<?>> parent) {
		for (TreeStore.TreeNode<GeneralDTO<?>> child : parent.getChildren()) {
			if (child.getData() instanceof AssetDTO) {
				((AssetDTO) child.getData()).setId(createTempId());
				processChildrenForCopy(child);
			}
		}
	}

	private Integer createTempId() {
		Integer tempId = Random.nextInt();
		if (tempId > 1)
			tempId *= -1;
		// -1 is reserved for no Parent
		tempId -= 2;
		return tempId;
	}

}
