package de.gsv.idm.client.event.db;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.transaction.DBTransactionEvent;

public class TransactionBufferEvent extends ObjectEvent<DBTransactionEvent,TransactionBufferEvent> {
	
	public TransactionBufferEvent(DBTransactionEvent event) {
		super(event);
	}
	
	public static Type<GeneralEventHandler<TransactionBufferEvent>> TYPE =
			new Type<GeneralEventHandler<TransactionBufferEvent>>();
	@Override
	public Type<GeneralEventHandler<TransactionBufferEvent>> getAssociatedType() {
		return TYPE;
	}
}
