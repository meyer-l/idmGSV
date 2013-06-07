package de.gsv.idm.server.service;

import java.util.List;

import de.gsv.idm.client.services.MeasureService;
import de.gsv.idm.server.general.GeneralServiceImpl;
import de.gsv.idm.shared.dto.MeasureDTO;
import de.gsv.idm.shared.model.Measure;
import de.gsv.idm.shared.push.event.PushEvent;
import de.gsv.idm.shared.push.event.measure.MeasureCreatedPushEvent;
import de.gsv.idm.shared.push.event.measure.MeasureDeletedPushEvent;
import de.gsv.idm.shared.push.event.measure.MeasurePushEvent;
import de.gsv.idm.shared.push.event.measure.MeasureUpdatedPushEvent;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;

@SuppressWarnings("serial")
public class MeasureServiceImpl extends GeneralServiceImpl<MeasureDTO, Measure> implements MeasureService {

	 private static final de.novanic.eventservice.client.event.domain.Domain CONVERSATION_DOMAIN;
	 static {
	        CONVERSATION_DOMAIN = DomainFactory.getDomain(MeasurePushEvent.CONVERSATION_DOMAIN);
	    }
	@Override
    protected List<Measure> findAll() {
	   return Measure.findAll();
    }
	@Override
    protected Measure findById(Integer id) {
	   return Measure.findById(id);
    }
	@Override
    protected Measure createObject() {
		return Measure.createIt();
    }
	@Override
    protected Domain getConversationDomain() {
	    return CONVERSATION_DOMAIN;
    }
	@Override
    protected PushEvent<?, ?> getUpdatedPushEvent(MeasureDTO updated) {
	    return new MeasureUpdatedPushEvent(updated);
    }
	@Override
    protected PushEvent<?, ?> getCreatedPushEvent(MeasureDTO updated) {
		return new MeasureCreatedPushEvent(updated);
    }
	@Override
    protected PushEvent<?, ?> getDeletedPushEvent(MeasureDTO updated) {
		return new MeasureDeletedPushEvent(updated);
    }
}
