package mc.duzo.timeless.core;

import dev.amble.lib.container.impl.ItemContainer;
import dev.amble.lib.item.AItemSettings;

import mc.duzo.timeless.suit.ironman.mk5.MarkFiveCase;

public class TimelessItems extends ItemContainer {
    public static MarkFiveCase MARK_FIVE_CASE = new MarkFiveCase(new AItemSettings().group(TimelessItemGroups.GROUP));
}
