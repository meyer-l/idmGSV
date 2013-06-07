package de.gsv.idm.client.presenter.helper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;

import de.gsv.idm.client.images.ImageResources;

public class ImageHelper {

	public static Image getImageFromIconName(String iconName) {
		Image image = new Image();
		ImageResource imageResource = getImageResourceFromIconName(iconName);
		if (imageResource != null) {
			image.setResource(imageResource);
			image.setTitle(imageResource.getName());
		}
		return image;
	}

	public static ImageResource getImageResourceFromIconName(String iconName) {
		ImageResources imageBundler = GWT.create(ImageResources.class);
		ImageResource resource = imageBundler.folder();
		if (iconName != null && !iconName.equals("")) {
			if (iconName.equals("application")) {
				resource = imageBundler.application();
			} else if (iconName.equals("basket")) {
				resource = imageBundler.basket();
			} else if (iconName.equals("bell")) {
				resource = imageBundler.bell();
			} else if (iconName.equals("bin")) {
				resource = imageBundler.bin();
			} else if (iconName.equals("book")) {
				resource = imageBundler.book();
			} else if (iconName.equals("box")) {
				resource = imageBundler.box();
			} else if (iconName.equals("brick")) {
				resource = imageBundler.brick();
			} else if (iconName.equals("briefcase")) {
				resource = imageBundler.briefcase();
			} else if (iconName.equals("building")) {
				resource = imageBundler.building();
			} else if (iconName.equals("camera")) {
				resource = imageBundler.camera();
			} else if (iconName.equals("cart")) {
				resource = imageBundler.cart();
			} else if (iconName.equals("chartBar")) {
				resource = imageBundler.chartBar();
			} else if (iconName.equals("chartCurve")) {
				resource = imageBundler.chartCurve();
			} else if (iconName.equals("chartLine")) {
				resource = imageBundler.chartLine();
			} else if (iconName.equals("chart")) {
				resource = imageBundler.chart();
			} else if (iconName.equals("chartPie")) {
				resource = imageBundler.chartPie();
			} else if (iconName.equals("clock")) {
				resource = imageBundler.clock();
			} else if (iconName.equals("cog")) {
				resource = imageBundler.cog();
			} else if (iconName.equals("coins")) {
				resource = imageBundler.coins();
			} else if (iconName.equals("comments")) {
				resource = imageBundler.comments();
			} else if (iconName.equals("compress")) {
				resource = imageBundler.compress();
			} else if (iconName.equals("computer")) {
				resource = imageBundler.computer();
			} else if (iconName.equals("connect")) {
				resource = imageBundler.connect();
			} else if (iconName.equals("cup")) {
				resource = imageBundler.cup();
			} else if (iconName.equals("database")) {
				resource = imageBundler.database();
			} else if (iconName.equals("disk")) {
				resource = imageBundler.disk();
			} else if (iconName.equals("door")) {
				resource = imageBundler.door();
			} else if (iconName.equals("drive")) {
				resource = imageBundler.drive();
			} else if (iconName.equals("driveNetwork")) {
				resource = imageBundler.driveNetwork();
			} else if (iconName.equals("email")) {
				resource = imageBundler.email();
			} else if (iconName.equals("folder")) {
				resource = imageBundler.folder();
			} else if (iconName.equals("house")) {
				resource = imageBundler.house();
			} else if (iconName.equals("image")) {
				resource = imageBundler.image();
			} else if (iconName.equals("ipod")) {
				resource = imageBundler.ipod();
			} else if (iconName.equals("keyboard")) {
				resource = imageBundler.keyboard();
			} else if (iconName.equals("lock")) {
				resource = imageBundler.lock();
			} else if (iconName.equals("lorry")) {
				resource = imageBundler.lorry();
			} else if (iconName.equals("map")) {
				resource = imageBundler.map();
			} else if (iconName.equals("monitor")) {
				resource = imageBundler.monitor();
			} else if (iconName.equals("newspaper")) {
				resource = imageBundler.newspaper();
			} else if (iconName.equals("note")) {
				resource = imageBundler.note();
			} else if (iconName.equals("none")) {
				resource = imageBundler.none();
			} else if (iconName.equals("printer")) {
				resource = imageBundler.printer();
			} else if (iconName.equals("report")) {
				resource = imageBundler.report();
			} else if (iconName.equals("script")) {
				resource = imageBundler.script();
			} else if (iconName.equals("server")) {
				resource = imageBundler.server();
			} else if (iconName.equals("serverDatabase")) {
				resource = imageBundler.serverDatabase();
			} else if (iconName.equals("serverLink")) {
				resource = imageBundler.serverLink();
			} else if (iconName.equals("shape")) {
				resource = imageBundler.shape();
			} else if (iconName.equals("shapeMiddle")) {
				resource = imageBundler.shapeMiddle();
			} else if (iconName.equals("shield")) {
				resource = imageBundler.shield();
			} else if (iconName.equals("sitemap")) {
				resource = imageBundler.sitemap();
			} else if (iconName.equals("table")) {
				resource = imageBundler.table();
			} else if (iconName.equals("telephone")) {
				resource = imageBundler.telephone();
			} else if (iconName.equals("terminal")) {
				resource = imageBundler.terminal();
			} else if (iconName.equals("user")) {
				resource = imageBundler.user();
			} else if (iconName.equals("world")) {
				resource = imageBundler.world();
			}
		}
		return resource;
	}

	public static ImageResource getErrorImageResourceFromIconName(String iconName) {
		ImageResources imageBundler = GWT.create(ImageResources.class);
		ImageResource resource = imageBundler.folderError();
		if (iconName != null && !iconName.equals("")) {
			if (iconName.equals("application")) {
				resource = imageBundler.applicationError();
			} else if (iconName.equals("basket")) {
				resource = imageBundler.basketError();
			} else if (iconName.equals("book")) {
				resource = imageBundler.bookError();
			} else if (iconName.equals("brick")) {
				resource = imageBundler.brickError();
			} else if (iconName.equals("building")) {
				resource = imageBundler.buildingError();
			} else if (iconName.equals("camera")) {
				resource = imageBundler.cameraError();
			} else if (iconName.equals("cart")) {
				resource = imageBundler.cartError();
			} else if (iconName.equals("chartBar")) {
				resource = imageBundler.chartBarError();
			} else if (iconName.equals("chartCurve")) {
				resource = imageBundler.chartCurveError();
			} else if (iconName.equals("chartLine")) {
				resource = imageBundler.chartLineError();
			} else if (iconName.equals("chartPie")) {
				resource = imageBundler.chartPieError();
			} else if (iconName.equals("clock")) {
				resource = imageBundler.clockError();
			} else if (iconName.equals("cog")) {
				resource = imageBundler.cogError();
			} else if (iconName.equals("computer")) {
				resource = imageBundler.computerError();
			} else if (iconName.equals("cup")) {
				resource = imageBundler.cupError();
			} else if (iconName.equals("database")) {
				resource = imageBundler.databaseError();
			} else if (iconName.equals("drive")) {
				resource = imageBundler.driveError();
			} else if (iconName.equals("email")) {
				resource = imageBundler.emailError();
			} else if (iconName.equals("folder")) {
				resource = imageBundler.folderError();
			} else if (iconName.equals("lorry")) {
				resource = imageBundler.lorryError();
			} else if (iconName.equals("monitor")) {
				resource = imageBundler.monitorError();
			} else if (iconName.equals("note")) {
				resource = imageBundler.noteError();
			} else if (iconName.equals("printer")) {
				resource = imageBundler.printerError();
			} else if (iconName.equals("script")) {
				resource = imageBundler.scriptError();
			} else if (iconName.equals("server")) {
				resource = imageBundler.serverError();
			} else if (iconName.equals("telephone")) {
				resource = imageBundler.telephoneError();
			}
		}
		return resource;
	}

}
