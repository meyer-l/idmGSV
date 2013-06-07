package de.gsv.idm.shared.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ChangeAppliedDTO extends GeneralDTO<ChangeAppliedDTO> implements Serializable {
	
	@Override
    public boolean equals(Object obj) {
	    if (this == obj)
		    return true;
	    if (getClass() != obj.getClass())
		    return false;
	    ChangeAppliedDTO other = (ChangeAppliedDTO) obj;
	    if (id == null) {
		    if (other.id != null)
			    return false;
	    } else if (!id.equals(other.id))
		    return false;
	    if (object == null) {
		    if (other.object != null)
			    return false;
	    } else if (!object.equals(other.object))
		    return false;
	    if (!status.equals(other.status))
		    return false;
	    return true;
    }

	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = super.hashCode();
	    result = prime * result + ((id == null) ? 0 : id.hashCode());
	    result = prime * result + ((object == null) ? 0 : object.hashCode());
	    result = prime * result + ((status == null) ? 0 : status.hashCode());
	    return result;
    }


	public enum ChangeStatus {
		created, updated, notApplied
	}
	Integer id;
	GeneralDTO<?> object;
	ChangeStatus status;

	
	
	public ChangeAppliedDTO() {

	}

	public ChangeAppliedDTO(GeneralDTO<?> object, ChangeStatus status) {
		this.object = object;
		this.status = status;
	}

	@Override
	public String getName() {
		return object.getLabel();
	}

	@Override
	public Integer getId() {
		return object.getId();
	}

	public String getObjectClassName() {
		return object.getClassName();
	}

	@Override
	public String getClassName() {
		return "Angewandte Änderung";
	}

	public GeneralDTO<?> getObject() {
		return object;
	}

	public void setObject(GeneralDTO<?> object) {
		this.object = object;
	}

	public ChangeStatus getStatus() {
		return status;
	}

	public String getStatusLabel() {
		String result = "Nicht angewendet";
		if (status.equals(ChangeStatus.created)) {
			result = "Erstellt";
		} else if (status.equals(ChangeStatus.updated)) {
			result = "Aktualisiert";
		}
		return result;
	}

	public void setStatus(ChangeStatus status) {
		this.status = status;
	}

	@Override
	public Boolean isSlim() {
		return false;
	}

	@Override
	public ChangeAppliedDTO update(ChangeAppliedDTO toUpdate) {
		this.object = toUpdate.getObject();
		this.status = toUpdate.getStatus();
		return toUpdate;
	}

}
