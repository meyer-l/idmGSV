package de.gsv.idm.server.http;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javalite.activejdbc.Base;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import de.gsv.idm.server.DBPropertiesProvider;
import de.gsv.idm.shared.comperator.GeneralComperator;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssetMeasureLinkDTO;
import de.gsv.idm.shared.model.Asset;
import de.gsv.idm.shared.model.Domain;
import de.gsv.idm.shared.model.User;

@SuppressWarnings("serial")
public class AssetsMeasuresSummaryProvider extends HttpServlet {
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
			HashMap<Integer, AssetDTO> assets = new HashMap<Integer, AssetDTO>();
			for (Asset asset : domain.getAll(Asset.class)) {
				assets.put(asset.getInteger("id"), asset.createFullDTO());
			}

			for (AssetDTO asset : assets.values()) {
				if (asset.getParent() != null) {
					asset.setParent(assets.get(asset.getParent().getId()));
				}
			}

			Font helvetica = FontFactory.getFont("Helvetica", 10);
			Font helveticaBold = FontFactory.getFont("Helvetica", 12, Font.BOLD);
			try {
				Document document = new Document();
				PdfWriter.getInstance(document, resp.getOutputStream());
				document.open();
				document.add(new Paragraph("Maßnahmenstatus-Zusammenfassung für Domäne: "
				        + domain.getString("name"), helveticaBold));
				PdfPTable table = new PdfPTable(3);
				table.setWidths(new int[]{3, 6, 2});
				table.setSpacingBefore(20);
				table.setSpacingAfter(30);
				table.setWidthPercentage(95f);
				table.addCell(new Phrase("Asset", helvetica));
				table.addCell(new Phrase("Maßnahme", helvetica));
				table.addCell(new Phrase("Status", helvetica));
				List<AssetDTO> sortedAssets = new ArrayList<AssetDTO>(assets.values());
				Collections.sort(sortedAssets, new GeneralComperator<AssetDTO>());
				for (AssetDTO asset : sortedAssets) {
					if (asset.getAllMeasureLinks().size() > 0) {
						PdfPCell assetNameCell = new PdfPCell(
						        new Phrase(asset.getName(), helvetica));
						assetNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
						assetNameCell.setRowspan(asset.getAllMeasureLinks().size());
						table.addCell(assetNameCell);
						for (AssetMeasureLinkDTO measure : asset.getAllMeasureLinks()) {
							table.addCell(new Phrase(measure.getName(), helvetica));
							if (measure.getStatus() != null && !measure.getStatus().equals("")) {
								table.addCell(new Phrase(measure.getStatus(), helvetica));
							} else {
								table.addCell("-");
							}
						}

					}
				}
				document.add(table);
				SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
				document.add(new Paragraph("Stand: " + format.format(new Date()), helvetica));
				document.close();
			} catch (DocumentException e) {
				e.printStackTrace();
			}

			status = HttpServletResponse.SC_OK;
			contentType = "application/pdf";
			resp.setHeader("Content-Disposition",
			        "attachment; filename=\"" + domain.getString("name")
			                + ": Maßnahmenstatus-Zusammenfassung.pdf\"");
		} else {
			status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			contentType = "text/plain";
		}

		Base.close();
		resp.setStatus(status);
		resp.setContentType(contentType);

	}

}
