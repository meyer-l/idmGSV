package de.gsv.idm.shared.model;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Model;

import de.gsv.idm.server.push.PushUpdateHandler;
import de.gsv.idm.server.service.AssetServiceImpl;
import de.gsv.idm.server.service.AssettypeServiceImpl;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssettypeCategoryDTO;
import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.dto.AssettypeMeasureLinkDTO;
import de.gsv.idm.shared.dto.AssettypeModuleLinkDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.MeasureStatusObject;
import de.gsv.idm.shared.dto.ModuleDTO;
import de.gsv.idm.shared.dto.SecurityLevelDTO;
import de.gsv.idm.shared.push.event.asset.AssetDeletedPushEvent;
import de.gsv.idm.shared.push.event.assettype.AssettypeUpdatedPushEvent;

public class Assettype extends Model implements HasDTOMethods<AssettypeDTO> {

	public AssettypeDTO createSlimDTO() {
		AssettypeCategoryDTO assetCategoryDTO = null;
		AssettypeCategory assetCategory = AssettypeCategory
		        .findById(getInteger("assettypecategory_id"));
		if (assetCategory != null) {
			assetCategoryDTO = assetCategory.createFullDTO();
		}
		return new AssettypeDTO(getInteger("id"), getInteger("domain_id"), getString("name"),
		        assetCategoryDTO, getString("icon_name"),
		        getBoolean("propagate_security_assesment"),
		        getBoolean("manual_security_assesment"),
		        SecurityLevelDTO.getSecurityLevel(getInteger("availability")),
		        SecurityLevelDTO.getSecurityLevel(getInteger("confidentiality")),
		        SecurityLevelDTO.getSecurityLevel(getInteger("integrity")),
		        getBoolean("personal_data"));
	}

	public AssettypeDTO createFullDTO() {
		Employee responsiplePersonDB = Employee.findById(getInteger("responsible_person_id"));
		EmployeeDTO responsiblePersonDTO = null;
		if (responsiplePersonDB != null) {
			responsiblePersonDTO = responsiplePersonDB.createSlimDTO();
		}

		List<AssettypeModuleLinkDTO> assetModulesList = new ArrayList<AssettypeModuleLinkDTO>();
		List<AssettypesModules> modulesList = getAll(AssettypesModules.class);
		for (AssettypesModules link : modulesList) {
			AssettypeModuleLinkDTO linkDTO = link.createDTO();
			if (linkDTO != null) {
				assetModulesList.add(linkDTO);
			}
		}

		List<AssettypeMeasureLinkDTO> assetMeasuresList = new ArrayList<AssettypeMeasureLinkDTO>();
		List<AssettypesMeasures> measuresList = getAll(AssettypesMeasures.class);
		for (AssettypesMeasures link : measuresList) {
			AssettypeMeasureLinkDTO linkDTO = link.createDTO();
			if (linkDTO != null) {
				assetMeasuresList.add(linkDTO);
			}
		}

		List<AssetDTO> linkedModelsDTO = new ArrayList<AssetDTO>();
		List<Asset> linkedModels = getAll(Asset.class);
		for (Asset model : linkedModels) {
			linkedModelsDTO.add(model.createFullDTO());
		}

		AssettypeCategoryDTO assetCategoryDTO = null;
		AssettypeCategory assetCategory = AssettypeCategory
		        .findById(getInteger("assettypecategory_id"));
		if (assetCategory != null) {
			assetCategoryDTO = assetCategory.createFullDTO();
		}

		return new AssettypeDTO(getInteger("id"), getInteger("domain_id"), getString("name"),
		        assetCategoryDTO, getString("icon_name"), getString("location"),
		        getString("description"), getBoolean("it_asset"), getString("architecture"),
		        getString("status"), getBoolean("propagate_security_assesment"),
		        getBoolean("manual_security_assesment"),
		        SecurityLevelDTO.getSecurityLevel(getInteger("availability")),
		        getString("availability_explanation"),
		        SecurityLevelDTO.getSecurityLevel(getInteger("confidentiality")),
		        getString("confidentiality_explanation"),
		        SecurityLevelDTO.getSecurityLevel(getInteger("integrity")),
		        getString("integrity_explanation"), assetModulesList, assetMeasuresList,
		        responsiblePersonDTO, linkedModelsDTO, getBoolean("personal_data"));
	}

	@Override
	public AssettypeDTO updateFromDTO(AssettypeDTO toUpdate, PushUpdateHandler server) {
		set("name", toUpdate.getName());
		if (toUpdate.getCategory() != null) {
			if (toUpdate.getCategory().getId() > 0) {
				set("assettypecategory_id", toUpdate.getCategory().getId());
			} else {
				AssettypeCategory newCategory = AssettypeCategory.createWithName(toUpdate
				        .getCategory().getName());
				set("assettypecategory_id", newCategory.getId());
			}
		}
		set("domain_id", toUpdate.getDomainId());
		set("location", toUpdate.getLocation());
		set("description", toUpdate.getDescription());
		set("icon_name", toUpdate.getIconName());
		set("it_asset", toUpdate.isItAsset());
		set("architecture", toUpdate.getArchitecture());
		set("status", toUpdate.getStatus());
		set("propagate_security_assesment", toUpdate.isPropagateSecurityAssesment());
		set("manual_security_assesment", toUpdate.isManuelSecurityAssesment());
		if (toUpdate.getAvailability() != null) {
			set("availability", toUpdate.getAvailability().getId());
		}
		if (toUpdate.getConfidentiality() != null) {
			set("confidentiality", toUpdate.getConfidentiality().getId());
		}
		if (toUpdate.getIntegrity() != null) {
			set("integrity", toUpdate.getIntegrity().getId());
		}
		set("availability_explanation", toUpdate.getAvailabilityExplanation());
		set("confidentiality_explanation", toUpdate.getConfidentialityExplanation());
		set("integrity_explanation", toUpdate.getIntegrityExplanation());
		set("personal_data", toUpdate.isPersonalData());
		if (toUpdate.getResponsiblePerson() != null) {
			set("responsible_person_id", toUpdate.getResponsiblePerson().getId());
		} else {
			set("responsible_person_id", null);
		}

		List<Asset> currentAssets = getAll(Asset.class);

		ArrayList<Module> currentModules = new ArrayList<Module>(getAll(Module.class));
		ArrayList<Module> newModules = new ArrayList<Module>();
		for (AssettypeModuleLinkDTO mDTO : toUpdate.getModules()) {

			Module moduleDB = Module.findById(mDTO.getModule().getId());
			if (moduleDB != null) {
				newModules.add(moduleDB);
				AssettypesModules linkDB = AssettypesModules.findById(mDTO.getId());
				if (linkDB == null) {
					linkDB = AssettypesModules.createIt("module_id", mDTO.getModule().getId());
					add(linkDB);
					for (Asset model : currentAssets) {
						AssetsAssettypesModules newLink = new AssetsAssettypesModules();
						newLink.set("assettypes_modules_id", linkDB.getId());
						newLink.set("asset_id", model.getId());
						newLink.saveIt();
					}
				}
				if (linkDB != null && mDTO.getResponsiblePerson() != null) {
					linkDB.set("responsible_person_id", mDTO.getResponsiblePerson().getId());
					linkDB.saveIt();
				}
			}
		}

		currentModules.removeAll(newModules);
		for (Module oldModule : currentModules) {
			List<AssettypesModules> moduleLinks = AssettypesModules.where(
			        "assettype_id = ? AND module_id = ?", getId(), oldModule.getId());
			for (AssettypesModules link : moduleLinks) {
				List<AssetsAssettypesModules> linked = AssetsAssettypesModules.where(
				        "assettypes_modules_id = ?", link.getId());
				for (AssetsAssettypesModules assetModelLink : linked) {
					assetModelLink.delete();
				}
				link.deleteCascadeShallow();
			}
		}

		ArrayList<Measure> currentMeasures = new ArrayList<Measure>(getAll(Measure.class));
		ArrayList<Measure> newMeasures = new ArrayList<Measure>();
		for (AssettypeMeasureLinkDTO mDTO : toUpdate.getMeasures()) {
			Measure measureDB = Measure.findById(mDTO.getMeasure().getId());
			if (measureDB != null) {
				newMeasures.add(measureDB);
				AssettypesMeasures linkDB = AssettypesMeasures.findById(mDTO.getId());
				if (linkDB == null) {
					Integer measureId = mDTO.getMeasure().getId();
					linkDB = AssettypesMeasures.createIt("measure_id", measureId);
					add(linkDB);
					for (Asset asset : currentAssets) {
						AssetsMeasures assetMeasure = AssetsMeasures.findFirst(
						        "asset_id = ? AND measure_id = ?", asset.getId(), measureId);
						if (assetMeasure == null) {
							assetMeasure = AssetsMeasures.createIt("asset_id", asset.getId(),
							        "measure_id", measureId);
						}
						AssetsMeasuresAssettypesMeasures newLink = new AssetsMeasuresAssettypesMeasures();
						newLink.set("assettypes_measures_id", linkDB.getId());
						newLink.set("assets_measures_id", assetMeasure.getInteger("id"));
						newLink.set("measure_id", measureId);
						newLink.saveIt();
					}
				}
				if (linkDB != null) {
					if (mDTO.getResponsiblePerson() != null) {
						linkDB.set("responsible_person_id", mDTO.getResponsiblePerson().getId());
					}
					linkDB.set("status", mDTO.getStatus());
					linkDB.set("cost", mDTO.getCost());
					linkDB.set("manual_add", mDTO.isManualAdd());
					linkDB.set("pass_on", mDTO.isPassOn());

					ArrayList<Module> currentLinkedModules = new ArrayList<Module>(
					        linkDB.getAll(Module.class));
					ArrayList<Module> newLinkedModules = new ArrayList<Module>();
					for (ModuleDTO moduleDTO : mDTO.getLinkedModules()) {
						Module moduleDB = Module.findById(moduleDTO.getId());
						if (moduleDB != null) {
							newLinkedModules.add(moduleDB);
						}
					}

					ArrayList<Module> clonedCurrent = new ArrayList<Module>(currentLinkedModules);
					currentLinkedModules.removeAll(newLinkedModules);
					newLinkedModules.removeAll(clonedCurrent);

					for (Module oldLink : currentLinkedModules) {
						linkDB.remove(oldLink);
					}

					for (Module newLink : newLinkedModules) {
						AssettypesMeasuresModules assettypeMeasureModule = new AssettypesMeasuresModules();
						assettypeMeasureModule.set("assettype_id", toUpdate.getId());
						assettypeMeasureModule.set("assettypes_measures_id", linkDB.getId());
						assettypeMeasureModule.set("module_id", newLink.getId());
						assettypeMeasureModule.saveIt();
					}

					linkDB.saveIt();
				}

			}
		}

		currentMeasures.removeAll(newMeasures);

		for (Measure oldMeasure : currentMeasures) {
			List<AssettypesMeasures> measureLinks = AssettypesMeasures.where(
			        "assettype_id = ? AND measure_id = ?", getId(), oldMeasure.getId());

			for (AssettypesMeasures assettypesMeasures : measureLinks) {
				List<AssetsMeasuresAssettypesMeasures> linked = AssetsMeasuresAssettypesMeasures
				        .where("assettypes_measures_id = ?", assettypesMeasures.getId());
				for (AssetsMeasuresAssettypesMeasures assetsmeasuresAssettypesMeasures : linked) {
					Integer assetsMeasuresId = assetsmeasuresAssettypesMeasures
					        .getInteger("assets_measures_id");
					AssetsMeasures assetsMeasures = AssetsMeasures.findById(assetsMeasuresId);
					if (assetsMeasures != null) {
						String status = assetsMeasures.getString("status");
						if (status == null || status.equals("")
						        || status.equals(MeasureStatusObject.getNotProcessed())
						        || status.equals(MeasureStatusObject.getNotImplemented())) {
							String assettypeStatus = assettypesMeasures.getString("status");
							if (assettypeStatus == null
							        || assettypeStatus.equals("")
							        || assettypeStatus
							                .equals(MeasureStatusObject.getNotProcessed())
							        || assettypeStatus.equals(MeasureStatusObject
							                .getNotImplemented())) {
								List<AssetsMeasuresSecurityzonesMeasures> securityzoneLinks = AssetsMeasuresSecurityzonesMeasures
								        .where("assets_measures_id = ?", assetsMeasuresId);
								if (securityzoneLinks.size() == 0) {
									assetsMeasures.deleteCascadeShallow();
								}
							} else {
								assetsMeasures.set("status", assettypeStatus);
								assetsMeasures.saveIt();
							}

						}
					}
					assetsmeasuresAssettypesMeasures.delete();
				}

				assettypesMeasures.deleteCascadeShallow();
			}
		}

		saveIt();
		AssettypeDTO updatedDTO = createFullDTO();
		server.addEvent(AssettypeServiceImpl.CONVERSATION_DOMAIN, new AssettypeUpdatedPushEvent(
		        updatedDTO));

		return updatedDTO;
	}

	@Override
	public void completeDelete(PushUpdateHandler server) {
		List<Asset> assets = getAll(Asset.class);
		for (Asset asset : assets) {
			server.addEvent(AssetServiceImpl.CONVERSATION_DOMAIN,
			        new AssetDeletedPushEvent(asset.createSlimDTO()));
			asset.completeDelete(server);
		}

		for (AssettypesMeasures assettypeMeasure : getAll(AssettypesMeasures.class)) {
			assettypeMeasure.deleteCascadeShallow();
		}
		deleteCascadeShallow();
	}

	@Override
	public List<GeneralDTO<?>> getAffectedObjects() {
		List<GeneralDTO<?>> result = new ArrayList<GeneralDTO<?>>();
		result.add(createFullDTO());
		for (Asset asset : getAll(Asset.class)) {
			AssetDTO assetDTO = asset.createFullDTO();
			if (!result.contains(assetDTO)) {
				result.add(assetDTO);
			}
		}
		return result;
	}
}
