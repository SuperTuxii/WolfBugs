package tuxi.wolfbugs.mixininterface;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public interface MorphPlayerInfo {
    MorphPlayerInfo wolfBugs$getMorphedPlayerInfo();
    boolean wolfBugs$isMorphed();
    void wolfBugs$morph(MorphPlayerInfo morphIntoPlayerInfo);
    void wolfBugs$unmorph();
    boolean wolfBugs$isCapeTrulyLoaded();
    boolean wolfBugs$isSkinTrulyLoaded();
    String wolfBugs$getTrueModelName();
    ResourceLocation wolfBugs$getTrueSkinLocation();
    @Nullable ResourceLocation wolfBugs$getTrueCapeLocation();
    @Nullable ResourceLocation wolfBugs$getTrueElytraLocation();
}
