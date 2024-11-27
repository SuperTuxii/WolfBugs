package tuxi.wolfbugs.mixininterface;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public interface MorphPlayerInfo {
    Map<MinecraftProfileTexture.Type, ResourceLocation> wolfBugs$getTextureLocations();
    void wolfBugs$morph(PlayerInfo morphIntoPlayerInfo);
    void wolfBugs$unmorph();
}
