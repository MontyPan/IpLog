package us.dontcareabout.ipLog.client.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.sencha.gxt.core.client.util.DateWrapper;

/**
 * 同一個 IP，哪些人（name）用了哪些時段（period）。
 */
public class IpData {
	public final String ip;
	public final int validityHour;

	/** 為了 {@link #asOverlapList()} 方便寫，所以存一下 */
	private List<RawData> rawList = new ArrayList<>();
	private List<NamePeriod> periodList = new ArrayList<>();
	private Set<String> nameSet = new HashSet<>();
	private Date start =  new Date();
	private Date end = new Date(0);
	private List<IpData> overlapList;

	public IpData(String ip, int validityHour) {
		this.ip = ip;
		this.validityHour = validityHour;
	}

	public void add(RawData raw) {
		if (!ip.equals(raw.ip)) { return; }	//習慣性預防 XD

		overlapList = null;	//預防 asOverlapMode() 之後又 add（謎之聲：誰敢？）
		rawList.add(raw);

		//raw 一定會加進來，所以稱這個時候處理 start / end
		if (start.after(raw.date)) { start = raw.date; }
		if (end.before(raw.date)) { end = raw.date; }

		for (NamePeriod np : periodList) {
			if (!np.name.equals(raw.name)) { continue; }

			//要加入的時間在有效期限內，所以合併進去
			if (inValidity(raw.date, np.getEnd(), validityHour)) {
				np.setEnd(raw.date);
				return;
			}
		}

		//找不到同樣 name、或是有同樣 name 但是時間間隔過長
		//就要增加一筆新的 NamePeriod
		NamePeriod np = new NamePeriod(raw.name, raw.date);
		periodList.add(np);
		nameSet.add(raw.name);
	}

	public List<NamePeriod> getPeriodList() {
		return periodList;
	}

	public List<NamePeriod> getPeriodList(String name) {
		return periodList.stream().filter(p -> name.equals(p.name))
			.collect(Collectors.toList());
	}

	public List<String> getNameList() {
		return new ArrayList<>(nameSet);
	}

	/**
	 * @return 整個 IpData 的起始時間
	 */
	public Date getStart() {
		return start;
	}

	/**
	 * @return 整個 IpData 的結束時間
	 */
	public Date getEnd() {
		return end;
	}

	/**
	 * 會把有時間重疊的部份切出來成獨立的 {@link IpData}。
	 * 會忽略「時間沒有重疊的資料」。
	 */
	public List<IpData> asOverlapList() {
		if (overlapList != null) { return overlapList; }

		//預防萬一，還是一律排序一下
		rawList.sort((e1, e2) -> e1.date.compareTo(e2.date));

		ArrayList<IpData> result = new ArrayList<>();
		IpData tmp = new IpData(ip, validityHour);
		tmp.add(rawList.get(0));

		for (int i = 1; i < rawList.size(); i++) {
			RawData raw = rawList.get(i);

			if (inValidity(raw.date, tmp.getEnd(), validityHour)) {
				tmp.add(raw);
				continue;
			}

			//raw 的時間已經超過時限
			if (tmp.periodList.size() != 1) {  //有 overlap
				result.add(tmp);
			}

			tmp = new IpData(ip, validityHour);
			tmp.add(raw);
		}

		//最後得再判斷一次有沒有 overlap
		if (tmp.periodList.size() != 1) {
			result.add(tmp);
		}

		return result;
	}

	public boolean hasOverlap() {
		return asOverlapList().size() > 0;
	}

	/**
	 * @return target 時間是否在 nowEnd + {@link #validityHour} 之前
	 */
	private static boolean inValidity(Date target, Date nowEnd, int validityHour) {
		return new DateWrapper(target).before(
			new DateWrapper(nowEnd).addHours(validityHour)
		);
	}
}
