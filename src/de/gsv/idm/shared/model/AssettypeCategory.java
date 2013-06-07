package de.gsv.idm.shared.model;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import de.gsv.idm.server.push.PushUpdateHandler;
import de.gsv.idm.server.service.AssettypeServiceImpl;
import de.gsv.idm.shared.dto.AssettypeCategoryDTO;
import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.push.event.assettype.AssettypeUpdatedPushEvent;

@Table("assettypecategories")
public class AssettypeCategory extends Model implements HasDTOMethods<AssettypeCategoryDTO> {

	@Override
	public AssettypeCategoryDTO createFullDTO() {
		return new AssettypeCategoryDTO(getInteger("id"), getString("name"), getString("icon_name"));
	}

	@Override
	public AssettypeCategoryDTO createSlimDTO() {
		return createFullDTO();
	}

	@Override
	public AssettypeCategoryDTO updateFromDTO(AssettypeCategoryDTO updateObject,
			PushUpdateHandler server) {
		set("name", updateObject.getName());
		set("icon_name", updateObject.getIconName());
		saveIt();
		return createFullDTO();
	}

	@Override
	public void completeDelete(PushUpdateHandler server) {
		List<Assettype> assettypes = new ArrayList<Assettype>(getAll(Assettype.class));
		delete();
		for (Assettype type : assettypes) {
			AssettypeDTO typeDTO = type.createSlimDTO();
			typeDTO.setCategory(null);
			server.addEvent(AssettypeServiceImpl.CONVERSATION_DOMAIN,
			        new AssettypeUpdatedPushEvent(typeDTO));
		}
		
	}

	public static AssettypeCategory createWithName(String name) {
		AssettypeCategory catWithSameName = AssettypeCategory.findFirst("name = ?", name);
		if (catWithSameName != null) {
			return catWithSameName;
		} else {
			return AssettypeCategory.createIt("name", name);
		}
	}

	@Override
    public List<GeneralDTO<?>> getAffectedObjects() {
		List<GeneralDTO<?>> result = new ArrayList<GeneralDTO<?>>();
		result.add(createFullDTO());
		
		return result;
    }

}
