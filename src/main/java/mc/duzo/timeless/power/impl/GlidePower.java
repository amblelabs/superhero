package mc.duzo.timeless.power.impl;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.power.PowerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class GlidePower extends CapeTogglePower {
    public GlidePower() {
        super();
    }

    @Override
    public Identifier id() {
        return new Identifier(Timeless.MOD_ID, "glide");
    }

    public static boolean hasGlide(PlayerEntity player) {
        return PowerRegistry.GLIDE_POWER.getValue(player);
    }
}
