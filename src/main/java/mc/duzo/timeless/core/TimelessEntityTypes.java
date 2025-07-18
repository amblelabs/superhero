package mc.duzo.timeless.core;

import mc.duzo.timeless.core.entities.BaseRangEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.suit.ironman.IronManEntity;

public class TimelessEntityTypes {
    public static final EntityType<IronManEntity> IRON_MAN = register(Registries.ENTITY_TYPE, "iron_man", EntityType.Builder.<IronManEntity>create(IronManEntity::new, SpawnGroup.MISC)
            .setDimensions(0.6f, 1.8f)
            .build("iron_man"));

    public static final EntityType<BaseRangEntity> BASE_RANG_ENTITY_ENTITY_TYPE = register(Registries.ENTITY_TYPE, "base_rang_entity", EntityType.Builder.<BaseRangEntity>create(BaseRangEntity::new, SpawnGroup.MISC)
            .setDimensions(0.5f, 0.5f)
            .build("base_rang_entity"));

    public static void init() {
        registerAttributes(IRON_MAN, MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3F).build());
    }

    private static void registerAttributes(EntityType<? extends LivingEntity> type, DefaultAttributeContainer attributes) {
        FabricDefaultAttributeRegistry.register(type, attributes);
    }

    public static <V, T extends V> T register(Registry<V> registry, String name, T entry) {
        return Registry.register(registry, new Identifier(Timeless.MOD_ID, name), entry);
    }
}
