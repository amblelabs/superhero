package mc.duzo.timeless.datagen;

import dev.amble.lib.datagen.lang.AmbleLanguageProvider;
import dev.amble.lib.datagen.lang.LanguageType;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.core.TimelessBlocks;
import mc.duzo.timeless.core.TimelessItems;
import mc.duzo.timeless.core.TimelessSounds;
import mc.duzo.timeless.datagen.provider.lang.AutomaticSuitEnglish;
import mc.duzo.timeless.datagen.provider.model.TimelessModelProvider;
import mc.duzo.timeless.datagen.provider.sound.BasicSoundProvider;
import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.suit.SuitRegistry;

public class TimelessDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        FabricDataGenerator.Pack pack = gen.createPack();

        genLang(pack);
        genSounds(pack);
        genModels(pack);
    }

    private void genLang(FabricDataGenerator.Pack pack) {
        genEnglish(pack);
    }

    private void genEnglish(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> {
            AmbleLanguageProvider provider = new AmbleLanguageProvider(output, LanguageType.EN_US);

            for (Suit suit : SuitRegistry.REGISTRY) {
                if (!(suit instanceof AutomaticSuitEnglish)) continue;

                provider.addTranslation(suit.getTranslationKey(), convertToName(suit.id().getPath()));
            }

            provider.translateItems(TimelessItems.class);
            provider.translateBlocks(TimelessBlocks.class);

            provider.addTranslation("itemGroup." + Timeless.MOD_ID, "Timeless Heroes");
            provider.addTranslation("key.categories." + Timeless.MOD_ID, "Suit Actions");

            // Keybinds

            provider.addTranslation("key." + Timeless.MOD_ID + ".power_1", "Action 1");
            provider.addTranslation("key." + Timeless.MOD_ID + ".power_2", "Action 2");
            provider.addTranslation("key." + Timeless.MOD_ID + ".power_3", "Action 3");
            provider.addTranslation("key." + Timeless.MOD_ID + ".power_4", "Action 4");
            provider.addTranslation("key." + Timeless.MOD_ID + ".power_5", "Action 5");
            provider.addTranslation("key." + Timeless.MOD_ID + ".power_6", "Action 6");
            provider.addTranslation("key." + Timeless.MOD_ID + ".power_7", "Action 7");
            provider.addTranslation("key." + Timeless.MOD_ID + ".power_8", "Action 8");
            provider.addTranslation("key." + Timeless.MOD_ID + ".power_9", "Action 9");

            // Items

            provider.addTranslation("item." + Timeless.MOD_ID + ".iron_man_mark_seven_helmet", "Mk. 7 Iron Man Helmet");
            provider.addTranslation("item." + Timeless.MOD_ID + ".iron_man_mark_seven_chestplate", "Mk. 7 Iron Man Chestplate");
            provider.addTranslation("item." + Timeless.MOD_ID + ".iron_man_mark_seven_leggings", "Mk. 7 Iron Man Leggings");
            provider.addTranslation("item." + Timeless.MOD_ID + ".iron_man_mark_seven_boots", "Mk. 7 Iron Man Boots");

            provider.addTranslation("item." + Timeless.MOD_ID + ".iron_man_mark_five_helmet", "Mk. 5 Iron Man Helmet");
            provider.addTranslation("item." + Timeless.MOD_ID + ".iron_man_mark_five_chestplate", "Mk. 5 Iron Man Chestplate");
            provider.addTranslation("item." + Timeless.MOD_ID + ".iron_man_mark_five_leggings", "Mk. 5 Iron Man Leggings");
            provider.addTranslation("item." + Timeless.MOD_ID + ".iron_man_mark_five_boots", "Mk. 5 Iron Man Boots");

            provider.addTranslation("item." + Timeless.MOD_ID + ".iron_man_mark_three_helmet", "Mk. 3 Iron Man Helmet");
            provider.addTranslation("item." + Timeless.MOD_ID + ".iron_man_mark_three_chestplate", "Mk. 3 Iron Man Chestplate");
            provider.addTranslation("item." + Timeless.MOD_ID + ".iron_man_mark_three_leggings", "Mk. 3 Iron Man Leggings");
            provider.addTranslation("item." + Timeless.MOD_ID + ".iron_man_mark_three_boots", "Mk. 3 Iron Man Boots");

            provider.addTranslation("item." + Timeless.MOD_ID + ".iron_man_mark_two_helmet", "Mk. 2 (Prototype) Iron Man Helmet");
            provider.addTranslation("item." + Timeless.MOD_ID + ".iron_man_mark_two_chestplate", "Mk. 2 (Prototype) Iron Man Chestplate");
            provider.addTranslation("item." + Timeless.MOD_ID + ".iron_man_mark_two_leggings", "Mk. 2 (Prototype) Iron Man Leggings");
            provider.addTranslation("item." + Timeless.MOD_ID + ".iron_man_mark_two_boots", "Mk. 2 (Prototype) Iron Man Boots");

            provider.addTranslation("item." + Timeless.MOD_ID + ".iron_man_mark_fifty_helmet", "Mk. 50 Iron Man Helmet");
            provider.addTranslation("item." + Timeless.MOD_ID + ".iron_man_mark_fifty_chestplate", "Mk. 50 Iron Man Chestplate");
            provider.addTranslation("item." + Timeless.MOD_ID + ".iron_man_mark_fifty_leggings", "Mk. 50 Iron Man Leggings");
            provider.addTranslation("item." + Timeless.MOD_ID + ".iron_man_mark_fifty_boots", "Mk. 50 Iron Man Boots");

            provider.addTranslation("item." + Timeless.MOD_ID + ".iron_man_mark_eighty_five_helmet", "Mk. 85 Iron Man Helmet");
            provider.addTranslation("item." + Timeless.MOD_ID + ".iron_man_mark_eighty_five_chestplate", "Mk. 85 Iron Man Chestplate");
            provider.addTranslation("item." + Timeless.MOD_ID + ".iron_man_mark_eighty_five_leggings", "Mk. 85 Iron Man Leggings");
            provider.addTranslation("item." + Timeless.MOD_ID + ".iron_man_mark_eighty_five_boots", "Mk. 85 Iron Man Boots");

            provider.addTranslation("item." + Timeless.MOD_ID + ".batman_66_helmet", "Batman Mask (66)");
            provider.addTranslation("item." + Timeless.MOD_ID + ".batman_66_chestplate", "Batman Chestplate (66)");
            provider.addTranslation("item." + Timeless.MOD_ID + ".batman_66_leggings", "Batman Leggings (66)");
            provider.addTranslation("item." + Timeless.MOD_ID + ".batman_66_boots", "Batman Boots (66)");

            return provider;
        })));
    }

    private void genSounds(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> {
            BasicSoundProvider provider = new BasicSoundProvider(output);

            provider.addSound("thruster", TimelessSounds.THRUSTER);
            provider.addSound("mark5_noises", TimelessSounds.MARK5_NOISES);
            provider.addSound("ironman_step", TimelessSounds.IRONMAN_STEP);
            provider.addSound("ironman_mask", TimelessSounds.IRONMAN_MASK);
            provider.addSound("ironman_powerup", TimelessSounds.IRONMAN_POWERUP);
            provider.addSound("ironman_powerdown", TimelessSounds.IRONMAN_POWERDOWN);
            provider.addSound("ironman_repulsor", TimelessSounds.IRONMAN_REPULSOR);
            provider.addSound("ironman_unibeam_charge", TimelessSounds.IRONMAN_UNIBEAM_CHARGE);
            provider.addSound("ironman_unibeam_fire", TimelessSounds.IRONMAN_UNIBEAM_FIRE);
            provider.addSound("ironman_missile_launch", TimelessSounds.IRONMAN_MISSILE_LAUNCH);
            provider.addSound("ironman_shield_hit", TimelessSounds.IRONMAN_SHIELD_HIT);
            provider.addSound("ironman_targetlock", TimelessSounds.IRONMAN_TARGETLOCK);
            provider.addSound("ironman_nano_assemble", TimelessSounds.IRONMAN_NANO_ASSEMBLE);
            provider.addSound("ironman_boost", TimelessSounds.IRONMAN_BOOST);
            provider.addSound("ironman_dive", TimelessSounds.IRONMAN_DIVE);

            return provider;
        })));
    }

    private void genModels(FabricDataGenerator.Pack pack) {
        pack.addProvider(((output, registriesFuture) -> new TimelessModelProvider(output)));
    }

    private static String convertToName(String str) {
        String[] split = str.split("_");

        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].substring(0, 1).toUpperCase() + split[i].substring(1).toLowerCase();
        }

        return String.join(" ", split);
    }
}
