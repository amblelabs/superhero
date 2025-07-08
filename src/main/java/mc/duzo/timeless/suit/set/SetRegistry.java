package mc.duzo.timeless.suit.set;

import mc.duzo.timeless.suit.moonknight.item.MoonKnightSuitItem;
import mc.duzo.timeless.suit.moonknight.jake.JakeSuit;
import mc.duzo.timeless.suit.moonknight.marc.MarcSuit;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.suit.batman.item.BatmanSuitItem;
import mc.duzo.timeless.suit.batman.sixer.Batman66Suit;
import mc.duzo.timeless.suit.ironman.IronManSuitItem;
import mc.duzo.timeless.suit.ironman.mk2.MarkTwoSuit;
import mc.duzo.timeless.suit.ironman.mk3.MarkThreeSuit;
import mc.duzo.timeless.suit.ironman.mk5.MarkFiveSuit;
import mc.duzo.timeless.suit.ironman.mk7.MarkSevenSuit;

public class SetRegistry {
    public static final SimpleRegistry<SuitSet> REGISTRY = FabricRegistryBuilder.createSimple(RegistryKey.<SuitSet>ofRegistry(new Identifier(Timeless.MOD_ID, "suit_set"))).buildAndRegister();

    public static <T extends SuitSet> T register(T suit) {
        return Registry.register(REGISTRY, suit.id(), suit);
    }

    public static SuitSet MARK_SEVEN;
    public static SuitSet MARK_FIVE;
    public static SuitSet MARK_THREE;
    public static SuitSet MARK_TWO;
    public static SuitSet BATMAN_66;
    public static SuitSet MOON_KNIGHT_MARC;
    public static SuitSet MOON_KNIGHT_JAKE;

    public static void init() {
        // Iron Man
        MARK_SEVEN = register(new RegisteringSuitSet(new MarkSevenSuit(), IronManSuitItem::new));
        MARK_FIVE = register(new RegisteringSuitSet(new MarkFiveSuit(), IronManSuitItem::new));
        MARK_THREE = register(new RegisteringSuitSet(new MarkThreeSuit(), IronManSuitItem::new));
        MARK_TWO = register(new RegisteringSuitSet(new MarkTwoSuit(), IronManSuitItem::new));

        // Batman
        BATMAN_66 = register(new RegisteringSuitSet(new Batman66Suit(), BatmanSuitItem::new));
        MOON_KNIGHT_MARC = register(new RegisteringSuitSet(new MarcSuit(), MoonKnightSuitItem::new));
        MOON_KNIGHT_JAKE = register(new RegisteringSuitSet(new JakeSuit(), MoonKnightSuitItem::new));
    }
}
