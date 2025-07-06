package mc.duzo.timeless.suit.batman.item;

import java.util.EnumMap;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;

import mc.duzo.timeless.core.items.SuitItem;
import mc.duzo.timeless.core.items.SuitMaterial;
import mc.duzo.timeless.datagen.provider.lang.AutomaticSuitEnglish;
import mc.duzo.timeless.datagen.provider.model.AutomaticModel;
import mc.duzo.timeless.suit.Suit;

public class BatmanSuitItem extends SuitItem implements AutomaticModel, AutomaticSuitEnglish {
    public BatmanSuitItem(Suit suit, Type type) {
        super(suit, BatmanMaterial.INSTANCE, type, new FabricItemSettings().maxCount(1));
    }

    private static class BatmanMaterial extends SuitMaterial {
        public BatmanMaterial(Item repair) {
            super("iron_man", 33, Util.make(new EnumMap(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 3);
                map.put(ArmorItem.Type.LEGGINGS, 6);
                map.put(ArmorItem.Type.CHESTPLATE, 8);
                map.put(ArmorItem.Type.HELMET, 3);
            }), 10, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 2.0F, 0.0F, () -> Ingredient.ofItems(repair));
        }

        public BatmanMaterial() {
            this(Items.LEATHER); // todo
        }

        public static BatmanMaterial INSTANCE = new BatmanMaterial();
    }
}
