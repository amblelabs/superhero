package mc.duzo.timeless.network.c2s;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.core.items.SuitItem;

public record UsePowerC2SPacket(int power) implements FabricPacket {
    public static final PacketType<UsePowerC2SPacket> TYPE = PacketType.create(new Identifier(Timeless.MOD_ID, "use_power"), UsePowerC2SPacket::new);

    public UsePowerC2SPacket(PacketByteBuf buf) {
        this(buf.readInt());
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(power);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    public boolean handle(ServerPlayerEntity source, PacketSender response) {
        if (!(source.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof SuitItem item)) return false;
        return item.getSuit().getPowers().run(this.power - 1, source);
    }
}
