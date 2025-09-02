package tuxi.wolfbugs.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tuxi.wolfbugs.WolfBugs;
import tuxi.wolfbugs.mixininterface.DeathProtectEntity;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements DeathProtectEntity {

    @Shadow protected abstract boolean checkTotemDeathProtection(DamageSource p_21263_);

    @Shadow public abstract void setHealth(float p_21154_);

    @Unique
    private boolean wolfBugs$deathProtection = false;

    @Override
    public boolean wolfBugs$hasDeathProtect() {
        return wolfBugs$deathProtection;
    }

    @Override
    public void wolfBugs$enableDeathProtect() {
        wolfBugs$deathProtection = true;
    }

    @Override
    public void wolfBugs$removeDeathProtect() {
        wolfBugs$deathProtection = false;
    }

    @Redirect(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;checkTotemDeathProtection(Lnet/minecraft/world/damagesource/DamageSource;)Z"))
    private boolean checkDeathProtection(LivingEntity instance, DamageSource interactionHand) {
        if (this.wolfBugs$hasDeathProtect() && instance instanceof ServerPlayer player) {
            this.setHealth(1.0F);
            WolfBugs.USED_DEATH_PROTECT.trigger(player);
            return true;
        }
        return checkTotemDeathProtection(interactionHand);
    }
}
