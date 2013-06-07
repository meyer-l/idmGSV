package de.gsv.idm.shared.model;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Model;

import de.gsv.idm.server.push.PushUpdateHandler;
import de.gsv.idm.server.service.AssetServiceImpl;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.InformationDTO;
import de.gsv.idm.shared.dto.OccupationDTO;
import de.gsv.idm.shared.push.event.asset.AssetUpdatedPushEvent;

public class Employee extends Model implements HasDTOMethods<EmployeeDTO> {

	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (other instanceof Employee)
			return getId() == ((Employee) other).getId();
		return false;
	}

	public Integer getId() {
		return getInteger("id");
	}

	public EmployeeDTO createSlimDTO() {
		return new EmployeeDTO(getInteger("id"), getString("name"), getString("surname"));
	}

	public EmployeeDTO createFullDTO() {
		List<InformationDTO> informationDTOList = new ArrayList<InformationDTO>();
		List<Information> informationList = getAll(Information.class);

		for (Information i : informationList) {
			informationDTOList.add(i.createSlimDTO());
		}

		List<OccupationDTO> occupationDTOList = new ArrayList<OccupationDTO>();
		List<Occupation> occupationList = getAll(Occupation.class);

		for (Occupation i : occupationList) {
			occupationDTOList.add(i.createSlimDTO());
		}

		List<AssetDTO> assetmodelDTOList = new ArrayList<AssetDTO>();
		List<Asset> assetmodelList = getAll(Asset.class);
		for (Asset assetmodel : assetmodelList) {
			assetmodelDTOList.add(assetmodel.createSlimDTO());
		}
		EmployeeDTO dto =  new EmployeeDTO(getInteger("id"), getInteger("domain_id"), getString("name"),
		        getString("surname"), getString("telefon"), informationDTOList, occupationDTOList,
		        assetmodelDTOList);
		return dto;
	}

	@Override
	public EmployeeDTO updateFromDTO(EmployeeDTO toUpdate, PushUpdateHandler server) {
		set("name", toUpdate.getName());
		set("surname", toUpdate.getSurname());
		set("telefon", toUpdate.getTelefon());
		set("domain_id", toUpdate.getDomainId());

		// Informations
		ArrayList<Information> currentInformations = new ArrayList<Information>(
		        getAll(Information.class));
		ArrayList<Information> newInformations = new ArrayList<Information>();
		for (InformationDTO iDTO : toUpdate.getInformations()) {
			Information iDB = Information.findById(iDTO.getId());
			if (iDB != null)
				newInformations.add(iDB);
		}
		DBHelper.manageAssociations(this, currentInformations, newInformations);

		// Occupations
		ArrayList<Occupation> currentOccupations = new ArrayList<Occupation>(
		        getAll(Occupation.class));
		ArrayList<Occupation> newOccupations = new ArrayList<Occupation>();
		for (OccupationDTO oDTO : toUpdate.getOccupations()) {
			Occupation oDB = Occupation.findById(oDTO.getId());
			if (oDB != null)
				newOccupations.add(oDB);
		}
		DBHelper.manageAssociations(this, currentOccupations, newOccupations);

		saveIt();

		return createFullDTO();
	}

	@Override
	public void completeDelete(PushUpdateHandler server) {
		List<Asset> assets = new ArrayList<Asset>(getAll(Asset.class));
		deleteCascadeShallow();
		for (Asset asset : assets) {
			server.addEvent(AssetServiceImpl.CONVERSATION_DOMAIN,
			        new AssetUpdatedPushEvent(asset.createSlimDTO()));
		}
	}

	List<SecurityzonesMeasures> getSecurityzonesMeasures() {
		Securityzone securityzone = getSecurityzone();
		List<SecurityzonesMeasures> result = new ArrayList<SecurityzonesMeasures>();
		if (securityzone != null) {
			List<SecurityzonesMeasures> tempSecurityzonesMeasures = new ArrayList<SecurityzonesMeasures>(
			        securityzone.getAll(SecurityzonesMeasures.class));
			result.addAll(tempSecurityzonesMeasures);
		}

		return result;
	}

	Securityzone getSecurityzone() {
		Securityzone result = null;

		for (Occupation occupation : getAll(Occupation.class)) {
			Securityzone occupationSecurityzone = occupation.getSecurityzone();
			if (occupationSecurityzone != null) {
				if (result == null) {
					result = occupationSecurityzone;
				} else if (occupationSecurityzone.getInteger("order_number") > result
				        .getInteger("order_number")) {
					result = occupationSecurityzone;
				}
			}
		}

		for (Information information : getAll(Information.class)) {
			Securityzone informationSecurityzone = information.getSecurityzone();
			if (informationSecurityzone != null) {

				if (result == null) {
					result = informationSecurityzone;
				} else if (informationSecurityzone.getInteger("order_number") > result
				        .getInteger("order_number")) {
					result = informationSecurityzone;
				}
			}
		}

		return result;
	}

	@Override
	public List<GeneralDTO<?>> getAffectedObjects() {
		List<GeneralDTO<?>> result = new ArrayList<GeneralDTO<?>>();
		EmployeeDTO employeeDTO = createFullDTO();
		if (!result.contains(employeeDTO)) {
			result.add(employeeDTO);
		}

		for (Asset asset : getAll(Asset.class)) {
			asset.checkSecurityzoneMeasures();
			for (GeneralDTO<?> item : asset.getAffectedObjects()) {
				if (!result.contains(item)) {
					result.add(item);
				}
			}
		}

		return result;
	}
}
