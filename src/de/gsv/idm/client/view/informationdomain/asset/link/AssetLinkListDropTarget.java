package de.gsv.idm.client.view.informationdomain.asset.link;

import java.util.ArrayList;
import java.util.List;

import com.sencha.gxt.data.shared.TreeStore.TreeNode;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.ListViewDropTarget;
import com.sencha.gxt.widget.core.client.ListView;

import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssetLinkDTO;
import de.gsv.idm.shared.dto.GeneralDTO;

//TODO: Added von Links durch assetcategory wenn bereits ein teil verlinkt ist
public class AssetLinkListDropTarget extends ListViewDropTarget<AssetLinkDTO> {

	private AssetDTO asset;

	public AssetLinkListDropTarget(ListView<AssetLinkDTO, ?> listView, AssetDTO asset) {
		super(listView);
		this.asset = asset;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onDragDrop(DndDropEvent event) {
		List<AssetLinkDTO> addData = new ArrayList<AssetLinkDTO>();
		List<TreeNode<GeneralDTO<?>>> data = (List<TreeNode<GeneralDTO<?>>>) event.getData();

		for (TreeNode<GeneralDTO<?>> node : data) {
			if (node.getData() instanceof AssetDTO) {
				AssetLinkDTO nodeData = new AssetLinkDTO(asset.getDomainId(), asset,
				        (AssetDTO) node.getData());
				if (!addData.contains(nodeData)) {
					addData.add(nodeData);
				}
			}
		}

		@SuppressWarnings("rawtypes")
		List<AssetLinkDTO> models = (List) prepareDropData(addData, true);
		if (models.size() > 0) {
			if (feedback == Feedback.APPEND) {
				listView.getStore().addAll(models);
			} else {
				listView.getStore().addAll(insertIndex, models);
			}
		}
		insertIndex = -1;
		activeItem = null;
	}

}
