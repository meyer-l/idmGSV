package de.gsv.idm.client.push;

import java.util.List;

import de.gsv.idm.client.push.listener.AssetLinkListenerAdapter;
import de.gsv.idm.client.push.listener.AssetListenerAdapter;
import de.gsv.idm.client.push.listener.AssettypeCategoryListenerAdapter;
import de.gsv.idm.client.push.listener.AssettypeListenerAdapter;
import de.gsv.idm.client.push.listener.ChangeEventListenerAdapter;
import de.gsv.idm.client.push.listener.DomainListenerAdapter;
import de.gsv.idm.client.push.listener.EmployeeListenerAdapter;
import de.gsv.idm.client.push.listener.InformationListenerAdapter;
import de.gsv.idm.client.push.listener.MeasureListenerAdapter;
import de.gsv.idm.client.push.listener.ModuleListenerAdapter;
import de.gsv.idm.client.push.listener.OccupationListenerAdapter;
import de.gsv.idm.client.push.listener.SecurityzoneListenerAdapter;
import de.gsv.idm.client.push.listener.ThreatListenerAdapter;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssetLinkDTO;
import de.gsv.idm.shared.dto.AssettypeCategoryDTO;
import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.dto.ChangeEventDTO;
import de.gsv.idm.shared.dto.DomainDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.InformationDTO;
import de.gsv.idm.shared.dto.MeasureDTO;
import de.gsv.idm.shared.dto.ModuleDTO;
import de.gsv.idm.shared.dto.OccupationDTO;
import de.gsv.idm.shared.dto.SecurityzoneDTO;
import de.gsv.idm.shared.dto.ThreatDTO;

public class AssociatedObjectsProcessor {

	public AssociatedObjectsProcessor(List<GeneralDTO<?>> objectList) {
		for (GeneralDTO<?> object : objectList) {
			if (object instanceof AssetLinkDTO) {
				AssetLinkListenerAdapter.getInstance().fireUpdatedEvent((AssetLinkDTO) object);
			} else if (object instanceof AssetDTO) {
				AssetListenerAdapter.getInstance().fireUpdatedEvent((AssetDTO) object);
			} else if (object instanceof AssettypeCategoryDTO) {
				AssettypeCategoryListenerAdapter.getInstance().fireUpdatedEvent(
				        (AssettypeCategoryDTO) object);
			} else if (object instanceof AssettypeDTO) {
				AssettypeListenerAdapter.getInstance().fireUpdatedEvent((AssettypeDTO) object);
			} else if (object instanceof ChangeEventDTO) {
				ChangeEventListenerAdapter.getInstance().fireUpdatedEvent((ChangeEventDTO) object);
			} else if (object instanceof DomainDTO) {
				DomainListenerAdapter.getInstance().fireUpdatedEvent((DomainDTO) object);
			} else if (object instanceof EmployeeDTO) {
				EmployeeListenerAdapter.getInstance().fireUpdatedEvent((EmployeeDTO) object);
			} else if (object instanceof InformationDTO) {
				InformationListenerAdapter.getInstance().fireUpdatedEvent((InformationDTO) object);
			} else if (object instanceof MeasureDTO) {
				MeasureListenerAdapter.getInstance().fireUpdatedEvent((MeasureDTO) object);
			} else if (object instanceof ModuleDTO) {
				ModuleListenerAdapter.getInstance().fireUpdatedEvent((ModuleDTO) object);
			} else if (object instanceof OccupationDTO) {
				OccupationListenerAdapter.getInstance().fireUpdatedEvent((OccupationDTO) object);
			} else if (object instanceof SecurityzoneDTO) {
				SecurityzoneListenerAdapter.getInstance()
				        .fireUpdatedEvent((SecurityzoneDTO) object);
			} else if (object instanceof ThreatDTO) {
				ThreatListenerAdapter.getInstance().fireUpdatedEvent((ThreatDTO) object);
			}
		}
	}
}
