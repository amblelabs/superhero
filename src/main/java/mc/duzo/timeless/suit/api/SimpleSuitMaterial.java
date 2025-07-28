package mc.duzo.timeless.suit.api;

import mc.duzo.timeless.core.items.SuitMaterial;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;

import java.util.EnumMap;

public class SimpleSuitMaterial extends SuitMaterial {
	public SimpleSuitMaterial(Item repair, String name) {
		super(name, 33, Util.make(new EnumMap(ArmorItem.Type.class), map -> {
			map.put(ArmorItem.Type.BOOTS, 3);
			map.put(ArmorItem.Type.LEGGINGS, 6);
			map.put(ArmorItem.Type.CHESTPLATE, 8);
			map.put(ArmorItem.Type.HELMET, 3);
		}), 10, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 2.0F, 0.0F, () -> Ingredient.ofItems(repair));
	}
}
