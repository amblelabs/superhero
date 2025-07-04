package mc.duzo.timeless.block.entity;

import mc.duzo.timeless.core.TimelessBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import mc.duzo.timeless.suit.Suit;
import mc.duzo.timeless.suit.set.SetRegistry;
import mc.duzo.timeless.suit.set.SuitSet;

public class SuitApplicationBlockEntity extends BlockEntity implements BlockEntityTicker<SuitApplicationBlockEntity> {
    private SuitSet set;
    private int cooldown;

    public SuitApplicationBlockEntity(BlockPos pos, BlockState state) {
        super(TimelessBlockEntityTypes.SUIT_APPLICATION_BE, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("Set")) {
            this.setSet(Identifier.tryParse(nbt.getString("Set")));
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        if (this.set != null) {
            nbt.putString("Set", this.set.id().toString());
        }
    }

    public SuitSet getSet() {
        return set;
    }
    private void setSet(SuitSet set) {
        this.set = set;
        this.markDirty();
    }
    private void setSet(Identifier id) {
        this.setSet(SetRegistry.REGISTRY.get(id));
    }
    private void setSet(Suit suit) {
        this.setSet(suit.getSet());
    }
    public boolean onCooldown() {
        return this.cooldown > 0;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, SuitApplicationBlockEntity blockEntity) {
        if (this.onCooldown()) {
            this.cooldown--;
        }
    }

    public void useOn(World world, boolean sneaking, PlayerEntity player) {

    }

    public void onStepOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!(entity instanceof ServerPlayerEntity player)) return;
        if (this.onCooldown()) return;
        this.cooldown = 60;

        Suit found = Suit.findSuit(player).orElse(null);

        if (found == null) {
            // try apply suit
            if (this.set == null) return; // no set to apply

            boolean success = this.set.wear(player);

            if (success) {
                this.set = null;
                this.markDirty();
            }

            return;
        }

        // try remove suit
        found.getSet().clear(player);
        this.setSet(found.getSet());
    }
}
