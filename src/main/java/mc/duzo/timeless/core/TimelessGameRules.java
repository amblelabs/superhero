package mc.duzo.timeless.core;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;

import net.minecraft.world.GameRules;

public final class TimelessGameRules {
    public static GameRules.Key<GameRules.BooleanRule> SUIT_BLOCK_DAMAGE;

    private TimelessGameRules() {}

    public static void init() {
        SUIT_BLOCK_DAMAGE = GameRuleRegistry.register(
                "timelessSuitBlockDamage",
                GameRules.Category.MOBS,
                GameRuleFactory.createBooleanRule(false));
    }
}
