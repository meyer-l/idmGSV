package de.gsv.idm.client.view.tree;

import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent.DndDragStartHandler;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.TreeDragSource;
import com.sencha.gxt.widget.core.client.tree.Tree;

import de.gsv.idm.shared.dto.AssettypeCategoryDTO;
import de.gsv.idm.shared.dto.GeneralDTO;

public class AssettypeTreeDrag extends TreeDragSource<GeneralDTO<?>> {

	public AssettypeTreeDrag(final Tree<GeneralDTO<?>, ?> tree) {
		super(tree);
		addDragStartHandler(new DndDragStartHandler() {

			@Override
			public void onDragStart(DndDragStartEvent event) {
				GeneralDTO<?> sel = tree.getSelectionModel().getSelectedItem();
				if (sel != null
				        && (sel instanceof AssettypeCategoryDTO)) {
					event.setCancelled(true);
					event.getStatusProxy().setStatus(false);
				}

			}
		});
	}

	@Override
	protected void onDragDrop(DndDropEvent event) {

	}

}
