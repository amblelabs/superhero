package mc.duzo.timeless.core;

import dev.amble.lib.container.impl.ItemGroupContainer;
import dev.amble.lib.itemgroup.AItemGroup;
import mc.duzo.timeless.Timeless;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TimelessItemGroups implements ItemGroupContainer {
    public static final AItemGroup GROUP = AItemGroup.builder(new Identifier(Timeless.MOD_ID, "group"))
            .icon(() -> new ItemStack(TimelessItems.MARK_FIVE_CASE))
            .displayName(Text.translatable("itemGroup." + Timeless.MOD_ID))
            .build();
}