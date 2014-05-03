package gwt.client.widgets;

import gwt.client.services.EbookService;
import gwt.client.services.EbookServiceAsync;
import gwt.client.services.LoginService;
import gwt.client.services.LoginServiceAsync;
import gwt.shared.Ebook;
import gwt.shared.FieldVerifier;
import gwt.shared.LoginInfo;
import gwt.shared.Person;

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
import com.google.gwt.user.client.ui.Image;
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
	Button logoutButton, createButton, ByURLButton, ByHandButton,
			EbookManageButton, AddChapterButton;
	@UiField
	TextBox Title, WebURL;
	@UiField
	Label errorLabel;
	@UiField
	VerticalPanel myEbooks, editebookinfo;

	@UiField
	HTMLPanel CreateEbookByURL, CreateEbookByHand, CreateEbookPanel,EbookManage, EbookCreate;

	@UiField
	Button refreshButton;
	@UiField
	HTMLPanel myPagePanel;
	LoginInfo loginInfo;
	LoginWidget parent;
	List<addChapterWidget> listAddChapterWidget = new ArrayList<addChapterWidget>();
	private final LoginServiceAsync loginService = GWT.create(LoginService.class);

	interface MypageWidgetUiBinder extends UiBinder<Widget, MypageWidget> {
	}

	public void init() {
		myPagePanel.setVisible(true);
		Title.getElement().setAttribute("placeHolder", "Title");
	}

	public MypageWidget(final LoginInfo loginInfo, LoginWidget parent) {
		this.loginInfo = loginInfo;
		this.parent = parent;
		initWidget(uiBinder.createAndBindUi(this));
		init();
		loadMainPanel();
		loadMyEbookPanel();
		Image refreshimg = new Image("img/refresh_icon.png");
		refreshButton.getElement().appendChild(refreshimg.getElement());

		refreshButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				loadMyEbookPanel();

			}
		});
		logoutButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(loginInfo.logoutUrl != null){
					Window.Location.replace(loginInfo.logoutUrl);
				}else{
					loginService.logout(loginInfo.userid, new AsyncCallback<Boolean>() {
						
						@Override
						public void onSuccess(Boolean result) {
							if(result){
								loadLogin();
							}
						}
						
						@Override
						public void onFailure(Throwable caught) {}
					});
				}
			}
		});

		ByURLButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				EbookCreate.setVisible(true);
				EbookManage.setVisible(false);
				CreateEbookByURL.setVisible(true);
				CreateEbookByHand.setVisible(false);
				editebookinfo.setVisible(false);
			}
		});
		ByHandButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				EbookCreate.setVisible(true);
				EbookManage.setVisible(false);
				CreateEbookByURL.setVisible(false);
				CreateEbookByHand.setVisible(true);
				editebookinfo.setVisible(false);
			}
		});
		EbookManageButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				EbookCreate.setVisible(false);
				EbookManage.setVisible(true);
			}
		});
		createButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createEbook(loginInfo);
			}
		});
		AddChapterButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addChapter();
			}
		});
	}

	public void loadMainPanel() {
		CreateEbookPanel.setVisible(true);
		CreateEbookByURL.setVisible(false);
		CreateEbookByHand.setVisible(true);
		editebookinfo.setVisible(false);
		EbookManage.setVisible(false);
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
					final Anchor linkdown = new Anchor();
					final String url = GWT.getHostPageBaseURL()
							+ "download?blobkey=" + result.get(i).blobkey
							+ "&&filename=" + result.get(i).name + ".epub";
					linkdown.setHref(url);
					Image downloadimg = new Image("img/download.jpg");
					linkdown.getElement().appendChild(downloadimg.getElement());
					final Button infoButton = new Button();
					Image infoimg = new Image("img/info.jpg");
					infoButton.getElement().appendChild(infoimg.getElement());
					infoButton.setWidth("40px");
					infoButton.setHeight("40px");

					Button delButton = new Button();
					Image deleteimg = new Image("img/delete.jpg");
					delButton.getElement().appendChild(deleteimg.getElement());
					delButton.setWidth("40px");
					delButton.setHeight("40px");

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
							if (ebookinfo.isVisible()) {
								ebookinfo.setVisible(false);
							} else {
								ebookinfo.setVisible(true);
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
					h.add(delButton);
					h.add(linkdown);
					h.add(linkRead);

					myEbooks.add(h);
					myEbooks.add(ebookinfo);

					editEbookButton.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							EbookCreate.setVisible(true);
							EbookManage.setVisible(false);
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

	public void addChapter() {
		addChapterWidget newChapter = new addChapterWidget(); 
		newChapter.chapter.getElement().setAttribute("placeHolder", "Chapter Name");
		listAddChapterWidget.add(newChapter);
		
		CreateEbookByHand.add(newChapter);
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
			for(int i=0;i<listAddChapterWidget.size();i++){
				listChapters.add(listAddChapterWidget.get(i).chapter.getText());
				listChapterContents.add(listAddChapterWidget.get(i).content.getHTML());
			}
			
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

	private void loadLogin() {
		LoginWidget login = new LoginWidget();
		RootPanel.get("abc").clear();
		RootPanel.get("abc").add(login);
	}
}
