package tuxi.wolfbugs.mixin;

import net.minecraft.world.damagesource.CombatEntry;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tuxi.wolfbugs.CombatTrackerContainer;

import java.util.Comparator;
import java.util.List;

@Mixin(CombatTracker.class)
public abstract class CombatTrackerMixin implements CombatTrackerContainer {
    @Shadow @Final private List<CombatEntry> entries;

    @Shadow @Final private LivingEntity mob;

    @Inject(method = "getKiller", at = @At("HEAD"), cancellable = true)
    private void onGetKiller(CallbackInfoReturnable<LivingEntity> cir) {
        cir.setReturnValue(null);
        if (entries.isEmpty()) return;
        entries.sort(Comparator.comparingInt(CombatEntry::getTime));
        if (entries.get(entries.size() - 1).getSource().getEntity() instanceof LivingEntity entity) {
            cir.setReturnValue(entity);
        }else {
            for (int i = entries.size()-1; i >= 0; i--) {
                CombatEntry entry = entries.get(i);
                if (entry.getSource().getEntity() instanceof LivingEntity entity) {
                    cir.setReturnValue(entity);
                    return;
                }
            }
        }
    }

    @Override
    public boolean wolfBugs$removeLatest(LivingEntity entity) {
        if (((LivingEntityAccessor) mob).getLastHurtByPlayer() != null && ((LivingEntityAccessor) mob).getLastHurtByPlayer().is(entity)) mob.setLastHurtByPlayer(null);
        if (mob.getLastHurtByMob() != null && mob.getLastHurtByMob().is(entity)) mob.setLastHurtByMob(null);
        entries.sort(Comparator.comparingInt(CombatEntry::getTime));
        for (int i = entries.size()-1; i >= 0; i--) {
            CombatEntry entry = entries.get(i);
            if (entry.getSource().getEntity() != null && entry.getSource().getEntity().equals(entity)) {
                entries.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public int wolfBugs$remove(LivingEntity entity) {
        int found = 0;
        if (((LivingEntityAccessor) mob).getLastHurtByPlayer() != null && ((LivingEntityAccessor) mob).getLastHurtByPlayer().is(entity)) mob.setLastHurtByPlayer(null);
        if (mob.getLastHurtByMob() != null && mob.getLastHurtByMob().is(entity)) mob.setLastHurtByMob(null);
        entries.sort(Comparator.comparingInt(CombatEntry::getTime));
        for (int i = entries.size()-1; i >= 0; i--) {
            CombatEntry entry = entries.get(i);
            if (entry.getSource().getEntity() != null && entry.getSource().getEntity().equals(entity)) {
                entries.remove(i);
                found++;
            }
        }
        return found;
    }

    @Override
    public boolean wolfBugs$clear() {
        if (entries.isEmpty()) return false;
        entries.clear();
        mob.setLastHurtByPlayer(null);
        mob.setLastHurtByMob(null);
        return true;
    }
}
