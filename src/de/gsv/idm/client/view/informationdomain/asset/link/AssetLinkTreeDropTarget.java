/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package de.gsv.idm.client.view.informationdomain.asset.link;

import java.util.List;

import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.TreeDropTarget;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.TreeNode;

import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssetLinkDTO;
import de.gsv.idm.shared.dto.AssettypeCategoryDTO;
import de.gsv.idm.shared.dto.GeneralDTO;

public class AssetLinkTreeDropTarget extends TreeDropTarget<GeneralDTO<?>> {

	public AssetLinkTreeDropTarget(Tree<GeneralDTO<?>, ?> tree) {
		super(tree);
	}

	@Override
	protected void handleAppendDrop(DndDropEvent event, TreeNode<GeneralDTO<?>> item) {
		TreeStore<GeneralDTO<?>> store = getWidget().getStore();
		@SuppressWarnings("unchecked")
		List<GeneralDTO<?>> models = (List<GeneralDTO<?>>) event.getData();
		for (GeneralDTO<?> model : models) {
			if (model instanceof AssetLinkDTO) {
				GeneralDTO<?> storedModel = store.findModel(((AssetLinkDTO) model).getAsset());
				if (storedModel == null) {
					AssetDTO asset = ((AssetLinkDTO) model).getAsset();
					GeneralDTO<?> storedType = store.findModel(asset.getAssettype());
					if (storedType == null) {
						AssettypeCategoryDTO treeCat = getAssetTreeCategory(asset.getAssettype()
						        .getCategory());
						storedType = asset.getAssettype();
						GeneralDTO<?> storeModel = store.findModel(treeCat);
						if (storeModel == null) {
							store.add(treeCat);
						}
						if (treeCat != null) {
							store.add(treeCat, storedType);
						}
					}
					store.add(storedType, asset);
				}
			}
		}
	}

	private AssettypeCategoryDTO getAssetTreeCategory(AssettypeCategoryDTO category) {
		if (category == null) {
			category = new AssettypeCategoryDTO(-1, "Sonstige");
		}
		return category;
	}

}
