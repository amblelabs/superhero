package mc.duzo.timeless.core.items;

import mc.duzo.timeless.suit.set.SetRegistry;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MoonTotemItem extends Item {
    public static final int MAX_BLOOD_METER = 50; // Maximum blood meter value

    static {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((serverWorld, entity, other) -> {
            if (entity instanceof ServerPlayerEntity player) {
                DefaultedList<ItemStack> list = player.getInventory().main;
                for (ItemStack stack : list) {
                    if (stack.getItem() instanceof MoonTotemItem totemItem) {
                        NbtCompound nbt = stack.getOrCreateNbt();
                        totemItem.addToBloodMeter(serverWorld.isNight() ? 1 : 5, nbt);
                        player.sendMessage(Text.literal("Khonshu smiles upon you, your blood meter is now: " + totemItem.getBloodMeter(nbt) + "/" + MAX_BLOOD_METER)
                                .formatted(Formatting.DARK_RED, Formatting.BOLD), true);
                        System.out.println(nbt);
                        break;
                    }
                }
                // Check offhand as well
                ItemStack offhandStack = player.getOffHandStack();
                if (offhandStack.getItem() instanceof MoonTotemItem totemItem) {
                    NbtCompound nbt = offhandStack.getOrCreateNbt();
                    totemItem.addToBloodMeter(serverWorld.isNight() ? 1 : 5, nbt);
                    player.sendMessage(Text.literal("Khonshu smiles upon you, your blood meter is now: " + totemItem.getBloodMeter(nbt) + "/" + MAX_BLOOD_METER)
                            .formatted(Formatting.DARK_RED, Formatting.BOLD), true);
                }
            }
        });
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt("bloodMeter", 0);
        nbt.putBoolean("isFullMoon", false);
        return super.getDefaultStack();
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (world.isClient) return; // Only run on the server side
        if (!world.isNight()) return;
        NbtCompound nbt = stack.getOrCreateNbt();
        boolean isFullMoon = world.getMoonSize() > 0.9f;
        if (isFullMoon && entity instanceof ServerPlayerEntity player && !stack.getOrCreateNbt().getBoolean("isFullMoon")) {
            player.sendMessage(Text.literal("The full moon rises, make your choice now champion.").formatted(Formatting.AQUA, Formatting.BOLD), true);
        }
        nbt.putBoolean("isFullMoon", isFullMoon);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getPlayer() == null || context.getWorld().isClient) {
            return ActionResult.PASS; // Only process on the server side
        }
        ItemStack stack = context.getStack();

        if (stack.getItem() instanceof MoonTotemItem totemItem) {
            NbtCompound nbt = stack.getOrCreateNbt();
            if (totemItem.shouldPulsate(nbt)) {
                if (context.getPlayer().getWorld().isClient()) {
                    MinecraftClient.getInstance().gameRenderer.showFloatingItem(context.getStack());
                }
                totemItem.setBloodMeter(0, nbt); // Reset blood meter on use
                context.getPlayer().sendMessage(Text.literal("You are now the Champion of Khonshu, your blood meter is zero.")
                        .formatted(Formatting.GREEN, Formatting.BOLD), true);
                SetRegistry.MOON_KNIGHT_MARC.wear(context.getPlayer());
                return ActionResult.CONSUME;
            } else {
                context.getPlayer().sendMessage(Text.literal("The Moon Totem is not ready to be used yet.")
                        .formatted(Formatting.RED, Formatting.BOLD), true);
                return ActionResult.FAIL;
            }
        }
        return super.useOnBlock(context);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getMainHandStack();

        if (stack.getItem() instanceof MoonTotemItem totemItem) {
            NbtCompound nbt = stack.getOrCreateNbt();
            if (totemItem.shouldPulsate(nbt)) {
                if (user.getWorld().isClient()) {
                    MinecraftClient.getInstance().gameRenderer.showFloatingItem(stack);
                }
                totemItem.setBloodMeter(0, nbt); // Reset blood meter on use
                user.sendMessage(Text.literal("You are now the Champion of Khonshu, your blood meter is zero.")
                        .formatted(Formatting.GREEN, Formatting.BOLD), true);
                SetRegistry.MOON_KNIGHT_MARC.wear(user);
                return TypedActionResult.consume(stack);
            } else {
                user.sendMessage(Text.literal("The Moon Totem is not ready to be used yet.")
                        .formatted(Formatting.RED, Formatting.BOLD), true);
                return TypedActionResult.fail(stack);
            }
        }

        return super.use(world, user, hand);
    }

    public MoonTotemItem(Settings settings) {
        super(settings);
    }

    public void addToBloodMeter(int amount, NbtCompound nbt) {
        this.setBloodMeter(Math.max(0, Math.min(this.getBloodMeter(nbt) + amount, MAX_BLOOD_METER)), nbt);
    }

    public void setBloodMeter(int amount, NbtCompound nbt) {
        nbt.putInt("bloodMeter", Math.max(0, Math.min(amount, MAX_BLOOD_METER)));
    }

    public int getBloodMeter(NbtCompound nbt) {
        return nbt.getInt("bloodMeter");
    }

    public boolean isFullMoon(NbtCompound nbt) {
        return nbt.getBoolean("isFullMoon");
    }

    public boolean shouldPulsate(NbtCompound nbt) {
        return nbt.getInt("bloodMeter") >= MAX_BLOOD_METER && this.isFullMoon(nbt);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        NbtCompound nbt = stack.getOrCreateNbt();

        int bloodMeter = !nbt.contains("bloodMeter") ? 0 : this.getBloodMeter(nbt);
        boolean isFullMoon = nbt.contains("isFullMoon") && this.isFullMoon(nbt);

        tooltip.add(Text.literal("Blood Meter: " + bloodMeter + "/" + MAX_BLOOD_METER)
                .formatted(Formatting.RED, Formatting.BOLD));
        tooltip.add(Text.literal("Full Moon: " + isFullMoon)
                .formatted(Formatting.RED, Formatting.BOLD));
    }
}
