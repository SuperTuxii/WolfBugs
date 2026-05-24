package tuxi.wolfbugs.mixin;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.TreeNodePosition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tuxi.wolfbugs.mixininterface.ExtraDisplayInfo;

@Mixin(TreeNodePosition.class)
public abstract class TreeNodePositionMixin {
    @Shadow
    @Final
    private Advancement advancement;
    @Shadow
    private int x;
    @Shadow
    private float y;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void setLocationFromDisplayInfo(Advancement advancement, TreeNodePosition p_16568_, TreeNodePosition p_16569_, int p_16570_, int p_16571_, CallbackInfo ci) {
        if (advancement.getDisplay() != null && ((ExtraDisplayInfo) advancement.getDisplay()).wolfBugs$isCustomLocation()) {
            this.x = (int) advancement.getDisplay().getX();
            this.y = advancement.getDisplay().getY();
        }
    }

    @Inject(method = "addChild", at = @At("HEAD"))
    private void setLocationAddChild(Advancement p_16590_, TreeNodePosition p_16591_, CallbackInfoReturnable<TreeNodePosition> cir) {
        if (this.advancement.getDisplay() != null && ((ExtraDisplayInfo) this.advancement.getDisplay()).wolfBugs$isCustomLocation()) {
            this.x = (int) advancement.getDisplay().getX();
            this.y = advancement.getDisplay().getY();
        }
    }
    @Inject(method = "firstWalk", at = @At("RETURN"))
    private void setLocationFirstWalk(CallbackInfo ci) {
        if (this.advancement.getDisplay() != null && ((ExtraDisplayInfo) this.advancement.getDisplay()).wolfBugs$isCustomLocation()) {
            this.x = (int) advancement.getDisplay().getX();
            this.y = advancement.getDisplay().getY();
        }
    }

    @ModifyVariable(method = "secondWalk", at = @At("HEAD"), argsOnly = true)
    private int setXSecondWalk(int arg1) {
        if (this.advancement.getDisplay() != null && ((ExtraDisplayInfo) this.advancement.getDisplay()).wolfBugs$isCustomLocation())
            return (int) advancement.getDisplay().getX();
        return arg1;
    }
    @Inject(method = "secondWalk", at = @At("RETURN"))
    private void setLocationSecondWalk(float p_16576_, int p_16577_, float p_16578_, CallbackInfoReturnable<Float> cir) {
        if (this.advancement.getDisplay() != null && ((ExtraDisplayInfo) this.advancement.getDisplay()).wolfBugs$isCustomLocation()) {
            this.x = (int) advancement.getDisplay().getX();
            this.y = advancement.getDisplay().getY();
        }
    }
    @Inject(method = "thirdWalk", at = @At("RETURN"))
    private void setLocationThirdWalk(float p_16574_, CallbackInfo ci) {
        if (this.advancement.getDisplay() != null && ((ExtraDisplayInfo) this.advancement.getDisplay()).wolfBugs$isCustomLocation()) {
            this.x = (int) advancement.getDisplay().getX();
            this.y = advancement.getDisplay().getY();
        }
    }
}
