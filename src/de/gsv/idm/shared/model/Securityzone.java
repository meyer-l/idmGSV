package de.gsv.idm.shared.model;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Many2Many;

import de.gsv.idm.server.push.PushUpdateHandler;
import de.gsv.idm.server.service.InformationServiceImpl;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.MeasureDTO;
import de.gsv.idm.shared.dto.SecurityzoneDTO;
import de.gsv.idm.shared.push.event.information.InformationUpdatedPushEvent;

@Many2Many(other = AssettypesMeasures.class, join = "assettypes_measures_securityzones", sourceFKName = "securityzone_id", targetFKName = "assettypes_measures_id")
public class Securityzone extends Model implements HasDTOMethods<SecurityzoneDTO> {

	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (other instanceof Securityzone)
			return getId() == ((Securityzone) other).getId();
		return false;
	}

	@Override
	public SecurityzoneDTO createFullDTO() {
		List<Measure> measuresDB = getAll(Measure.class);
		List<MeasureDTO> measuresDTO = new ArrayList<MeasureDTO>();
		for (Measure measureDB : measuresDB) {
			measuresDTO.add(measureDB.createSlimDTO());
		}

		Integer orderNumber = getInteger("order_number");
		SecurityzoneDTO lowerOrderSecurityzoneDTO = null;
		Integer lowerOrderSecurityzoneOrderNumber = orderNumber - 1;
		if (lowerOrderSecurityzoneOrderNumber >= 0) {
			Securityzone lowerOrderSecurityzoneDB = Securityzone.findFirst("order_number = ?",
			        lowerOrderSecurityzoneOrderNumber);
			if (lowerOrderSecurityzoneDB != null) {
				lowerOrderSecurityzoneDTO = lowerOrderSecurityzoneDB.createSlimDTO();
			}

		}

		return new SecurityzoneDTO(getInteger("id"), orderNumber, getString("name"),
		        lowerOrderSecurityzoneDTO, measuresDTO);
	}

	@Override
	public SecurityzoneDTO createSlimDTO() {
		return new SecurityzoneDTO(getInteger("id"), getString("name"), getInteger("order_number"));
	}

	@Override
	public SecurityzoneDTO updateFromDTO(SecurityzoneDTO toUpdate, PushUpdateHandler server) {
		set("name", toUpdate.getName());

		if (toUpdate.getOrderNumber() != null) {
			Integer oldOrderNumber = getInteger("order_number");
			if (oldOrderNumber != null && !oldOrderNumber.equals(toUpdate.getOrderNumber())) {
				Integer newOrderNumber = toUpdate.getOrderNumber();
				Securityzone maxOrderNumberSecurityzone = Securityzone
				        .findFirst("order_number=(select max(order_number) from securityzones)");
				Integer maxOrderNumber = maxOrderNumberSecurityzone.getInteger("order_number");
				if (newOrderNumber >= 0 && newOrderNumber <= maxOrderNumber) {
					Integer orderChangeVar = 1;
					List<Securityzone> securityzones;
					if (oldOrderNumber > newOrderNumber) {
						securityzones = Securityzone.where(
						        "order_number BETWEEN ? AND ? order by order_number",
						        newOrderNumber, oldOrderNumber);
					} else {
						orderChangeVar = -1;
						securityzones = Securityzone.where(
						        "order_number BETWEEN ? AND ? order by order_number",
						        oldOrderNumber, newOrderNumber);
					}

					for (Securityzone securityzone : securityzones) {
						if (securityzone.getInteger("order_number").equals(oldOrderNumber)) {
							securityzone.set("order_number", newOrderNumber);
						} else {
							securityzone.set("order_number",
							        securityzone.getInteger("order_number") + orderChangeVar);
						}
						set("order_number", toUpdate.getOrderNumber());
						securityzone.saveIt();
					}
				}
			}
		}

		ArrayList<Measure> currentMeasures = new ArrayList<Measure>(getAll(Measure.class));
		ArrayList<Measure> newMeasures = new ArrayList<Measure>();
		for (MeasureDTO measureDTO : toUpdate.getMeasures()) {
			Measure iDB = Measure.findById(measureDTO.getId());
			if (iDB != null)
				newMeasures.add(iDB);
		}
		ArrayList<Measure> measuresToAddList = new ArrayList<Measure>(newMeasures);
		measuresToAddList.removeAll(currentMeasures);
		ArrayList<Measure> measuresToRemoveList = new ArrayList<Measure>(currentMeasures);
		measuresToRemoveList.removeAll(newMeasures);

		for (Measure toRemove : measuresToRemoveList) {
			SecurityzonesMeasures securityzoneMeasure = SecurityzonesMeasures.findFirst(
			        "securityzone_id = ? AND measure_id = ?", getInteger("id"),
			        toRemove.getInteger("id"));
			if (securityzoneMeasure != null) {
				List<AssetsMeasuresSecurityzonesMeasures> links = AssetsMeasuresSecurityzonesMeasures
				        .where("securityzones_measures_id = ?",
				                securityzoneMeasure.getInteger("id"));
				for (AssetsMeasuresSecurityzonesMeasures link : links) {
					link.deleteCascadeShallow();
				}
			}
		}

		DBHelper.manageAssociations(this, currentMeasures, newMeasures);

		saveIt();

		return createFullDTO();
	}

	@Override
	public void completeDelete(PushUpdateHandler server) {
		List<Information> informations = new ArrayList<Information>(getAllInformations());
		deleteCascadeShallow();
		for (Information information : informations) {
			server.addEvent(InformationServiceImpl.CONVERSATION_DOMAIN,
			        new InformationUpdatedPushEvent(information.createSlimDTO()));
		}
	}

	public List<Securityzone> getHeirSecurityzones() {
		return Securityzone.where("order_number >= ?", getInteger("order_number"));
	}

	public List<Securityzone> getInheritedSecurityzones() {
		return Securityzone.where("order_number <= ?", getInteger("order_number"));
	}

	public List<SecurityzonesMeasures> getAllSecurityzonesMeasures() {
		List<SecurityzonesMeasures> result = new ArrayList<SecurityzonesMeasures>();
		for (Securityzone securityzone : getInheritedSecurityzones()) {
			result.addAll(securityzone.getAll(SecurityzonesMeasures.class));
		}
		return result;
	}

	public List<Measure> getAllMeasures() {
		List<Measure> result = new ArrayList<Measure>();
		for (Securityzone securityzone : getInheritedSecurityzones()) {
			result.addAll(securityzone.getAll(Measure.class));
		}
		return result;
	}

	private List<Information> getAllInformations() {
		List<Information> informationToLink = new ArrayList<Information>();

		for (Securityzone heir : getHeirSecurityzones()) {
			informationToLink.addAll(heir.getAll(Information.class));
		}
		return informationToLink;
	}

	@Override
	public List<GeneralDTO<?>> getAffectedObjects() {
		List<GeneralDTO<?>> result = new ArrayList<GeneralDTO<?>>();

		List<Securityzone> securityzones = Securityzone.findAll();
		for(Securityzone zone : securityzones){
			SecurityzoneDTO zoneDTO = zone.createFullDTO();
				if(zoneDTO != null && !result.contains(zoneDTO)){
					result.add(zoneDTO);
				}			
		}

		for (Information information : getAllInformations()) {
			for (GeneralDTO<?> item : information.getAffectedObjects()) {
				if (!result.contains(item)) {
					result.add(item);
				}
			}
		}
		return result;
	}
}
