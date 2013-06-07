package de.gsv.idm.shared.model;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Model;

import de.gsv.idm.server.push.PushUpdateHandler;
import de.gsv.idm.server.service.EmployeeServiceImpl;
import de.gsv.idm.server.service.OccupationServiceImpl;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.InformationDTO;
import de.gsv.idm.shared.dto.SecurityLevelDTO;
import de.gsv.idm.shared.dto.SecurityzoneDTO;
import de.gsv.idm.shared.push.event.employee.EmployeeUpdatedPushEvent;
import de.gsv.idm.shared.push.event.occupation.OccupationUpdatedPushEvent;

public class Information extends Model implements HasDTOMethods<InformationDTO> {

	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (other instanceof Information)
			return getId() == ((Information) other).getId();
		return false;
	}

	public Integer getId() {
		return getInteger("id");
	}

	public InformationDTO createSlimDTO() {
		Securityzone securityzoneDB = getSecurityzone();
		SecurityzoneDTO securityzoneDTO = null;
		if (securityzoneDB != null) {
			securityzoneDTO = securityzoneDB.createSlimDTO();
		} else {
			List<Securityzone> securityzonesDBList = Securityzone.where("order_number = ?", 0);
			if (securityzonesDBList.size() == 1) {
				securityzoneDTO = securityzonesDBList.get(0).createSlimDTO();
			} else if (securityzonesDBList.size() == 0) {
				securityzoneDB = Securityzone.createIt("order_number", 0, "name", "Schutzzone 0");
				securityzoneDTO = securityzoneDB.createFullDTO();
			}
		}

		return new InformationDTO(getInteger("id"), getString("name"), securityzoneDTO);
	}

	public InformationDTO createFullDTO() {
		Securityzone securityzoneDB = getSecurityzone();
		SecurityzoneDTO securityzoneDTO = null;
		if (securityzoneDB != null) {
			securityzoneDTO = securityzoneDB.createSlimDTO();
		} else {
			List<Securityzone> securityzonesDBList = Securityzone.where("order_number = ?", 0);
			if (securityzonesDBList.size() == 1) {
				securityzoneDTO = securityzonesDBList.get(0).createSlimDTO();
			} else if (securityzonesDBList.size() == 0) {
				securityzoneDB = Securityzone.createIt("order_number", 0, "name", "Schutzzone 0");
				securityzoneDTO = securityzoneDB.createFullDTO();
			}
		}

		return new InformationDTO(getInteger("id"), getInteger("domain_id"), getString("name"),
		        getString("ident"), SecurityLevelDTO.getSecurityLevel(getInteger("availability")),
		        getString("availability_explanation"),
		        SecurityLevelDTO.getSecurityLevel(getInteger("confidentiality")),
		        getString("confidentiality_explanation"),
		        SecurityLevelDTO.getSecurityLevel(getInteger("integrity")),
		        getString("integrity_explanation"), getBoolean("personal_data"), securityzoneDTO);
	}

	@Override
	public InformationDTO updateFromDTO(InformationDTO toUpdate, PushUpdateHandler server) {
		set("name", toUpdate.getName());
		set("ident", toUpdate.getIdent());
		set("domain_id", toUpdate.getDomainId());
		set("availability", toUpdate.getAvailability().getId());
		set("confidentiality", toUpdate.getConfidentiality().getId());
		set("integrity", toUpdate.getIntegrity().getId());
		set("availability_explanation", toUpdate.getAvailabilityExplanation());
		set("confidentiality_explanation", toUpdate.getConfidentialityExplanation());
		set("integrity_explanation", toUpdate.getIntegrityExplanation());
		set("personal_data", toUpdate.isPersonalData());
		if (toUpdate.getSecurityzone() != null) {
			set("securityzone_id", toUpdate.getSecurityzone().getId());
		}
		saveIt();

		return createFullDTO();
	}

	@Override
	public void completeDelete(PushUpdateHandler server) {
		List<Employee> employees = new ArrayList<Employee>(getAll(Employee.class));
		List<Occupation> occupations = new ArrayList<Occupation>(getAll(Occupation.class));
		deleteCascadeShallow();
		for (Employee employee : employees) {
			server.addEvent(EmployeeServiceImpl.CONVERSATION_DOMAIN, new EmployeeUpdatedPushEvent(
			        employee.createSlimDTO()));
		}
		for (Occupation occupation : occupations) {
			server.addEvent(OccupationServiceImpl.CONVERSATION_DOMAIN,
			        new OccupationUpdatedPushEvent(occupation.createSlimDTO()));
		}
	}

	public Securityzone getSecurityzone() {
		return Securityzone.findById(getInteger("securityzone_id"));
	}

	@Override
	public List<GeneralDTO<?>> getAffectedObjects() {
		List<GeneralDTO<?>> result = new ArrayList<GeneralDTO<?>>();

		InformationDTO informationDTO = createFullDTO();
		if (informationDTO != null && !result.contains(informationDTO)) {
			result.add(informationDTO);
		}

		for (Occupation occupation : getAll(Occupation.class)) {

			for (GeneralDTO<?> item : occupation.getAffectedObjects()) {
				if (!result.contains(item)) {
					result.add(item);
				}
			}
		}

		for (Employee employee : getAll(Employee.class)) {
			for (GeneralDTO<?> item : employee.getAffectedObjects()) {
				if (!result.contains(item)) {
					result.add(item);
				}
			}
		}

		return result;
	}
}
