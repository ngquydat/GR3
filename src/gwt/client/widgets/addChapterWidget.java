package gwt.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.RichTextArea;

public class addChapterWidget extends Composite {

	private static addChapterWidgetUiBinder uiBinder = GWT
			.create(addChapterWidgetUiBinder.class);
	public @UiField TextBox chapter;
	public @UiField RichTextArea content;

	interface addChapterWidgetUiBinder extends
			UiBinder<Widget, addChapterWidget> {
	}

	public addChapterWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public addChapterWidget(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
