package tuxi.wolfbugs.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
import net.minecraftforge.network.HandshakeHandler;
import net.minecraftforge.network.HandshakeMessages;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tuxi.wolfbugs.WolfBugs;
import tuxi.wolfbugs.WolfBugsConfig;

import java.util.function.Supplier;

@Mixin(HandshakeHandler.class)
public class HandshakeHandlerMixin {
    @Shadow
    @Final
    private static Logger LOGGER;

    @Shadow
    @Final
    private static Marker FMLHSMARKER;

    @Inject(method = "handleClientModListOnServer", at = @At("HEAD"), remap = false, cancellable = true)
    private void saveModList(HandshakeMessages.C2SModListReply clientModList, Supplier<NetworkEvent.Context> c, CallbackInfo ci) {
        if (WolfBugs.strictModListValue && clientModList.getModList().stream().anyMatch((modId) -> !WolfBugsConfig.modWhitelist.get().contains(modId))) {
            String mismatchedMods = String.join(", ", clientModList.getModList().stream().filter((modId) -> !WolfBugsConfig.modWhitelist.get().contains(modId)).toList());
            LOGGER.error(
                    FMLHSMARKER,
                    "Terminating connection with client ({}), mismatched strict mod list (Mismatches: [{}])",
                    c.get().getNetworkManager().getRemoteAddress(),
                    mismatchedMods
            );
            c.get().getNetworkManager().send(new ClientboundLoginDisconnectPacket(Component.literal("§4§lUsing not allowed mods:\n§r§c" + mismatchedMods + "\n§r§4Please remove these mods to be able to join\n(§oBitte entferne die oben gennanten Mods um dem Server beitreten zu können§r§4)")));
            c.get().getNetworkManager().disconnect(Component.literal("Using not allowed mods: [" + mismatchedMods + "] Please remove these mods to be able to join"));
            ci.cancel();
            return;
        }
        WolfBugs.scheduleModList.put(c.get().getNetworkManager().getRemoteAddress(), clientModList.getModList());
    }
}
