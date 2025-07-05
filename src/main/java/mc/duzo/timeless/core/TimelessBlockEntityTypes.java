package mc.duzo.timeless.core;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.block.entity.SuitApplicationBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TimelessBlockEntityTypes {
    public static final BlockEntityType<SuitApplicationBlockEntity> SUIT_APPLICATION_BE = register(Registries.BLOCK_ENTITY_TYPE, "suit_application", BlockEntityType.Builder.create(SuitApplicationBlockEntity::new, TimelessBlocks.SUIT_APPLICATION).build(null));

    public static void init() {

    }

    public static <V, T extends V> T register(Registry<V> registry, String name, T entry) {
        return Registry.register(registry, new Identifier(Timeless.MOD_ID, name), entry);
    }
}
