package tuxi.wolfbugs;

import net.minecraft.world.entity.LivingEntity;

public interface CombatTrackerContainer {
    boolean wolfBugs$removeLatest(LivingEntity entity);
    int wolfBugs$remove(LivingEntity entity);
    boolean wolfBugs$clear();
}
