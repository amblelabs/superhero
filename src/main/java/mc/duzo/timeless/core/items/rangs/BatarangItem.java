package mc.duzo.timeless.core.items.rangs;

import mc.duzo.timeless.core.TimelessItems;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class BatarangItem extends BaseRangItem {
    public BatarangItem(Settings settings) {
        super(settings);
    }

    @Override
    public Item getDefaultItem() {
        return TimelessItems.BATARANG;
    }

    @Override
    public SoundEvent getDefaultSound() {
        return SoundEvents.ITEM_TRIDENT_THROW;
    }
}
