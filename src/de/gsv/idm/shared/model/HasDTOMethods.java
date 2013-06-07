package de.gsv.idm.shared.model;

import java.util.List;

import de.gsv.idm.server.push.PushUpdateHandler;
import de.gsv.idm.shared.dto.GeneralDTO;

public interface HasDTOMethods<T extends GeneralDTO<T>> {
	
	public T createFullDTO();
	public T createSlimDTO();

	public T updateFromDTO(T toUpdate, PushUpdateHandler pushUpdateHandler);
	public void completeDelete(PushUpdateHandler pushUpdate);
	public List<GeneralDTO<?>> getAffectedObjects();
}
