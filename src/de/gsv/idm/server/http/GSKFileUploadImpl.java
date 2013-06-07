package de.gsv.idm.server.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.javalite.activejdbc.Base;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.select.Elements;

import de.gsv.idm.server.DBPropertiesProvider;
import de.gsv.idm.shared.model.Measure;
import de.gsv.idm.shared.model.MeasureMeasureParentChild;
import de.gsv.idm.shared.model.MeasureModuleParentChild;
import de.gsv.idm.shared.model.MeasureThreatParentChild;
import de.gsv.idm.shared.model.Module;
import de.gsv.idm.shared.model.ModuleMeasureParentChild;
import de.gsv.idm.shared.model.ModuleModuleParentChild;
import de.gsv.idm.shared.model.ModuleThreatParentChild;
import de.gsv.idm.shared.model.Threat;
import de.gsv.idm.shared.model.ThreatMeasureParentChild;
import de.gsv.idm.shared.model.ThreatModuleParentChild;
import de.gsv.idm.shared.model.ThreatThreatParentChild;
import de.gsv.idm.shared.model.User;

@SuppressWarnings("serial")
public class GSKFileUploadImpl extends HttpServlet {

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest inRequest, HttpServletResponse inResponse)
	        throws ServletException, IOException {
		System.out.println("received gsk upload request");
		if (ServletFileUpload.isMultipartContent(inRequest)) {
			FileItemFactory aFactory = new DiskFileItemFactory();

			ServletFileUpload aServletFileUpload = new ServletFileUpload(aFactory);
			// Set upload parameters. See Apache Commons FileUpload for more
			// information
			// http://jakarta.apache.org/commons/fileupload/using.html
			aServletFileUpload.setSizeMax(-1);

			try {

				List<FileItem> fileItemList = (List<FileItem>) aServletFileUpload
				        .parseRequest(inRequest);

				if (fileItemList == null || fileItemList.size() == 0) {
					System.out.println("no file found");
				}
				FileItem file = null;
				String sessionId = "";

				for (FileItem fileItem : fileItemList) {
					if (fileItem.getFieldName().equals("uploadedfile")) {
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
				if (file != null && User.sessionIdIsValid(sessionId)) {
					System.out.println("Start parsing");
					Document doc = Jsoup.parse(new String(file.getString().getBytes("iso-8859-1"),
					        "UTF-8"));
					doc.outputSettings().escapeMode(EscapeMode.xhtml);

					HashMap<Module, Element> modulesToLink = new HashMap<Module, Element>();
					HashMap<Threat, Element> threatsToLink = new HashMap<Threat, Element>();
					HashMap<Measure, Element> measuresToLink = new HashMap<Measure, Element>();
					for (Element modules : doc.select("modules")) {
						for (Element module : modules.select("module")) {
							parseModule(modules.attr("title"), module, modulesToLink);
						}
					}

					for (Element threats : doc.select("threats")) {
						for (Element threat : threats.select("threat")) {
							parseThreat(threats.attr("title"), threat, threatsToLink);
						}
					}

					for (Element measures : doc.select("measures")) {
						for (Element measure : measures.select("measure")) {
							parseMeasure(measures.attr("title"), measure, measuresToLink);
						}
					}

					System.out.println(modulesToLink.size() + " Bausteine zu linken");
					for (Entry<Module, Element> m : modulesToLink.entrySet()) {
						linkModule(m.getKey(), m.getValue());
					}
					System.out.println(threatsToLink.size() + " Gefährdungen zu linken");
					for (Entry<Threat, Element> m : threatsToLink.entrySet()) {
						linkThreat(m.getKey(), m.getValue());
					}
					System.out.println(measuresToLink.size() + " Maßnahmen zu linken");
					for (Entry<Measure, Element> m : measuresToLink.entrySet()) {
						linkMeasure(m.getKey(), m.getValue());
					}
					inResponse.setStatus(HttpServletResponse.SC_OK);
					inResponse.setContentType("text/plain");
					inResponse.getWriter().write("1");
					inResponse.getWriter().flush();
					inResponse.getWriter().close();
				} else {
					inResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					inResponse.setContentType("text/plain");
					inResponse.getWriter().write("0");
					inResponse.getWriter().flush();
					inResponse.getWriter().close();
				}
				Base.close();

			} catch (Exception anUploadProblem) {
				inResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				inResponse.setContentType("text/plain");
				inResponse.getWriter().write("-1");
				inResponse.getWriter().flush();
				inResponse.getWriter().close();
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

	private void parseModule(String category, Element module, HashMap<Module, Element> modulesToLink) {
		String title = module.attr("title");
		Module dbModule = Module.findFirst("title = ?", title);
		if (dbModule == null)
			dbModule = new Module();
		dbModule.set("category", category);
		dbModule.set("title", title);
		dbModule.set("version", module.attr("version"));
		dbModule.set("module_description", parseDescription(module.select("moduledescription")
		        .first()));

		dbModule.set("threat_description", parseDescription(module.select("threatdescription")
		        .first()));

		dbModule.set("measure_description", parseDescription(module.select("measuredescription")
		        .first()));

		dbModule.saveIt();
		modulesToLink.put(dbModule, module);
	}

	private void linkModule(Module dbModule, Element module) {

		for (Element link : parseLinks(module.select("modulelinks"))) {
			Module child = Module.findFirst("title = ?", link.attr("title"));
			if (child != null
			        && ModuleModuleParentChild.findFirst("parent_id = ? AND child_id = ?",
			                dbModule.getId(), child.getId()) == null) {
				ModuleModuleParentChild.createIt("parent_id", dbModule.getId(), "child_id",
				        child.getId());
			}
		}
		for (Element link : parseLinks(module.select("threatlinks"))) {
			Threat child = Threat.findFirst("title = ?", link.attr("title"));
			if (child != null
			        && ModuleThreatParentChild.findFirst("parent_id = ? AND child_id = ?",
			                dbModule.getId(), child.getId()) == null) {
				ModuleThreatParentChild.createIt("parent_id", dbModule.getId(), "child_id",
				        child.getId(), "additional", 0);
			}

		}
		for (Element link : parseLinks(module.select("threatlinksadd"))) {
			Threat child = Threat.findFirst("title = ?", link.attr("title"));
			if (child != null
			        && ModuleThreatParentChild.findFirst("parent_id = ? AND child_id = ?",
			                dbModule.getId(), child.getId()) == null) {
				ModuleThreatParentChild.createIt("parent_id", dbModule.getId(), "child_id",
				        child.getId(), "additional", 1);
			}

		}
		for (Element link : parseLinks(module.select("measurelinks"))) {
			Measure child = Measure.findFirst("title = ?", link.attr("title"));
			if (child != null
			        && ModuleMeasureParentChild.findFirst("parent_id = ? AND child_id = ?",
			                dbModule.getId(), child.getId()) == null) {
				ModuleMeasureParentChild.createIt("parent_id", dbModule.getId(), "child_id",
				        child.getId(), "additional", 0, "category", link.attr("category"));
			}

		}

		for (Element link : parseLinks(module.select("measurelinksadd"))) {
			Measure child = Measure.findFirst("title = ?", link.attr("title"));
			if (child != null
			        && ModuleMeasureParentChild.findFirst("parent_id = ? AND child_id = ?",
			                dbModule.getId(), child.getId()) == null) {
				ModuleMeasureParentChild.createIt("parent_id", dbModule.getId(), "child_id",
				        child.getId(), "additional", 1, "category", link.attr("category"));
			}
		}
	}

	private void parseThreat(String category, Element threat, HashMap<Threat, Element> threatsToLink) {
		String title = threat.attr("title");
		Threat dbThreat = Threat.findFirst("title = ?", title);
		if (dbThreat == null)
			dbThreat = new Threat();
		dbThreat.set("category", category);
		dbThreat.set("title", title);
		dbThreat.set("version", threat.attr("version"));
		dbThreat.set("description", parseDescription(threat));
		dbThreat.saveIt();
		threatsToLink.put(dbThreat, threat);
	}

	private void linkThreat(Threat dbThreat, Element threat) {
		for (Element link : parseLinks(threat.select("modulelinks"))) {
			Module child = Module.findFirst("title = ?", link.attr("title"));
			if (child != null
			        && ThreatModuleParentChild.findFirst("parent_id = ? AND child_id = ?",
			                dbThreat.getId(), child.getId()) == null) {
				ThreatModuleParentChild.createIt("parent_id", dbThreat.getId(), "child_id",
				        child.getId());
			}
		}
		for (Element link : parseLinks(threat.select("threatlinks"))) {
			Threat child = Threat.findFirst("title = ?", link.attr("title"));
			if (child != null
			        && ThreatThreatParentChild.findFirst("parent_id = ? AND child_id = ?",
			                dbThreat.getId(), child.getId()) == null) {
				ThreatThreatParentChild.createIt("parent_id", dbThreat.getId(), "child_id",
				        child.getId());
			}
		}
		for (Element link : parseLinks(threat.select("measurelinks"))) {
			Measure child = Measure.findFirst("title = ?", link.attr("title"));
			if (child != null
			        && ThreatMeasureParentChild.findFirst("parent_id = ? AND child_id = ?",
			                dbThreat.getId(), child.getId()) == null) {
				ThreatMeasureParentChild.createIt("parent_id", dbThreat.getId(), "child_id",
				        child.getId());
			}
		}
	}

	private void parseMeasure(String categroy, Element measure,
	        HashMap<Measure, Element> measuresToLink) {
		String title = measure.attr("title");
		Measure dbMeasure = Measure.findFirst("title = ?", title);
		if (dbMeasure == null)
			dbMeasure = new Measure();
		dbMeasure.set("category", categroy);
		dbMeasure.set("title", title);
		dbMeasure.set("version", measure.attr("version"));
		dbMeasure.set("initiator", measure.attr("initiator"));
		dbMeasure.set("implementation", measure.attr("implementation"));
		dbMeasure.set("description", parseDescription(measure));
		dbMeasure.saveIt();
		measuresToLink.put(dbMeasure, measure);
	}

	private void linkMeasure(Measure dbMeasure, Element measure) {
		for (Element link : parseLinks(measure.select("modulelinks"))) {
			Module child = Module.findFirst("title = ?", link.attr("title"));
			if (child != null
			        && MeasureModuleParentChild.findFirst("parent_id = ? AND child_id = ?",
			                dbMeasure.getId(), child.getId()) == null) {
				MeasureModuleParentChild.createIt("parent_id", dbMeasure.getId(), "child_id",
				        child.getId());
			}
		}
		for (Element link : parseLinks(measure.select("threatlinks"))) {
			Threat child = Threat.findFirst("title = ?", link.attr("title"));
			if (child != null
			        && MeasureThreatParentChild.findFirst("parent_id = ? AND child_id = ?",
			                dbMeasure.getId(), child.getId()) == null) {
				MeasureThreatParentChild.createIt("parent_id", dbMeasure.getId(), "child_id",
				        child.getId());
			}
		}
		for (Element link : parseLinks(measure.select("measurelinks"))) {
			Measure child = Measure.findFirst("title = ?", link.attr("title"));
			if (child != null
			        && MeasureMeasureParentChild.findFirst("parent_id = ? AND child_id = ?",
			                dbMeasure.getId(), child.getId()) == null) {
				MeasureMeasureParentChild.createIt("parent_id", dbMeasure.getId(), "child_id",
				        child.getId());
			}
		}
	}

	private String parseDescription(Element desc) {
		String description = "";
		for (Element c : desc.children()) {
			if (c.tagName().equals("list")) {
				description += parseList(c);
			} else if (c.tagName().equals("headline")) {
				description += c.text() + "\n\n";
			} else {
				description += c.text() + "\n\n";
			}
		}
		return description;
	}

	private String parseList(Element c) {
		String list = "\n";
		for (Element child : c.children()) {
			list += "- " + child.text() + "\n";
		}
		list += "\n";
		return list;
	}

	private List<Element> parseLinks(Elements links) {
		List<Element> linksList = new ArrayList<Element>();
		for (Element l : links) {
			for (Element c : l.children()) {
				linksList.add(c);
			}
		}

		return linksList;
	}

}