package us.dontcareabout.ipLog.client.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import us.dontcareabout.ipLog.client.vo.IpData;
import us.dontcareabout.ipLog.client.vo.RawData;

public class LocalDB {
	private int validityHour = 8;

	private List<RawData> rawList;
	private HashMap<String, IpData> ipMap = new HashMap<>();
	private Set<String> nameSet = new HashSet<>();

	public void importRaw(List<RawData> list) {
		rawList = list;
		build();
	}

	public int getValidityHour() {
		return validityHour;
	}

	public void setValidityHour(int validityHour) {
		this.validityHour = validityHour;

		if (rawList == null) { return; }	//還沒有 import 過不用重新 build

		build();
	}

	public List<RawData> getRawList() {
		return rawList;
	}

	public HashMap<String, IpData> getIpMap() {
		return ipMap;
	}

	public Set<String> getNameSet() {
		return nameSet;
	}

	private void build() {
		ipMap.clear();
		nameSet.clear();

		rawList.forEach(raw -> {
			nameSet.add(raw.name);
			IpData ip = ipMap.get(raw.ip);

			if (ip == null) {
				ip = new IpData(raw.ip, validityHour);
				ipMap.put(raw.ip, ip);
			}

			ip.add(raw);
		});
	}
}
