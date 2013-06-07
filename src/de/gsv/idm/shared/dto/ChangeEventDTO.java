package de.gsv.idm.shared.dto;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class ChangeEventDTO extends GeneralDTO<ChangeEventDTO> implements Serializable {

	public enum ProcessType {
		NotProcessed, Manual, Automatic, NotNeeded
	}

	Integer id;
	Integer domainId;
	ChangeTypeDTO changeType;
	ChangeItemDTO changeItem;
	ProcessType processType;
	Boolean applied;
	Date date;

	public ChangeEventDTO() {

	}

	public ChangeEventDTO(Integer id, Integer domainId, ChangeTypeDTO changeType,
	        ChangeItemDTO changeItem, ProcessType processType, Boolean applied, Date date) {
		this.id = id;
		this.domainId = domainId;
		this.changeType = changeType;
		this.changeItem = changeItem;
		this.processType = processType;
		this.applied = applied;
		this.date = date;
	}

	@Override
	public String getName() {
		return changeType.getName();
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Integer getDomainId() {
		return domainId;
	}

	public void setDomainId(Integer domainId) {
		this.domainId = domainId;
	}

	public ChangeTypeDTO getChangeType() {
		return changeType;
	}

	public void setChangeType(ChangeTypeDTO changeType) {
		this.changeType = changeType;
	}

	public ChangeItemDTO getChangeItem() {
		return changeItem;
	}

	public String getEmployeeName() {
		if (changeItem.getEmployee() != null) {
			return changeItem.getEmployee().getLabel();
		} else {
			return changeItem.getMissingEmployeeName() + " (Neu)";
		}
	}

	public void setChangeItem(ChangeItemDTO changeItem) {
		this.changeItem = changeItem;
	}

	public ProcessType getProcessType() {
		return processType;
	}

	public void setProcessType(ProcessType processType) {
		this.processType = processType;
	}

	public Boolean isManual() {
		return processType == null ? false : processType.equals(ProcessType.Manual);
	}

	public void setManual(Boolean bool) {
		if(bool){
			processType = ProcessType.Manual;
		} else {
			processType = ProcessType.NotProcessed;
		}		
	}
	
	public Boolean isAutomatic() {
		return processType == null ? false : processType.equals(ProcessType.Automatic);
	}

	public void setAutomatic(Boolean bool) {
		if(bool){
			processType = ProcessType.Automatic;
		} else {
			processType = ProcessType.NotProcessed;
		}		
	}
	
	public Boolean isNotNeeded() {
		return processType == null ? false : processType.equals(ProcessType.NotNeeded);
	}

	public void setNotNeeded(Boolean bool) {
		if(bool){
			processType = ProcessType.NotNeeded;
		} else {
			processType = ProcessType.NotProcessed;
		}		
	}

	public Boolean getApplied() {
    	return applied;
    }

	public void setApplied(Boolean applied) {
    	this.applied = applied;
    }

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getOldValue() {
		String oldValue = "";
		String prefix = "";
		switch (changeType.getId()) {
		case 0:
			prefix = "Telefon Nr.: ";
			oldValue = changeItem.getOldTelefonLabel();
			break;
		case 1:
			for (AssetDTO asset : changeItem.getOldAssets()) {
				if (!oldValue.equals("")) {
					oldValue += ", ";
				}
				oldValue += asset.getName();
			}
			if (oldValue.equals("")) {
				oldValue = "-";
			}
			break;
		case 2:
			for (OccupationDTO occupation : changeItem.getOldOccupations()) {
				if (!oldValue.equals("")) {
					oldValue += ", ";
				}
				oldValue += occupation.getName();
			}
			if (oldValue.equals("")) {
				oldValue = "-";
			}
			break;
		}

		return prefix + oldValue;
	}

	public String getNewValue() {
		String newValue = "";
		String prefix = "";
		switch (changeType.getId()) {
		case 0:
			prefix = "Telefon Nr.: ";
			newValue = changeItem.getNewTelefonLabel();
			break;
		case 1:
			for (AssetDTO asset : changeItem.getNewAssets()) {
				if (!newValue.equals("")) {
					newValue += ", ";
				}
				newValue += asset.getName();
			}
			for (String assetName : changeItem.getMissingAssetsNames()) {
				if (!newValue.equals("")) {
					newValue += ", ";
				}
				newValue += assetName + " (Neu)";
			}
			if (newValue.equals("")) {
				newValue = "-";
			}
			break;
		case 2:
			for (OccupationDTO occupation : changeItem.getNewOccupations()) {
				if (!newValue.equals("")) {
					newValue += ", ";
				}
				newValue += occupation.getName();
			}

			for (String occupationName : changeItem.getMissingOccupationsNames()) {
				if (!newValue.equals("")) {
					newValue += ", ";
				}
				newValue += occupationName + " (Neu)";
			}
			if (newValue.equals("")) {
				newValue = "-";
			}
			break;
		}

		return prefix + newValue;
	}

	public boolean getForceManual() {
		if (changeType.getId().equals(1)) {
			return changeItem.getMissingAssetsNames().size() != 0;
		} else {
			return false;
		}
	}

	@Override
	public String getClassName() {
		return "Änderung: " + getName();
	}

	@Override
	public Boolean isSlim() {
		return false;
	}

	public ChangeEventDTO getInstance() {
		return this;
	}

	@Override
	public ChangeEventDTO update(ChangeEventDTO toUpdate) {
		this.id = toUpdate.id;
		this.domainId = toUpdate.domainId;
		this.changeType = toUpdate.changeType;
		this.changeItem = toUpdate.changeItem;
		this.processType = toUpdate.processType;
		this.applied = toUpdate.applied;
		this.date = toUpdate.date;
		return this;
	}

	public boolean notProcessed() {
	    return !(isManual() || isAutomatic() || isNotNeeded());
    }

}
