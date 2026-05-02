package mc.duzo.timeless;

import dev.amble.lib.container.RegistryContainer;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mc.duzo.timeless.commands.command.TimelessCommands;
import mc.duzo.timeless.core.*;
import mc.duzo.timeless.network.Network;
import mc.duzo.timeless.power.PowerRegistry;
import mc.duzo.timeless.suit.SuitRegistry;
import mc.duzo.timeless.suit.set.SetRegistry;

public class Timeless implements ModInitializer {
    public static final String MOD_ID = "timeless";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        RegistryContainer.register(TimelessItemGroups.class, Timeless.MOD_ID);
        RegistryContainer.register(TimelessItems.class, Timeless.MOD_ID);
        RegistryContainer.register(TimelessBlocks.class, Timeless.MOD_ID);

        // Suits and powers
        PowerRegistry.init();
        SetRegistry.init();
        SuitRegistry.init();
        TimelessTrackers.init();

        // Register blocks, items, block entities, sounds, and entities
        TimelessBlockEntityTypes.init();
        TimelessEntityTypes.init();
        TimelessSounds.init();
        TimelessItems.init();

        TimelessCommands.init();
        // Networking
        Network.init();
    }
}