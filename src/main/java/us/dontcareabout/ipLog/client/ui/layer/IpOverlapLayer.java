package us.dontcareabout.ipLog.client.ui.layer;

import java.util.List;

import us.dontcareabout.gxt.client.draw.LSprite;
import us.dontcareabout.gxt.client.draw.layout.HorizontalLayoutLayer;
import us.dontcareabout.ipLog.client.vo.IpData;

public class IpOverlapLayer extends HorizontalLayoutLayer {
	public IpOverlapLayer(List<IpData> data) {
		setGap(3);
		data.stream().forEach(slim -> {
			IpDataLayer ipDL = new IpDataLayer(slim);
			addChild(ipDL, ipDL.getViewSize());
		});

		adjustMember();
	}

	public double computeHeight() {
		double result = 0;

		for (LSprite sprite : getMembers()) {
			if (sprite instanceof IpDataLayer) {
				IpDataLayer ipDL = (IpDataLayer)sprite;
				result = Math.max(result, ipDL.computeHeight());
			}
		}

		return result;
	}
}
