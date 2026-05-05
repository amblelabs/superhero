package mc.duzo.timeless.network.c2s;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.entity.WebRopeEntity;

public record WebRappelC2SPacket(int direction) implements FabricPacket {
    public static final PacketType<WebRappelC2SPacket> TYPE = PacketType.create(new Identifier(Timeless.MOD_ID, "web_rappel"), WebRappelC2SPacket::new);

    public WebRappelC2SPacket(PacketByteBuf buf) {
        this(buf.readByte());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeByte(direction);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    public boolean handle(ServerPlayerEntity source, PacketSender response) {
        if (source.getServerWorld() == null) return false;
        for (Entity e : source.getServerWorld().iterateEntities()) {
            if (e instanceof WebRopeEntity rope
                    && source.getUuid().equals(rope.getShooterUuid())
                    && rope.isAnchored()
                    && !rope.isDisconnected()
                    && rope.getMode() == WebRopeEntity.Mode.SWING) {
                rope.rappel(direction);
                return true;
            }
        }
        return false;
    }
}
