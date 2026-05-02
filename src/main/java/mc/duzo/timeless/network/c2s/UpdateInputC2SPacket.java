package mc.duzo.timeless.network.c2s;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.util.ServerKeybind;

public record UpdateInputC2SPacket(boolean jumping, boolean forward, boolean left, boolean right, boolean back) implements FabricPacket {
    public static final PacketType<UpdateInputC2SPacket> TYPE = PacketType.create(new Identifier(Timeless.MOD_ID, "update_input"), UpdateInputC2SPacket::new);

    public UpdateInputC2SPacket(PacketByteBuf buf) {
        this(buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBoolean(jumping);
        buf.writeBoolean(forward);
        buf.writeBoolean(left);
        buf.writeBoolean(right);
        buf.writeBoolean(back);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    public void handle(ServerPlayerEntity source, PacketSender response) {
        ServerKeybind.Keymap map = ServerKeybind.get(source);
        map.setJumping(jumping);
        map.setForward(forward);
        map.setLeft(left);
        map.setRight(right);
        map.setBackward(back);
    }
}
