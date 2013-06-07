package de.gsv.idm.shared.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("serial")
public class AssetDTO extends GeneralDTO<AssetDTO> implements Serializable {

	private AssettypeDTO assettype;
	private Integer id;
	private Integer domainId;
	private String identifier;
	private String status;
	private AssetDTO parent;
	private EmployeeDTO responsiblePerson;
	private List<EmployeeDTO> associatedPersons;

	private List<AssetDTO> children;
	private List<AssetLinkDTO> linkedChildren;
	private List<AssetModuleLinkDTO> moduleLinks;
	private List<AssetMeasureLinkDTO> measureLinks;

	private boolean slim = false;

	public AssetDTO() {

	}

	public AssetDTO(Integer id, Integer domainId) {
		this.id = id;
		this.domainId = domainId;
		this.responsiblePerson = null;
		this.parent = null;

		this.children = new ArrayList<AssetDTO>();
		this.associatedPersons = new ArrayList<EmployeeDTO>();
		this.moduleLinks = new ArrayList<AssetModuleLinkDTO>();
		this.measureLinks = new ArrayList<AssetMeasureLinkDTO>();
		this.linkedChildren = new ArrayList<AssetLinkDTO>();
		slim = true;
	}

	public AssetDTO(Integer id, Integer domainId, AssettypeDTO asset) {
		this.assettype = asset;
		this.id = id;
		this.domainId = domainId;
		this.parent = null;
		this.responsiblePerson = null;

		this.children = new ArrayList<AssetDTO>();
		this.linkedChildren = new ArrayList<AssetLinkDTO>();
		this.associatedPersons = new ArrayList<EmployeeDTO>();
		this.moduleLinks = new ArrayList<AssetModuleLinkDTO>();
		this.measureLinks = new ArrayList<AssetMeasureLinkDTO>();
	}

	public AssetDTO(Integer id, Integer domainId, AssettypeDTO asset, String identifier,
	        String status, AssetDTO parent, EmployeeDTO responsiblePerson, List<AssetDTO> children,
	        List<AssetLinkDTO> linkedChildren, List<EmployeeDTO> associatedPersons,
	        List<AssetModuleLinkDTO> modules, List<AssetMeasureLinkDTO> measures) {
		this.assettype = asset;
		this.id = id;
		this.domainId = domainId;
		this.parent = parent;
		this.responsiblePerson = responsiblePerson;
		this.identifier = identifier;
		this.status = status;
		this.slim = false;

		this.associatedPersons = associatedPersons;
		this.children = children;
		this.linkedChildren = linkedChildren;
		this.moduleLinks = modules;
		this.measureLinks = measures;
	}

	public AssetDTO getParent() {
		return parent;
	}

	public void setParent(AssetDTO parent) {
		this.parent = parent;
	}

	public String getParentLabel() {
		if (getParent() != null) {
			return parent.getLabel();
		} else {
			return "Wurzelknoten";
		}
	}

	public void setAssettype(AssettypeDTO asset) {
		this.assettype = asset;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AssettypeDTO getAssettype() {
		return assettype;
	}

	public String getAssettypeName() {
		if (assettype != null) {
			return assettype.getName();
		}
		return "";
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public String getTreeKey() {
		return "asset" + id;
	}

	public List<AssetDTO> getChildren() {
		return children;
	}

	public void setChildren(List<AssetDTO> children) {
		this.children = children;
	}

	public List<AssetModuleLinkDTO> getModuleLinks() {
		return moduleLinks;
	}

	public List<AssetLinkDTO> getLinkedAssets() {
		return linkedChildren;
	}

	public void setLinkedChildren(List<AssetLinkDTO> linkedChildren) {
		this.linkedChildren = linkedChildren;
	}

	public void setModuleLinks(List<AssetModuleLinkDTO> modules) {
		this.moduleLinks = modules;
	}

	public List<AssetMeasureLinkDTO> getMeasureLinks() {
		return measureLinks;
	}

	public List<AssetMeasureLinkDTO> getMeasureLinksWithoutInherited() {
		List<AssetMeasureLinkDTO> measureLinksWithoutInherited = getMeasureLinks();
		measureLinksWithoutInherited.removeAll(getInheritedMeasures());
		return measureLinksWithoutInherited;
	}

	public List<AssetMeasureLinkDTO> getInheritedMeasures() {
		List<AssetMeasureLinkDTO> inheritedMeasureLinks = new ArrayList<AssetMeasureLinkDTO>();
		if (parent != null) {
			inheritedMeasureLinks = parent.getInheritedMeasures();
			for (AssetMeasureLinkDTO link : parent.getMeasureLinks()) {
				if (link.getAssettypeMeasureLink() != null
				        && link.getAssettypeMeasureLink().isPassOn() || link.getPassOn()) {
					inheritedMeasureLinks.add(link);
				}
			}
		}
		return inheritedMeasureLinks;
	}

	public void setMeasureLinks(List<AssetMeasureLinkDTO> measures) {
		this.measureLinks = measures;
	}

	public List<AssetMeasureLinkDTO> getAllMeasureLinks() {
		List<AssetMeasureLinkDTO> result = new ArrayList<AssetMeasureLinkDTO>();
		result.addAll(getMeasureLinksWithoutInherited());
		result.addAll(getInheritedMeasures());
		return result;
	}

	public String getName() {
		if (assettype == null && (identifier == null || identifier.equals(""))) {
			return "";
		} else {
			String label = "";
			if (identifier != null && !identifier.equals("")) {
				label = identifier + " - ";
			}
			label += assettype.getName();
			return label;
		}
	}

	public GeneralDTO<?> getSecurityAssesmentSource() {
		GeneralDTO<?> result = this;
		SecurityLevelDTO resultLevel = SecurityLevelDTO.getDefaultSecurityLevel();

		if (assettype != null && assettype.isManuelSecurityAssesment()) {
			if (assettype.getAvailability() != null
			        && assettype.getAvailability().getId() > resultLevel.getId()) {
				result = assettype;
				resultLevel = assettype.getAvailability();
			}
			if (assettype.getCalculatedConfidentiality() != null
			        && assettype.getCalculatedConfidentiality().getId() > resultLevel.getId()) {
				result = assettype;
				resultLevel = assettype.getCalculatedConfidentiality();
			}
			if (assettype.getIntegrity() != null
			        && assettype.getIntegrity().getId() > resultLevel.getId()) {
				result = assettype;
				resultLevel = assettype.getIntegrity();
			}
		}

		for (EmployeeDTO employee : associatedPersons) {
			if (employee.getCalculatedSecurityAssesment() != null
			        && employee.getCalculatedSecurityAssesment().getId() > resultLevel.getId()) {
				result = employee;
				resultLevel = employee.getCalculatedSecurityAssesment();
			}
		}

		for (AssetDTO asset : children) {
			SecurityLevelDTO assetLevel = asset.getCalculatedSecurityAssesment();
			if (assetLevel != null && assetLevel.getId() > resultLevel.getId()) {
				result = asset;
				resultLevel = assetLevel;
			}
		}

		for (AssetLinkDTO assetLink : linkedChildren) {
			SecurityLevelDTO assetLinkLevel = assetLink.getAsset().getCalculatedSecurityAssesment();
			if (assetLinkLevel != null && assetLinkLevel.getId() > resultLevel.getId()) {
				result = assetLink;
				resultLevel = assetLinkLevel;
			}
		}

		return result;
	}

	public String getSecurityAssesmentSourceName() {
		GeneralDTO<?> sourceElement = getSecurityAssesmentSource();
		String result = null;

		if (sourceElement instanceof AssettypeDTO) {
			result = "Asset-Grundtyp: ";
		} else if (sourceElement instanceof EmployeeDTO) {
			result = "Mitarbeiter: ";
		} else if (sourceElement instanceof AssetDTO) {
			result = "Kind-Asset: ";
		} else if (sourceElement instanceof AssetLinkDTO) {
			result = "Asset-Verknüpfung: ";
		}
		if (sourceElement.equals(this)) {
			result = "Selbst";
		} else {
			result += sourceElement.getLabel();
		}
		return result;
	}

	public SecurityLevelDTO getCalculatedAvailability() {
		SecurityLevelDTO result = SecurityLevelDTO.getDefaultSecurityLevel();
		if (assettype != null && assettype.isManuelSecurityAssesment()) {
			result = assettype.getAvailability();
		}

		for (EmployeeDTO employee : getInheritedAssociatedPersons()) {
			if (employee.getCalculatedAvailability() != null
			        && employee.getCalculatedAvailability().getId() > result.getId()) {
				result = employee.getCalculatedAvailability();
			}
		}

		return result;
	}

	public String getCalculatedAvailabilityName() {
		return getCalculatedAvailability().getName();
	}

	public SecurityLevelDTO getCalculatedConfidentiality() {
		SecurityLevelDTO result = SecurityLevelDTO.getDefaultSecurityLevel();
		if (assettype != null && assettype.isManuelSecurityAssesment()) {
			result = assettype.getCalculatedConfidentiality();
		}

		for (EmployeeDTO employee : getInheritedAssociatedPersons()) {
			if (employee.getCalculatedConfidentiality() != null
			        && employee.getCalculatedConfidentiality().getId() > result.getId()) {
				result = employee.getCalculatedConfidentiality();
			}
		}

		return result;
	}

	public String getCalculatedConfidentialityName() {
		return getCalculatedConfidentiality().getName();
	}

	public SecurityLevelDTO getCalculatedIntegrity() {
		SecurityLevelDTO result = SecurityLevelDTO.getDefaultSecurityLevel();
		if (assettype != null && assettype.isManuelSecurityAssesment()) {
			result = assettype.getIntegrity();
		}

		for (EmployeeDTO employee : getInheritedAssociatedPersons()) {
			if (employee.getCalculatedIntegrity() != null
			        && employee.getCalculatedIntegrity().getId() > result.getId()) {
				result = employee.getCalculatedIntegrity();
			}
		}

		return result;
	}

	public String getCalculatedIntegrityName() {
		return getCalculatedIntegrity().getName();
	}

	public SecurityLevelDTO getCalculatedSecurityAssesment() {
		return getCalculatedSecurityAssesment(new HashMap<AssetDTO, SecurityLevelDTO>());
	}

	public SecurityLevelDTO getCalculatedSecurityAssesment(
	        HashMap<AssetDTO, SecurityLevelDTO> visitedNodes) {
		SecurityLevelDTO result = getCalculatedAvailability();

		if (getCalculatedConfidentiality().getId() > result.getId()) {
			result = getCalculatedConfidentiality();
		}

		if (getCalculatedIntegrity().getId() > result.getId()) {
			result = getCalculatedIntegrity();
		}

		visitedNodes.put(this, result);

		for (AssetDTO child : children) {
			SecurityLevelDTO childSecurityAssesment = null;
			if (visitedNodes.containsKey(child)) {
				childSecurityAssesment = visitedNodes.get(child);
			} else {
				if (child.getAssettype() != null
				        && child.getAssettype().isPropagateSecurityAssesment()) {
					childSecurityAssesment = child.getCalculatedSecurityAssesment(visitedNodes);
				}

			}
			if (childSecurityAssesment != null && childSecurityAssesment.getId() > result.getId()) {
				result = childSecurityAssesment;
			}
		}

		for (AssetLinkDTO link : linkedChildren) {
			SecurityLevelDTO linkSecurityLevel = link.getSecurityAssesment(visitedNodes);
			if (linkSecurityLevel != null && linkSecurityLevel.getId() > result.getId()) {
				result = linkSecurityLevel;
			}
		}

		return result;
	}

	public String getCalculatedSecurityAssesmentName() {
		return getCalculatedSecurityAssesment().getName();
	}

	public SecurityLevelDTO getAssettypeSecurityAssesment() {
		SecurityLevelDTO result = SecurityLevelDTO.getDefaultSecurityLevel();

		if (assettype != null && assettype.isManuelSecurityAssesment()) {
			if (assettype.getIntegrity() != null
			        && assettype.getIntegrity().getId() > result.getId()) {
				result = assettype.getIntegrity();
			}
			if (assettype.getAvailability() != null
			        && assettype.getAvailability().getId() > result.getId()) {
				result = assettype.getAvailability();
			}
			if (assettype.getCalculatedConfidentiality() != null
			        && assettype.getCalculatedConfidentiality().getId() > result.getId()) {
				result = assettype.getCalculatedConfidentiality();
			}
		}

		return result;
	}

	@Override
	public String getLabel() {
		String label = getName();
		if (!isSlim()) {
			label += " (" + getCalculatedSecurityAssesment().getName();
			if (getAssignedSecurityzone() != null) {
				label += " | " + getAssignedSecurityzoneName();
			}
			label += ")";
		}
		return label;
	}

	public String getTreeToListLabel() {
		return getLabel();
	}

	public void setName(String name) {
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getStatus() {
		if (!status.isEmpty() || assettype == null) {
			return status;
		} else {
			return assettype.getStatus();
		}
	}

	public String getAssetStatus() {
		return status;
	}

	public String getAssettypeStatus() {
		if (assettype != null) {
			return assettype.getStatus();
		}
		return "";
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setAssetStatus(String status) {
		this.status = status;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public EmployeeDTO getResponsiblePerson() {
		if (responsiblePerson != null || assettype == null) {
			return responsiblePerson;
		} else {
			return assettype.getResponsiblePerson();
		}
	}

	public EmployeeDTO getAssetResponsiblePerson() {
		return responsiblePerson;
	}

	public void setResponsiblePerson(EmployeeDTO responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}

	public String getAssettypeResponsiblePerson() {
		if (assettype.getResponsiblePerson() != null) {
			return assettype.getResponsiblePerson().getLabel();
		}
		return "";
	}

	public List<EmployeeDTO> getAssociatedPersons() {
		return associatedPersons;
	}

	public List<EmployeeDTO> getInheritedAssociatedPersons() {
		if (parent == null || associatedPersons.size() > 0) {
			return associatedPersons;
		} else {
			return parent.getInheritedAssociatedPersons();
		}

	}

	public void setAssociatedPersons(List<EmployeeDTO> associatedPersons) {
		this.associatedPersons = associatedPersons;
	}

	public SecurityzoneDTO getAssignedSecurityzone() {
		SecurityzoneDTO result = null;
		for (EmployeeDTO employee : getAssociatedPersons()) {
			SecurityzoneDTO employeeSecurityzone = employee.getAssignedSecurityzone();
			if (result == null) {
				result = employeeSecurityzone;
			}
			if (employeeSecurityzone != null
			        && result.getOrderNumber() < employeeSecurityzone.getOrderNumber()) {
				result = employeeSecurityzone;
			}
		}

		return result;
	}

	public String getAssignedSecurityzoneName() {
		SecurityzoneDTO securityzone = getAssignedSecurityzone();
		if (securityzone != null) {
			return securityzone.getName();
		} else {
			return "";
		}
	}

	@Override
	public String getClassName() {
		return "Asset";
	}

	@Override
	public Boolean isSlim() {
		return slim;
	}

	public Boolean isSecurityAssesmentValid() {
		if (assettype != null && assettype.isManuelSecurityAssesment()) {
			if (getCalculatedSecurityAssesment().getId() > getAssettypeSecurityAssesment().getId()) {
				return false;
			}
		}

		return true;
	}

	public AssetDTO clone() {
		AssetDTO clone = new AssetDTO();
		if (id != null) {
			clone.parent = parent;
			clone.id = id;
			clone.domainId = domainId;
			clone.assettype = assettype;
			clone.slim = slim;
			clone.identifier = identifier;
			clone.status = status;
			List<AssetDTO> clonedChildren = new ArrayList<AssetDTO>();
			for (AssetDTO model : children) {
				clonedChildren.add(model);
			}
			clone.children = clonedChildren;

			List<AssetLinkDTO> clonedlinkedChildren = new ArrayList<AssetLinkDTO>();
			for (AssetLinkDTO model : linkedChildren) {
				clonedlinkedChildren.add(model);
			}
			clone.linkedChildren = clonedlinkedChildren;

			List<AssetMeasureLinkDTO> clonedMeasureLinks = new ArrayList<AssetMeasureLinkDTO>();
			for (AssetMeasureLinkDTO model : measureLinks) {
				clonedMeasureLinks.add(model);
			}
			clone.measureLinks = clonedMeasureLinks;

			List<AssetModuleLinkDTO> clonedModuleLinks = new ArrayList<AssetModuleLinkDTO>();
			for (AssetModuleLinkDTO model : moduleLinks) {
				clonedModuleLinks.add(model);
			}
			clone.moduleLinks = clonedModuleLinks;

			clone.responsiblePerson = this.responsiblePerson;

			List<EmployeeDTO> clonedEmployee = new ArrayList<EmployeeDTO>();
			for (EmployeeDTO model : associatedPersons) {
				clonedEmployee.add(model);
			}
			clone.associatedPersons = clonedEmployee;
		}

		return clone;
	}

	@Override
	public AssetDTO update(AssetDTO toUpdate) {
		this.domainId = toUpdate.domainId;
		this.children = toUpdate.children;
		this.linkedChildren = toUpdate.linkedChildren;
		this.assettype = toUpdate.assettype;
		this.identifier = toUpdate.identifier;
		this.status = toUpdate.status;
		this.parent = toUpdate.parent;
		this.responsiblePerson = toUpdate.responsiblePerson;
		this.associatedPersons = toUpdate.associatedPersons;

		this.measureLinks = toUpdate.measureLinks;
		this.moduleLinks = toUpdate.moduleLinks;
		this.slim = toUpdate.slim;

		return this;
	}

	public Integer getDomainId() {
		return domainId;
	}

	public void setDomainId(Integer domainId) {
		this.domainId = domainId;
	}

	public List<MeasureDTO> getAllImplementedMeasures() {
		List<MeasureDTO> result = new ArrayList<MeasureDTO>();
		for (AssetMeasureLinkDTO measureLink : getAllMeasureLinks()) {
			if (measureLink.getStatus() != null
			        && measureLink.getStatus().equals(MeasureStatusObject.getImplemented())) {
				result.add(measureLink.getMeasure());
			}
		}
		return result;
	}

	public String getTreeIconName() {
		if (assettype != null) {
			return assettype.getTreeIconName();
		} else {
			return "";
		}
	}

	public static String getStaticClassName() {
		return "Asset";
	}

}
