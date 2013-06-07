package de.gsv.idm.shared.model;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Model;

import de.gsv.idm.server.push.PushUpdateHandler;
import de.gsv.idm.server.service.EmployeeServiceImpl;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.InformationDTO;
import de.gsv.idm.shared.dto.OccupationDTO;
import de.gsv.idm.shared.dto.SecurityLevelDTO;
import de.gsv.idm.shared.push.event.employee.EmployeeUpdatedPushEvent;

public class Occupation extends Model implements HasDTOMethods<OccupationDTO> {

	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (other instanceof Occupation)
			return getId() == ((Occupation) other).getId();
		return false;
	}

	public Integer getId() {
		return getInteger("id");
	}

	public OccupationDTO createSlimDTO() {
		return new OccupationDTO(getInteger("id"), getString("name"));
	}

	public OccupationDTO createFullDTO() {
		List<InformationDTO> informationDTOList = new ArrayList<InformationDTO>();
		List<Information> informationList = getAll(Information.class);

		for (Information i : informationList) {
			informationDTOList.add(i.createSlimDTO());
		}

		return new OccupationDTO(getInteger("id"), getInteger("domain_id"), getString("name"),
		        getString("ident"), SecurityLevelDTO.getSecurityLevel(getInteger("availability")),
		        getString("availability_explanation"),
		        SecurityLevelDTO.getSecurityLevel(getInteger("confidentiality")),
		        getString("confidentiality_explanation"),
		        SecurityLevelDTO.getSecurityLevel(getInteger("integrity")),
		        getString("integrity_explanation"), getBoolean("personal_data"),
		        getBoolean("manual_security_assesment"), informationDTOList);
	}

	@Override
	public OccupationDTO updateFromDTO(OccupationDTO toUpdate, PushUpdateHandler server) {
		set("name", toUpdate.getName());
		set("ident", toUpdate.getIdent());
		set("domain_id", toUpdate.getDomainId());
		set("manual_security_assesment", toUpdate.isManuelSecurityAssesment());
		set("availability", toUpdate.getAvailability().getId());
		set("confidentiality", toUpdate.getConfidentiality().getId());
		set("integrity", toUpdate.getIntegrity().getId());
		set("availability_explanation", toUpdate.getAvailabilityExplanation());
		set("confidentiality_explanation", toUpdate.getConfidentialityExplanation());
		set("integrity_explanation", toUpdate.getIntegrityExplanation());
		set("personal_data", toUpdate.isPersonalData());

		ArrayList<Information> currentInformations = new ArrayList<Information>(
		        getAll(Information.class));
		ArrayList<Information> newInformations = new ArrayList<Information>();
		for (InformationDTO iDTO : toUpdate.getInformations()) {
			Information iDB = Information.findById(iDTO.getId());
			if (iDB != null)
				newInformations.add(iDB);
		}

		DBHelper.manageAssociations(this, currentInformations, newInformations);

		saveIt();

		return createFullDTO();
	}

	@Override
	public void completeDelete(PushUpdateHandler server) {
		List<Employee> employees = getAll(Employee.class);
		deleteCascadeShallow();
		for (Employee employee : employees) {
			server.addEvent(EmployeeServiceImpl.CONVERSATION_DOMAIN, new EmployeeUpdatedPushEvent(
			        employee.createSlimDTO()));
		}
	}

	public Securityzone getSecurityzone() {
		Securityzone result = null;

		for (Information information : getAll(Information.class)) {
			Securityzone securityzone = information.getSecurityzone();
			if (securityzone != null) {
				if (result == null) {
					result = securityzone;
				} else if (securityzone.getInteger("order_number") > result
				        .getInteger("order_number")) {
					result = securityzone;
				}
			}
		}
		return result;
	}

	@Override
	public List<GeneralDTO<?>> getAffectedObjects() {
		List<GeneralDTO<?>> result = new ArrayList<GeneralDTO<?>>();
		OccupationDTO occupationDTO = createFullDTO();
		if (!result.contains(occupationDTO)) {
			result.add(occupationDTO);
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
