package tuxi.wolfbugs.mixininterface;

import gg.essential.cosmetics.EquippedCosmetic;
import gg.essential.gui.elementa.state.v2.State;
import gg.essential.mod.cosmetics.CosmeticSlot;
import gg.essential.util.UIdentifier;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.UUID;

public interface MorphAbstractClientPlayer {
    boolean wolfBugs$isMorphed();
    void wolfBugs$morph(MorphAbstractClientPlayer morphIntoPlayer);
    void wolfBugs$unmorph();
    UUID wolfBugs$getTrueCosmeticsSourceUuid();
    State<Map<CosmeticSlot, EquippedCosmetic>> wolfBugs$getTrueCosmeticsSource();
    ResourceLocation wolfBugs$getTrueCapeTextureLocation();
    UIdentifier wolfBugs$getTrueEmissiveCapeTexture();
    float wolfBugs$getTrueCosmeticFrozenYaw();
}
