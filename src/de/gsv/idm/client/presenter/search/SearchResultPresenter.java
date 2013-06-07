package de.gsv.idm.client.presenter.search;

import java.util.List;

import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;

import de.gsv.idm.client.presenter.general.GeneralGridPresenter;
import de.gsv.idm.client.presenter.informationdomain.asset.AssetEditorPresenter;
import de.gsv.idm.client.view.informationdomain.asset.AssetEditor;
import de.gsv.idm.client.view.widgets.window.EditorWindow;
import de.gsv.idm.shared.dto.AssetDTO;

public class SearchResultPresenter extends GeneralGridPresenter<SearchResult> {

	public interface SearchResultDisplay extends GeneralGridDisplay<SearchResult> {
		void setOkButtonText(String text);

		HasSelectHandlers getOkButtonClick();
	}

	private final SearchResultDisplay display;
	private List<SearchResult> searchResult;
	private Integer domainId;

	public SearchResultPresenter(SearchResultDisplay view, List<SearchResult> result,
	        Integer domainId) {
		super(view);
		this.display = view;
		this.searchResult = result;
		this.domainId = domainId;
	}

	@Override
	protected void bindBus() {

	}

	@Override
	protected void bindView() {
		display.setStore(searchResult);
		display.setOkButtonText("Ok");
		display.addDoubleClickHandler(new CellDoubleClickHandler() {

			@Override
			public void onCellClick(CellDoubleClickEvent event) {
				SearchResult selectedSearchResult = display.getGridSelectionModel()
				        .getSelectedItem();
				if (selectedSearchResult != null && selectedSearchResult.getAsset() != null) {
					new EditorWindow<AssetDTO>(
					        new AssetEditorPresenter(new AssetEditor(), domainId),
					        selectedSearchResult.getAsset(),"Asset editieren");
				}

			}
		});
	}

	public HasSelectHandlers getOkButtonClick() {
		return display.getOkButtonClick();
	}

}
