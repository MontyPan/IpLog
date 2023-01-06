package us.dontcareabout.ipLog.client.ui.component;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

import us.dontcareabout.gxt.client.draw.LayerContainer;
import us.dontcareabout.gxt.client.draw.LayerSprite;
import us.dontcareabout.gxt.client.draw.component.TextButton;
import us.dontcareabout.gxt.client.draw.layout.HorizontalLayoutLayer;
import us.dontcareabout.gxt.client.draw.layout.VerticalLayoutLayer;
import us.dontcareabout.gxt.client.util.ColorUtil;
import us.dontcareabout.ipLog.client.data.DataCenter;
import us.dontcareabout.ipLog.client.ui.layer.NameListLayer;
import us.dontcareabout.ipLog.client.vo.IpData;
import us.dontcareabout.ipLog.client.vo.NamePeriod;

public class IpTimeline extends VerticalLayoutContainer {
	private static final VerticalLayoutData V_1x_1 = new VerticalLayoutData(-1, -1);

	public IpTimeline() {
		setAdjustForScroll(true);
		setScrollMode(ScrollMode.AUTO);
		DataCenter.addImportComplete(e -> refresh());
	}

	private void refresh() {
		clear();
		add(new Timeline(), V_1x_1);
		forceLayout();
	}
}

class Timeline extends LayerContainer {
	private static final int HOUR_W = 2;
	private static final Color[] BG = {RGB.GRAY, RGB.LIGHTGRAY};

	private VerticalLayoutLayer root = new VerticalLayoutLayer();

	private Date start = new Date();
	private Date end = new Date(0);

	Timeline() {
		Map<String, IpData> ipMap = DataCenter.db.getIpMap();
		int bgIndex = 0;

		for (String ip : ipMap.keySet()) {
			IpData ipData = ipMap.get(ip);
			if (ipData.getNameSet().size() < 2) { continue; }
			if (start.after(ipData.getStart())) { start = ipData.getStart(); }
			if (end.before(ipData.getEnd())) { end = ipData.getEnd(); }
		}

		HorizontalLayoutLayer infoLayer = buildInfo();
		root.addChild(infoLayer, 32);

		for (String ip : ipMap.keySet()) {
			IpData ipData = ipMap.get(ip);
			if (ipData.getNameSet().size() < 2) { continue; }
			IpDataLayer ipDL = new IpDataLayer(ipData);
			ipDL.setBgColor(BG[bgIndex++ % 2]);
			root.addChild(ipDL, ipDL.getViewSize());
		}

		root.setGap(5);
		addLayer(root);
		adjustMember(500, 500);	//只是為了 call root.adjustMember() 讓下面 getViewSize() 能有值
		setPixelSize(
			(int)Math.max(periodWidth(start, end), infoLayer.getViewSize()),
			(int)root.getViewSize()
		);
	}

	@Override
	protected void adjustMember(int width, int height) {
		root.resize(width, height);
	}

	private HorizontalLayoutLayer buildInfo() {
		HorizontalLayoutLayer result = new HorizontalLayoutLayer();
		DateTimeFormat format = DateTimeFormat.getFormat("yyyy/MM/dd");
		result.setGap(5);

		NameListLayer nameList = new NameListLayer();
		result.addChild(nameList, nameList.getViewSize());
		TextButton date = new TextButton(format.format(start) + "～" + format.format(end));
		result.addChild(date, 200);
		return result;
	}

	private static int periodWidth(Date start, Date end) {
		return (int)(
			(end.getTime() - start.getTime())
			/ (60 * 60 * 1000) + DataCenter.db.getValidityHour()
		) * HOUR_W;
	}

	class IpDataLayer extends VerticalLayoutLayer {
		static final int H = 30;
		final IpData ipData;

		IpDataLayer(IpData ipData) {
			this.ipData = ipData;
			setGap(3);
			ipData.getNameSet().forEach(name -> build(name));
			adjustMember();
		}

		private void build(String name) {
			addChild(new NameLayer(ipData.getPeriodList(name)), H);
		}

		class NameLayer extends LayerSprite {
			NameLayer(List<NamePeriod> periodList) {
				periodList.forEach(p -> {
					TextButton n = new TextButton("" + p.getCount());
					n.setBgColor(ColorUtil.differential(DataCenter.db.getAllName().indexOf(p.name)));
					n.setBgRadius(5);
					n.setLX(periodWidth(start, p.start));
					n.resize(periodWidth(p.start, p.getEnd()), H);
					add(n);
				});
			}
		}
	}
}