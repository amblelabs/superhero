package mc.duzo.timeless.item;

import java.util.List;
import java.util.Map;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.*;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPointerImpl;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

public class BatRadioItem extends Item {
    public BatRadioItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient) {
            BlockPos pos = context.getBlockPos();
            context.getStack().getOrCreateNbt().putLong("LinkedPos", pos.asLong());
            context.getPlayer().sendMessage(Text.literal("Linked to " + pos.toShortString()).formatted(Formatting.GREEN), true);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) return TypedActionResult.success(user.getStackInHand(hand));

        ItemStack stack = user.getStackInHand(hand);
        if (!stack.hasNbt() || !stack.getNbt().contains("LinkedPos")) {
            user.sendMessage(Text.literal("No block linked.").formatted(Formatting.RED), true);
            return TypedActionResult.success(stack);
        }

        BlockPos linkedPos = BlockPos.fromLong(stack.getNbt().getLong("LinkedPos"));
        BlockState state = world.getBlockState(linkedPos);
        Block block = state.getBlock();

        if (block == Blocks.REDSTONE_LAMP) {
            boolean lit = state.get(RedstoneLampBlock.LIT);
            world.setBlockState(linkedPos, state.with(RedstoneLampBlock.LIT, !lit), 3);
            user.sendMessage(Text.literal("Toggled Redstone Lamp").formatted(Formatting.GREEN), true);
        } else if (block == Blocks.DISPENSER) {
            if (world instanceof ServerWorld serverWorld) {
                dispense(serverWorld, linkedPos);
                user.sendMessage(Text.literal("Dispensed item!").formatted(Formatting.GREEN), true);
            }
        } else if (block == Blocks.TNT) {
            if (world instanceof ServerWorld serverWorld) {
                world.removeBlock(linkedPos, false);
                TntEntity tnt = new TntEntity(serverWorld, linkedPos.getX() + 0.5, linkedPos.getY(), linkedPos.getZ() + 0.5, user);
                serverWorld.spawnEntity(tnt);
                user.sendMessage(Text.literal("Activated TNT!").formatted(Formatting.GREEN), true);
            }
        } else {
            world.updateNeighbors(linkedPos, block);
            user.sendMessage(Text.literal("Triggered block update").formatted(Formatting.GREEN), true);
        }

        return TypedActionResult.success(stack);
    }

    protected void dispense(ServerWorld world, BlockPos pos) {
        BlockPointerImpl pointer = new BlockPointerImpl(world, pos);
        BlockEntity blockEntity = pointer.getBlockEntity();
        if (!(blockEntity instanceof DispenserBlockEntity dispenser)) return;
        int slot = dispenser.chooseNonEmptySlot(world.random);
        if (slot < 0) {
            world.syncWorldEvent(WorldEvents.DISPENSER_FAILS, pos, 0);
            world.emitGameEvent(GameEvent.BLOCK_ACTIVATE, pos, GameEvent.Emitter.of(dispenser.getCachedState()));
        } else {
            ItemStack stack = dispenser.getStack(slot);
            DispenserBehavior behavior = getBehaviorForItem(stack);
            if (behavior != DispenserBehavior.NOOP) {
                dispenser.setStack(slot, behavior.dispense(pointer, stack));
            }
        }
    }

    protected DispenserBehavior getBehaviorForItem(ItemStack stack) {
        return BEHAVIORS.getOrDefault(stack.getItem(), new ItemDispenserBehavior());
    }

    private static final Map<Item, DispenserBehavior> BEHAVIORS = Util.make(
            new Object2ObjectOpenHashMap<>(), map -> map.defaultReturnValue(new ItemDispenserBehavior())
    );

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("Right-click on a block to link").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Right-click in air to activate the linked block").formatted(Formatting.GRAY));
        if (stack.hasNbt() && stack.getNbt().contains("LinkedPos")) {
            BlockPos pos = BlockPos.fromLong(stack.getNbt().getLong("LinkedPos"));
            tooltip.add(Text.literal("Linked to: " + pos.toShortString()).formatted(Formatting.GREEN));
        } else {
            tooltip.add(Text.literal("Not linked to any block").formatted(Formatting.RED));
        }
    }

    public void onPlayerChangedDimension(ServerPlayerEntity player) {
        ItemStack stack = player.getMainHandStack();
        if (stack.getItem() instanceof BatRadioItem && stack.hasNbt() && stack.getNbt().contains("LinkedPos")) {
            stack.getNbt().remove("LinkedPos");
            player.sendMessage(Text.literal("Connection lost to linked block!").formatted(Formatting.RED), true);
        }
    }
}
