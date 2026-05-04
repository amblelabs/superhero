package mc.duzo.timeless.core.items;

import mc.duzo.animation.registry.Identifiable;

import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import mc.duzo.timeless.network.Network;
import mc.duzo.timeless.network.s2c.UpdateSuitDataS2CPacket;
import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.util.SuitDataAccess;

public abstract class SuitItem extends ArmorItem implements Identifiable {
    private final Suit parent;

    protected SuitItem(Suit suit, ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings.maxCount(1));

        this.parent = suit;
    }

    public boolean isBinding() {
        return this.parent.isBinding();
    }

    @Override
    public Identifier id() {
        return this.parent.id().withSuffixedPath("_" + this.type.getName());
    }

    public Suit getSuit() {
        return this.parent;
    }

    @Override
    public TypedActionResult<ItemStack> equipAndSwap(Item item, World world, PlayerEntity user, Hand hand) {
        if (this.isBinding()) return TypedActionResult.fail(user.getStackInHand(hand));

        return super.equipAndSwap(item, world, user, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (!(entity instanceof PlayerEntity player)) return;
        if (!stack.equals(player.getEquippedStack(EquipmentSlot.CHEST))) return;
        if (!this.parent.getSet().isWearing(player)) return;

        if (world.isClient()) {
            this.parent.getPowers().forEach(power -> power.tick((AbstractClientPlayerEntity) player));
            return;
        }

        this.parent.getPowers().forEach(power -> power.tick((ServerPlayerEntity) player));
    }

    public static class Data {
        private static final String LEGACY_KEY = "SuitData";

        static {
            ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
                ServerPlayerEntity player = handler.getPlayer();
                migrateLegacy(player);
                sync(player);
            });

            EntityTrackingEvents.START_TRACKING.register((tracked, watcher) -> {
                if (!(tracked instanceof ServerPlayerEntity sp)) return;
                NbtCompound data = get(sp);
                if (data == null) return;
                ServerPlayNetworking.send(watcher, new UpdateSuitDataS2CPacket(sp.getUuid(), data));
            });
        }

        private static void migrateLegacy(ServerPlayerEntity player) {
            if (!(player instanceof SuitDataAccess access)) return;
            if (!access.timeless$getSuitData().isEmpty()) return;

            ItemStack chest = player.getEquippedStack(EquipmentSlot.CHEST);
            if (!(chest.getItem() instanceof SuitItem)) return;
            if (!chest.hasNbt()) return;
            NbtCompound chestNbt = chest.getNbt();
            if (!chestNbt.contains(LEGACY_KEY)) return;

            access.timeless$setSuitData(chestNbt.getCompound(LEGACY_KEY).copy());
            chestNbt.remove(LEGACY_KEY);
        }

        public static NbtCompound get(LivingEntity entity) {
            if (!(entity instanceof SuitDataAccess access)) return null;
            return access.timeless$getSuitData();
        }
        public static NbtElement get(LivingEntity entity, String key) {
            NbtCompound data = get(entity);
            if (data == null) return null;
            return data.get(key);
        }
        public static void set(LivingEntity entity, String key, NbtElement value) {
            NbtCompound data = get(entity);
            if (data == null) return;
            data.put(key, value);
            sync(entity);
        }
        public static void putBoolean(LivingEntity entity, String key, boolean value) {
            NbtCompound data = get(entity);
            if (data == null) return;
            data.putBoolean(key, value);
            sync(entity);
        }
        public static boolean getBoolean(LivingEntity entity, String key) {
            NbtCompound data = get(entity);
            if (data == null) return false;
            return data.getBoolean(key);
        }
        public static void putInt(LivingEntity entity, String key, int value) {
            NbtCompound data = get(entity);
            if (data == null) return;
            data.putInt(key, value);
            sync(entity);
        }
        public static int getInt(LivingEntity entity, String key) {
            NbtCompound data = get(entity);
            if (data == null) return 0;
            return data.getInt(key);
        }
        public static void putFloat(LivingEntity entity, String key, float value) {
            NbtCompound data = get(entity);
            if (data == null) return;
            data.putFloat(key, value);
            sync(entity);
        }
        public static float getFloat(LivingEntity entity, String key) {
            NbtCompound data = get(entity);
            if (data == null) return 0f;
            return data.getFloat(key);
        }
        public static boolean contains(LivingEntity entity, String key) {
            NbtCompound data = get(entity);
            if (data == null) return false;
            return data.contains(key);
        }
        public static void sync(LivingEntity entity) {
            if (!(entity instanceof ServerPlayerEntity sp)) return;
            NbtCompound data = get(sp);
            if (data == null) return;
            Network.toTracking(new UpdateSuitDataS2CPacket(sp.getUuid(), data), sp);
        }
    }
}
