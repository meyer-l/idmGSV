package de.gsv.idm.shared.model;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Model;

import de.gsv.idm.server.push.PushUpdateHandler;
import de.gsv.idm.shared.dto.DomainDTO;
import de.gsv.idm.shared.dto.GeneralDTO;

public class Domain extends Model implements HasDTOMethods<DomainDTO> {

	public DomainDTO createSlimDTO() {
		return new DomainDTO(getInteger("id"), getString("name"));
	}

	public DomainDTO createFullDTO() {
		return new DomainDTO(getInteger("id"), getString("name"), getString("ident"));
	}

	@Override
	public DomainDTO updateFromDTO(DomainDTO updateObject, PushUpdateHandler server) {
		set("name", updateObject.getName());
		set("ident",updateObject.getIdent());
		saveIt();
		return createFullDTO();
	}

	@Override
	public void completeDelete(PushUpdateHandler server) {
		for(Asset asset : getAll(Asset.class)){
			asset.completeDelete(server);
		}
		
		for(Assettype assettype : getAll(Assettype.class)){
			assettype.completeDelete(server);
		}
		
		for(Employee employee : getAll(Employee.class)){
			employee.completeDelete(server);
		}
		
		for(Occupation occupation : getAll(Occupation.class)){
			occupation.completeDelete(server);
		}
		
		for(Information information : getAll(Information.class)){
			information.completeDelete(server);
		}
		
		for(ChangeEvent changeEvent : getAll(ChangeEvent.class)){
			changeEvent.completeDelete(server);
		}
		deleteCascade();
	}

	@Override
    public List<GeneralDTO<?>> getAffectedObjects() {
		List<GeneralDTO<?>> result = new ArrayList<GeneralDTO<?>>();
		result.add(createFullDTO());
		
		return result;
    }

}
