package us.dontcareabout.ipLog.client.ui;

import java.util.Map;

import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

import us.dontcareabout.gxt.client.draw.LayerContainer;
import us.dontcareabout.gxt.client.draw.layout.VerticalLayoutLayer;
import us.dontcareabout.ipLog.client.data.DataCenter;
import us.dontcareabout.ipLog.client.ui.layer.IpDataLayer;
import us.dontcareabout.ipLog.client.ui.layer.NameListLayer;
import us.dontcareabout.ipLog.client.vo.IpData;

public class IpDataList extends VerticalLayoutContainer {
	private static final VerticalLayoutData V_1x_1 = new VerticalLayoutData(-1, -1);

	public IpDataList() {
		setAdjustForScroll(true);
		setScrollMode(ScrollMode.AUTO);
		DataCenter.addImportComplete(e -> refresh());
	}

	private void refresh() {
		clear();
		add(new List(), V_1x_1);
		forceLayout();
	}


	private class List extends LayerContainer {
		private VerticalLayoutLayer root = new VerticalLayoutLayer();

		List() {
			root.setGap(5);

			NameListLayer nameList = new NameListLayer();
			root.addChild(nameList, 32);
			double maxWidth = nameList.getViewSize();

			Map<String, IpData> ipMap = DataCenter.db.getIpMap();

			for (String ip : ipMap.keySet()) {
				IpData ipData = ipMap.get(ip);
				if (ipData.getNameList().size() < 2) { continue; }
				IpDataLayer ipDL = new IpDataLayer(ipData);
				root.addChild(ipDL, ipDL.computeHeight());
				maxWidth = Math.max(maxWidth, ipDL.getViewSize());
			}

			addLayer(root);
			adjustMember(500, 500);	//只是為了 call root.adjustMember() 讓下面 getViewSize() 能有值
			setPixelSize(
				(int)maxWidth,
				(int)root.getViewSize()
			);
		}

		@Override
		protected void adjustMember(int width, int height) {
			root.resize(width, height);
		}
	}
}
