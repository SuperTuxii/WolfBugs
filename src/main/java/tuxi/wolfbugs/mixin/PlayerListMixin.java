package tuxi.wolfbugs.mixin;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tuxi.wolfbugs.WolfBugs;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Inject(method = "placeNewPlayer", at = @At("HEAD"))
    private void onPlaceNewPlayer(Connection connection, ServerPlayer player, CallbackInfo ci) {
        if (WolfBugs.scheduleModList.containsKey(connection.getRemoteAddress()))  {
            WolfBugs.modList.put(player.getUUID(), WolfBugs.scheduleModList.get(connection.getRemoteAddress()));
            WolfBugs.scheduleModList.remove(connection.getRemoteAddress());
        }else {
            WolfBugs.LOGGER.error("Could not find ModList for Player {}", player.getScoreboardName());
        }
    }
}
