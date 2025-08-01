package tuxi.wolfbugs.mixin;

import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tuxi.wolfbugs.WolfBugs;
import tuxi.wolfbugs.WolfBugsConfig;
import tuxi.wolfbugs.commands.ModListCommand;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Shadow public abstract List<ServerPlayer> getPlayers();

    @Inject(method = "placeNewPlayer", at = @At("HEAD"))
    private void onPlaceNewPlayer(Connection connection, ServerPlayer player, CallbackInfo ci) {
        if (WolfBugs.scheduleModList.containsKey(connection.getRemoteAddress()))  {
            List<? extends String> deniedMods = WolfBugsConfig.modBlacklist.get();
            List<String> modList = WolfBugs.scheduleModList.get(connection.getRemoteAddress());
            WolfBugs.modList.put(player.getUUID(), modList);
            WolfBugs.scheduleModList.remove(connection.getRemoteAddress());
            if (modList.stream().anyMatch(deniedMods::contains)) {
                if (this.getPlayers().stream().anyMatch(mod -> mod.getTags().contains("MOD"))) {
                    this.getPlayers().stream()
                            .filter(mod -> mod.getTags().contains("MOD"))
                            .forEach(mod -> mod.sendSystemMessage(
                                    Component.literal(String.format("§c§lThe Player §4§l%s§c§l is using blacklisted Mods!\n§cModList for §4%s§c: %s",
                                            player.getScoreboardName(), player.getScoreboardName(),
                                            ModListCommand.getColoredModList(modList, false)))
                                    /*Component.translatable("wolfbugs.modlist.player_using_blacklisted",
                                                    "§4§l" + player.getScoreboardName())
                                            .append(Component.literal("\n"))
                                            .append(Component.translatable("wolfbugs.modlist.modlist_display",
                                                    "§4" + player.getScoreboardName(),
                                                    ModListCommand.getColoredModList(modList, false)))*/));
                } else {
                    String data = player.getScoreboardName() + "|" + ModListCommand.getColoredModList(modList, false).getString();
                    List<String> savedBlacklistUsers = new ArrayList<>(WolfBugsConfig.savedBlacklistUsers.get());
                    if (!savedBlacklistUsers.contains(data))
                        savedBlacklistUsers.add(data);
                    WolfBugsConfig.savedBlacklistUsers.set(savedBlacklistUsers);
                    WolfBugsConfig.savedBlacklistUsers.save();
                }
            }
        }else {
            WolfBugs.LOGGER.error("Could not find ModList for Player {}", player.getScoreboardName());
        }
    }
}
