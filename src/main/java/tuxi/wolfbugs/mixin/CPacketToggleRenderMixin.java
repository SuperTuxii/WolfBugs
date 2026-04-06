package tuxi.wolfbugs.mixin;

import net.minecraftforge.network.NetworkEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import top.theillusivec4.curios.common.network.client.CPacketToggleRender;

import java.util.concurrent.CompletableFuture;

@Mixin(CPacketToggleRender.class)
public class CPacketToggleRenderMixin {
    @Redirect(method = "handle", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/network/NetworkEvent$Context;enqueueWork(Ljava/lang/Runnable;)Ljava/util/concurrent/CompletableFuture;"), remap = false)
    private static CompletableFuture<Void> preventHandlingOfRenderToggle(NetworkEvent.Context instance, Runnable runnable) {
        return CompletableFuture.completedFuture(null);
    }
}
