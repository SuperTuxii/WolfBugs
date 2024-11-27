package tuxi.wolfbugs.mixin;

import net.minecraft.client.multiplayer.PlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import tuxi.wolfbugs.mixininterface.MorphPlayerInfo;

@Mixin(PlayerInfo.class)
public abstract class PlayerInfoMixin implements MorphPlayerInfo {
    @Unique
    private PlayerInfo wolfBugs$morphedPlayerInfo = null;

    @Override
    public PlayerInfo wolfBugs$getMorphedPlayerInfo() {
        return wolfBugs$morphedPlayerInfo;
    }

    @Override
    public boolean wolfBugs$isMorphed() {
        return wolfBugs$morphedPlayerInfo != null;
    }

    @Override
    public void wolfBugs$morph(PlayerInfo morphIntoPlayerInfo) {
        wolfBugs$morphedPlayerInfo = morphIntoPlayerInfo;
    }

    @Override
    public void wolfBugs$unmorph() {
        wolfBugs$morphedPlayerInfo = null;
    }
}
