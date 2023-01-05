package us.dontcareabout.ipLog.client.ui;

import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.TextArea;

import us.dontcareabout.ipLog.client.data.DataCenter;
import us.dontcareabout.ipLog.client.layer.IpTimeline;

public class MainView extends VerticalLayoutContainer {
	private static final VerticalLayoutData V1x1 = new VerticalLayoutData(1, 1);

	private TextArea log = new TextArea();
	private TextButton submit = new TextButton("Go!");

	public MainView() {
		HorizontalLayoutContainer inputHL = new HorizontalLayoutContainer();
		inputHL.add(log, new HorizontalLayoutData(1, 1));
		inputHL.add(submit, new HorizontalLayoutData(100, 1));

		add(inputHL, new VerticalLayoutData(1, 200));
		add(new IpTimeline(), V1x1);

		submit.addSelectHandler(e -> DataCenter.importPlain(log.getText()));
	}
}