package de.gsv.idm.server.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.gsv.idm.client.services.AssetService;
import de.gsv.idm.server.general.GeneralDomainServiceImpl;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssetLinkDTO;
import de.gsv.idm.shared.model.Asset;
import de.gsv.idm.shared.model.AssetLink;
import de.gsv.idm.shared.model.Assettype;
import de.gsv.idm.shared.model.User;
import de.gsv.idm.shared.push.event.PushEvent;
import de.gsv.idm.shared.push.event.asset.AssetCreatedPushEvent;
import de.gsv.idm.shared.push.event.asset.AssetDeletedPushEvent;
import de.gsv.idm.shared.push.event.asset.AssetPushEvent;
import de.gsv.idm.shared.push.event.asset.AssetUpdatedPushEvent;
import de.gsv.idm.shared.push.event.asset.link.AssetLinkCreatedPushEvent;
import de.gsv.idm.shared.push.event.assettype.AssettypeUpdatedPushEvent;
import de.novanic.eventservice.client.event.domain.DomainFactory;

@SuppressWarnings("serial")
public class AssetServiceImpl extends GeneralDomainServiceImpl<AssetDTO, Asset> implements
        AssetService {

	private HashMap<Integer, Asset> tempIdToModelMap = new HashMap<Integer, Asset>();

	public static final de.novanic.eventservice.client.event.domain.Domain CONVERSATION_DOMAIN;
	static {
		CONVERSATION_DOMAIN = DomainFactory.getDomain(AssetPushEvent.CONVERSATION_DOMAIN);
	}

	public ArrayList<AssetDTO> getTreeRoot(Integer domain_id, String sessionId) {
		ArrayList<AssetDTO> assetsDTO = new ArrayList<AssetDTO>();
		buildConnection();
		if (User.sessionIdIsValid(sessionId)) {
			List<Asset> assets = Asset.where("domain_id = ? && parent_id = -1", domain_id);
			for (Asset asset : assets) {
				assetsDTO.add(asset.createFullDTO());
			}
		}
		closeConnection();
		return assetsDTO;
	}

	@Override
	public AssetDTO update(AssetDTO toUpdate, String sessionId) {
		buildConnection();
		Asset dbAssetModel = Asset.findById(toUpdate.getId());
		if (dbAssetModel != null && User.sessionIdIsValid(sessionId)) {
			Integer oldAssettypeId = dbAssetModel.getInteger("assettype_id");
			toUpdate = dbAssetModel.updateFromDTO(toUpdate, pushUpdateHandler);
			addEvent(CONVERSATION_DOMAIN, new AssetUpdatedPushEvent(toUpdate));

			if (!oldAssettypeId.equals(toUpdate.getAssettype().getId())) {
				Assettype newAsset = Assettype.findById(toUpdate.getAssettype().getId());
				pushUpdateHandler.addPushEvent(AssettypeServiceImpl.CONVERSATION_DOMAIN, new AssettypeUpdatedPushEvent(
				        newAsset.createSlimDTO()));

				Assettype oldAsset = Assettype.findById(oldAssettypeId);
				pushUpdateHandler.addPushEvent(AssettypeServiceImpl.CONVERSATION_DOMAIN, new AssettypeUpdatedPushEvent(
				        oldAsset.createSlimDTO()));
			}

			closeConnection();
		}
		return toUpdate;
	}

	@Override
	public AssetDTO create(AssetDTO toCreate, String sessionId) {
		buildConnection();
		if (User.sessionIdIsValid(sessionId)) {
			Integer tempId = toCreate.getId();
			Asset newAssetModel = Asset.createIt();
			tempIdToModelMap.put(tempId, newAssetModel);
			if (toCreate.getParent() != null && toCreate.getParent().getId() < -1) {
				Asset realParent = tempIdToModelMap.get(toCreate.getParent().getId());
				if (realParent != null) {
					toCreate.setParent(realParent.createFullDTO());
				}
			}

			toCreate = newAssetModel.updateFromDTO(toCreate, pushUpdateHandler);

			addEvent(CONVERSATION_DOMAIN, new AssetCreatedPushEvent(newAssetModel.createSlimDTO(), tempId));
			if (toCreate.getParent() != null) {
				Asset parentModel = Asset.findById(toCreate.getParent().getId());
				pushUpdateHandler.addPushEvent(CONVERSATION_DOMAIN,
				        new AssetUpdatedPushEvent(parentModel.createSlimDTO()));
			}
			Assettype asset = Assettype.findById(toCreate.getAssettype().getId());
			pushUpdateHandler.addPushEvent(AssettypeServiceImpl.CONVERSATION_DOMAIN,
			        new AssettypeUpdatedPushEvent(asset.createSlimDTO()));

			for (AssetLinkDTO link : toCreate.getLinkedAssets()) {
				AssetLink newLink1 = AssetLink
				        .createIt("parent_id", newAssetModel.getId(), "linked_asset_id", link
				                .getAsset().getId(), "domain_id", link.getDomainId());
				addEvent(AssetLinkServiceImpl.CONVERSATION_DOMAIN, new AssetLinkCreatedPushEvent(
				        newLink1.createFullDTO()));
				AssetLink newLink2 = AssetLink.createIt("parent_id", link.getAsset().getId(),
				        "linked_asset_id", newAssetModel.getId(), "domain_id", link.getDomainId());
				addEvent(AssetLinkServiceImpl.CONVERSATION_DOMAIN, new AssetLinkCreatedPushEvent(
				        newLink2.createFullDTO()));
			}
		}
		closeConnection();
		return toCreate;
	}

	@Override
	public AssetDTO delete(AssetDTO toDelete, String sessionId) {
		buildConnection();

		Asset asset = Asset.findById(toDelete.getId());
		if (asset != null && User.sessionIdIsValid(sessionId)) {
			toDelete = asset.createSlimDTO();
			asset.completeDelete(pushUpdateHandler);
		}
		closeConnection();
		return toDelete;
	}

	@Override
	protected Class<Asset> getDBClass() {
		return Asset.class;
	}

	@Override
	protected List<Asset> findAll() {
		return Asset.findAll();
	}

	@Override
	protected Asset findById(Integer id) {
		return Asset.findById(id);
	}

	@Override
	protected Asset createObject() {
		return Asset.createIt();
	}

	@Override
	protected de.novanic.eventservice.client.event.domain.Domain getConversationDomain() {
		return CONVERSATION_DOMAIN;
	}

	@Override
	protected PushEvent<?, ?> getUpdatedPushEvent(AssetDTO updated) {
		return new AssetUpdatedPushEvent(updated);
	}

	// Inheritetd Create-Method is overwritten,
	// because of asset temp-ids, this method ist not used
	@Override
	protected PushEvent<?, ?> getCreatedPushEvent(AssetDTO created) {
		return null;
	}

	@Override
	protected PushEvent<?, ?> getDeletedPushEvent(AssetDTO deleted) {
		return new AssetDeletedPushEvent(deleted);
	}

}
