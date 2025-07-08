package mc.duzo.timeless.suit.api;

public interface FlightSuit {
	int getFlightSpeed(boolean hasBoost);
	default float getHoverScale() {
		return 1;
	}
}
