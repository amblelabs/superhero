package mc.duzo.timeless.core.items;

import mc.duzo.timeless.core.entities.BaseRangEntity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class RangItem extends Item {
    public RangItem(Item.Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.NEUTRAL, 0.5F, 1F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!world.isClient) {
            //for (int i = 0; i < 3; i++) { // TODO: make this an upgrade for the suit
                BaseRangEntity rangEntity = new BaseRangEntity(world, user);
                rangEntity.setItem(itemStack);
                // Slightly offset each projectile's angle for a burst effect
                float yawOffset = user.getYaw() /*+ (i - 1) * 5.0F*/; // -5, 0, +5 degrees
                rangEntity.setVelocity(user, user.getPitch(), yawOffset, 0.0F, 1.5F, 1.0F);
                world.spawnEntity(rangEntity);
            //}
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        ItemCooldownManager cooldownManager = user.getItemCooldownManager();
        cooldownManager.set(this, world.isNight() && world.getMoonPhase() > 0 ? 10 : 20);

        return TypedActionResult.success(itemStack, world.isClient());
    }
}
