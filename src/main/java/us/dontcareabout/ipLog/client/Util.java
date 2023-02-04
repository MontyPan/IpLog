package us.dontcareabout.ipLog.client;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

public class Util {
	public static final DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy/MM/dd HH:mm:ss");

	/**
	 * @return 兩個時間相差幾分鐘，無條件進位至整數
	 */
	public static int minuteCeilDiff(Date start, Date end) {
		if (start.equals(end)) { return 1; }

		return (int)Math.ceil(
			1.0 * (end.getTime() - start.getTime()) / (60 * 1000)
		);
	}

	public static int hourCeilDiff(Date start, Date end) {
		if (start.equals(end)) { return 1; }

		return (int)Math.ceil(
			1.0 * (end.getTime() - start.getTime()) / (60 * 60 * 1000)
		);
	}
}