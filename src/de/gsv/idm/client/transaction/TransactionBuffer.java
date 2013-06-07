package de.gsv.idm.client.transaction;

import java.util.LinkedList;

@SuppressWarnings("serial")
public class TransactionBuffer extends LinkedList<DBTransactionEvent> {
	private Integer limit;

	public TransactionBuffer(Integer limit) {
		this.limit =  limit;
	}

	public void push(DBTransactionEvent event) {
		this.addLast(event);
		while (size() > limit) { super.remove(); }
	}

	public DBTransactionEvent pop() {
		return this.removeLast();
	}
	
	public DBTransactionEvent peekHead() {
		return this.getLast();
	}
	
}
