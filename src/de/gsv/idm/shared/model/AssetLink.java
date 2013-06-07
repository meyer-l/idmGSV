package de.gsv.idm.shared.model;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;

import de.gsv.idm.server.push.PushUpdateHandler;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssetLinkDTO;
import de.gsv.idm.shared.dto.GeneralDTO;

@BelongsTo(parent = Asset.class, foreignKeyName = "parent_id")
public class AssetLink extends Model implements HasDTOMethods<AssetLinkDTO> {

	@Override
	public AssetLinkDTO createFullDTO() {
		return createSlimDTO();
	}

	@Override
	public AssetLinkDTO createSlimDTO() {
		AssetDTO parentDTO = null;
		Asset parentDB = Asset.findById(getInteger("parent_id"));
		if (parentDB != null) {
			parentDTO = parentDB.createSlimDTO();
		}

		AssetDTO assetDTO = null;
		Asset assetmodelDB = Asset.findById(getInteger("linked_asset_id"));
		if (assetmodelDB != null) {
			assetDTO = assetmodelDB.createSlimDTO();
		}

		return new AssetLinkDTO(getInteger("id"), getInteger("domain_id"), parentDTO,
		        assetDTO);
	}

	@Override
	public AssetLinkDTO updateFromDTO(AssetLinkDTO toUpdate,
			PushUpdateHandler serverImpl) {
		set("domain_id", toUpdate.getDomainId());
		if (toUpdate.getAsset() != null) {
			set("linked_asset_id", toUpdate.getAsset().getId());
		}
		if (toUpdate.getParent() != null) {
			set("parent_id", toUpdate.getParent().getId());
		}
		saveIt();
		
		return createFullDTO();
	}

	@Override
	public void completeDelete(PushUpdateHandler server) {
		deleteCascadeShallow();
	}

	@Override
    public List<GeneralDTO<?>> getAffectedObjects() {
		List<GeneralDTO<?>> result = new ArrayList<GeneralDTO<?>>();
		Asset parent = Asset.findById(getInteger("parent_id"));
		if(parent != null) {
			result.add(parent.createFullDTO());
		}
		Asset asset = Asset.findById(getInteger("linked_asset_id"));
		if(asset != null) {
			result.add(asset.createFullDTO());
		}
		result.add(createFullDTO());
		
		return result;
    }

}
