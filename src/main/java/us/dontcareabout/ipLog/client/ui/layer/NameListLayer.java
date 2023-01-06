package us.dontcareabout.ipLog.client.ui.layer;

import java.util.List;

import com.google.gwt.user.client.Window;

import us.dontcareabout.gxt.client.draw.component.TextButton;
import us.dontcareabout.gxt.client.draw.layout.HorizontalLayoutLayer;
import us.dontcareabout.gxt.client.util.ColorUtil;
import us.dontcareabout.ipLog.client.data.DataCenter;

public class NameListLayer extends HorizontalLayoutLayer {

	public NameListLayer() {
		setGap(5);
		List<String> allName = DataCenter.db.getAllName();

		for(int i = 0; i < allName.size(); i++) {
			TextButton nameTB = new TextButton(allName.get(i));
			nameTB.setBgColor(ColorUtil.differential(i));
			nameTB.addSpriteSelectionHandler(
				e -> Window.open("https://www.google.com/search?q=ptt+" + nameTB.getText(), "_blank", null)
			);
			addChild(nameTB, 80);
		}

		adjustMember();
	}
}