package tuxi.wolfbugs.mixin;

import com.google.common.base.MoreObjects;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.scores.PlayerTeam;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tuxi.wolfbugs.mixininterface.MorphPlayerInfo;

import java.util.Map;

@Mixin(PlayerInfo.class)
public abstract class PlayerInfoMixin implements MorphPlayerInfo {
    @Shadow
    @Final
    private GameProfile profile;
    @Shadow private int latency;
    @Shadow @Nullable
    private String skinModel;
    @Shadow @Final private Map<MinecraftProfileTexture.Type, ResourceLocation> textureLocations;
    @Shadow @Nullable private Component tabListDisplayName;

    @Shadow protected abstract void registerTextures();

    @Unique
    private MorphPlayerInfo wolfBugs$morphedPlayerInfo = null;

    @Override
    public MorphPlayerInfo wolfBugs$getMorphedPlayerInfo() {
        return wolfBugs$morphedPlayerInfo;
    }

    @Override
    public boolean wolfBugs$isMorphed() {
        return wolfBugs$morphedPlayerInfo != null;
    }

    @Override
    public void wolfBugs$morph(MorphPlayerInfo morphIntoPlayerInfo) {
        wolfBugs$morphedPlayerInfo = morphIntoPlayerInfo;
    }

    @Override
    public void wolfBugs$unmorph() {
        wolfBugs$morphedPlayerInfo = null;
    }


    @Override
    public GameProfile wolfBugs$getTrueProfile() {
        return this.profile;
    }
    @Override
    public int wolfBugs$getTrueLatency() {
        return this.latency;
    }
    @Override
    public boolean wolfBugs$isCapeTrulyLoaded() {
        return wolfBugs$getTrueCapeLocation() != null;
    }
    @Override
    public boolean wolfBugs$isSkinTrulyLoaded() {
        return wolfBugs$getTrueSkinLocation() != null;
    }
    @Override
    public String wolfBugs$getTrueModelName() {
        return this.skinModel == null ? DefaultPlayerSkin.getSkinModelName(this.profile.getId()) : this.skinModel;
    }
    @Override
    public ResourceLocation wolfBugs$getTrueSkinLocation() {
        this.registerTextures();
        return MoreObjects.firstNonNull(this.textureLocations.get(MinecraftProfileTexture.Type.SKIN), DefaultPlayerSkin.getDefaultSkin(this.profile.getId()));
    }
    @Override @Nullable
    public ResourceLocation wolfBugs$getTrueCapeLocation() {
        this.registerTextures();
        return this.textureLocations.get(MinecraftProfileTexture.Type.CAPE);
    }
    @Override @Nullable
    public ResourceLocation wolfBugs$getTrueElytraLocation() {
        this.registerTextures();
        return this.textureLocations.get(MinecraftProfileTexture.Type.ELYTRA);
    }
    @Override @Nullable
    public PlayerTeam wolfBugs$getTrueTeam() {
        return Minecraft.getInstance().level.getScoreboard().getPlayersTeam(this.profile.getName());
    }
    @Override @Nullable
    public Component wolfBugs$getTrueTabListDisplayName() {
        return this.tabListDisplayName;
    }

    @Inject(method = "getProfile", at = @At("HEAD"), cancellable = true)
    private void overrideProfile(CallbackInfoReturnable<GameProfile> cir) {
        if (wolfBugs$isMorphed()) {
            cir.setReturnValue(this.wolfBugs$getMorphedPlayerInfo().wolfBugs$getTrueProfile());
        }
    }
    @Inject(method = "getLatency", at = @At("HEAD"), cancellable = true)
    private void overrideLatency(CallbackInfoReturnable<Integer> cir) {
        if (wolfBugs$isMorphed()) {
            cir.setReturnValue(this.wolfBugs$getMorphedPlayerInfo().wolfBugs$getTrueLatency());
        }
    }
    @Inject(method = "isCapeLoaded", at = @At("HEAD"), cancellable = true)
    private void overrideCapeLoaded(CallbackInfoReturnable<Boolean> cir) {
        if (wolfBugs$isMorphed()) {
            cir.setReturnValue(this.wolfBugs$getMorphedPlayerInfo().wolfBugs$isCapeTrulyLoaded());
        }
    }
    @Inject(method = "isSkinLoaded", at = @At("HEAD"), cancellable = true)
    private void overrideSkinLoaded(CallbackInfoReturnable<Boolean> cir) {
        if (wolfBugs$isMorphed()) {
            cir.setReturnValue(this.wolfBugs$getMorphedPlayerInfo().wolfBugs$isSkinTrulyLoaded());
        }
    }
    @Inject(method = "getModelName", at = @At("HEAD"), cancellable = true)
    private void overrideModelName(CallbackInfoReturnable<String> cir) {
        if (wolfBugs$isMorphed()) {
            cir.setReturnValue(this.wolfBugs$getMorphedPlayerInfo().wolfBugs$getTrueModelName());
        }
    }
    @Inject(method = "getSkinLocation", at = @At("HEAD"), cancellable = true)
    private void overrideSkinLocation(CallbackInfoReturnable<ResourceLocation> cir) {
        if (wolfBugs$isMorphed()) {
            cir.setReturnValue(this.wolfBugs$getMorphedPlayerInfo().wolfBugs$getTrueSkinLocation());
        }
    }
    @Inject(method = "getCapeLocation", at = @At("HEAD"), cancellable = true)
    private void overrideCapeLocation(CallbackInfoReturnable<ResourceLocation> cir) {
        if (wolfBugs$isMorphed()) {
            cir.setReturnValue(this.wolfBugs$getMorphedPlayerInfo().wolfBugs$getTrueCapeLocation());
        }
    }
    @Inject(method = "getElytraLocation", at = @At("HEAD"), cancellable = true)
    private void overrideElytraLocation(CallbackInfoReturnable<ResourceLocation> cir) {
        if (wolfBugs$isMorphed()) {
            cir.setReturnValue(this.wolfBugs$getMorphedPlayerInfo().wolfBugs$getTrueElytraLocation());
        }
    }
    @Inject(method = "getTeam", at = @At("HEAD"), cancellable = true)
    private void overrideTeam(CallbackInfoReturnable<PlayerTeam> cir) {
        if (wolfBugs$isMorphed()) {
            cir.setReturnValue(this.wolfBugs$getMorphedPlayerInfo().wolfBugs$getTrueTeam());
        }
    }
    @Inject(method = "getTabListDisplayName", at = @At("HEAD"), cancellable = true)
    private void overrideTabListDisplayName(CallbackInfoReturnable<Component> cir) {
        if (wolfBugs$isMorphed()) {
            cir.setReturnValue(this.wolfBugs$getMorphedPlayerInfo().wolfBugs$getTrueTabListDisplayName());
        }
    }
}
