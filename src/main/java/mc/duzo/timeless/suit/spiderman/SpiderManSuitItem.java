package mc.duzo.timeless.suit.spiderman;

import mc.duzo.timeless.datagen.provider.lang.AutomaticSuitEnglish;
import mc.duzo.timeless.datagen.provider.model.AutomaticModel;
import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.suit.ironman.IronManSuitItem;
import mc.duzo.timeless.suit.item.SuitItem;
import mc.duzo.timeless.suit.item.SuitMaterial;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;

import java.util.EnumMap;

public class SpiderManSuitItem extends SuitItem implements AutomaticModel, AutomaticSuitEnglish {
    public SpiderManSuitItem(Suit suit, Type type) {
        super(suit, SpiderManSuitItem.SpiderManMaterial.INSTANCE, type, new FabricItemSettings().maxCount(1));
    }

    private static class SpiderManMaterial extends SuitMaterial {
        public SpiderManMaterial(Item repair) {
            super("spider_man", 33, Util.make(new EnumMap(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 3);
                map.put(ArmorItem.Type.LEGGINGS, 6);
                map.put(ArmorItem.Type.CHESTPLATE, 8);
                map.put(ArmorItem.Type.HELMET, 3);
            }), 10, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 2.0F, 0.0F, () -> Ingredient.ofItems(repair));
        }
        public SpiderManMaterial() {
            this(Items.BLAZE_POWDER); // todo
        }

        public static SpiderManSuitItem.SpiderManMaterial INSTANCE = new SpiderManSuitItem.SpiderManMaterial();
    }
}
