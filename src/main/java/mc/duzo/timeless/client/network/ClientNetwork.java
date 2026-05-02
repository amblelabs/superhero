package mc.duzo.timeless.client.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import mc.duzo.timeless.network.s2c.UpdateFlyingStatusS2CPacket;
import mc.duzo.timeless.network.s2c.UpdateSuitDataS2CPacket;

public class ClientNetwork {
    static {
        ClientPlayNetworking.registerGlobalReceiver(UpdateFlyingStatusS2CPacket.TYPE, UpdateFlyingStatusS2CPacket::handle);
        ClientPlayNetworking.registerGlobalReceiver(UpdateSuitDataS2CPacket.TYPE, UpdateSuitDataS2CPacket::handle);
    }

    public static void init() {

    }
}
