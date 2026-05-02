package mc.duzo.timeless.util;

import java.util.HashMap;
import java.util.UUID;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import net.minecraft.server.network.ServerPlayerEntity;

public class ServerKeybind {
    public static final HashMap<UUID, Keymap> MOVEMENT = new HashMap<>();

    static {
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> MOVEMENT.remove(handler.getPlayer().getUuid()));
    }

    public static Keymap get(UUID id) {
        return MOVEMENT.computeIfAbsent(id, k -> new Keymap());
    }
    public static Keymap get(ServerPlayerEntity player) {
        return get(player.getUuid());
    }

    public static class Keymap extends HashMap<String, Boolean> {
        public boolean isMovingForward() {
            return this.computeIfAbsent("forward", k -> false);
        }
        public void setForward(boolean val) {
            this.put("forward", val);
        }

        public boolean isMovingLeft() {
            return this.computeIfAbsent("left", k -> false);
        }
        public void setLeft(boolean val) {
            this.put("left", val);
        }

        public boolean isMovingRight() {
            return this.computeIfAbsent("right", k -> false);
        }
        public void setRight(boolean val) {
            this.put("right", val);
        }
        public boolean isMovingBackward() {
            return this.computeIfAbsent("backward", k -> false);
        }
        public void setBackward(boolean val) {
            this.put("backward", val);
        }

        public boolean isJumping() {
            return this.computeIfAbsent("jump", k -> false);
        }
        public void setJumping(boolean val) {
            this.put("jump", val);
        }
    }
}
