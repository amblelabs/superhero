package mc.duzo.timeless.power.impl;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.core.items.SuitItem;
import mc.duzo.timeless.power.Power;

public class HoverPower extends Power {
    private final Identifier id;

    public HoverPower() {
        this.id = new Identifier(Timeless.MOD_ID, "hover");
    }

    @Override
    public boolean run(ServerPlayerEntity player) {
        boolean hasHover = hasHover(player);

        if (!hasHover && player.getVelocity().getY() < 0) return false;

        setHover(player, !hasHover);

        return true;
    }

    @Override
    public void tick(ServerPlayerEntity player) {

    }

    @Override
    public void onLoad(ServerPlayerEntity player) {
        setHover(player, hasHover(player));
    }

    private static void setHover(ServerPlayerEntity player, boolean val) {
        SuitItem.Data.putBoolean(player, "HoverEnabled", val);
    }
    public static boolean hasHover(PlayerEntity player) {
        return SuitItem.Data.getBoolean(player, "HoverEnabled");
    }

    @Override
    public Identifier id() {
        return this.id;
    }
}
