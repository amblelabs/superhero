package mc.duzo.timeless.network.s2c;

import java.util.UUID;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.util.SuitDataAccess;

public record UpdateSuitDataS2CPacket(UUID player, NbtCompound data) implements FabricPacket {
    public static final PacketType<UpdateSuitDataS2CPacket> TYPE = PacketType.create(new Identifier(Timeless.MOD_ID, "update_suit_data"), UpdateSuitDataS2CPacket::new);

    public UpdateSuitDataS2CPacket(PacketByteBuf buf) {
        this(buf.readUuid(), buf.readNbt());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeUuid(player);
        buf.writeNbt(data);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    public void handle(ClientPlayerEntity client, PacketSender sender) {
        PlayerEntity target = client.getWorld().getPlayerByUuid(player);
        if (target instanceof SuitDataAccess access) {
            access.timeless$setSuitData(data);
        }
    }
}
