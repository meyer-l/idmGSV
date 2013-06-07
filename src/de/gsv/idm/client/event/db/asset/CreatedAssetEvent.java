package de.gsv.idm.client.event.db.asset;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.AssetDTO;

public class CreatedAssetEvent extends ObjectEvent<AssetDTO,CreatedAssetEvent > {

		public static Type<GeneralEventHandler<CreatedAssetEvent>> TYPE = new Type<GeneralEventHandler<CreatedAssetEvent>>();
		private Integer tempId;
		public CreatedAssetEvent(AssetDTO object, Integer tempId) {
			super(object);
			this.tempId = tempId;
		}

		public Integer getTempId() {
        	return tempId;
        }

		public void setTempId(Integer tempId) {
        	this.tempId = tempId;
        }

		@Override
		public Type<GeneralEventHandler<CreatedAssetEvent>> getAssociatedType() {
			return TYPE;
		}
}
