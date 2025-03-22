package tuxi.wolfbugs.mixininterface;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.scores.PlayerTeam;
import org.jetbrains.annotations.Nullable;

public interface MorphPlayerInfo {
    MorphPlayerInfo wolfBugs$getMorphedPlayerInfo();
    boolean wolfBugs$isMorphed();
    void wolfBugs$morph(MorphPlayerInfo morphIntoPlayerInfo);
    void wolfBugs$unmorph();
    GameProfile wolfBugs$getTrueProfile();
    int wolfBugs$getTrueLatency();
    boolean wolfBugs$isCapeTrulyLoaded();
    boolean wolfBugs$isSkinTrulyLoaded();
    String wolfBugs$getTrueModelName();
    ResourceLocation wolfBugs$getTrueSkinLocation();
    @Nullable ResourceLocation wolfBugs$getTrueCapeLocation();
    @Nullable ResourceLocation wolfBugs$getTrueElytraLocation();
    @Nullable PlayerTeam wolfBugs$getTrueTeam();
    @Nullable Component wolfBugs$getTrueTabListDisplayName();
}
