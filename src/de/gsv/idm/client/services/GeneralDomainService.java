package de.gsv.idm.client.services;

import java.util.ArrayList;

public interface GeneralDomainService<T> extends GeneralService<T> {
	ArrayList<T> getAll(Integer domain_id, String sessionId);
}
