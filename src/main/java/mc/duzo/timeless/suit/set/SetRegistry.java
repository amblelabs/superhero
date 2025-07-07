package mc.duzo.timeless.suit.set;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.suit.batman.item.BatmanSuitItem;
import mc.duzo.timeless.suit.batman.sixer.Batman66Suit;
import mc.duzo.timeless.suit.ironman.IronManSuitItem;
import mc.duzo.timeless.suit.ironman.mk2.MarkTwoSuit;
import mc.duzo.timeless.suit.ironman.mk3.MarkThreeSuit;
import mc.duzo.timeless.suit.ironman.mk5.MarkFiveSuit;
import mc.duzo.timeless.suit.ironman.mk7.MarkSevenSuit;
import mc.duzo.timeless.suit.spiderman.item.SpidermanSuitItem;
import mc.duzo.timeless.suit.spiderman.miles.MilesSuit;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

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
    public static SuitSet MILES;

    public static void init() {
        // Iron Man
        MARK_SEVEN = register(new RegisteringSuitSet(new MarkSevenSuit(), IronManSuitItem::new));
        MARK_FIVE = register(new RegisteringSuitSet(new MarkFiveSuit(), IronManSuitItem::new));
        MARK_THREE = register(new RegisteringSuitSet(new MarkThreeSuit(), IronManSuitItem::new));
        MARK_TWO = register(new RegisteringSuitSet(new MarkTwoSuit(), IronManSuitItem::new));

        // Batman
        BATMAN_66 = register(new RegisteringSuitSet(new Batman66Suit(), BatmanSuitItem::new));

        // Spiderman
        MILES = register(new RegisteringSuitSet(new MilesSuit(), SpidermanSuitItem::new));
    }
}
