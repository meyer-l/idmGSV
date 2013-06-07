package de.gsv.idm.client.presenter.search;

import java.util.List;

import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.MeasureDTO;

public class SearchResult extends GeneralDTO<SearchResult> {

	List<MeasureDTO> implementedMeasures;
	List<MeasureDTO> completeList;
	AssetDTO asset;

	public SearchResult(AssetDTO asset, List<MeasureDTO> implementedMeasures,
	        List<MeasureDTO> completeList) {
		this.implementedMeasures = implementedMeasures;
		this.completeList = completeList;
		this.asset = asset;
	}

	public List<MeasureDTO> getImplementedMeasures() {
		return implementedMeasures;
	}
	
	public Integer getImplementedMeasuresSize() {
		return implementedMeasures.size();
	}

	public String getImplementedMeasuresLabel() {
		return implementedMeasures.size() + "/" + completeList.size() + " Maßnahmen umgesetzt";
	}

	public void setImplementedMeasures(List<MeasureDTO> numberOfImplementedMeasures) {
		this.implementedMeasures = numberOfImplementedMeasures;
	}

	public AssetDTO getAsset() {
		return asset;
	}

	public void setAsset(AssetDTO asset) {
		this.asset = asset;
	}

	public String getName() {
		return asset.getName();
	}

	public Integer getId() {
		return asset.getId();
	}

	@Override
	public String getClassName() {
		return "Suchergebnis";
	}

	@Override
	public Boolean isSlim() {
		return false;
	}

	@Override
	public SearchResult update(SearchResult toUpdate) {
		implementedMeasures = toUpdate.implementedMeasures;
		asset = toUpdate.asset;

		return toUpdate;
	}

}
