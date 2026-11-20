/*
 * All Rights Reserved (c) MoriyaShiine
 */

package kitsuyadev.bewitchment.client.screen;

import kitsuyadev.bewitchment.common.entity.DemonMerchant;
import kitsuyadev.bewitchment.common.registry.BWScreenHandlerTypes;

public class BaphometScreenHandler extends DemonScreenHandler {
	public BaphometScreenHandler(int syncId, DemonMerchant merchant) {
		super(BWScreenHandlerTypes.BAPHOMET_SCREEN_HANDLER.get(), syncId, merchant);
		addSlot(new DemonTradeSlot(demonInventory, 3, 26, 64));
		addSlot(new DemonTradeSlot(demonInventory, 4, 134, 64));
	}

	public int getOfferCount() {
		return 5;
	}
}
