package de.gsv.idm.shared.dto;


//XXX: do i need the comparable<T> or is comparable GeneralDTO good too?
public abstract class GeneralDTO<T> implements HasName, HasId, Comparable<T>, Cloneable {

	@Override
    public int hashCode() {
		Integer id = getId();
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((id == null) ? 0 : id.hashCode());
	    return result;
    }
	

	@Override
    public boolean equals(Object other) {
		if (other == null)
		    return false;
	    if (this == other)
		    return true;
	    if (getClass() != other.getClass())
		    return false;
	    if (getId() == null) {
		    if (((HasId) other).getId() != null)
			    return false;
	    } else if (!getId().equals(((HasId) other).getId()))
		    return false;
	    return true;
    }	

	public String getTreeKey() {
		return getId().toString();
	}

	public int compareTo(T other) {
		return this.getId().compareTo(((HasId) other).getId());
	}

	public String getLabel() {
		return getName();
	}

	public String getTreeToListLabel() {
		return getLabel();
	}	

	abstract public String getClassName();

	abstract public Boolean isSlim();

	abstract public T update(T toUpdate);

}
