package mc.duzo.timeless.core;

import dev.amble.lib.container.impl.ItemContainer;
import dev.amble.lib.item.AItemSettings;
import mc.duzo.timeless.suit.ironman.mk5.MarkFiveCase;
import mc.duzo.timeless.item.BatRadioItem;

public class TimelessItems extends ItemContainer {
    public static MarkFiveCase MARK_FIVE_CASE = new MarkFiveCase(new AItemSettings().group(TimelessItemGroups.GROUP));
    public static BatRadioItem BAT_RADIO = new BatRadioItem(new AItemSettings().group(TimelessItemGroups.GROUP));
}
