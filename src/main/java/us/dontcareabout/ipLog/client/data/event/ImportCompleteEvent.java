package us.dontcareabout.ipLog.client.data.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.ipLog.client.data.event.ImportCompleteEvent.ImportCompleteHandler;

public class ImportCompleteEvent extends GwtEvent<ImportCompleteHandler> {
	public static final Type<ImportCompleteHandler> TYPE = new Type<ImportCompleteHandler>();

	@Override
	public Type<ImportCompleteHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ImportCompleteHandler handler) {
		handler.onImportComplete(this);
	}

	public interface ImportCompleteHandler extends EventHandler{
		public void onImportComplete(ImportCompleteEvent event);
	}
}
