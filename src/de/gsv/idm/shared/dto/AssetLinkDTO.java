package de.gsv.idm.shared.dto;

import java.io.Serializable;
import java.util.HashMap;

@SuppressWarnings("serial")
public class AssetLinkDTO extends GeneralDTO<AssetLinkDTO> implements Serializable {

	private Integer id;
	private Integer domainId;
	private AssetDTO parent;
	private AssetDTO asset;

	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (other instanceof AssetLinkDTO)
			return (getId() != null && getId() == ((HasId) other).getId())
			        || (getParent().equals(((AssetLinkDTO) other).getParent()) && (getAsset()
			                .equals(((AssetLinkDTO) other).getAsset())));
		if (other instanceof HasId)
			return (getId() != null && getId() == ((HasId) other).getId());
		return false;
	}

	public AssetLinkDTO() {

	}

	public AssetLinkDTO(Integer domainId) {
		this.domainId = domainId;
	}

	public AssetLinkDTO(Integer domainId, AssetDTO parent, AssetDTO assetmodel) {
		this.domainId = domainId;
		this.asset = assetmodel;
		this.parent = parent;
	}

	public AssetLinkDTO(Integer id, Integer domainId, AssetDTO parent, AssetDTO assetmodel) {
		this.id = id;
		this.domainId = domainId;
		this.parent = parent;
		this.asset = assetmodel;
	}

	@Override
	public String getName() {
		if (asset != null) {
			return "Verbindung mit " + asset.getName() + " ("
			        + asset.getCalculatedSecurityAssesment().getName() + ")";
		}
		return "";
	}

	public SecurityLevelDTO getSecurityAssesment(HashMap<AssetDTO, SecurityLevelDTO> visitedNodes) {
		if (visitedNodes.containsKey(getAsset())) {
			return visitedNodes.get(getAsset());
		} else {
			if (asset != null && asset.getAssettype() != null && asset.getAssettype().isPropagateSecurityAssesment()) {
				return asset.getCalculatedSecurityAssesment(visitedNodes);
			} else {
				return SecurityLevelDTO.getDefaultSecurityLevel();
			}
		}

	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public String getTreeKey() {
		return "assetLink" + id;
	}

	public Integer getDomainId() {
		return domainId;
	}

	public void setDomainId(Integer domainId) {
		this.domainId = domainId;
	}

	public AssetDTO getParent() {
		return parent;
	}

	public void setParent(AssetDTO parent) {
		this.parent = parent;
	}

	public AssetDTO getAsset() {
		return asset;
	}

	public void setAsset(AssetDTO asset) {
		this.asset = asset;
	}

	public String getAssetName() {
		if (asset != null) {
			return asset.getName();
		} else {
			return "";
		}

	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getClassName() {
		return "Asset-Verknüpfung";
	}

	@Override
	public Boolean isSlim() {
		return false;
	}

	public AssetLinkDTO clone() {
		AssetLinkDTO clone = new AssetLinkDTO();
		if (id != null) {
			clone.id = id;

		}
		clone.parent = parent;
		clone.domainId = domainId;
		clone.asset = asset;

		return clone;
	}

	@Override
	public AssetLinkDTO update(AssetLinkDTO toUpdate) {
		domainId = toUpdate.domainId;
		parent = toUpdate.parent;
		asset = toUpdate.asset;

		return this;
	}

}
