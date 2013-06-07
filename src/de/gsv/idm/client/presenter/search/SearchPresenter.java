package de.gsv.idm.client.presenter.search;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.presenter.Presenter;
import de.gsv.idm.client.presenter.informationdomain.assettype.AssettypeGridToGridChooser;
import de.gsv.idm.client.view.gsk.widgets.MeasureGridToGridChooser;
import de.gsv.idm.client.view.search.ChooseSearchStartView;
import de.gsv.idm.client.view.search.ChooseSearchStartWindow;
import de.gsv.idm.client.view.search.SearchResultView;
import de.gsv.idm.client.view.search.SearchResultWindow;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.dto.MeasureDTO;
import de.gsv.idm.shared.dto.SecurityzoneDTO;

public class SearchPresenter implements Presenter {

	public interface SearchDisplay extends IsWidget {
		HasSelectHandlers getOkButtonClick();

		void setAbortButtonText(String text);

		HasSelectHandlers getSearchButtonClick();

		void setSearchButtonText(String text);

		HasSelectHandlers getAddAssettypeButtonClick();

		ListStore<AssettypeDTO> getAssettypeStore();

		void setSecurityzoneStore(List<SecurityzoneDTO> list);

		HasSelectHandlers getAddMeasuresButtonClick();

		ListStore<MeasureDTO> getMeasureStore();

		void setAddAssettypeButtonText(String string);

		void setAddMeasureButtonText(String string);

		void setChooseStartAssetButtonText(String string);

		HasSelectHandlers getChooseStartAssetButtonClick();

		void setChooseStartAssetFieldValue(String string);

		SecurityzoneDTO getSelectedSecurityzone();

		void addSecurityzoneComboBoxSelectionHandler(
		        SelectionHandler<SecurityzoneDTO> selectionHandler);

		SecurityzoneDTO getPreviousSecurityzone();
	}

	private final SearchDisplay display;
	private Integer domainId;
	private final DBController dbConnector;
	private AssetDTO startPosition;
	private List<AssetDTO> treeRootItems;

	public SearchPresenter(SearchDisplay display, Integer domainId, List<AssetDTO> rootItems) {
		this.display = display;
		this.domainId = domainId;
		dbConnector = DBController.getInstance();
		treeRootItems = rootItems;
		this.startPosition = null;
	}

	public SearchPresenter(SearchDisplay display, Integer domainId) {
		this.display = display;
		this.domainId = domainId;
		dbConnector = DBController.getInstance();
		dbConnector.getAssetController().getTreeRoot(domainId,
		        new AsyncCallback<ArrayList<AssetDTO>>() {

			        @Override
			        public void onFailure(Throwable caught) {
			        	 DBController.getLogger().log(Level.SEVERE,
					                "Error while calling AssetImpl.getTreeRoot");
			        }

			        @Override
			        public void onSuccess(ArrayList<AssetDTO> result) {
				        treeRootItems = result;
			        }
		        });

		this.startPosition = null;
	}

	@Override
	public IsWidget go() {
		bindView();
		return display.asWidget();
	}

	private void bindView() {
		setStartPositionTextField();
		display.setSearchButtonText("Suchen");
		display.setAbortButtonText("Abbrechen");
		display.setAddAssettypeButtonText("Asset-Grundtyp hinzufügen");
		display.setAddMeasureButtonText("Maßnahme hinzufügen");
		display.setChooseStartAssetButtonText("Startknoten festlegen");
		final SearchPresenter thisPresenter = this;
		display.getChooseStartAssetButtonClick().addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				new ChooseSearchStartWindow(new ChooseSearchStartPresenter(
				        new ChooseSearchStartView(), treeRootItems, startPosition, thisPresenter));
			}
		});

		display.getAddAssettypeButtonClick().addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				new AssettypeGridToGridChooser(display.getAssettypeStore(), domainId);
			}
		});

		dbConnector.getSecurityzoneController().getAll(
		        new AsyncCallback<ArrayList<SecurityzoneDTO>>() {

			        @Override
			        public void onSuccess(ArrayList<SecurityzoneDTO> result) {
				        display.setSecurityzoneStore(result);
			        }

			        @Override
			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(Level.SEVERE,
				                "Error while fetching Securityzones");
			        }
		        });

		display.getAddMeasuresButtonClick().addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				new MeasureGridToGridChooser(display.getMeasureStore());
			}
		});

		display.getSearchButtonClick().addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				List<MeasureDTO> measures = new ArrayList<MeasureDTO>(display.getMeasureStore()
				        .getAll());
				SecurityzoneDTO securityzone = display.getSelectedSecurityzone();
				if (securityzone != null) {
					measures.removeAll(securityzone.getAllMeasures());
					measures.addAll(securityzone.getAllMeasures());
				}
				List<SearchResult> result = searchForAssets(display.getAssettypeStore().getAll(),
				        measures);
				new SearchResultWindow(new SearchResultPresenter(new SearchResultView(), result,
				        domainId));
			}

		});

		display.addSecurityzoneComboBoxSelectionHandler(new SelectionHandler<SecurityzoneDTO>() {

			@Override
			public void onSelection(SelectionEvent<SecurityzoneDTO> event) {
				ListStore<MeasureDTO> store = display.getMeasureStore();
				if (display.getPreviousSecurityzone() != null) {
					for (MeasureDTO measure : display.getPreviousSecurityzone().getAllMeasures()) {
						store.remove(measure);
					}
				}
				store.addAll(event.getSelectedItem().getAllMeasures());
			}
		});
	}

	private List<SearchResult> searchForAssets(List<AssettypeDTO> assettypes,
	        List<MeasureDTO> measures) {
		List<SearchResult> result = new ArrayList<SearchResult>();
		List<AssetDTO> startAssets = new ArrayList<AssetDTO>();
		if (startPosition != null) {
			startAssets.add(startPosition);
		} else {
			startAssets.addAll(treeRootItems);
		}
		result.addAll(processAssetListForSearch(startAssets, assettypes, measures));

		return result;
	}

	private List<SearchResult> processAssetListForSearch(List<AssetDTO> assets,
	        List<AssettypeDTO> assettypes, List<MeasureDTO> measures) {
		List<SearchResult> result = new ArrayList<SearchResult>();
		for (AssetDTO asset : assets) {
			if (assettypes.size() == 0 || assettypes.contains(asset.getAssettype())) {
				SearchResult tempSearchResult = compareAsset(asset, measures);
				if (tempSearchResult != null) {
					result.add(tempSearchResult);
				}
			}
			result.addAll(processAssetListForSearch(asset.getChildren(), assettypes, measures));
		}
		return result;
	}

	private SearchResult compareAsset(AssetDTO asset, List<MeasureDTO> measures) {
		List<MeasureDTO> implementedSubset = new ArrayList<MeasureDTO>(
		        asset.getAllImplementedMeasures());
		implementedSubset.retainAll(measures);
		if (implementedSubset.size() > 0 || measures.size() == 0) {
			return new SearchResult(asset, implementedSubset, measures);
		} else {
			return null;
		}

	}

	public HasSelectHandlers getSearchButtonClick() {
		return display.getSearchButtonClick();
	}

	public HasSelectHandlers getOkButtonClick() {
		return display.getOkButtonClick();
	}

	public void setStartPosition(AssetDTO asset) {
		startPosition = asset;
		setStartPositionTextField();
	}

	private void setStartPositionTextField() {
		if (startPosition != null) {
			display.setChooseStartAssetFieldValue(startPosition.getName());
		} else {
			display.setChooseStartAssetFieldValue("Wurzelknoten");
		}
	}

}
