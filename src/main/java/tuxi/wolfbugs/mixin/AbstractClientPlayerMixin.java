package tuxi.wolfbugs.mixin;

import gg.essential.cosmetics.EquippedCosmetic;
import gg.essential.gui.elementa.state.v2.State;
import gg.essential.lib.mixinextras.injector.ModifyReturnValue;
import gg.essential.mixins.impl.client.entity.AbstractClientPlayerExt;
import gg.essential.mod.cosmetics.CosmeticSlot;
import gg.essential.util.UIdentifier;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tuxi.wolfbugs.mixininterface.MorphAbstractClientPlayer;

import java.util.Map;
import java.util.UUID;

@Mixin(value = AbstractClientPlayer.class, priority = 999)
public abstract class AbstractClientPlayerMixin implements MorphAbstractClientPlayer {
    @Unique
    private MorphAbstractClientPlayer wolfBugs$morphedAbstractClientPlayer = null;

    @Unique
    private UUID wolfBugs$trueCosmeticsSourceUuid = null;
    @Unique
    private State<Map<CosmeticSlot, EquippedCosmetic>> wolfBugs$trueCosmeticsSource = null;
    @Unique
    private ResourceLocation wolfBugs$trueCapeTextureLocation = null;
    @Unique
    private UIdentifier wolfBugs$trueEmissiveCapeTexture = null;
    @Unique
    private float wolfBugs$trueCosmeticFrozenYaw = Float.NaN;

    @Override
    public boolean wolfBugs$isMorphed() {
        return wolfBugs$morphedAbstractClientPlayer != null;
    }

    @Override
    public void wolfBugs$morph(MorphAbstractClientPlayer morphIntoPlayer) {
        wolfBugs$morphedAbstractClientPlayer = morphIntoPlayer;
    }

    @Override
    public void wolfBugs$unmorph() {
        wolfBugs$morphedAbstractClientPlayer = null;
    }

    @Override
    public UUID wolfBugs$getTrueCosmeticsSourceUuid() {
        if (wolfBugs$trueCosmeticsSourceUuid == null)
            ((AbstractClientPlayerExt) this).getCosmeticsSourceUuid();
        return wolfBugs$trueCosmeticsSourceUuid;
    }

    @Override
    public State<Map<CosmeticSlot, EquippedCosmetic>> wolfBugs$getTrueCosmeticsSource() {
        if (wolfBugs$trueCosmeticsSource == null)
            ((AbstractClientPlayerExt) this).getCosmeticsSource();
        return wolfBugs$trueCosmeticsSource;
    }

    @Override
    public ResourceLocation wolfBugs$getTrueCapeTextureLocation() {
        ((AbstractClientPlayer) (Object) this).getCloakTextureLocation();
        return wolfBugs$trueCapeTextureLocation;
    }

    @Override
    public UIdentifier wolfBugs$getTrueEmissiveCapeTexture() {
        if (wolfBugs$trueEmissiveCapeTexture == null)
            ((AbstractClientPlayerExt) this).getEmissiveCapeTexture();
        return wolfBugs$trueEmissiveCapeTexture;
    }

    @Override
    public float wolfBugs$getTrueCosmeticFrozenYaw() {
        if (Float.isNaN(wolfBugs$trueCosmeticFrozenYaw))
            ((AbstractClientPlayerExt) this).essential$getCosmeticFrozenYaw();
        return wolfBugs$trueCosmeticFrozenYaw;
    }

    @ModifyReturnValue(method = "getCosmeticsSourceUuid", at = @At("RETURN"), remap = false)
    private UUID overrideCosmeticsSourceUuid(UUID cosmeticsSourceUuid) {
        wolfBugs$trueCosmeticsSourceUuid = cosmeticsSourceUuid;
        if (wolfBugs$isMorphed())
            return wolfBugs$morphedAbstractClientPlayer.wolfBugs$getTrueCosmeticsSourceUuid();
        return cosmeticsSourceUuid;
    }

    @ModifyReturnValue(method = "getCosmeticsSource", at = @At("RETURN"), remap = false)
    private State<Map<CosmeticSlot, EquippedCosmetic>> overrideCosmeticsSource(State<Map<CosmeticSlot, EquippedCosmetic>> cosmeticsSource) {
        wolfBugs$trueCosmeticsSource = cosmeticsSource;
        if (wolfBugs$isMorphed())
            return wolfBugs$morphedAbstractClientPlayer.wolfBugs$getTrueCosmeticsSource();
        return cosmeticsSource;
    }

    @Unique
    private boolean wolfBugs$capeTextureRecursion = false;
    @Inject(method = "getCloakTextureLocation", at = @At("HEAD"), cancellable = true)
    private void overrideCapeTexture(CallbackInfoReturnable<ResourceLocation> cir) {
        if (wolfBugs$capeTextureRecursion)
            return;
        wolfBugs$capeTextureRecursion = true;
        wolfBugs$trueCapeTextureLocation = ((AbstractClientPlayer) (Object) this).getCloakTextureLocation();
        if (wolfBugs$isMorphed()) {
            cir.setReturnValue(wolfBugs$morphedAbstractClientPlayer.wolfBugs$getTrueCapeTextureLocation());
        } else {
            cir.setReturnValue(wolfBugs$trueCapeTextureLocation);
        }
        wolfBugs$capeTextureRecursion = false;
    }

    @ModifyReturnValue(method = "getEmissiveCapeTexture", at = @At("RETURN"), remap = false)
    private UIdentifier overrideEmissiveCapeTexture(UIdentifier emissiveCapeTexture) {
        wolfBugs$trueEmissiveCapeTexture = emissiveCapeTexture;
        if (wolfBugs$isMorphed())
            return wolfBugs$morphedAbstractClientPlayer.wolfBugs$getTrueEmissiveCapeTexture();
        return emissiveCapeTexture;
    }

    @ModifyReturnValue(method = "essential$getCosmeticFrozenYaw", at = @At("RETURN"), remap = false)
    private float overrideCosmeticFrozenYaw(float cosmeticFrozenYaw) {
        wolfBugs$trueCosmeticFrozenYaw = cosmeticFrozenYaw;
        if (wolfBugs$isMorphed())
            return wolfBugs$morphedAbstractClientPlayer.wolfBugs$getTrueCosmeticFrozenYaw();
        return cosmeticFrozenYaw;
    }
}
