package tuxi.wolfbugs.mixin;

import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tuxi.wolfbugs.commands.MorphCommands;

import java.util.function.Consumer;

@Mixin(ServerEntity.class)
public class ServerEntityMixin {
    @Shadow @Final private Entity entity;

    @Redirect(method = "sendPairingData", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", ordinal = 0))
    private <T> void sendExtraPacketsOnMorphed(Consumer<T> packetConsumer, T addEntityPacket) {
        if (this.entity instanceof ServerPlayer player && MorphCommands.isPlayerMorphed(player)) {
            ClientboundPlayerInfoPacket morphPlayerInfoPacket = new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, player);
            ((PlayerUpdateAccessor) morphPlayerInfoPacket.getEntries().get(0)).setProfile(MorphCommands.getMorphProfile(player));
            packetConsumer.accept((T) morphPlayerInfoPacket);
            packetConsumer.accept(addEntityPacket);
            packetConsumer.accept((T) new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, player));
        }else {
            packetConsumer.accept(addEntityPacket);
        }
    }
}
