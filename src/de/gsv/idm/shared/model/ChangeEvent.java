package de.gsv.idm.shared.model;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Model;

import de.gsv.idm.server.push.PushUpdateHandler;
import de.gsv.idm.server.service.EmployeeServiceImpl;
import de.gsv.idm.server.service.OccupationServiceImpl;
import de.gsv.idm.shared.dto.ChangeAppliedDTO;
import de.gsv.idm.shared.dto.ChangeEventDTO;
import de.gsv.idm.shared.dto.ChangeItemDTO;
import de.gsv.idm.shared.dto.ChangeTypeDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.ChangeAppliedDTO.ChangeStatus;
import de.gsv.idm.shared.dto.ChangeEventDTO.ProcessType;
import de.gsv.idm.shared.push.event.employee.EmployeeCreatedPushEvent;
import de.gsv.idm.shared.push.event.occupation.OccupationCreatedPushEvent;

public class ChangeEvent extends Model implements HasDTOMethods<ChangeEventDTO> {

	@Override
	public ChangeEventDTO createFullDTO() {
		ChangeItem changeItemDB = ChangeItem.findById(getInteger("change_item_id"));
		ChangeTypeDTO changeType = ChangeTypeDTO.getChangeType(getInteger("change_type"));
		ChangeItemDTO changeItemDTO = null;
		if (changeItemDB != null) {
			changeItemDTO = changeItemDB.createDTO();
		}
		if (changeItemDTO != null) {
			return new ChangeEventDTO(getInteger("id"), getInteger("domain_id"), changeType,
			        changeItemDTO, getProcessType(getString("process_type")),
			        getBoolean("applied"), getTimestamp("created_at"));
		} else {
			deleteCascadeShallow();
			return null;
		}

	}

	@Override
	public ChangeEventDTO createSlimDTO() {
		return createFullDTO();
	}

	@Override
	public ChangeEventDTO updateFromDTO(ChangeEventDTO toUpdate, PushUpdateHandler serverImpl) {
		if (toUpdate.getProcessType() != null) {
			set("process_type", toUpdate.getProcessType().toString());
		}
		set("applied", toUpdate.getApplied());
		saveIt();
		return createFullDTO();
	}

	@Override
	public void completeDelete(PushUpdateHandler pushUpdate) {
		ChangeItem item = ChangeItem.findById(getInteger("change_item_id"));
		deleteCascadeShallow();
		if (item != null) {
			List<ChangeEvent> associations = item.getAll(ChangeEvent.class);
			if (associations.size() == 0) {
				item.delete();
			}
		}

	}

	public ProcessType getProcessType(String asString) {
		ProcessType processType = null;
		if (asString != null) {
			if (asString.equals(ChangeEventDTO.ProcessType.Manual.toString())) {
				processType = ProcessType.Manual;
			} else if (asString.equals(ChangeEventDTO.ProcessType.Automatic.toString())) {
				processType = ProcessType.Automatic;
			} else if (asString.equals(ChangeEventDTO.ProcessType.NotNeeded.toString())) {
				processType = ProcessType.NotNeeded;
			} else if (asString.equals(ChangeEventDTO.ProcessType.NotProcessed.toString())) {
				processType = ProcessType.NotProcessed;
			}
		}
		return processType;
	}

	public List<ChangeAppliedDTO> applyChange(PushUpdateHandler server) {
		List<ChangeAppliedDTO> result = new ArrayList<ChangeAppliedDTO>();
		switch (getInteger("change_type")) {
		case 0:
			result = applyEmployeeChange(server);
			break;
		case 1:
			result = applyAssetChange(server);
			break;
		case 2:
			result = applyOccupationChange(server);
			break;
		}
		if (result.size() > 0) {
			set("applied", true);
			saveIt();
		} else {
			// Change couldn't be applied, set it to manual and handle
			// errorinformation on clientside
			if (!getBoolean("applied")) {
				set("process_type", ChangeEventDTO.ProcessType.Manual.toString());
				saveIt();
				result.add(new ChangeAppliedDTO(createFullDTO(),
				        ChangeAppliedDTO.ChangeStatus.notApplied));
			}
		}

		return result;
	}

	// TODO: how to handle only partial applied changes
	// create new Change Event, set to manual with the problematic items ...
	private List<ChangeAppliedDTO> applyOccupationChange(PushUpdateHandler server) {
		List<ChangeAppliedDTO> result = new ArrayList<ChangeAppliedDTO>();

		ChangeItem item = ChangeItem.findById(getInteger("change_item_id"));
		Employee employee = findEmployee(item);
		if (employee != null && item != null) {
			ChangeStatus status = getEmployeeStatus(employee);

			List<Occupation> oldParsedOccupations = new ArrayList<Occupation>();
			String oldOccupationsIds = item.getString("old_occupations_ids");
			if (!oldOccupationsIds.isEmpty()) {
				for (String occupationId : oldOccupationsIds.split(";")) {
					Occupation occupationDB = Occupation.findById(occupationId);
					if (occupationDB != null) {
						oldParsedOccupations.add(occupationDB);
					}
				}
			}
			List<Occupation> newOccupations = new ArrayList<Occupation>();
			String newOccupationsIds = item.getString("new_occupations_ids");
			if (!newOccupationsIds.isEmpty()) {
				for (String occupationId : newOccupationsIds.split(";")) {
					Occupation occupationDB = Occupation.findById(occupationId);
					if (occupationDB != null) {
						newOccupations.add(occupationDB);
					}
				}
			}

			List<String> missingOccupations = new ArrayList<String>();
			String missingOccupationsNames = item.getString("missing_occupations_names");
			if (!missingOccupationsNames.isEmpty()) {
				for (String occupationName : missingOccupationsNames.split(";")) {
					if (!occupationName.equals("")) {
						missingOccupations.add(occupationName);
					}
				}
			}
			List<Occupation> currentOccupations = employee.getAll(Occupation.class);

			List<Occupation> occupationsToAdd = new ArrayList<Occupation>(newOccupations);
			occupationsToAdd.removeAll(currentOccupations);

			List<Occupation> occupationsToRemove = new ArrayList<Occupation>(oldParsedOccupations);
			occupationsToRemove.removeAll(newOccupations);

			for (Occupation occupation : occupationsToRemove) {
				employee.remove(occupation);
			}

			for (Occupation occupation : occupationsToAdd) {
				employee.add(occupation);
			}

			for (String occupationName : missingOccupations) {
				List<Occupation> foundOccupations = Occupation.where("name = ?", occupationName);
				Occupation occupation = null;
				if (foundOccupations.size() > 0) {
					if (foundOccupations.size() == 1) {
						occupation = foundOccupations.get(0);
					}
				} else {
					occupation = Occupation.createIt("domain_id", item.getInteger("domain_id"),
					        "name", occupationName);
					result.add(new ChangeAppliedDTO(occupation.createFullDTO(),
					        ChangeStatus.created));
					server.addEvent(OccupationServiceImpl.CONVERSATION_DOMAIN,
					        new OccupationCreatedPushEvent(occupation.createFullDTO()));
				}
				if (occupation != null) {
					employee.add(occupation);
				}

			}

			result.add(handleEmployeeChanges(employee, status, server));
		}
		return result;
	}

	private List<ChangeAppliedDTO> applyAssetChange(PushUpdateHandler server) {
		List<ChangeAppliedDTO> result = new ArrayList<ChangeAppliedDTO>();
		ChangeItem item = ChangeItem.findById(getInteger("change_item_id"));
		Employee employee = findEmployee(item);
		if (employee != null && item != null) {

			ChangeStatus status = getEmployeeStatus(employee);

			List<Asset> oldAssets = getOldAssets(item);
			List<Asset> newAssets = getNewAssets(item);

			List<Asset> currentAssets = employee.getAll(Asset.class);

			List<Asset> assetsToAdd = new ArrayList<Asset>(newAssets);
			assetsToAdd.removeAll(currentAssets);

			List<Asset> assetsToRemove = new ArrayList<Asset>(oldAssets);
			assetsToRemove.removeAll(newAssets);

			List<Asset> updatedAsset = new ArrayList<Asset>();

			for (Asset asset : assetsToRemove) {
				employee.remove(asset);
				if (!updatedAsset.contains(asset)) {
					updatedAsset.add(asset);
				}
			}

			for (Asset asset : assetsToAdd) {
				employee.add(asset);
				if (!updatedAsset.contains(asset)) {
					updatedAsset.add(asset);
				}
			}

			result.add(handleEmployeeChanges(employee, status, server));
			for (Asset asset : updatedAsset) {
				result.add(new ChangeAppliedDTO(asset.createFullDTO(), ChangeStatus.updated));
			}
		}
		return result;
	}

	private List<Asset> getNewAssets(ChangeItem item) {
		List<Asset> result = new ArrayList<Asset>();
		for (String assetId : item.getString("new_assets_ids").split(";")) {
			Asset assetDB = Asset.findById(assetId);
			if (assetDB != null) {
				result.add(assetDB);
			}
		}
		return result;
	}

	private List<Asset> getOldAssets(ChangeItem item) {
		List<Asset> result = new ArrayList<Asset>();
		for (String assetId : item.getString("old_assets_ids").split(";")) {
			Asset assetDB = Asset.findById(assetId);
			if (assetDB != null) {
				result.add(assetDB);
			}
		}
		return result;
	}

	private List<ChangeAppliedDTO> applyEmployeeChange(PushUpdateHandler server) {
		List<ChangeAppliedDTO> result = new ArrayList<ChangeAppliedDTO>();
		ChangeItem item = ChangeItem.findById(getInteger("change_item_id"));
		Employee employee = findEmployee(item);
		ChangeStatus status = getEmployeeStatus(employee);
		if (employee != null && item != null) {
			employee.set("telefon", item.getString("new_telefon"));
			result.add(handleEmployeeChanges(employee, status, server));
		}

		return result;
	}

	private Employee findEmployee(ChangeItem item) {
		if (item != null) {
			Employee employee = Employee.findById(item.getInteger("employee_id"));
			if (employee == null) {
				String firstName = "";
				String surName = "";
				String[] splitted = item.getString("parsed_employee_name").split(",");
				if (splitted.length == 2) {
					surName = splitted[0].trim();
					firstName = splitted[1].trim();
					// Was the Employee created between parsing and appliance of
					// the change?
					List<Employee> newlyCreated = Employee.where("name = ? and surname = ?",
					        firstName, surName);
					if (newlyCreated.size() > 0) {
						if (newlyCreated.size() == 1) {
							employee = newlyCreated.get(0);
						}
					} else {
						employee = new Employee();
						employee.set("domain_id", item.getInteger("domain_id"), "name", firstName,
						        "surname", surName);
					}

				}

			}
			return employee;
		}
		return null;
	}

	private ChangeAppliedDTO handleEmployeeChanges(Employee employee, ChangeStatus status,
	        PushUpdateHandler server) {
		employee.saveIt();
		EmployeeDTO employeeDTO = employee.createFullDTO();
		if (status.equals(ChangeStatus.created)) {
			server.addEvent(EmployeeServiceImpl.CONVERSATION_DOMAIN, new EmployeeCreatedPushEvent(
			        employeeDTO));
		}

		return new ChangeAppliedDTO(employeeDTO, status);
	}

	private ChangeStatus getEmployeeStatus(Employee employee) {
		ChangeStatus status = ChangeStatus.updated;
		if (employee.isNew()) {
			status = ChangeStatus.created;
			employee.saveIt();
		}
		return status;
	}

	@Override
	public List<GeneralDTO<?>> getAffectedObjects() {
		List<GeneralDTO<?>> result = new ArrayList<GeneralDTO<?>>();
		result.add(createFullDTO());
		ChangeItem item = ChangeItem.findById(getInteger("change_item_id"));
		if (item != null) {
			Employee employee = findEmployee(item);
			switch (getInteger("change_type")) {
			case 0:
				if (employee != null && employee.getInteger("id") != null) {
					EmployeeDTO employeeDTO = employee.createFullDTO();
					if (employeeDTO != null && !result.contains(employeeDTO)) {
						result.add(employeeDTO);
					}
				}

				break;

			case 2:
				if (employee != null && employee.getInteger("id") != null) {
					for (GeneralDTO<?> affected : employee.getAffectedObjects()) {
						if (!result.contains(affected)) {
							result.add(affected);
						}
					}
				}

				break;
			case 1:
				List<Asset> oldAssets = getOldAssets(item);
				List<Asset> newAssets = getNewAssets(item);

				List<Asset> currentAssets = employee.getAll(Asset.class);

				List<Asset> assetsToAdd = new ArrayList<Asset>(newAssets);
				assetsToAdd.removeAll(currentAssets);

				List<Asset> assetsToRemove = new ArrayList<Asset>(oldAssets);
				assetsToRemove.removeAll(newAssets);

				List<Asset> updatedAsset = new ArrayList<Asset>(assetsToAdd);
				updatedAsset.addAll(assetsToRemove);
				for (Asset asset : updatedAsset) {
					for (GeneralDTO<?> affected : asset.getAffectedObjects()) {
						if (!result.contains(affected)) {
							result.add(affected);
						}
					}
				}
				break;

			}
		}

		return result;
	}
}
