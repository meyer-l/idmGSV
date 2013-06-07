package de.gsv.idm.client.view.tree;

import java.util.List;

import com.sencha.gxt.data.shared.TreeStore.TreeNode;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent.DndDragStartHandler;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.TreeDragSource;
import com.sencha.gxt.widget.core.client.tree.Tree;

import de.gsv.idm.shared.dto.AssetLinkDTO;
import de.gsv.idm.shared.dto.GeneralDTO;

public class AssetDragSource extends TreeDragSource<GeneralDTO<?>> {

	public AssetDragSource(final Tree<GeneralDTO<?>, String> tree) {
		super(tree);
		addDragStartHandler(new DndDragStartHandler() {

			@Override
			public void onDragStart(DndDragStartEvent event) {
				GeneralDTO<?> sel = tree.getSelectionModel().getSelectedItem();
				if (sel != null
				        && (sel instanceof AssetLinkDTO)) {
					event.setCancelled(true);
					event.getStatusProxy().setStatus(false);
				}

			}
		});
	}

	@Override
	protected void onDragDrop(DndDropEvent event) {
		if (!event.getDragEndEvent().getNativeEvent().getCtrlKey()) {
			@SuppressWarnings("unchecked")
			List<TreeNode<GeneralDTO<?>>> sel = (List<TreeNode<GeneralDTO<?>>>) event.getData();
			for (TreeNode<GeneralDTO<?>> s : sel) {
				getWidget().getStore().remove(s.getData());
			}
		}
	}

}
