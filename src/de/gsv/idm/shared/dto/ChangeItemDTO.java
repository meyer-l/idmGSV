package de.gsv.idm.shared.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
public class ChangeItemDTO extends GeneralDTO<ChangeItemDTO> implements Serializable {

	private Integer id;
	private Integer domainId;
	private Date createTime;
	private EmployeeDTO employee;
	private String missingEmployeeName;
	private List<AssetDTO> oldAssets;
	private List<AssetDTO> newAssets;
	private List<String> missingAssetsNames;
	private List<OccupationDTO> oldOccupations;
	private List<OccupationDTO> newOccupations;
	private List<String> missingOccupationsNames;
	private String oldTelefon;
	private String newTelefon;

	public ChangeItemDTO() {

	}

	public ChangeItemDTO(Integer id, Integer domainId, Date createTime, EmployeeDTO employee,
	        String missingEmployeeName, List<AssetDTO> oldAssets, List<AssetDTO> newAssets,
	        List<String> missingAssetsNames, List<OccupationDTO> oldOccupations,
	        List<OccupationDTO> newOccupations, List<String> missingOccupationsNames,
	        String oldTelefon, String newTelefon) {
		this.id = id;
		this.domainId = domainId;
		this.createTime = createTime;
		this.employee = employee;
		this.missingEmployeeName = missingEmployeeName;
		this.oldAssets = oldAssets;
		this.newAssets = newAssets;
		this.missingAssetsNames = missingAssetsNames;
		this.oldOccupations = oldOccupations;
		this.newOccupations = newOccupations;
		this.missingOccupationsNames = missingOccupationsNames;
		this.oldTelefon = oldTelefon;
		this.newTelefon = newTelefon;
	}

	@Override
	public String getName() {
		return "Änderung: " + employee.getName();
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public EmployeeDTO getEmployee() {
		return employee;
	}

	public void setEmployee(EmployeeDTO employee) {
		this.employee = employee;
	}

	public String getMissingEmployeeName() {
		return missingEmployeeName;
	}

	public void setMissingEmployeeName(String missingEmployeeName) {
		this.missingEmployeeName = missingEmployeeName;
	}

	public List<AssetDTO> getOldAssets() {
		return oldAssets;
	}

	public void setOldAssets(List<AssetDTO> oldAssets) {
		this.oldAssets = oldAssets;
	}

	public List<AssetDTO> getNewAssets() {
		return newAssets;
	}

	public void setNewAssets(List<AssetDTO> newAssets) {
		this.newAssets = newAssets;
	}

	public List<String> getMissingAssetsNames() {
		return missingAssetsNames;
	}

	public void setMissingAssetsNames(List<String> missingAssetsNames) {
		this.missingAssetsNames = missingAssetsNames;
	}

	public List<OccupationDTO> getOldOccupations() {
		return oldOccupations;
	}

	public void setOldOccupations(List<OccupationDTO> oldOccupations) {
		this.oldOccupations = oldOccupations;
	}

	public List<OccupationDTO> getNewOccupations() {
		return newOccupations;
	}

	public void setNewOccupations(List<OccupationDTO> newOccupations) {
		this.newOccupations = newOccupations;
	}

	public List<String> getMissingOccupationsNames() {
		return missingOccupationsNames;
	}

	public void setMissingOccupationsNames(List<String> missingOccupationsNames) {
		this.missingOccupationsNames = missingOccupationsNames;
	}

	public String getOldTelefon() {
		return oldTelefon;
	}

	public String getOldTelefonLabel() {
		if (employee == null || employee.getTelefon() == null || employee.getTelefon().isEmpty()
		        || employee.getTelefon().equals(oldTelefon)) {
			if (oldTelefon == null || oldTelefon.equals("")) {
				return "-";
			} else {
				return oldTelefon;
			}

		} else {
			String telefonString = "-";
			if (oldTelefon != null && !oldTelefon.equals("")) {
				telefonString = oldTelefon;
			}
			return telefonString + " (" + "Aktuell: " + employee.getTelefon() + ")";
		}

	}

	public void setOldTelefon(String oldTelefon) {
		this.oldTelefon = oldTelefon;
	}

	public String getNewTelefon() {
		return newTelefon;
	}

	public String getNewTelefonLabel() {
		if (newTelefon == null || newTelefon.equals("")) {
			return "-";
		} else {
			return newTelefon;
		}
	}

	public void setNewTelefon(String newTelefon) {
		this.newTelefon = newTelefon;
	}

	@Override
	public String getClassName() {
		return "Änderungsdaten";
	}

	@Override
	public Boolean isSlim() {
		return false;
	}

	@Override
	public ChangeItemDTO update(ChangeItemDTO toUpdate) {
		this.id = toUpdate.id;
		this.domainId = toUpdate.domainId;
		this.createTime = toUpdate.createTime;
		this.employee = toUpdate.employee;
		this.missingEmployeeName = toUpdate.missingEmployeeName;
		this.oldAssets = toUpdate.oldAssets;
		this.newAssets = toUpdate.newAssets;
		this.missingAssetsNames = toUpdate.missingAssetsNames;
		this.oldOccupations = toUpdate.oldOccupations;
		this.newOccupations = toUpdate.newOccupations;
		this.missingOccupationsNames = toUpdate.missingOccupationsNames;
		this.oldTelefon = toUpdate.oldTelefon;
		this.newTelefon = toUpdate.newTelefon;
		return toUpdate;
	}

}
