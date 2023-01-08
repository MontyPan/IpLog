package us.dontcareabout.ipLog.client.ui.layer;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.core.client.util.DateWrapper;

import us.dontcareabout.gxt.client.draw.LayerSprite;
import us.dontcareabout.gxt.client.draw.component.TextButton;
import us.dontcareabout.gxt.client.draw.layout.HorizontalLayoutLayer;
import us.dontcareabout.gxt.client.draw.layout.VerticalLayoutLayer;
import us.dontcareabout.gxt.client.util.ColorUtil;
import us.dontcareabout.ipLog.client.Util;
import us.dontcareabout.ipLog.client.data.DataCenter;
import us.dontcareabout.ipLog.client.vo.IpData;

public class IpDataLayer extends HorizontalLayoutLayer {
	private static DateTimeFormat dateF = DateTimeFormat.getFormat("yyyy/MM/dd");
	private static DateTimeFormat timeF = DateTimeFormat.getFormat("HH:mm");

	public final IpData data;

	public IpDataLayer(IpData data) {
		this.data = data;

		addChild(new DateLayer(data.getStart()), 100);
		addChild(
			new PeriodLayer(),
			periodWidth(data.getStart(), addValidity(data.getEnd(), data.validityHour)) + MARGIN * 2
		);
		addChild(new DateLayer(data.getEnd()), 100);

		adjustMember();
	}

	public double computeHeight() {
		int size = data.getNameList().size();
		return H + //IP 欄
			+ MARGIN * 2 //上下都有
			+ size * H - (size - 1) * H_OVERLAP;
	}

	////////

	private static int MARGIN = 5;
	private static int H = 30;
	private static int H_OVERLAP = 8;

	private class PeriodLayer extends LayerSprite {
		TextButton ip = new TextButton(data.ip);

		PeriodLayer() {
			setBgColor(RGB.LIGHTGRAY);

			ip.setBgColor(RGB.BLACK);
			ip.setTextColor(RGB.WHITE);
			ip.setLX(0);
			ip.setLY(0);
			ip.resize(120, H);	//w 值給多少不重要，adjustMember() 會蓋掉
			add(ip);

			data.getPeriodList().forEach(p -> {
				int nameIndex = data.getNameList().indexOf(p.name);
				TextButton n = new TextButton("" + p.getCount());
				n.setBgOpacity(0.85);
				n.setBgRadius(5);
				n.setBgColor(
					ColorUtil.differential(DataCenter.db.getAllName().indexOf(p.name))
				);
				n.setLX(periodWidth(data.getStart(), p.start) + MARGIN);
				n.setLY(
					(nameIndex == 0 ? 0 : nameIndex * (H - H_OVERLAP))
					+ H + MARGIN
				);
				n.resize(periodWidth(p.start, addValidity(p.getEnd(), data.validityHour)), H);
				add(n);
			});
		}

		@Override
		protected void adjustMember() {
			getMembers().forEach(m -> {
				if (!(m instanceof TextButton)) { return; }

				TextButton b = (TextButton)m;
				b.resize(b == ip ? getWidth() : b.getWidth(), b.getHeight());
			});
		}
	}

	private class DateLayer extends VerticalLayoutLayer {
		DateLayer(Date dateTime) {
			addChild(new TextButton(dateF.format(dateTime)), H);
			addChild(new TextButton(timeF.format(dateTime)), H);
		}
	}

	private static int periodWidth(Date start, Date end) {
		return Util.minuteCeilDiff(start, end);
	}

	private static Date addValidity(Date date, int validity) {
		return new DateWrapper(date).addHours(validity).asDate();
	}
}
