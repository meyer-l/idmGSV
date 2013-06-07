package de.gsv.idm.client.view.widgets.window;

import java.util.Comparator;

import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Image;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.ListViewCustomAppearance;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.gsv.idm.client.images.ImageResources;
import de.gsv.idm.client.presenter.helper.ImageHelper;

public class ChooseImageWindow extends Window {

	interface Style extends CssResource {
		String over();

		String select();

		String thumb();

		String thumbWrap();
	}

	interface Resources extends ClientBundle {
		@Source("ChooseImageWindow.css")
		Style css();
	}

	ListStore<ImageResource> store;
	private ListView<ImageResource, ImageResource> view;

	public ChooseImageWindow(Image image) {
		this("Icon auswählen", image);
	}

	public ChooseImageWindow(String text, final Image image) {
		ContentPanel mainPanel = new ContentPanel();
		mainPanel.setHeaderVisible(false);
		setHeadingText(text);

		ModelKeyProvider<ImageResource> kp = new ModelKeyProvider<ImageResource>() {
			@Override
			public String getKey(ImageResource item) {
				return item.getName();
			}
		};

		store = new ListStore<ImageResource>(kp);
		store.addSortInfo(new StoreSortInfo<ImageResource>(new Comparator<ImageResource>() {

			@Override
			public int compare(ImageResource o1, ImageResource o2) {
				return o1.getName().compareTo(o2.getName());
			}
		}, SortDir.ASC));

		addImagesToStore();

		final Resources resources = GWT.create(Resources.class);
		resources.css().ensureInjected();
		final Style style = resources.css();

		ListViewCustomAppearance<ImageResource> appearance = new ListViewCustomAppearance<ImageResource>(
		        "." + style.thumbWrap(), style.over(), style.select()) {
			@Override
			public void renderEnd(SafeHtmlBuilder builder) {
				String markup = new StringBuilder("<div class=\"")
				        .append(CommonStyles.get().clear()).append("\"></div>").toString();
				builder.appendHtmlConstant(markup);
			}

			@Override
			public void renderItem(SafeHtmlBuilder builder, SafeHtml content) {
				builder.appendHtmlConstant("<div class='" + style.thumbWrap()
				        + "' style='border: 1px solid white'>");
				builder.append(content);
				builder.appendHtmlConstant("</div>");
			}

		};

		view = new ListView<ImageResource, ImageResource>(store,
		        new IdentityValueProvider<ImageResource>() {

			        @Override
			        public void setValue(ImageResource object, ImageResource value) {

			        }
		        }, appearance);

		view.setCell(new ImageResourceCell());
		view.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		mainPanel.add(view);

		if (image != null) {
			ImageResource resource = ImageHelper.getImageResourceFromIconName(image.getTitle());
			if (resource != null && store.findModel(resource) != null) {
				view.getSelectionModel().select(resource, false);
			}
		}

		final ChooseImageWindow window = this;
		TextButton abortButton = new TextButton("Abbrechen");
		abortButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				window.hide();

			}
		});
		TextButton okButton = new TextButton("Ok");
		okButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				ImageResource selected = view.getSelectionModel().getSelectedItem();

				if (selected != null) {
					image.setVisible(true);
					image.setResource(selected);
					image.setTitle(selected.getName());

				} else {
					image.setTitle("");
					image.setVisible(false);
				}
				window.hide();
			}
		});

		mainPanel.addButton(abortButton);
		mainPanel.addButton(okButton);
		add(mainPanel);
		setWidth(275);
		show();

	}

	private void addImagesToStore() {
		ImageResources imageBundler = GWT.create(ImageResources.class);
		store.add(imageBundler.application());
		store.add(imageBundler.basket());
		store.add(imageBundler.bell());
		store.add(imageBundler.bin());
		store.add(imageBundler.book());
		store.add(imageBundler.box());
		store.add(imageBundler.brick());
		store.add(imageBundler.briefcase());
		store.add(imageBundler.building());
		store.add(imageBundler.camera());
		store.add(imageBundler.cart());
		store.add(imageBundler.chartBar());
		store.add(imageBundler.chartCurve());
		store.add(imageBundler.chartLine());
		store.add(imageBundler.chart());
		store.add(imageBundler.clock());
		store.add(imageBundler.cog());
		store.add(imageBundler.coins());
		store.add(imageBundler.comments());
		store.add(imageBundler.compress());
		store.add(imageBundler.computer());
		store.add(imageBundler.connect());
		store.add(imageBundler.cup());
		store.add(imageBundler.database());
		store.add(imageBundler.disk());
		store.add(imageBundler.door());
		store.add(imageBundler.drive());
		store.add(imageBundler.driveNetwork());
		store.add(imageBundler.email());
		store.add(imageBundler.folder());
		store.add(imageBundler.house());
		store.add(imageBundler.image());
		store.add(imageBundler.ipod());
		store.add(imageBundler.keyboard());
		store.add(imageBundler.lock());
		store.add(imageBundler.lorry());
		store.add(imageBundler.map());
		store.add(imageBundler.monitor());
		store.add(imageBundler.newspaper());
		store.add(imageBundler.note());
		store.add(imageBundler.printer());
		store.add(imageBundler.report());
		store.add(imageBundler.script());
		store.add(imageBundler.server());
		store.add(imageBundler.serverDatabase());
		store.add(imageBundler.serverLink());
		store.add(imageBundler.shape());
		store.add(imageBundler.shapeMiddle());
		store.add(imageBundler.shield());
		store.add(imageBundler.sitemap());
		store.add(imageBundler.table());
		store.add(imageBundler.telephone());
		store.add(imageBundler.terminal());
		store.add(imageBundler.user());
		store.add(imageBundler.world());
	}
}
