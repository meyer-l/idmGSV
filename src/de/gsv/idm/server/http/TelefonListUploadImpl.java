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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.javalite.activejdbc.Base;

import de.gsv.idm.server.DBPropertiesProvider;
import de.gsv.idm.server.push.PushUpdateHandler;
import de.gsv.idm.server.service.ChangeEventServiceImpl;
import de.gsv.idm.shared.dto.ChangeEventDTO;
import de.gsv.idm.shared.dto.ChangeItemDTO;
import de.gsv.idm.shared.model.Asset;
import de.gsv.idm.shared.model.ChangeEvent;
import de.gsv.idm.shared.model.ChangeItem;
import de.gsv.idm.shared.model.Employee;
import de.gsv.idm.shared.model.Occupation;
import de.gsv.idm.shared.model.User;
import de.gsv.idm.shared.push.event.change.event.ChangeEventCreatedPushEvent;

@SuppressWarnings("serial")
public class TelefonListUploadImpl extends HttpServlet {

protected PushUpdateHandler pushUpdateHandler;
	
	public TelefonListUploadImpl(){
		pushUpdateHandler = PushUpdateHandler.getInstance();
	}
	
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest inRequest, HttpServletResponse inResponse)
	        throws ServletException, IOException {
		System.out.println("received telefonlist upload request");
		if (ServletFileUpload.isMultipartContent(inRequest)) {
			FileItemFactory aFactory = new DiskFileItemFactory();

			ServletFileUpload aServletFileUpload = new ServletFileUpload(aFactory);
			// Set upload parameters. See Apache Commons FileUpload for more
			// information
			// http://jakarta.apache.org/commons/fileupload/using.html
			aServletFileUpload.setSizeMax(-1);
			String status = "-1";
			try {
				List<FileItem> fileItemList = (List<FileItem>) aServletFileUpload
				        .parseRequest(inRequest);

				if (fileItemList == null || fileItemList.size() == 0) {
					System.out.println("no file found");
				}
				FileItem file = null;
				Integer domainId = null;
				String sessionId = "";

				for (FileItem fileItem : fileItemList) {
					if (fileItem.isFormField() && fileItem.getFieldName().equals("domainId")) {
						domainId = Integer.parseInt(fileItem.getString());
					} else if (fileItem.getFieldName().equals("telefonList")) {
						file = fileItem;
					} else if (fileItem.getFieldName().equals("sessionId")) {
						sessionId = fileItem.getString();
					}
				}
				Properties dbProperties = DBPropertiesProvider.getProperties(getServletContext());
				if (!Base.hasConnection()) {
					Base.open(dbProperties.getProperty("driver"), dbProperties.getProperty("url"),
					        dbProperties.getProperty("user"), dbProperties.getProperty("password"));
				}
				if (file != null && domainId != null && User.sessionIdIsValid(sessionId)) {
					List<ChangeItem> changes = new ArrayList<ChangeItem>();
					changes = parseTelefonList(new String(file.getString().getBytes("iso-8859-1"),
					        "UTF-8"), domainId);
					List<ChangeEvent> newChangeEvents = new ArrayList<ChangeEvent>();
					if (changes.size() > 0) {
						for (ChangeItem item : changes) {
							ChangeItemDTO itemDTO = item.createDTO();
							// Employee Changes
							if (itemDTO.getEmployee() == null || itemDTO.getOldTelefon() != null
							        && !itemDTO.getOldTelefon().equals(itemDTO.getNewTelefon())
							        || itemDTO.getOldTelefon() == null
							        && !itemDTO.getNewTelefon().isEmpty()) {
								newChangeEvents.add((ChangeEvent) ChangeEvent.createIt("domain_id",
								        itemDTO.getDomainId(), "change_item_id", itemDTO.getId(),
								        "change_type", 0, "process_type",
								        ChangeEventDTO.ProcessType.NotProcessed.toString()));
							}
							// AssetsChanges
							if (!itemDTO.getMissingAssetsNames().isEmpty()
							        || itemDTO.getOldAssets() != null
							        && !itemDTO.getOldAssets().containsAll(itemDTO.getNewAssets())) {
								newChangeEvents.add((ChangeEvent) ChangeEvent.createIt("domain_id",
								        itemDTO.getDomainId(), "change_item_id", itemDTO.getId(),
								        "change_type", 1, "process_type",
								        ChangeEventDTO.ProcessType.NotProcessed.toString()));
							}

							if (!itemDTO.getMissingOccupationsNames().isEmpty()
							        || (!itemDTO.getOldOccupations().containsAll(
							                itemDTO.getNewOccupations()))) {

								newChangeEvents.add((ChangeEvent) ChangeEvent.createIt("domain_id",
								        itemDTO.getDomainId(), "change_item_id", itemDTO.getId(),
								        "change_type", 2, "process_type",
								        ChangeEventDTO.ProcessType.NotProcessed.toString()));
							}

							status = "1";
						}
						for (ChangeEvent event : newChangeEvents) {
							pushUpdateHandler.addEvent(
							        ChangeEventServiceImpl.CONVERSATION_DOMAIN,
							        new ChangeEventCreatedPushEvent(event.createFullDTO()));
						}
					} else {
						status = "0";
					}
					inResponse.setStatus(HttpServletResponse.SC_OK);
					inResponse.setContentType("text/plain");
					inResponse.getWriter().write(status);
					inResponse.getWriter().flush();
					inResponse.getWriter().close();
				} else {
					inResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					inResponse.setContentType("text/plain");
				}
				Base.close();
			} catch (Exception anUploadProblem) {
				anUploadProblem.printStackTrace();
				inResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				inResponse.setContentType("text/plain");
				inResponse.getWriter().write("-1");
				inResponse.getWriter().flush();
				inResponse.getWriter().close();
				System.out.println("failure while parsing");
			}

		} else {
			System.out.println("not multipart");
			inResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			inResponse.setContentType("text/plain");
			inResponse.getWriter().write("-1");
			inResponse.getWriter().flush();
			inResponse.getWriter().close();
		}
	}

	private List<ChangeItem> parseTelefonList(String telefonList, Integer domainId) {
		List<ChangeItem> changes = new ArrayList<ChangeItem>();
		HashMap<String, Integer> headlineColumnMapping = new HashMap<String, Integer>();
		headlineColumnMapping.put("mitarbeitername", 0);
		headlineColumnMapping.put("raum", 1);
		headlineColumnMapping.put("dienstposten", 2);
		headlineColumnMapping.put("telefon", 3);
		for (String lineEntry : telefonList.split("\n")) {
			if (!lineEntry.toLowerCase().contains("mitarbeitername")) {
				ChangeItem item = parseLine(lineEntry, domainId, headlineColumnMapping);
				if (item != null) {
					changes.add(item);
				}
			} else {
				String[] splittedLine = lineEntry.split(";");
				for (int i = 0; i < splittedLine.length; i++) {
					String compareItem = splittedLine[i].toLowerCase().trim();
					if (compareItem.equals("mitarbeitername")) {
						headlineColumnMapping.put("mitarbeitername", i);
					} else if (compareItem.equals("raum")) {
						headlineColumnMapping.put("raum", i);
					} else if (compareItem.equals("dienstposten")) {
						headlineColumnMapping.put("dienstposten", i);
					} else if (compareItem.equals("telefon")) {
						headlineColumnMapping.put("telefon", i);
					}
				}
			}

		}
		return changes;
	}

	private ChangeItem parseLine(String lineEntry, Integer domainId,
	        HashMap<String, Integer> headlineColumnMapping) {
		String[] columns = lineEntry.split(";");
		if (!(columns.length < 3 || columns.length > 4)) {
			String parsedName = "";
			String parsedAssets = "";
			String parsedOccupations = "";
			String parsedTelefon = "";
			for (Integer i = 0; i < columns.length; i++) {
				String value = columns[i].trim();
				if (i.equals(headlineColumnMapping.get("mitarbeitername"))) {
					parsedName = value;
				} else if (i.equals(headlineColumnMapping.get("raum"))) {
					parsedAssets = value;
				} else if (i.equals(headlineColumnMapping.get("dienstposten"))) {
					parsedOccupations = value;
				} else if (i.equals(headlineColumnMapping.get("telefon"))) {
					parsedTelefon = value;
				}
			}
			// Find Employee in DB
			Integer employeeId = null;
			String oldAssetsIds = "";
			String oldOccupationsIds = "";
			String oldTelefon = "";
			if (!parsedName.equals("")) {
				String[] splittedName = parsedName.split(",");
				if (splittedName.length == 2) {
					List<Employee> employeeList = Employee.where(
					        "surname = ? and name = ? and domain_id = ?", splittedName[0].trim(),
					        splittedName[1].trim(), domainId);
					if (employeeList.size() == 1) {
						Employee employee = employeeList.get(0);
						employeeId = employee.getInteger("id");
						oldTelefon = employee.getString("telefon");

						for (Asset asset : employee.getAll(Asset.class)) {
							if (!oldAssetsIds.equals("")) {

								oldAssetsIds += ";";
							}
							oldAssetsIds += asset.getInteger("id");
						}

						for (Occupation occupation : employee.getAll(Occupation.class)) {
							if (!oldOccupationsIds.equals("")) {
								oldOccupationsIds += ";";
							}
							oldOccupationsIds += occupation.getInteger("id");
						}
					}
				} else {
					System.out.println("error parsing name: " + parsedName);
				}
			}

			// Find Rooms in DB
			String newAssetsIds = "";
			String missingAssetsNames = "";
			if (!parsedAssets.equals("")) {
				String[] assetNames = parsedAssets.split(",");
				for (String assetName : assetNames) {
					List<Asset> tempAssets = Asset.where("identifier = ? and domain_id = ?",
					        assetName.trim(), domainId);
					if (tempAssets.size() == 1) {
						if (!newAssetsIds.equals("")) {
							newAssetsIds += ";";
						}
						newAssetsIds += tempAssets.get(0).getInteger("id");
					} else {
						if (!missingAssetsNames.equals("")) {
							missingAssetsNames += ";";
						}
						missingAssetsNames += assetName;
					}

				}
			}

			// Find Occupations in DB
			String newOccupationsIds = "";
			String missingOccupationsNames = "";
			if (!parsedOccupations.equals("")) {
				String[] occupationNames = parsedOccupations.split(",");
				for (String occupationName : occupationNames) {
					List<Occupation> tempOccupations = Occupation.where(
					        "name = ? and domain_id = ?", occupationName.trim(), domainId);
					if (tempOccupations.size() == 1) {
						if (!newOccupationsIds.equals("")) {
							newOccupationsIds += ";";
						}
						newOccupationsIds += tempOccupations.get(0).getInteger("id");
					} else {
						if (!missingOccupationsNames.equals("")) {
							missingOccupationsNames += ";";
						}
						missingOccupationsNames += occupationName;
					}

				}
			}

			ChangeItem item = ChangeItem.createIt("domain_id", domainId, "employee_id", employeeId,
			        "parsed_employee_name", parsedName, "old_assets_ids", oldAssetsIds,
			        "new_assets_ids", newAssetsIds, "missing_assets_names", missingAssetsNames,
			        "old_occupations_ids", oldOccupationsIds, "new_occupations_ids",
			        newOccupationsIds, "missing_occupations_names", missingOccupationsNames,
			        "old_telefon", oldTelefon, "new_telefon", parsedTelefon);
			return item;
		} else {
			System.out.println("Failure while parsing line: " + lineEntry);
			return null;
		}
	}

}
