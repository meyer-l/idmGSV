package de.gsv.idm.client.push;

import de.novanic.eventservice.client.event.listener.RemoteEventListener;

/*
 *  Create, Update, Delete-Listener
 */
public interface CUDListener<T> extends RemoteEventListener {
    void onCreated(T createdEvent);
    void onDeleted(T deletedEvent);
    void onUpdated(T updatedEvent);
}
