package tuxi.wolfbugs.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tuxi.wolfbugs.WolfBugs;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
    @Shadow public abstract void send(Packet<?> p_9830_);

    @Shadow public ServerPlayer player;

    @Shadow @Final private MinecraftServer server;

    @Inject(method = "handleChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;submit(Ljava/lang/Runnable;)Ljava/util/concurrent/CompletableFuture;"), cancellable = true)
    private void changeChatVisiblity(ServerboundChatPacket p_9841_, CallbackInfo ci) {
        if (server.getProfilePermissions(player.getGameProfile()) < 2 && !server.getGameRules().getBoolean(WolfBugs.RULE_ALLOWCHATTING)) {
            send(new ClientboundSystemChatPacket(Component.translatable("chat.cannotSend").withStyle(ChatFormatting.RED), false));
            ci.cancel();
        }
    }
}
