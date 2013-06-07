package de.gsv.idm.server.push;

import java.util.ArrayList;
import java.util.List;

import de.gsv.idm.shared.push.event.PushEvent;
import de.novanic.eventservice.client.event.Event;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.service.RemoteEventServiceServlet;

@SuppressWarnings("serial")
public class PushUpdateHandler extends RemoteEventServiceServlet {
	private static PushUpdateHandler instance;
	private static List<PushHandlerData> toPushList;

	static synchronized public PushUpdateHandler getInstance() {
		if (instance == null) {
			instance = new PushUpdateHandler();
		}
		return instance;
	}

	public PushUpdateHandler() {
		toPushList = new ArrayList<PushHandlerData>();
		(new Thread(new PushUpdateSender(this))).start();
	}

	@Override
	public void addEvent(Domain eventDomain, Event event) {
		if (event instanceof PushEvent) {
			addPushEvent(eventDomain, (PushEvent<?, ?>) event);
		} else {
			super.addEvent(eventDomain, event);
		}
	}

	public void addPushEvent(de.novanic.eventservice.client.event.domain.Domain eventDomain,
	        PushEvent<?, ?> event) {
		synchronized (this) {
			toPushList.add(new PushHandlerData(eventDomain, event));
		}
	}

	private void sendPushEvent(PushHandlerData data) {
		super.addEvent(data.getEventDomain(), data.getEvent());
	}

	public void flush() {
		Integer dataToPush = 1;
		Integer i = 0;

		List<PushHandlerData> sendedData = new ArrayList<PushHandlerData>();
		synchronized (this) {
			for (PushHandlerData data : toPushList) {
				if (i >= dataToPush) {
					break;
				}
				sendPushEvent(data);
				sendedData.add(data);
				i++;
			}
			toPushList.removeAll(sendedData);
		}
	}
}
