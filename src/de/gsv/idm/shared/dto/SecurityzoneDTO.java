package de.gsv.idm.shared.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class SecurityzoneDTO extends GeneralDTO<SecurityzoneDTO> implements Serializable {

	private Integer id;
	private Integer orderNumber;
	private String name;
	private List<MeasureDTO> measures;
	private SecurityzoneDTO child;
	private Boolean slim = false;

	public SecurityzoneDTO() {
	}

	public SecurityzoneDTO(Integer id, String name) {
		this.id = id;
		this.name = name;
		measures = new ArrayList<MeasureDTO>();
	}

	public SecurityzoneDTO(Integer id, String name, Integer orderNumber) {
		this.id = id;
		this.name = name;
		this.orderNumber = orderNumber;
		this.slim = true;
		measures = new ArrayList<MeasureDTO>();
	}

	public SecurityzoneDTO(Integer id, Integer orderNumber, String name, SecurityzoneDTO child,
	        List<MeasureDTO> measures) {
		this.id = id;
		this.orderNumber = orderNumber;
		this.name = name;
		this.measures = measures;
		this.child = child;
		this.slim = false;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer order_number) {
		this.orderNumber = order_number;
	}

	public String getOrderNumberString() {
		if(orderNumber != null){
			return orderNumber.toString();
		} else {
			return "";
		}
		
	}

	public SecurityzoneDTO getChild() {
		return child;
	}
	
	public String getChildName() {
		if(child != null){
			return child.getName();
		} else {
			return "";
		}
		
	}

	public void setChild(SecurityzoneDTO child) {
		this.child = child;
	}

	public List<MeasureDTO> getMeasures() {
		return measures;
	}

	public void setMeasures(List<MeasureDTO> measures) {
		this.measures = measures;
	}

	public List<MeasureDTO> getInheritedMeasures() {
		if (child != null) {
			return child.getAllMeasures();
		} else {
			return new ArrayList<MeasureDTO>();
		}
	}

	public List<MeasureDTO> getAllMeasures() {
		List<MeasureDTO> result = new ArrayList<MeasureDTO>(getInheritedMeasures());
		result.addAll(measures);

		return result;
	}

	@Override
	public String getClassName() {
		return "Schutzzone";
	}

	@Override
	public Boolean isSlim() {
		return slim;
	}

	@Override
	public SecurityzoneDTO update(SecurityzoneDTO toUpdate) {
		this.name = toUpdate.name;
		this.measures = toUpdate.measures;
		this.orderNumber = toUpdate.orderNumber;
		this.child = toUpdate.child;

		this.slim = toUpdate.slim;

		return this;
	}

	public SecurityzoneDTO clone() {
		SecurityzoneDTO cloned = new SecurityzoneDTO();
		cloned.setId(getId());
		cloned.setName(getName());
		cloned.setOrderNumber(getOrderNumber());
		cloned.setChild(getChild());

		cloned.slim = isSlim();

		List<MeasureDTO> clonedMeasures = new ArrayList<MeasureDTO>();
		for (MeasureDTO measure : measures) {
			clonedMeasures.add(measure);
		}
		cloned.setMeasures(clonedMeasures);

		return cloned;
	}

}
