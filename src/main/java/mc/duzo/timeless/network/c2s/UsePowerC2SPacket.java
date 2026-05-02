package mc.duzo.timeless.network.c2s;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import mc.duzo.timeless.Timeless;
import mc.duzo.timeless.suit.Suit;

public record UsePowerC2SPacket(int power) implements FabricPacket {
    public static final PacketType<UsePowerC2SPacket> TYPE = PacketType.create(new Identifier(Timeless.MOD_ID, "use_power"), UsePowerC2SPacket::new);

    private static final long COOLDOWN_TICKS = 5;
    private static final Map<UUID, Long> LAST_USE = new ConcurrentHashMap<>();

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
        if (source.getServer() == null) return false;

        long now = source.getServer().getOverworld().getTime();
        Long last = LAST_USE.get(source.getUuid());
        if (last != null && now - last < COOLDOWN_TICKS) return false;
        LAST_USE.put(source.getUuid(), now);

        Optional<Suit> suit = Suit.findSuit(source);
        if (suit.isEmpty()) return false;

        return suit.get().getPowers().run(this.power - 1, source);
    }
}
