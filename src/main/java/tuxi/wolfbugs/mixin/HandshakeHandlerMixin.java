package tuxi.wolfbugs.mixin;

import net.minecraftforge.network.HandshakeHandler;
import net.minecraftforge.network.HandshakeMessages;
import net.minecraftforge.network.NetworkEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tuxi.wolfbugs.WolfBugs;

import java.util.function.Supplier;

@Mixin(HandshakeHandler.class)
public class HandshakeHandlerMixin {
    @Inject(method = "handleClientModListOnServer", at = @At("HEAD"), remap = false)
    private void saveModList(HandshakeMessages.C2SModListReply clientModList, Supplier<NetworkEvent.Context> c, CallbackInfo ci) {
        WolfBugs.scheduleModList.put(c.get().getNetworkManager().getRemoteAddress(), clientModList.getModList());
    }
}
