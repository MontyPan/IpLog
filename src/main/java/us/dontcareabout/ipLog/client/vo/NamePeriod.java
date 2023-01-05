package us.dontcareabout.ipLog.client.vo;

import java.util.Date;

public class NamePeriod {
	public final String name;
	public final Date start;
	private Date end;
	private int count;

	public NamePeriod(String name, Date start) {
		this.name = name;
		this.start = start;
		setEnd(start);
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date newEnd) {
		//其實 end 只有 caller 是 c'tor 時才會是 null...
		if (end != null && end.after(newEnd)) { return; }

		end = newEnd;
		count++;
	}

	/** @return 由幾筆記錄合併成一筆 */
	public int getCount() {
		return count;
	}
}
