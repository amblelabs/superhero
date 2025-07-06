package mc.duzo.timeless.core;

import java.util.Objects;

import dev.amble.lib.container.impl.ItemContainer;
import dev.amble.lib.item.AItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import org.jetbrains.annotations.Nullable;

import net.minecraft.item.ItemGroup;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.item.BatRadioItem;
import mc.duzo.timeless.suit.ironman.mk5.MarkFiveCase;
import mc.duzo.timeless.suit.set.SetRegistry;
import mc.duzo.timeless.suit.set.SuitSet;

public class TimelessItems extends ItemContainer {
    public static MarkFiveCase MARK_FIVE_CASE = new MarkFiveCase(new AItemSettings());
    public static BatRadioItem BAT_RADIO = new BatRadioItem(new AItemSettings());

    @Override
    public @Nullable ItemGroup getDefaultGroup() {
        return TimelessItemGroups.GROUP;
    }

    public static void init() {
        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((group, entries) -> {
            if (group == TimelessItemGroups.GROUP) {
                for (SuitSet set : SetRegistry.REGISTRY) {
                    if (!Objects.equals(set.id().getNamespace(), Timeless.MOD_ID)) continue;

                    for (var item : set.values()) {
                        entries.add(item);
                    }
                }
            }
        });
    }
}
