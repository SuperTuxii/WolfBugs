package tuxi.wolfbugs.mixin;

import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientboundPlayerInfoPacket.class)
public class ClientboundPlayerInfoPacketMixin {
    @Redirect(method = "createPlayerUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayerGameMode;getGameModeForPlayer()Lnet/minecraft/world/level/GameType;"))
    private static GameType changeDisplayName(ServerPlayerGameMode instance) {
        return GameType.DEFAULT_MODE;
    }
}
