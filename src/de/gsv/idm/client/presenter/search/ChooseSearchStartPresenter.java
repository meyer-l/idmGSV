package de.gsv.idm.client.presenter.search;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.tree.TreeSelectionModel;

import de.gsv.idm.client.presenter.Presenter;
import de.gsv.idm.shared.dto.AssetDTO;

public class ChooseSearchStartPresenter implements Presenter {

	public interface ChooseSearchStartDisplay extends IsWidget {
		HasSelectHandlers getAbortButtonClick();

		void setAbortButtonText(String text);

		HasSelectHandlers getOkButtonClick();

		void setOkButtonText(String text);
		
		void setTreeRootItems(List<AssetDTO> list);
		
		void setSelected(AssetDTO asset);
		
		TreeSelectionModel<AssetDTO> getSelectionModel();
		
	}

	private final ChooseSearchStartDisplay display;
	private AssetDTO startPosition = null;
	private final SearchPresenter searchPresenter;

	public ChooseSearchStartPresenter(ChooseSearchStartDisplay dispaly,
	        List<AssetDTO> treeRootItems, AssetDTO startPosition, SearchPresenter searchPresenter) {
		this.display = dispaly;
		this.startPosition = startPosition;
		display.setTreeRootItems(treeRootItems);
		this.searchPresenter = searchPresenter;
	}

	@Override
	public IsWidget go() {
		bindView();
		return display.asWidget();
	}

	private void bindView() {
	   display.setAbortButtonText("Abbrechen");
	   display.setOkButtonText("Ok");
	   display.setSelected(startPosition);
	   display.getOkButtonClick().addSelectHandler(new SelectHandler() {
		
		@Override
		public void onSelect(SelectEvent event) {
			AssetDTO selected = display.getSelectionModel().getSelectedItem();
			searchPresenter.setStartPosition(selected);
		}
	});
    }

	public HasSelectHandlers getOkButtonClick() {
		return display.getOkButtonClick();
	}

	public HasSelectHandlers getAbortButtonClick() {
		return display.getAbortButtonClick();
	}

}
