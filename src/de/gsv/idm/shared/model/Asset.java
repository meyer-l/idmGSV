package de.gsv.idm.shared.model;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;

import de.gsv.idm.server.push.PushUpdateHandler;
import de.gsv.idm.server.service.AssetLinkServiceImpl;
import de.gsv.idm.server.service.AssetServiceImpl;
import de.gsv.idm.server.service.AssettypeServiceImpl;
import de.gsv.idm.server.service.SecurityLevelChangeServiceImpl;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssetLinkDTO;
import de.gsv.idm.shared.dto.AssetMeasureLinkDTO;
import de.gsv.idm.shared.dto.AssetModuleLinkDTO;
import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.MeasureStatusObject;
import de.gsv.idm.shared.dto.SecurityLevelChangeDTO;
import de.gsv.idm.shared.push.event.asset.AssetDeletedPushEvent;
import de.gsv.idm.shared.push.event.asset.AssetUpdatedPushEvent;
import de.gsv.idm.shared.push.event.asset.link.AssetLinkDeletedPushEvent;
import de.gsv.idm.shared.push.event.assettype.AssettypeUpdatedPushEvent;
import de.gsv.idm.shared.push.event.securitylevelchange.SecurityLevelChangeDeletedPushEvent;

//TODO: null nach updateFromDTO nach assettype wechsel, überprüfen ob id in den dinge null ist. 
//TODO: Add Room Boolean?
// Fürs parsen gut, und zur erstellung von asset namen, in relation zum raum
@BelongsTo(parent = Asset.class, foreignKeyName = "parent_id")
public class Asset extends Model implements HasDTOMethods<AssetDTO> {

	public boolean equals(Object object) {
		if (object == null) {
			return false;
		} else if (object instanceof Asset
		        && ((Asset) object).getInteger("id") == this.getInteger("id")) {
			return true;
		} else {
			return false;
		}

	}

	public AssetDTO createSlimDTO() {
		return new AssetDTO(getInteger("id"), getInteger("domain_id"));
	}

	public AssetDTO createFullDTO() {

		List<Asset> children = getAll(Asset.class);
		List<AssetDTO> childrenDTO = new ArrayList<AssetDTO>();
		for (Asset child : children) {
			childrenDTO.add(child.createFullDTO());
		}

		List<AssetLink> linkedChildren = getAll(AssetLink.class);
		List<AssetLinkDTO> linkedChildrenDTO = new ArrayList<AssetLinkDTO>();
		for (AssetLink child : linkedChildren) {
			// Sanity-Check for AssetLinks, if somethings messed up while
			// createing or deleteing the links
			if (child.getInteger("linked_asset_id") > 0) {
				linkedChildrenDTO.add(child.createFullDTO());
			} else {
				Integer linkedId = child.getInteger("linked_asset_id");
				child.delete();
				AssetLink partnerLink = AssetLink.findFirst("parent_id = ?", linkedId);
				if (partnerLink != null) {
					partnerLink.delete();
				}
			}

		}

		AssettypeDTO assettypeDTO = null;
		Assettype assettype = Assettype.findById(getInteger("assettype_id"));
		if (assettype != null) {
			assettypeDTO = assettype.createSlimDTO();
		}

		AssetDTO parentDTO = null;
		Asset parentDB = Asset.findById(getInteger("parent_id"));
		if (parentDB != null) {
			parentDTO = parentDB.createSlimDTO();
		}

		EmployeeDTO responsiblePersonDTO = null;
		Employee responsiblePersonDB = Employee.findById(getInteger("responsible_person_id"));
		if (responsiblePersonDB != null) {
			responsiblePersonDTO = responsiblePersonDB.createSlimDTO();
		}

		List<EmployeeDTO> associatedPersonsDTO = new ArrayList<EmployeeDTO>();
		List<Employee> assetmodelsEmployees = getAll(Employee.class);
		for (Employee employeelink : assetmodelsEmployees) {
			associatedPersonsDTO.add(employeelink.createSlimDTO());
		}

		List<AssetModuleLinkDTO> assetModelModulesList = new ArrayList<AssetModuleLinkDTO>();
		List<AssetsAssettypesModules> modulesList = getAll(AssetsAssettypesModules.class);
		for (AssetsAssettypesModules assetsModuleslink : modulesList) {
			AssetModuleLinkDTO assetsModuleslinkDTO = assetsModuleslink.createDTO();
			if (assetsModuleslinkDTO != null) {
				assetModelModulesList.add(assetsModuleslinkDTO);
			}
		}

		List<AssetMeasureLinkDTO> assetModelMeasuresList = new ArrayList<AssetMeasureLinkDTO>();
		List<AssetsMeasures> measuresList = getAll(AssetsMeasures.class);
		for (AssetsMeasures assetsMeasuresLink : measuresList) {
			AssetMeasureLinkDTO assetsMeasuresLinkDTO = assetsMeasuresLink.createDTO();
			if (assetsMeasuresLinkDTO != null) {
				assetModelMeasuresList.add(assetsMeasuresLinkDTO);
			}
		}

		return new AssetDTO(getInteger("id"), getInteger("domain_id"), assettypeDTO,
		        getString("identifier"), getString("status"), parentDTO, responsiblePersonDTO,
		        childrenDTO, linkedChildrenDTO, associatedPersonsDTO, assetModelModulesList,
		        assetModelMeasuresList);
	}

	@Override
	public AssetDTO updateFromDTO(AssetDTO toUpdate, PushUpdateHandler server) {
		Asset oldParent = null;
		Integer oldParentId = getInteger("parent_id");
		if (toUpdate.getParent() != null) {
			if (!toUpdate.getParent().getId().equals(oldParentId)) {
				oldParent = Asset.findById(oldParentId);
			}
			set("parent_id", toUpdate.getParent().getId());
		} else {
			oldParent = Asset.findById(oldParentId);
			set("parent_id", -1);
		}

		set("domain_id", toUpdate.getDomainId());
		set("identifier", toUpdate.getIdentifier());
		set("status", toUpdate.getAssetStatus());
		Integer oldAssettypeId = null;
		if (toUpdate.getId() < 0) {
			oldAssettypeId = null;
		} else {
			oldAssettypeId = getInteger("assettype_id");
		}

		set("assettype_id", toUpdate.getAssettype().getId());
		if (toUpdate.getAssetResponsiblePerson() != null) {
			set("responsible_person_id", toUpdate.getAssetResponsiblePerson().getId());
		} else {
			set("responsible_person_id", null);
		}
		
		for (AssetModuleLinkDTO moduleLink : toUpdate.getModuleLinks()) {
			AssetsAssettypesModules dbModuleLink = AssetsAssettypesModules.findById(moduleLink
			        .getId());
			if (dbModuleLink != null) {
				if (moduleLink.getModelResponsiblePerson() != null) {
					dbModuleLink.set("responsible_person_id", moduleLink
					        .getModelResponsiblePerson().getId());
				} else {
					dbModuleLink.set("responsible_person_id", null);
				}
				dbModuleLink.saveIt();
			}
		}

		for (AssetMeasureLinkDTO measureLinks : toUpdate.getMeasureLinks()) {
			AssetsMeasures dbMeasureLink = AssetsMeasures.findById(measureLinks.getId());
			if (measureLinks.getId() == null) {
				dbMeasureLink = AssetsMeasures.createIt("asset_id", getInteger("id"), "measure_id",
				        measureLinks.getMeasure().getId());
			}
			if (dbMeasureLink != null) {
				if (measureLinks.getModelResponsiblePerson() != null) {
					dbMeasureLink.set("responsible_person_id", measureLinks
					        .getModelResponsiblePerson().getId());
				} else {
					dbMeasureLink.set("responsible_person_id", null);
				}
				if (measureLinks.getModelCost() != null) {
					dbMeasureLink.set("cost", measureLinks.getModelCost());
				} else {
					dbMeasureLink.set("cost", null);
				}
				if (measureLinks.getModelStatus() != null) {
					dbMeasureLink.set("status", measureLinks.getStatus());
				} else {
					dbMeasureLink.set("status", null);
				}
				dbMeasureLink.saveIt();
			}
		}

		ArrayList<Employee> currentEmployees = new ArrayList<Employee>(getAll(Employee.class));
		ArrayList<Employee> newEmployees = new ArrayList<Employee>();
		for (EmployeeDTO eDTO : toUpdate.getAssociatedPersons()) {
			Employee employeeDB = Employee.findById(eDTO.getId());
			if (employeeDB != null)
				newEmployees.add(employeeDB);
		}
		ArrayList<Employee> clonedAssociaitons = new ArrayList<Employee>(currentEmployees);
		currentEmployees.removeAll(newEmployees);
		newEmployees.removeAll(clonedAssociaitons);

		for (Employee oldEmployee : currentEmployees) {
			remove(oldEmployee);
		}

		for (Employee newEmployee : newEmployees) {
			add(newEmployee);
			List<SecurityzonesMeasures> securityzonesMeasures = newEmployee
			        .getSecurityzonesMeasures();
			for (SecurityzonesMeasures securityzoneMeasure : securityzonesMeasures) {
				Integer measureId = securityzoneMeasure.getInteger("measure_id");
				Integer assetId = getInteger("id");
				AssetsMeasures association = AssetsMeasures.findFirst(
				        "measure_id = ? AND asset_id = ?", measureId, assetId);
				if (association == null) {
					association = AssetsMeasures.createIt("measure_id", measureId, "asset_id",
					        getId());
				}
				AssetsMeasuresSecurityzonesMeasures assetMeasureAssociation = AssetsMeasuresSecurityzonesMeasures
				        .findFirst("assets_measures_id = ? AND securityzones_measures_id = ?",
				                association.getInteger("id"), securityzoneMeasure.getInteger("id"));
				if (assetMeasureAssociation == null) {
					AssetsMeasuresSecurityzonesMeasures.createIt("assets_measures_id",
					        association.getInteger("id"), "securityzones_measures_id",
					        securityzoneMeasure.getInteger("id"));
				}
			}

		}

		saveIt();
		if (oldParent != null) {
			server.addEvent(AssetServiceImpl.CONVERSATION_DOMAIN, new AssetUpdatedPushEvent(
			        oldParent.createSlimDTO()));
		}
		
		if (toUpdate.getId() < 0 || oldAssettypeId != null
		        && !oldAssettypeId.equals(toUpdate.getAssettype().getId())) {
			List<AssetsMeasures> linkedMeasures = AssetsMeasures.where("asset_id = ?",
			        toUpdate.getId());
			for (AssetsMeasures assetsMeasures : linkedMeasures) {
				AssetsMeasuresAssettypesMeasures assetAssettypeMeasureLink = AssetsMeasuresAssettypesMeasures
				        .findFirst("assets_measures_id = ?", assetsMeasures.getInteger("id"));
				if (assetsMeasures != null) {
					String assetsMeasuresStatus = assetsMeasures.getString("status");
					if (assetsMeasuresStatus == null || assetsMeasuresStatus.equals("")
					        || assetsMeasuresStatus.equals(MeasureStatusObject.getNotImplemented())
					        || assetsMeasuresStatus.equals(MeasureStatusObject.getNotProcessed())) {
						AssettypesMeasures assettypesMeasures = AssettypesMeasures
						        .findById(assetAssettypeMeasureLink
						                .getInteger("assettypes_measures_id"));
						String assettypesMeasuresStatus = assettypesMeasures.getString("status");
						if (assettypesMeasuresStatus == null
						        || assettypesMeasuresStatus.equals("")
						        || assettypesMeasuresStatus.equals(MeasureStatusObject
						                .getNotImplemented())
						        || assettypesMeasuresStatus.equals(MeasureStatusObject
						                .getNotProcessed())) {
							if (AssetsMeasuresSecurityzonesMeasures.where("assets_measures_id = ?",
							        assetsMeasures.getInteger("id")).size() == 0) {
								assetsMeasures.deleteCascadeShallow();
							}
						} else {
							assetsMeasures.set("status", assettypesMeasuresStatus);
							assetsMeasures.saveIt();
						}
					}

				}

				assetAssettypeMeasureLink.delete();
			}
			
			List<AssetsAssettypesModules> linkedModules = AssetsAssettypesModules.where(
			        "asset_id = ?", toUpdate.getId());
			for (AssetsAssettypesModules assetModelLink : linkedModules) {
				assetModelLink.delete();
			}
			List<AssettypesModules> assettypeModulesList = AssettypesModules.where(
			        "assettype_id = ?", toUpdate.getAssettype().getId());
			for (AssettypesModules moduleLink : assettypeModulesList) {
				AssetsAssettypesModules newLink = new AssetsAssettypesModules();
				newLink.set("assettypes_modules_id", moduleLink.getId());
				newLink.set("asset_id", getInteger("id"));
				newLink.saveIt();
			}
			List<AssettypesMeasures> assettypeMeasuresList = AssettypesMeasures.where(
			        "assettype_id = ?", toUpdate.getAssettype().getId());
			for (AssettypesMeasures measureLink : assettypeMeasuresList) {
				Integer measureId = measureLink.getInteger("measure_id");
				AssetsMeasures assetsMeasures = AssetsMeasures.findFirst(
				        "asset_id = ? and measure_id= ?", getInteger("id"), measureId);
				if (assetsMeasures == null) {
					assetsMeasures = AssetsMeasures.createIt("asset_id", getInteger("id"),
					        "measure_id", measureId);
				}
				AssetsMeasuresAssettypesMeasures newLink = new AssetsMeasuresAssettypesMeasures();
				newLink.set("assettypes_measures_id", measureLink.getId());
				newLink.set("assets_measures_id", assetsMeasures.getInteger("id"));
				newLink.set("measure_id", measureId);
				newLink.saveIt();
			}
		}
		return createFullDTO();
	}

	@Override
	public void completeDelete(PushUpdateHandler server) {
		Integer assetId = getInteger("id");
		List<AssetLink> links = AssetLink.where("parent_id = ? or linked_asset_id = ?", assetId,
		        assetId);

		for (AssetLink link : links) {
			Asset otherParent = null;
			Integer otherParentId = getInteger("parent_id");
			if (otherParentId != null && !otherParentId.equals(assetId)) {
				otherParent = Asset.findById(link.getInteger("parent_id"));
			}
			AssetLinkDTO linkDTO = link.createFullDTO();
			link.deleteCascadeShallow();
			server.addEvent(AssetLinkServiceImpl.CONVERSATION_DOMAIN,
			        new AssetLinkDeletedPushEvent(linkDTO));
			if (otherParent != null) {
				server.addEvent(AssetServiceImpl.CONVERSATION_DOMAIN, new AssetUpdatedPushEvent(
				        otherParent.createSlimDTO()));
			}
		}
		Assettype assettype = Assettype.findById(getInteger("assettype_id"));

		for (Asset childAsset : getAll(Asset.class)) {
			childAsset.completeDelete(server);
		}

		for (AssetsMeasures assetMeasure : getAll(AssetsMeasures.class)) {
			assetMeasure.completeDelete();
		}

		for (SecurityLevelChange securityLevelChange : getAll(SecurityLevelChange.class)) {
			SecurityLevelChangeDTO securityLevelChangeDTO = securityLevelChange.createFullDTO();
			server.addEvent(SecurityLevelChangeServiceImpl.CONVERSATION_DOMAIN,
			        new SecurityLevelChangeDeletedPushEvent(securityLevelChangeDTO));
			securityLevelChange.completeDelete(server);
		}

		server.addEvent(AssetServiceImpl.CONVERSATION_DOMAIN, new AssetDeletedPushEvent(
		        createSlimDTO()));
		deleteCascadeShallow();
		if (assettype != null) {
			server.addEvent(AssettypeServiceImpl.CONVERSATION_DOMAIN,
			        new AssettypeUpdatedPushEvent(assettype.createSlimDTO()));
		}
	}

	public void checkSecurityzoneMeasures() {
		List<Employee> associatedEmployees = getAll(Employee.class);
		Securityzone securityzone = null;
		for (Employee employee : associatedEmployees) {
			Securityzone employeeSecurityzone = employee.getSecurityzone();
			if (employeeSecurityzone != null) {
				if (securityzone == null) {
					securityzone = employeeSecurityzone;
				} else if (employeeSecurityzone.getInteger("order_number") > securityzone
				        .getInteger("order_number")) {
					securityzone = employeeSecurityzone;
				}
			}
		}
		if (securityzone != null) {
			List<Measure> securityzoneMeasureList = securityzone.getAllMeasures();
			List<Measure> assetMeasureList = getAll(Measure.class);

			List<Measure> measuresToRemoveList = new ArrayList<Measure>(assetMeasureList);
			measuresToRemoveList.removeAll(securityzoneMeasureList);

			// Remove Measures
			Integer assetId = getInteger("id");
			for (Measure measure : measuresToRemoveList) {
				Integer measureId = measure.getInteger("id");
				AssetsMeasures assetMeasure = AssetsMeasures.findFirst(
				        "asset_id = ? AND measure_id = ?", assetId, measureId);
				if (assetMeasure != null) {
					AssetsMeasuresSecurityzonesMeasures securityzoneLink = AssetsMeasuresSecurityzonesMeasures
					        .findFirst("assets_measures_id = ?", assetMeasure.getInteger("id"));
					if (securityzoneLink != null) {
						securityzoneLink.deleteCascadeShallow();
					}
					// If the Measure isn't linked through a assettype and is
					// not implemented, delete the assetMeasure
					String assetMeasureStatus = assetMeasure.getString("status");
					if (assetMeasureStatus == null || assetMeasureStatus.equals("")
					        || assetMeasureStatus.equals(MeasureStatusObject.getNotImplemented())
					        || assetMeasureStatus.equals(MeasureStatusObject.getNotProcessed())) {
						if (AssetsMeasuresAssettypesMeasures.where("assets_measures_id = ?",
						        assetMeasure.getInteger("id")).size() == 0) {
							assetMeasure.deleteCascadeShallow();
						}
					}
				}

			}

			List<SecurityzonesMeasures> securityzonesMeasures = securityzone
			        .getAllSecurityzonesMeasures();

			// Create Links between Securityzone and AssetsMeasures
			for (SecurityzonesMeasures securityzoneMeasure : securityzonesMeasures) {
				Integer measureId = securityzoneMeasure.getInteger("measure_id");
				AssetsMeasures assetMeasure = AssetsMeasures.findFirst(
				        "asset_id = ? And measure_id = ?", assetId, measureId);
				// If the assetMeasure doesn't exist, create it
				if (assetMeasure == null) {
					assetMeasure = AssetsMeasures.createIt("asset_id", assetId, "measure_id",
					        measureId, "status", MeasureStatusObject.getNotProcessed() );
				}
				AssetsMeasuresSecurityzonesMeasures assetMeasureSecurityzoneMeasure = AssetsMeasuresSecurityzonesMeasures
				        .findFirst("assets_measures_id = ? AND securityzones_measures_id = ?",
				                assetMeasure.getInteger("id"), securityzoneMeasure.getInteger("id"));
				// Create the Link
				if (assetMeasureSecurityzoneMeasure == null) {
					AssetsMeasuresSecurityzonesMeasures.createIt("assets_measures_id",
					        assetMeasure.getInteger("id"), "securityzones_measures_id",
					        securityzoneMeasure.getInteger("id"));
				}
			}
		}
	}

	@Override
	public List<GeneralDTO<?>> getAffectedObjects() {
		List<GeneralDTO<?>> result = new ArrayList<GeneralDTO<?>>();

		AssetDTO assetDTO = createFullDTO();
		if (!result.contains(assetDTO)) {
			result.add(assetDTO);
		}

		Asset parent = Asset.findById(getInteger("parent_id"));
		if (parent != null) {
			AssetDTO parentDTO = parent.createFullDTO();
			if (!result.contains(parentDTO)) {
				result.add(parentDTO);
			}
		}

		List<AssetLink> assetLinks = AssetLink.where("linked_asset_id = ?", getInteger("id"));
		for (AssetLink assetLink : assetLinks) {
			Asset assetLinkParent = Asset.findById(assetLink.getInteger("parent_id"));
			if (assetLinkParent != null) {
				AssetDTO linkAssetDTO = assetLinkParent.createFullDTO();
				if (assetDTO != null && !result.contains(linkAssetDTO)) {
					result.add(linkAssetDTO);
				}

			}
		}

		for (Employee employee : getAll(Employee.class)) {
			EmployeeDTO employeeDTO = employee.createFullDTO();
			if (!result.contains(employeeDTO)) {
				result.add(employeeDTO);
			}
		}

		return result;
	}
}
