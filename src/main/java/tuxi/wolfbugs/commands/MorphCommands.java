package tuxi.wolfbugs.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import tuxi.wolfbugs.mixin.PlayerUpdateAccessor;

import java.util.HashMap;
import java.util.UUID;

public class MorphCommands {

    private static final HashMap<UUID, UUID> playerUuidToFakeUuid = new HashMap<>();
    private static final HashMap<UUID, GameProfile> playerUuidToMorphProfile = new HashMap<>();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("morph")
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.argument("morphPlayer", EntityArgument.player())
                        .then(Commands.argument("morphIntoTarget", EntityArgument.player())
                                .executes(ctx -> {
                                    ServerPlayer morphPlayer = EntityArgument.getPlayer(ctx, "morphPlayer");
                                    ServerPlayer morphIntoTarget = EntityArgument.getPlayer(ctx, "morphIntoTarget");
                                    boolean morph = !morphPlayer.equals(morphIntoTarget);
                                    if (morph) {
                                        Util.backgroundExecutor().execute(() -> morphPlayer(ctx.getSource().getServer(), morphPlayer, morphIntoTarget));
                                    }else {
                                        unMorphPlayer(ctx.getSource().getServer(), morphPlayer);
                                    }
                                    return 0;
                                })
                        )
                )
        );
        dispatcher.register(Commands.literal("unmorph")
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.argument("morphedPlayer", EntityArgument.player())
                        .executes(ctx -> {
                            ServerPlayer morphedPlayer = EntityArgument.getPlayer(ctx, "morphedPlayer");
                            unMorphPlayer(ctx.getSource().getServer(), morphedPlayer);
                            return 0;
                        })
                )
        );
    }

    public static void morphPlayer(MinecraftServer server, ServerPlayer morphPlayer, ServerPlayer morphIntoTarget) {
        PlayerList playerList = server.getPlayerList();
        ClientboundRemoveEntitiesPacket removePlayerPacket = new ClientboundRemoveEntitiesPacket(morphPlayer.getId());
        ClientboundPlayerInfoPacket removePlayerInfoPacket = new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, morphPlayer);
        ClientboundPlayerInfoPacket removeFakePlayerInfoPacket = null;
        if (playerUuidToFakeUuid.containsKey(morphPlayer.getGameProfile().getId())) {
            removeFakePlayerInfoPacket = new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, morphPlayer);
            ((PlayerUpdateAccessor) removeFakePlayerInfoPacket.getEntries().get(0)).setProfile(new GameProfile(playerUuidToFakeUuid.get(morphPlayer.getGameProfile().getId()), null));
        }
        for (ServerPlayer player : playerList.getPlayers()) {
            if (!player.equals(morphPlayer)) {
                player.connection.send(removePlayerPacket);
                player.connection.send(removePlayerInfoPacket);
                if (removeFakePlayerInfoPacket != null)
                    player.connection.send(removeFakePlayerInfoPacket);
            }
        }

        ClientboundPlayerInfoPacket morphPlayerInfoPacket = new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, morphPlayer);
        ClientboundPlayerInfoPacket removeMorphPlayerInfoPacket = new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, morphPlayer);
        GameProfile morphGameProfile = new GameProfile(morphPlayer.getGameProfile().getId(), morphIntoTarget.getGameProfile().getName());
        morphGameProfile.getProperties().putAll(server.getSessionService().fillProfileProperties(morphIntoTarget.getGameProfile(), true).getProperties());
        ((PlayerUpdateAccessor) morphPlayerInfoPacket.getEntries().get(0)).setProfile(morphGameProfile);
        playerUuidToMorphProfile.put(morphPlayer.getGameProfile().getId(), morphGameProfile);
        ClientboundPlayerInfoPacket fakePlayerInfoPacket = new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, morphPlayer);
        GameProfile fakeGameProfile = new GameProfile(UUID.randomUUID(), morphPlayer.getGameProfile().getName());
        fakeGameProfile.getProperties().putAll(server.getSessionService().fillProfileProperties(morphPlayer.getGameProfile(), true).getProperties());
        ((PlayerUpdateAccessor) fakePlayerInfoPacket.getEntries().get(0)).setProfile(fakeGameProfile);
        playerUuidToFakeUuid.put(morphPlayer.getGameProfile().getId(), fakePlayerInfoPacket.getEntries().get(0).getProfile().getId());
        ClientboundAddPlayerPacket addPlayerPacket = new ClientboundAddPlayerPacket(morphPlayer);
        for (ServerPlayer player : playerList.getPlayers()) {
            if (!player.equals(morphPlayer)) {
                player.connection.send(morphPlayerInfoPacket);
                player.connection.send(addPlayerPacket);
                player.connection.send(removeMorphPlayerInfoPacket);
                player.connection.send(fakePlayerInfoPacket);
            }
        }
    }

    public static void unMorphPlayer(MinecraftServer server, ServerPlayer morphedPlayer) {
        if (playerUuidToFakeUuid.containsKey(morphedPlayer.getGameProfile().getId())) {
            PlayerList playerList = server.getPlayerList();
            ClientboundRemoveEntitiesPacket removePlayerPacket = new ClientboundRemoveEntitiesPacket(morphedPlayer.getId());
            ClientboundPlayerInfoPacket removeMorphPlayerInfoPacket = new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, morphedPlayer);
            ClientboundPlayerInfoPacket removeFakePlayerInfoPacket = new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, morphedPlayer);
            ((PlayerUpdateAccessor) removeFakePlayerInfoPacket.getEntries().get(0)).setProfile(new GameProfile(playerUuidToFakeUuid.get(morphedPlayer.getGameProfile().getId()), null));
            for (ServerPlayer player : playerList.getPlayers()) {
                if (!player.equals(morphedPlayer)) {
                    player.connection.send(removePlayerPacket);
                    player.connection.send(removeMorphPlayerInfoPacket);
                    player.connection.send(removeFakePlayerInfoPacket);
                }
            }

            ClientboundPlayerInfoPacket playerInfoPacket = new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, morphedPlayer);
            playerUuidToMorphProfile.remove(morphedPlayer.getGameProfile().getId());
            playerUuidToFakeUuid.remove(morphedPlayer.getGameProfile().getId());
            ClientboundAddPlayerPacket addPlayerPacket = new ClientboundAddPlayerPacket(morphedPlayer);
            for (ServerPlayer player : playerList.getPlayers()) {
                if (!player.equals(morphedPlayer)) {
                    player.connection.send(playerInfoPacket);
                    player.connection.send(addPlayerPacket);
                }
            }
        }
    }

    public static boolean isPlayerMorphed(Player player) {
        return playerUuidToMorphProfile.containsKey(player.getGameProfile().getId());
    }

    public static GameProfile getMorphProfile(Player player) {
        return playerUuidToMorphProfile.get(player.getGameProfile().getId());
    }
}
