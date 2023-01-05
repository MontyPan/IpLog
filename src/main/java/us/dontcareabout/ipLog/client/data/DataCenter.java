package us.dontcareabout.ipLog.client.data;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

import us.dontcareabout.ipLog.client.Util;
import us.dontcareabout.ipLog.client.data.event.ImportCompleteEvent;
import us.dontcareabout.ipLog.client.data.event.ImportCompleteEvent.ImportCompleteHandler;
import us.dontcareabout.ipLog.client.vo.RawData;

public class DataCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();

	public static LocalDB db = new LocalDB();

	public static void importPlain(String text) {
		db.importRaw(
			Stream.of(text.split("\n"))
				.map(DataCenter::parsePlain)
				.filter(r -> r != null)
				.collect(Collectors.toList())
		);
		eventBus.fireEvent(new ImportCompleteEvent());
	}

	public static HandlerRegistration addImportComplete(ImportCompleteHandler handler) {
		return eventBus.addHandler(ImportCompleteEvent.TYPE, handler);
	}

	private static RawData parsePlain(String line) {
		try {
			return new RawData(
				Util.dateFormat.parse(line.substring(0, 10) + " " + line.substring(16, 24)),
				line.substring(32, 46).trim(),
				line.substring(48).trim()
			);
		} catch (Exception e) {}
		return null;
	}
}
