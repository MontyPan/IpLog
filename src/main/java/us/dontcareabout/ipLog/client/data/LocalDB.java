package us.dontcareabout.ipLog.client.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import us.dontcareabout.ipLog.client.vo.IpData;
import us.dontcareabout.ipLog.client.vo.RawData;

public class LocalDB {
	private int validityHour = 1;

	private List<RawData> rawList;
	private HashMap<String, IpData> ipDataMap = new HashMap<>();
	private HashMap<String, List<IpData>> ipOverlapMap = new HashMap<>();
	private HashMap<String, Set<String>> nameIpMap = new HashMap<>();

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

	public HashMap<String, IpData> getIpDataMap() {
		return ipDataMap;
	}

	public HashMap<String, List<IpData>> getIpOverlapMap() {
		return ipOverlapMap;
	}

	public List<String> getAllName() {
		return nameIpMap.keySet().stream().collect(Collectors.toList());
	}

	public Set<String> getUsedIp(String name) {
		return nameIpMap.get(name);
	}

	private void build() {
		ipDataMap.clear();
		nameIpMap.clear();

		rawList.forEach(raw -> {
			IpData ip = ipDataMap.get(raw.ip);

			if (ip == null) {
				ip = new IpData(raw.ip, validityHour);
				ipDataMap.put(raw.ip, ip);
			}

			ip.add(raw);

			Set<String> ipList = nameIpMap.get(raw.name);

			if (ipList == null) {
				ipList = new HashSet<>();
				nameIpMap.put(raw.name, ipList);
			}

			ipList.add(raw.ip);
		});

		//ipDataMap 建完才能搞這個
		ipOverlapMap.clear();

		for (String ip : ipDataMap.keySet()) {
			IpData ipData = ipDataMap.get(ip);
			if (ipData.getNameList().size() < 2) { continue; }
			if (!ipData.hasOverlap()) { continue; }
			ipOverlapMap.put(ip, ipData.asOverlapList());
		}
	}
}
