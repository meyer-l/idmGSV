package de.gsv.idm.server.service;

import java.util.ArrayList;
import java.util.List;

import de.gsv.idm.client.services.AssetLinkService;
import de.gsv.idm.server.general.GeneralDomainServiceImpl;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssetLinkDTO;
import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.model.Asset;
import de.gsv.idm.shared.model.AssetLink;
import de.gsv.idm.shared.model.User;
import de.gsv.idm.shared.push.event.PushEvent;
import de.gsv.idm.shared.push.event.asset.AssetUpdatedPushEvent;
import de.gsv.idm.shared.push.event.asset.link.AssetLinkCreatedPushEvent;
import de.gsv.idm.shared.push.event.asset.link.AssetLinkDeletedPushEvent;
import de.gsv.idm.shared.push.event.asset.link.AssetLinkPushEvent;
import de.gsv.idm.shared.push.event.asset.link.AssetLinkUpdatedPushEvent;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;

@SuppressWarnings("serial")
public class AssetLinkServiceImpl extends GeneralDomainServiceImpl<AssetLinkDTO, AssetLink>
        implements AssetLinkService {

	public static final de.novanic.eventservice.client.event.domain.Domain CONVERSATION_DOMAIN;
	static {
		CONVERSATION_DOMAIN = DomainFactory.getDomain(AssetLinkPushEvent.CONVERSATION_DOMAIN);
	}

	@Override
	protected Class<AssetLink> getDBClass() {
		return AssetLink.class;
	}

	@Override
	protected List<AssetLink> findAll() {
		return AssetLink.findAll();
	}

	@Override
	protected AssetLink findById(Integer id) {
		return AssetLink.findById(id);
	}

	@Override
	protected AssetLink createObject() {
		return AssetLink.createIt();
	}

	@Override
	protected Domain getConversationDomain() {
		return CONVERSATION_DOMAIN;
	}

	@Override
	protected PushEvent<?, ?> getUpdatedPushEvent(AssetLinkDTO updated) {
		return new AssetLinkUpdatedPushEvent(updated);
	}

	@Override
	protected PushEvent<?, ?> getCreatedPushEvent(AssetLinkDTO created) {
		return new AssetLinkCreatedPushEvent(created);
	}

	@Override
	protected PushEvent<?, ?> getDeletedPushEvent(AssetLinkDTO deleted) {
		return new AssetLinkDeletedPushEvent(deleted);
	}

	@Override
	public AssetLinkDTO create(AssetLinkDTO toCreate, String sessionId) {
		buildConnection();
		if (User.sessionIdIsValid(sessionId)) {

			AssetLinkDTO resultLink = handleLinkToCreate(toCreate);
			AssetLinkDTO resultLink2 = handleLinkToCreate(new AssetLinkDTO(toCreate.getDomainId(),
			        toCreate.getAsset(), toCreate.getParent()));

			sendUpdateForParent(resultLink);
			sendUpdateForParent(resultLink2);
			toCreate = resultLink;
		}
		closeConnection();
		return toCreate;
	}

	@Override
	public List<AssetLinkDTO> create(AssetDTO model1, AssetDTO model2, String sessionId) {
		List<AssetLinkDTO> result = new ArrayList<AssetLinkDTO>();

		buildConnection();
		if (User.sessionIdIsValid(sessionId)) {
			AssetLinkDTO link1DTO = new AssetLinkDTO(model1.getDomainId(), model1, model2);
			AssetLinkDTO resultLink = handleLinkToCreate(link1DTO);
			if (resultLink != null) {
				result.add(resultLink);
			}

			AssetLinkDTO link2DTO = new AssetLinkDTO(model1.getDomainId(), model2, model1);
			AssetLinkDTO resultLink2 = handleLinkToCreate(link2DTO);
			if (resultLink2 != null) {
				result.add(resultLink2);
			}
			sendUpdateForParent(resultLink);
			sendUpdateForParent(resultLink2);

			closeConnection();
		}
		return result;
	}

	@Override
	public List<AssetLinkDTO> create(AssettypeDTO assettype, AssetDTO toLinkAsset, String sessionId) {
		List<AssetLinkDTO> result = new ArrayList<AssetLinkDTO>();
		List<AssetDTO> assetsToUpdate = new ArrayList<AssetDTO>();

		buildConnection();
		if (User.sessionIdIsValid(sessionId)) {
			for (AssetDTO modelFromAsset : assettype.getLinkedAssets()) {
				AssetLinkDTO link1DTO = new AssetLinkDTO(toLinkAsset.getDomainId(), toLinkAsset,
				        modelFromAsset);
				AssetLinkDTO resultLink = handleLinkToCreate(link1DTO);
				if (resultLink != null) {
					result.add(resultLink);
					if (!assetsToUpdate.contains(resultLink.getParent())) {
						assetsToUpdate.add(resultLink.getParent());
					}
				}

				AssetLinkDTO link2DTO = new AssetLinkDTO(toLinkAsset.getDomainId(), modelFromAsset,
				        toLinkAsset);
				resultLink = handleLinkToCreate(link2DTO);
				if (resultLink != null) {
					result.add(resultLink);
					if (!assetsToUpdate.contains(resultLink.getParent())) {
						assetsToUpdate.add(resultLink.getParent());
					}
				}
			}

			for (AssetDTO assetDTO : assetsToUpdate) {
				Asset asset = Asset.findById(assetDTO.getParent().getId());
				if (asset != null) {
					addEvent(AssetServiceImpl.CONVERSATION_DOMAIN,
					        new AssetUpdatedPushEvent(asset.createSlimDTO()));
				}
			}
		}
		closeConnection();

		return result;
	}

	@Override
	public AssetLinkDTO delete(AssetLinkDTO toDelete, String sessionId) {
		buildConnection();
		AssetLink theLink = AssetLink.findById(toDelete.getId());
		if (theLink != null && User.sessionIdIsValid(sessionId)) {
			AssetLink otherLink = AssetLink.findFirst("parent_id = ? && linked_asset_id = ?",
			        theLink.getInteger("linked_asset_id"), theLink.getInteger("parent_id"));
			AssetLinkDTO otherLinkDTO = null;
			if (otherLink != null) {
				otherLinkDTO = otherLink.createFullDTO();
				addEvent(CONVERSATION_DOMAIN, new AssetLinkDeletedPushEvent(otherLinkDTO));
				otherLink.completeDelete(pushUpdateHandler);
			}
			addEvent(CONVERSATION_DOMAIN, new AssetLinkDeletedPushEvent(theLink.createFullDTO()));
			theLink.completeDelete(pushUpdateHandler);
			if (otherLinkDTO != null) {
				sendUpdateForParent(otherLinkDTO);
			}
			sendUpdateForParent(toDelete);
		}
		closeConnection();
		return toDelete;
	}

	private AssetLinkDTO handleLinkToCreate(AssetLinkDTO linkToCreate) {
		AssetLink dbLink = AssetLink.findFirst("parent_id = ? && linked_asset_id = ?", linkToCreate
		        .getParent().getId(), linkToCreate.getAsset().getId());
		if (dbLink == null) {
			AssetLink link1 = createObject();
			link1.updateFromDTO(linkToCreate, pushUpdateHandler);
			AssetLinkDTO linkDTO = link1.createFullDTO();
			addEvent(CONVERSATION_DOMAIN, new AssetLinkCreatedPushEvent(linkDTO));
			return linkDTO;
		}
		return null;
	}

	private void sendUpdateForParent(AssetLinkDTO link) {
		if (link != null && link.getParent() != null) {
			Asset linkParent = Asset.findById(link.getParent().getId());
			if (linkParent != null) {
				addEvent(AssetServiceImpl.CONVERSATION_DOMAIN,
				        new AssetUpdatedPushEvent(linkParent.createSlimDTO()));
			}
		}
	}

}
