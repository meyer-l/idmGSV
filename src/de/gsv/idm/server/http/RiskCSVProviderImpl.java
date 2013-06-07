package de.gsv.idm.server.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javalite.activejdbc.Base;

import de.gsv.idm.server.DBPropertiesProvider;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssetLinkDTO;
import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;
import de.gsv.idm.shared.dto.InformationDTO;
import de.gsv.idm.shared.dto.OccupationDTO;
import de.gsv.idm.shared.dto.SecurityLevelDTO;
import de.gsv.idm.shared.model.Asset;
import de.gsv.idm.shared.model.Assettype;
import de.gsv.idm.shared.model.Domain;
import de.gsv.idm.shared.model.Employee;
import de.gsv.idm.shared.model.Information;
import de.gsv.idm.shared.model.Occupation;
import de.gsv.idm.shared.model.User;

@SuppressWarnings("serial")
public class RiskCSVProviderImpl extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
	        IOException {
		Integer domainId = Integer.parseInt(req.getParameter("domainId"));
		Properties dbProperties = DBPropertiesProvider.getProperties(getServletContext());
		if (!Base.hasConnection()) {
			Base.open(dbProperties.getProperty("driver"), dbProperties.getProperty("url"),
			        dbProperties.getProperty("user"), dbProperties.getProperty("password"));
		}
		Domain domain = Domain.findById(domainId);
		String contentType;
		Integer status;
		if (User.sessionIdIsValid(req.getParameter("sessionId")) && domain != null) {
			String csvFile = "Name;Asset-Grundtyp;Schutzbedarf: Gesamt;Schutzbedarf: Verfügbarkeit;"
			        + "Schutzbedarf: Vertraulichkeit;Schutzbedarf: Integrität\n";

			// Gather all objects that influence security assesments
			HashMap<Integer, InformationDTO> informationDTOs = new HashMap<Integer, InformationDTO>();
			for (Information information : domain.getAll(Information.class)) {
				InformationDTO tempInformationDTO = information.createFullDTO();
				informationDTOs.put(tempInformationDTO.getId(), tempInformationDTO);
			}

			HashMap<Integer, OccupationDTO> occupationsDTO = new HashMap<Integer, OccupationDTO>();
			for (Occupation occupation : domain.getAll(Occupation.class)) {
				OccupationDTO tempOccupation = occupation.createFullDTO();

				List<InformationDTO> fullInformationDTOs = new ArrayList<InformationDTO>();
				for (InformationDTO information : tempOccupation.getInformations()) {
					fullInformationDTOs.add(informationDTOs.get(information.getId()));
				}
				tempOccupation.setInformations(fullInformationDTOs);

				occupationsDTO.put(tempOccupation.getId(), tempOccupation);
			}

			HashMap<Integer, EmployeeDTO> employeesDTO = new HashMap<Integer, EmployeeDTO>();
			for (Employee employee : domain.getAll(Employee.class)) {
				EmployeeDTO tempEmployee = employee.createFullDTO();

				List<InformationDTO> fullInformationDTOs = new ArrayList<InformationDTO>();
				for (InformationDTO information : tempEmployee.getInformations()) {
					fullInformationDTOs.add(informationDTOs.get(information.getId()));
				}
				tempEmployee.setInformations(fullInformationDTOs);

				List<OccupationDTO> fullOccupationDTOs = new ArrayList<OccupationDTO>();
				for (OccupationDTO occupation : tempEmployee.getOccupations()) {
					fullOccupationDTOs.add(occupationsDTO.get(occupation.getId()));
				}
				tempEmployee.setOccupations(fullOccupationDTOs);

				employeesDTO.put(tempEmployee.getId(), tempEmployee);
			}

			HashMap<Integer, AssettypeDTO> assettypeDTOs = new HashMap<Integer, AssettypeDTO>();
			for (Assettype assettype : domain.getAll(Assettype.class)) {
				AssettypeDTO tempAssettype = assettype.createFullDTO();
				assettypeDTOs.put(tempAssettype.getId(), tempAssettype);
			}

			HashMap<Integer, AssetDTO> assetDTOs = new HashMap<Integer, AssetDTO>();
			for (Asset asset : domain.getAll(Asset.class)) {
				AssetDTO tempAssetDTO = asset.createFullDTO();
				tempAssetDTO.setAssettype(assettypeDTOs.get(tempAssetDTO.getAssettype().getId()));
				assetDTOs.put(tempAssetDTO.getId(), tempAssetDTO);
			}

			// Now glue the assets together with all objects that can influence
			// the securityassesment
			for (AssetDTO asset : assetDTOs.values()) {
				if (asset.getParent() != null) {
					asset.setParent(assetDTOs.get(asset.getParent().getId()));
				}

				List<EmployeeDTO> fullEmployeesDTOs = new ArrayList<EmployeeDTO>();
				for (EmployeeDTO employee : asset.getAssociatedPersons()) {
					fullEmployeesDTOs.add(employeesDTO.get(employee.getId()));
				}
				asset.setAssociatedPersons(fullEmployeesDTOs);

				List<AssetDTO> fullChildrenAssetsDTOs = new ArrayList<AssetDTO>();
				for (AssetDTO childAsset : asset.getChildren()) {
					fullChildrenAssetsDTOs.add(assetDTOs.get(childAsset.getId()));
				}
				asset.setChildren(fullChildrenAssetsDTOs);
				for (AssetLinkDTO link : asset.getLinkedAssets()) {
					if (link.getParent() != null) {
						link.setParent(assetDTOs.get(link.getParent().getId()));
					}
					if (link.getAsset() != null) {
						link.setAsset(assetDTOs.get(link.getAsset().getId()));
					}
				}
			}

			// Now calcualted the security assements
			for (AssetDTO asset : assetDTOs.values()) {
				if (asset.getCalculatedSecurityAssesment().getId() > SecurityLevelDTO
				        .getDefaultSecurityLevel().getId()) {
					csvFile += asset.getName() + ";" + asset.getAssettype().getName() + ";"
					        + asset.getCalculatedSecurityAssesment().getName() + ";"
					        + asset.getCalculatedAvailability().getName() + ";"
					        + asset.getCalculatedConfidentiality().getName() + ";"
					        + asset.getCalculatedIntegrity().getName() + "\n";
				}
			}

			status = HttpServletResponse.SC_OK;
			contentType = "application/csv; charset=UTF-8";
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(csvFile);
			resp.setHeader("Content-Disposition",
			        "attachment; filename=\"" + domain.getString("name")
			                + ": Risikoanalyse-Assets.csv\"");

		} else {
			status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			contentType = "text/plain";
		}

		Base.close();
		resp.setStatus(status);
		resp.setContentType(contentType);
		resp.getWriter().flush();
		resp.getWriter().close();

	}
}
