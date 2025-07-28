package mc.duzo.timeless.suit.api;

import mc.duzo.timeless.core.items.SuitItem;
import mc.duzo.timeless.core.items.SuitMaterial;
import mc.duzo.timeless.datagen.provider.lang.AutomaticSuitEnglish;
import mc.duzo.timeless.datagen.provider.model.AutomaticModel;
import mc.duzo.timeless.suit.Suit;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

public class SimpleSuitItem extends SuitItem implements AutomaticModel, AutomaticSuitEnglish {
	public SimpleSuitItem(Suit suit, Type type, SuitMaterial material) {
		super(suit, material, type, new FabricItemSettings().maxCount(1));
	}
}
