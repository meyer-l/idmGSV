package de.gsv.idm.client.view.search;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.TreeSelectionModel;

import de.gsv.idm.client.presenter.search.ChooseSearchStartPresenter.ChooseSearchStartDisplay;
import de.gsv.idm.client.view.properties.AssetDTOProperties;
import de.gsv.idm.shared.dto.AssetDTO;

public class ChooseSearchStartView implements ChooseSearchStartDisplay {

	ContentPanel mainPanel;
	TextButton okButton;
	TextButton abortButton;
	TreeStore<AssetDTO> treeStore;
	Tree<AssetDTO, ?> tree;

	public ChooseSearchStartView() {
		mainPanel = new ContentPanel();
		mainPanel.setHeaderVisible(false);
		AssetDTOProperties properties = GWT.create(AssetDTOProperties.class);
		treeStore = new TreeStore<AssetDTO>(properties.key());
		treeStore.addSortInfo(new StoreSortInfo<AssetDTO>(properties.valueProviderLabel(),
		        SortDir.ASC));
		tree = new Tree<AssetDTO, String>(treeStore, properties.valueProviderLabel()) {
			protected boolean hasChildren(AssetDTO model) {
				return true;
			}
		};
		mainPanel.add(tree);
		abortButton = new TextButton();
		mainPanel.addButton(abortButton);
		okButton = new TextButton();
		mainPanel.addButton(okButton);
	}

	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public HasSelectHandlers getAbortButtonClick() {
		return abortButton;
	}

	@Override
	public void setAbortButtonText(String text) {
		abortButton.setText(text);
	}

	@Override
	public HasSelectHandlers getOkButtonClick() {
		return okButton;
	}

	@Override
	public void setOkButtonText(String text) {
		okButton.setText(text);
	}

	@Override
	public void setTreeRootItems(List<AssetDTO> rootItems) {
		treeStore.clear();
		for (AssetDTO model : rootItems) {
			treeStore.add(model);
			processAssetDTOChildren(model);
		}
		// XXX: Remove when treeStore add-bug while fitering is fixed. Maybe
		// that is fixed with gxt 3.03 ...
		// treeStore.fireEvent(new StoreFilterEvent<GeneralDTO<?>>());
	}

	private void processAssetDTOChildren(AssetDTO model) {
		for (AssetDTO child : model.getChildren()) {
			treeStore.add(model, child);
			processAssetDTOChildren(child);
		}
	}

	@Override
	public void setSelected(AssetDTO asset) {
		if(asset != null) {
			tree.setExpanded(asset, true);
		}		
	}

	@Override
	public TreeSelectionModel<AssetDTO> getSelectionModel() {
		return tree.getSelectionModel();
	}

}
