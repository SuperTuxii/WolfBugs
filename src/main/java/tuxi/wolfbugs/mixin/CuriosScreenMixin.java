package tuxi.wolfbugs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.client.gui.CuriosScreen;

@Mixin(CuriosScreen.class)
public class CuriosScreenMixin {
    @Inject(method = "updateRenderButtons", at = @At("HEAD"), cancellable = true, remap = false)
    public void removeRenderButtons(CallbackInfo ci) {
        ci.cancel();
    }
}
