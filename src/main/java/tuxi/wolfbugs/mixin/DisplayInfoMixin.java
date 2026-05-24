package tuxi.wolfbugs.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tuxi.wolfbugs.mixininterface.ExtraDisplayInfo;

@Mixin(DisplayInfo.class)
public class DisplayInfoMixin implements ExtraDisplayInfo {
    @Unique
    private boolean wolfBugs$customLocation = false;
    @Unique
    private LineStyle wolfBugs$lineStyle = LineStyle.DEFAULT;

    @Inject(method = "fromJson", at = @At("RETURN"))
    private static void readLocationAndLineStyle(JsonObject displayObject, CallbackInfoReturnable<DisplayInfo> cir) {
        if (displayObject.has("pos_x") && displayObject.has("pos_y")) {
            cir.getReturnValue().setLocation(displayObject.get("pos_x").getAsFloat(), displayObject.get("pos_y").getAsFloat());
            ((ExtraDisplayInfo) cir.getReturnValue()).wolfBugs$setCustomLocation(true);
        }
        if (displayObject.has("line_style")) {
            ((ExtraDisplayInfo) cir.getReturnValue()).wolfBugs$setLineStyle(switch (displayObject.get("line_style").getAsString().toLowerCase()) {
                case "straight"         -> LineStyle.STRAIGHT;
                case "begin"            -> LineStyle.BEGIN;
                case "middle"           -> LineStyle.MIDDLE;
                case "end"              -> LineStyle.END;
                case "begin_vertical"   -> LineStyle.BEGIN_VERTICAL;
                case "middle_vertical"  -> LineStyle.MIDDLE_VERTICAL;
                case "end_vertical"     -> LineStyle.END_VERTICAL;
                case "begin_auto"       -> LineStyle.BEGIN_AUTO;
                case "middle_auto"      -> LineStyle.MIDDLE_AUTO;
                case "end_auto"         -> LineStyle.END_AUTO;
                case "none"             -> LineStyle.NONE;
                default                 -> LineStyle.DEFAULT;
            });
        }
    }

    @Inject(method = "fromNetwork", at = @At("RETURN"))
    private static void readLineStyle(FriendlyByteBuf byteBuf, CallbackInfoReturnable<DisplayInfo> cir) {
        ((ExtraDisplayInfo) cir.getReturnValue()).wolfBugs$setLineStyle(LineStyle.values()[byteBuf.readInt()]);
    }

    @Inject(method = "serializeToNetwork", at = @At("RETURN"))
    private void serializeLineStyleToNetwork(FriendlyByteBuf byteBuf, CallbackInfo ci) {
        byteBuf.writeInt(wolfBugs$lineStyle.ordinal());
    }

    @Inject(method = "serializeToJson", at = @At("RETURN"))
    private void serializeLineStyleToJson(CallbackInfoReturnable<JsonElement> cir) {
        if (wolfBugs$lineStyle != LineStyle.DEFAULT)
            cir.getReturnValue().getAsJsonObject().addProperty("line_style", wolfBugs$lineStyle.toString().toLowerCase());
    }

    @Inject(method = "setLocation", at = @At("HEAD"), cancellable = true)
    private void preventSetLocation(float p_14979_, float p_14980_, CallbackInfo ci) {
        if (wolfBugs$isCustomLocation())
            ci.cancel();
    }

    @Override
    public boolean wolfBugs$isCustomLocation() {
        return wolfBugs$customLocation;
    }
    @Override
    public LineStyle wolfBugs$getLineStyle() {
        return wolfBugs$lineStyle;
    }

    @Override
    public void wolfBugs$setCustomLocation(boolean value) {
        wolfBugs$customLocation = value;
    }
    @Override
    public void wolfBugs$setLineStyle(LineStyle value) {
        wolfBugs$lineStyle = value;
    }
}
