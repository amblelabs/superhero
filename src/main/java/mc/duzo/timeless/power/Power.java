package mc.duzo.timeless.power;

import mc.duzo.animation.registry.Identifiable;
import mc.duzo.timeless.datagen.provider.lang.Translatable;
import mc.duzo.timeless.suit.Suit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public abstract class Power implements Identifiable, Translatable {
    static {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            Suit suit = Suit.findSuit(handler.player).orElse(null);
            if (suit == null) return;

            suit.getPowers().forEach(power -> power.onLoad(handler.player));
        });
    }

    public abstract boolean run(ServerPlayerEntity player);
    public abstract void tick(ServerPlayerEntity player);
    @Environment(EnvType.CLIENT)
    public void tick(AbstractClientPlayerEntity player) {
        
    }
    public abstract void onLoad(ServerPlayerEntity player);

    @Override
    public String getTranslationKey() {
        return this.id().getNamespace() + ".power." + this.id().getPath();
    }

    public <T extends Power> T register() {
        return (T) PowerRegistry.register(this);
    }


    public static class Builder {
        private final Identifier id;
        private Consumer<ServerPlayerEntity> run = player -> {};
        private Consumer<ServerPlayerEntity> tick = player -> {};
        private Consumer<ServerPlayerEntity> load = player -> {};

        private Builder(Identifier id) {
            this.id = id;
        }
        public static Builder create(Identifier id) {
            return new Builder(id);
        }

        public Builder run(Consumer<ServerPlayerEntity> run) {
            this.run = run;
            return this;
        }

        public Builder tick(Consumer<ServerPlayerEntity> tick) {
            this.tick = tick;
            return this;
        }

        public Builder load(Consumer<ServerPlayerEntity> load) {
            this.load = load;
            return this;
        }

        public Power build() {
            return new Power() {
                @Override
                public Identifier id() {
                    return id;
                }

                @Override
                public boolean run(ServerPlayerEntity player) {
                    run.accept(player);
                    return true;
                }

                @Override
                public void tick(ServerPlayerEntity player) {
                    tick.accept(player);
                }

                @Override
                public void onLoad(ServerPlayerEntity player) {
                    load.accept(player);
                }
            };
        }
    }
}
