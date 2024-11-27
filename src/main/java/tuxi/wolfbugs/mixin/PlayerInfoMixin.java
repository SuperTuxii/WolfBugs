package tuxi.wolfbugs.mixin;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import tuxi.wolfbugs.mixininterface.MorphPlayerInfo;

import java.util.Map;

@Mixin(PlayerInfo.class)
public abstract class PlayerInfoMixin implements MorphPlayerInfo {
    @Shadow @Final
    private Map<MinecraftProfileTexture.Type, ResourceLocation> textureLocations;

    @Shadow protected abstract void registerTextures();

    @Unique
    private Map<MinecraftProfileTexture.Type, ResourceLocation> wolfBugs$originalTextureLocations = null;

    @Override
    public Map<MinecraftProfileTexture.Type, ResourceLocation> wolfBugs$getTextureLocations() {
        return wolfBugs$originalTextureLocations == null ? textureLocations : wolfBugs$originalTextureLocations;
    }

    @Override
    public void wolfBugs$morph(PlayerInfo morphIntoPlayerInfo) {
        this.registerTextures();
        if (wolfBugs$originalTextureLocations == null) {
             wolfBugs$originalTextureLocations = Map.copyOf(textureLocations);
        }
        textureLocations.clear();
        textureLocations.putAll(((MorphPlayerInfo) morphIntoPlayerInfo).wolfBugs$getTextureLocations());
    }

    @Override
    public void wolfBugs$unmorph() {
        if (wolfBugs$originalTextureLocations != null) {
            textureLocations.clear();
            textureLocations.putAll(wolfBugs$originalTextureLocations);
        }
    }
}
