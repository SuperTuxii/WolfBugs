package tuxi.wolfbugs.mixin;

import betteradvancements.gui.BetterAdvancementWidget;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tuxi.wolfbugs.mixininterface.ExtraDisplayInfo;

@Mixin(value = BetterAdvancementWidget.class, remap = false)
public abstract class BetterAdvancementWidgetMixin extends GuiComponent {
    @Shadow
    @Final
    private DisplayInfo displayInfo;
    @Shadow
    protected int x;
    @Shadow
    protected int y;

    @Inject(method = "drawConnection", at = @At(value = "INVOKE", target = "Lbetteradvancements/advancements/BetterDisplayInfo;drawDirectLines()Ljava/lang/Boolean;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void changeLineStyle(PoseStack poseStack, BetterAdvancementWidget parent, int scrollX, int scrollY, boolean drawInside, CallbackInfo ci, int innerLineColor, int borderLineColor) {
        ExtraDisplayInfo.LineStyle lineStyle = ((ExtraDisplayInfo) displayInfo).wolfBugs$getLineStyle();
        if (lineStyle == ExtraDisplayInfo.LineStyle.DEFAULT)
            return;
        poseStack.pushPose();
        poseStack.translate(scrollX + parent.getX() + 15.5, scrollY + parent.getY() + 13, 0);
        int offsetX = this.x - parent.getX();
        int offsetY = this.y - parent.getY();
        int xDirection = offsetX >= 0 ? 1 : -1;
        int yDirection = offsetY >= 0 ? 1 : -1;
        if (lineStyle == ExtraDisplayInfo.LineStyle.STRAIGHT) {
            poseStack.mulPose(new Quaternion(new Vector3f(0, 0, 1), (float) Math.atan2(offsetY, offsetX), false));
            int distance = Mth.floor(Mth.sqrt(offsetX*offsetX + offsetY*offsetY));
            if (drawInside) {
                this.hLine(poseStack, 0, distance, -1, borderLineColor);
                this.hLine(poseStack, 0, distance, 1, borderLineColor);
            } else {
                this.hLine(poseStack, 0, distance, 0, innerLineColor);
            }
        } else if (lineStyle != ExtraDisplayInfo.LineStyle.NONE) {
            if (offsetX == 0) {
                if (drawInside) {
                    this.vLine(poseStack, -1, 0, offsetY, borderLineColor);
                    this.vLine(poseStack, 1, 0, offsetY, borderLineColor);
                } else {
                    this.vLine(poseStack, 0, 0, offsetY, innerLineColor);
                }
            } else if (offsetY == 0) {
                if (drawInside) {
                    this.hLine(poseStack, 0, offsetX, -1, borderLineColor);
                    this.hLine(poseStack, 0, offsetX, 1, borderLineColor);
                } else {
                    this.hLine(poseStack, 0, offsetX, 0, innerLineColor);
                }
            } else {
                int frontLength = 0;
                boolean horizontal = (lineStyle.ordinal() >= ExtraDisplayInfo.LineStyle.BEGIN.ordinal() && lineStyle.ordinal() <= ExtraDisplayInfo.LineStyle.END.ordinal()) ||
                        (lineStyle.ordinal() >= ExtraDisplayInfo.LineStyle.BEGIN_AUTO.ordinal() && lineStyle.ordinal() <= ExtraDisplayInfo.LineStyle.END_AUTO.ordinal() && Math.abs(offsetX) >= Math.abs(offsetY));
                if ((lineStyle.ordinal() - ExtraDisplayInfo.LineStyle.BEGIN.ordinal()) % 3 == 0) {
                    frontLength = horizontal ? 16 * xDirection : 16 * yDirection;
                } else if ((lineStyle.ordinal() - ExtraDisplayInfo.LineStyle.MIDDLE.ordinal()) % 3 == 0) {
                    frontLength = horizontal ? offsetX / 2 : offsetY / 2;
                } else if ((lineStyle.ordinal() - ExtraDisplayInfo.LineStyle.END.ordinal()) % 3 == 0) {
                    frontLength = horizontal ? offsetX - (16 * xDirection) : offsetY - (16 * yDirection);
                }
                if (horizontal) {
                    poseStack.translate(0.5, 0, 0);
                    if (drawInside) {
                        this.hLine(poseStack, 0, frontLength + xDirection, -1, borderLineColor);
                        this.hLine(poseStack, 0, frontLength + xDirection, 1, borderLineColor);
                        this.vLine(poseStack, frontLength - 1, -yDirection, offsetY + yDirection, borderLineColor);
                        this.vLine(poseStack, frontLength + 1, -yDirection, offsetY + yDirection, borderLineColor);
                        this.hLine(poseStack, frontLength - xDirection, offsetX, offsetY - 1, borderLineColor);
                        this.hLine(poseStack, frontLength - xDirection, offsetX, offsetY + 1, borderLineColor);
                    } else {
                        this.hLine(poseStack, 0, frontLength, 0, innerLineColor);
                        this.vLine(poseStack, frontLength, 0, offsetY, innerLineColor);
                        this.hLine(poseStack, frontLength, offsetX, offsetY, innerLineColor);
                    }
                } else {
                    if (drawInside) {
                        this.vLine(poseStack, -1, 0, frontLength + yDirection, borderLineColor);
                        this.vLine(poseStack, 1, 0, frontLength + yDirection, borderLineColor);
                        this.hLine(poseStack, -xDirection, offsetX + xDirection, frontLength - 1, borderLineColor);
                        this.hLine(poseStack, -xDirection, offsetX + xDirection, frontLength + 1, borderLineColor);
                        this.vLine(poseStack, offsetX - 1, frontLength - yDirection, offsetY, borderLineColor);
                        this.vLine(poseStack, offsetX + 1, frontLength - yDirection, offsetY, borderLineColor);
                    } else {
                        this.vLine(poseStack, 0, 0, frontLength, innerLineColor);
                        this.hLine(poseStack, 0, offsetX, frontLength, innerLineColor);
                        this.vLine(poseStack, offsetX, frontLength, offsetY, innerLineColor);
                    }
                }
            }
        }
        poseStack.popPose();
        ci.cancel();
    }
}
