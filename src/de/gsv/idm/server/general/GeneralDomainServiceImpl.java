package de.gsv.idm.server.general;

import java.util.ArrayList;
import java.util.List;

import de.gsv.idm.client.services.GeneralDomainService;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.model.Domain;
import de.gsv.idm.shared.model.HasDTOMethods;
import de.gsv.idm.shared.model.User;

@SuppressWarnings("serial")
public abstract class GeneralDomainServiceImpl<T extends GeneralDTO<T>, V extends HasDTOMethods<T>> extends
        GeneralServiceImpl<T, V> implements GeneralDomainService<T> {

	@Override
    public ArrayList<T> getAll(Integer domain_id, String sessionId) {
		ArrayList<T> objectsDTO = new ArrayList<T>();
		buildConnection();
		Domain domain = Domain.findById(domain_id);
		if(domain != null && User.sessionIdIsValid(sessionId)){
			@SuppressWarnings("unchecked")
            List<V> objectsDB = domain.getAll(getDBClass());
			for(V objectDB : objectsDB) {
				T objectDTO = objectDB.createFullDTO();
				objectsDTO.add(objectDTO);
			}
		}
        
		closeConnection();
		return objectsDTO;
    }

	@SuppressWarnings("rawtypes")
    protected abstract Class getDBClass();
}
