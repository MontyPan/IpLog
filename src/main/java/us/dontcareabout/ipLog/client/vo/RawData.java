package us.dontcareabout.ipLog.client.vo;

import java.util.Date;

public class RawData {
	public final Date date;
	public final String ip;
	public final String name;

	public RawData(Date date, String ip, String name) {
		this.date = date;
		this.ip = ip;
		this.name = name;
	}
}
