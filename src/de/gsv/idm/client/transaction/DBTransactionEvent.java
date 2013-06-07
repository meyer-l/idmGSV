package de.gsv.idm.client.transaction;

public interface DBTransactionEvent {	
	public void rollback();	
	public void applyTransaction();
	public String getType();
	public String getString();
    
}
