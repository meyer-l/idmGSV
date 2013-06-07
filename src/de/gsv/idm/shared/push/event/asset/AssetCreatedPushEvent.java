package de.gsv.idm.shared.push.event.asset;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.AssetDTO;

@SuppressWarnings("serial")
public class AssetCreatedPushEvent extends AssetPushEvent {

	public AssetCreatedPushEvent() {
    }
	
	private Integer tempId;	

	public AssetCreatedPushEvent(AssetDTO object, Integer tempId) {
		super(object);
		this.tempId = tempId;
    }
	
	@Override
	public void call(CUDListener<AssetPushEvent> listener) {
		listener.onCreated(this);
	}
	
	public Integer getTempId() {
    	return tempId;
    }

	public void setTempId(Integer tempId) {
    	this.tempId = tempId;
    }

}
