package mc.duzo.timeless.suit.moonknight.item;

import mc.duzo.timeless.core.items.SuitItem;
import mc.duzo.timeless.core.items.SuitMaterial;
import mc.duzo.timeless.datagen.provider.lang.AutomaticSuitEnglish;
import mc.duzo.timeless.datagen.provider.model.AutomaticModel;
import mc.duzo.timeless.power.impl.GlidePower;
import mc.duzo.timeless.suit.Suit;
import net.fabricmc.fabric.api.entity.event.v1.FabricElytraItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;

import java.util.EnumMap;

public class MoonKnightSuitItem extends SuitItem implements AutomaticModel, AutomaticSuitEnglish, FabricElytraItem {
    public MoonKnightSuitItem(Suit suit, Type type) {
        super(suit, MoonKnightMaterial.INSTANCE, type, new FabricItemSettings().maxCount(1));
    }

    private static class MoonKnightMaterial extends SuitMaterial {
        public MoonKnightMaterial(Item repair) {
            super("moon_knight", 33, Util.make(new EnumMap(Type.class), map -> {
                map.put(Type.BOOTS, 3);
                map.put(Type.LEGGINGS, 6);
                map.put(Type.CHESTPLATE, 8);
                map.put(Type.HELMET, 3);
            }), 10, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 2.0F, 0.0F, () -> Ingredient.ofItems(repair));
        }

        public MoonKnightMaterial() {
            this(Items.LEATHER); // todo
        }

        public static MoonKnightMaterial INSTANCE = new MoonKnightMaterial();
    }
}
