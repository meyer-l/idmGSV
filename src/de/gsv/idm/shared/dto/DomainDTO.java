package de.gsv.idm.shared.dto;

import java.io.Serializable;


@SuppressWarnings("serial")
public class DomainDTO extends GeneralDTO<DomainDTO> implements Serializable {

	private String name;
	private String ident;
	private Integer id;
	private Boolean slim = false;
	
	public DomainDTO() {
		
	}
		
	public DomainDTO(int id, String domainName, String domainIdent) {
		this.id = id;
		this.name = domainName;
		this.ident = domainIdent;
	}

	public DomainDTO(Integer id, String name) {
		this.id = id;
		this.name = name;
		slim = true;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String domainName) {
		this.name = domainName;
	}
	
	public String getIdent() {
		return ident;
	}
	
	public void setIdent(String domainIdent) {
		this.ident = domainIdent;
	}
	
	public Integer getId(){
		return id;
	}
	
	public void setId(Integer id){
		this.id = id;
	}
	
	public DomainDTO update(DomainDTO domain){
		this.name = domain.name;
		this.ident = domain.ident;
		
		this.slim = domain.slim;
		return this;
	}
	
	public DomainDTO clone() {
		DomainDTO result = new DomainDTO();
		if(id != null) {
			result.id = this.id;
			result.name = this.name;
			result.ident = this.ident;
		}
		
		return result;
	}

	@Override
	public String getClassName() {
		return "Informationsverbund";
	}

	@Override
    public Boolean isSlim() {
	    return slim;
    }
}
