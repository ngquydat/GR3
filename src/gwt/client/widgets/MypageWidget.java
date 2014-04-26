package gwt.client.widgets;

import gwt.client.Main;
import gwt.client.services.EbookService;
import gwt.client.services.EbookServiceAsync;
import gwt.shared.Ebook;
import gwt.shared.FieldVerifier;
import gwt.shared.LoginInfo;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MypageWidget extends Composite {

	private static MypageWidgetUiBinder uiBinder = GWT
			.create(MypageWidgetUiBinder.class);
	private final EbookServiceAsync ebookService = GWT
			.create(EbookService.class);
	@UiField
	Button logoutButton, createButton, ByURLButton, ByHandButton;
	@UiField
	TextBox Title, WebURL, Chapter1Name;
	@UiField
	RichTextArea Chapter1Content;
	@UiField
	Label errorLabel;
	@UiField
	VerticalPanel myEbooks, editebookinfo;

	@UiField
	HTMLPanel CreateEbookByURL, CreateEbookByHand, CreateEbookPanel;

	@UiField
	Button refreshButton;
	@UiField
	HTMLPanel myPagePanel;
	@UiField
	HTMLPanel myReaderPanel;
	LoginInfo loginInfo;
	Main parent;

	interface MypageWidgetUiBinder extends UiBinder<Widget, MypageWidget> {
	}

	public void init() {
		myPagePanel.setVisible(true);
		myReaderPanel.setVisible(false);

	}

	public MypageWidget(final LoginInfo loginInfo, Main parent) {
		this.loginInfo = loginInfo;
		this.parent = parent;
		initWidget(uiBinder.createAndBindUi(this));
		init();
		loadMainPanel();
		loadMyEbookPanel();
		refreshButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				loadMyEbookPanel();

			}
		});
		logoutButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace(loginInfo.logoutUrl);

			}
		});

		ByURLButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				CreateEbookByURL.setVisible(true);
				CreateEbookByHand.setVisible(false);
			}
		});
		ByHandButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				CreateEbookByURL.setVisible(false);
				CreateEbookByHand.setVisible(true);
			}
		});
		createButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createEbook(loginInfo);
			}
		});
	}

	public void loadMainPanel() {
		CreateEbookPanel.setVisible(true);
		CreateEbookByURL.setVisible(false);
		CreateEbookByHand.setVisible(true);
		editebookinfo.setVisible(false);
	}

	public void loadeditebookPanel(final Ebook ebook) {
		CreateEbookPanel.setVisible(true);
		editebookinfo.setVisible(true);
		final TextBox editTitle = new TextBox();
		editTitle.setText(ebook.name);
		final List<TextBox> listTextBox = new ArrayList<TextBox>();
		for (int j = 0; j < ebook.listChapters.size(); j++) {
			TextBox editchapter = new TextBox();
			editchapter.setText(ebook.listChapters.get(j));
			editchapter.setWidth("650px");
			editchapter.setStyleName("form-control");
			listTextBox.add(editchapter);
		}
		final List<RichTextArea> listRichTextArea = new ArrayList<RichTextArea>();
		for (int j = 0; j < ebook.listChapterContents.size(); j++) {
			RichTextArea editchapterContent = new RichTextArea();
			editchapterContent.setHTML(ebook.listChapterContents.get(j));
			editchapterContent.setHeight("300px");
			editchapterContent.setWidth("650px");
			editchapterContent.setStyleName("form-control");
			listRichTextArea.add(editchapterContent);

		}
		final Button saveEbookButton = new Button("Save");
		saveEbookButton.setStyleName("btn btn-default");
		editebookinfo.add(editTitle);
		for (int j = 0; j < listTextBox.size(); j++) {
			editebookinfo.add(listTextBox.get(j));
			editebookinfo.add(listRichTextArea.get(j));
			editebookinfo.add(new Label(" chap:  "));
		}
		editebookinfo.add(saveEbookButton);
		saveEbookButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				saveEbookButton.setText("Saving ...");
				saveEbookButton.setEnabled(false);

				ebook.listChapters.clear();
				ebook.listChapterContents.clear();
				for (int j = 0; j < listTextBox.size(); j++) {
					ebook.listChapters.add(listTextBox.get(j).getText());
					ebook.listChapterContents.add(listRichTextArea.get(j)
							.getHTML());
				}
				ebookService.createEbook(loginInfo, editTitle.getText(),
						ebook.listChapters, ebook.listChapterContents,
						new AsyncCallback<Ebook>() {
							@Override
							public void onFailure(Throwable caught) {
							}

							@Override
							public void onSuccess(final Ebook result) {
								ebookService.delete(ebook.id,
										new AsyncCallback<Void>() {

											@Override
											public void onSuccess(Void result) {
												loadMyEbookPanel();
												Window.alert("Succeed!!! The Ebook you have saved is in your \"My ebooks\"");
												editebookinfo.setVisible(false);
												CreateEbookByHand
														.setVisible(true);
											}

											@Override
											public void onFailure(
													Throwable caught) {
											}
										});

							}
						});
			}
		});
	}

	public static native void jsreader(String url) /*-{
		$wnd.EPUBJS.VERSION = "0.1.7";
		$wnd.EPUBJS.filePath = "js/libs/";
		$wnd.EPUBJS.cssPath = "css/";
		$wnd.ePubReader(url);
	}-*/;

	public void loadMyEbookPanel() {
		myEbooks.clear();

		ebookService.getAll(loginInfo.email, new AsyncCallback<List<Ebook>>() {

			@Override
			public void onSuccess(final List<Ebook> result) {
				for (int i = 0; i < result.size(); i++) {
					final VerticalPanel ebookinfo = new VerticalPanel();
					ebookinfo.clear();
					ebookinfo.setVisible(false);
					final Ebook ebook = result.get(i);
					Label ebookname = new Label("File name: " + ebook.name);
					Label owner = new Label("owner: " + ebook.owner);
					Label created = new Label("created: "
							+ ebook.created.toString());
					Label updated = new Label();
					if (ebook.updated != null) {
						updated.setText("updated: " + ebook.updated.toString());
					}
					ebookinfo.add(ebookname);
					ebookinfo.add(owner);
					ebookinfo.add(created);
					ebookinfo.add(updated);
					for (int j = 0; j < ebook.listChapters.size(); j++) {
						Label chaptername = new Label(ebook.listChapters.get(j));
						ebookinfo.add(chaptername);
					}
					Anchor linkRead = new Anchor(result.get(i).name);
					final Anchor linkdown = new Anchor("download");
					final String url = GWT.getHostPageBaseURL()
							+ "download?blobkey=" + result.get(i).blobkey
							+ "&&filename=" + result.get(i).name + ".epub";
					linkdown.setHref(url);
					final Button infoButton = new Button("Info");
					Button delButton = new Button("X");
					infoButton.setStyleName("btn btn-default");
					delButton.setStyleName("btn btn-default");

					linkRead.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							RootPanel.get("abc").setVisible(false);
							RootPanel.get("reader").setVisible(true);
							jsreader(url);
						}
					});

					infoButton.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							if (infoButton.getText().equalsIgnoreCase("Info")) {
								infoButton.setText("Hide");
								ebookinfo.setVisible(true);
							} else {
								infoButton.setText("Info");
								ebookinfo.setVisible(false);
							}

						}
					});

					delButton.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							ebookService.delete(ebook.id,
									new AsyncCallback<Void>() {

										@Override
										public void onSuccess(Void result) {
											loadMyEbookPanel();
										}

										@Override
										public void onFailure(Throwable caught) {
										}
									});

						}
					});
					Button editEbookButton = new Button("Edit");
					editEbookButton.setStyleName("btn btn-primary");
					ebookinfo.add(editEbookButton);
					final HorizontalPanel h = new HorizontalPanel();
					h.add(infoButton);
					h.add(linkRead);
					h.add(delButton);
					h.add(linkdown);

					myEbooks.add(h);
					myEbooks.add(ebookinfo);

					editEbookButton.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							CreateEbookByHand.setVisible(false);
							CreateEbookByURL.setVisible(false);
							editebookinfo.clear();
							loadeditebookPanel(ebook);
						}
					});

				}
			}

			@Override
			public void onFailure(Throwable caught) {
			}
		});

	}

	public void createEbook(LoginInfo loginInfo) {
		createButton.setText("converting ...");
		createButton.setEnabled(false);
		errorLabel.setText("");

		String title = Title.getText();
		if (CreateEbookByURL.isVisible()) {
			String webURL = WebURL.getText();
			if (!FieldVerifier.isValidName(title)
					&& !FieldVerifier.isValidName(webURL)) {
				errorLabel.setText("Please enter at least four characters");
				return;
			}
			ebookService.createEbook(loginInfo, title, webURL,
					new AsyncCallback<Ebook>() {
						@Override
						public void onFailure(Throwable caught) {
						}

						@Override
						public void onSuccess(final Ebook result) {
							loadMyEbookPanel();
							Window.alert("Succeed!!! See your file in \"My ebooks\"");
							createButton.setText("Create Ebook");
							createButton.setEnabled(true);
						}
					});
		} else {
			List<String> listChapters = new ArrayList<String>();
			List<String> listChapterContents = new ArrayList<String>();
			listChapters.add(Chapter1Name.getText());
			listChapterContents.add(Chapter1Content.getHTML());
			ebookService.createEbook(loginInfo, title, listChapters,
					listChapterContents, new AsyncCallback<Ebook>() {
						@Override
						public void onFailure(Throwable caught) {
						}

						@Override
						public void onSuccess(final Ebook result) {
							Window.alert("Succeed!!! The Ebook you have created is in your \"My ebooks\"");

							loadMyEbookPanel();
							createButton.setText("Create Ebook");
							createButton.setEnabled(true);
						}
					});

		}
	}
}
