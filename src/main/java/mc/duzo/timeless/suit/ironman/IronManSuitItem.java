package mc.duzo.timeless.suit.ironman;

import java.util.EnumMap;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;

import mc.duzo.timeless.datagen.provider.lang.AutomaticSuitEnglish;
import mc.duzo.timeless.datagen.provider.model.AutomaticModel;
import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.core.items.SuitItem;
import mc.duzo.timeless.core.items.SuitMaterial;

public class IronManSuitItem extends SuitItem implements AutomaticModel, AutomaticSuitEnglish {
    public IronManSuitItem(Suit suit, Type type) {
        super(suit, IronManMaterial.INSTANCE, type, new FabricItemSettings().maxCount(1));
    }

    private static class IronManMaterial extends SuitMaterial {
        public IronManMaterial(Item repair) {
            super("iron_man", 33, Util.make(new EnumMap(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 3);
                map.put(ArmorItem.Type.LEGGINGS, 6);
                map.put(ArmorItem.Type.CHESTPLATE, 8);
                map.put(ArmorItem.Type.HELMET, 3);
            }), 10, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 2.0F, 0.0F, () -> Ingredient.ofItems(repair));
        }
        public IronManMaterial() {
            this(Items.BLAZE_POWDER); // todo
        }

        public static IronManMaterial INSTANCE = new IronManMaterial();
    }
}
