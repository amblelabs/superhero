package mc.duzo.timeless.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.JumpingMount;

import mc.duzo.timeless.client.gui.JarvisGui;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void timeless$hideCrosshair(DrawContext context, CallbackInfo ci) {
        if (JarvisGui.shouldSuppressVanillaHud(MinecraftClient.getInstance())) ci.cancel();
    }

    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void timeless$hideHotbar(float tickDelta, DrawContext context, CallbackInfo ci) {
        if (JarvisGui.shouldSuppressVanillaHud(MinecraftClient.getInstance())) ci.cancel();
    }

    @Inject(method = "renderStatusBars", at = @At("HEAD"), cancellable = true)
    private void timeless$hideStatusBars(DrawContext context, CallbackInfo ci) {
        if (JarvisGui.shouldSuppressVanillaHud(MinecraftClient.getInstance())) ci.cancel();
    }

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    private void timeless$hideExperienceBar(DrawContext context, int x, CallbackInfo ci) {
        if (JarvisGui.shouldSuppressVanillaHud(MinecraftClient.getInstance())) ci.cancel();
    }

    @Inject(method = "renderMountJumpBar", at = @At("HEAD"), cancellable = true)
    private void timeless$hideMountJumpBar(JumpingMount mount, DrawContext context, int x, CallbackInfo ci) {
        if (JarvisGui.shouldSuppressVanillaHud(MinecraftClient.getInstance())) ci.cancel();
    }

    @Inject(method = "renderHeldItemTooltip", at = @At("HEAD"), cancellable = true)
    private void timeless$hideHeldItemTooltip(DrawContext context, CallbackInfo ci) {
        if (JarvisGui.shouldSuppressVanillaHud(MinecraftClient.getInstance())) ci.cancel();
    }
}
