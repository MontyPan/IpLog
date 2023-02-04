package us.dontcareabout.ipLog.client;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.i18n.client.DateTimeFormat;

import us.dontcareabout.ipLog.client.vo.IpData;

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


	public static String toString(Map<String, IpData> ipMap) {
		StringBuilder result = new StringBuilder();

		for (String ip : ipMap.keySet()) {
			IpData ipData = ipMap.get(ip);
			if (ipData.getNameList().size() < 2) { continue; }
			if (!ipData.hasOverlap()) { continue; }

			result.append(toString(ip, ipData.asOverlapList()));
			result.append("\n\n");
		}

		return result.toString();
	}

	private static String toString(String ip, List<IpData> overlapList) {
		StringBuilder result = new StringBuilder("《" + ip + "》");
		overlapList.forEach(ipd -> {
			//時間區間
			result.append("\n");
			result.append(
				Util.dateFormat.format(ipd.getStart()) + "～" +
				Util.dateFormat.format(ipd.getEnd()) +
				"（" + Util.hourCeilDiff(ipd.getStart(), ipd.getEnd()) + "hr）"
			);

			HashMap<String, Integer> countMap = new HashMap<>();

			ipd.getPeriodList().forEach(np -> {
				Integer count = countMap.get(np.name);

				if (count == null) {
					countMap.put(np.name, np.getCount());
				} else {
					countMap.put(np.name, np.getCount() + count);
				}
			});

			//名稱 : 次數
			countMap.keySet().forEach(name -> {
				result.append("\n    " + name + " : " + countMap.get(name));
			});
			result.append("\n");
		});

		return result.toString();
	}
}