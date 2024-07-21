package tuxi.wolfbugs.mixin;

import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {
    @Inject(method = "decorateName", at = @At("HEAD"), cancellable = true)
    private void removeItalic(PlayerInfo playerInfo, MutableComponent component, CallbackInfoReturnable<Component> cir) {
        cir.setReturnValue(component);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerInfo;getGameMode()Lnet/minecraft/world/level/GameType;"))
    private GameType hideGameMode(PlayerInfo playerInfo) {
        return GameType.DEFAULT_MODE;
    }
}
