package mc.duzo.timeless.suit.api;

import net.minecraft.server.network.ServerPlayerEntity;

public interface FlightSuit {
	int getFlightSpeed(boolean hasBoost);
	default float getHoverScale() {
		return 1;
	}
	default void createFlightParticles(ServerPlayerEntity player) {}
}
