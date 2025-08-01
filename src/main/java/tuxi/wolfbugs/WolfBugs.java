package tuxi.wolfbugs;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;
import tuxi.wolfbugs.commands.CombatTrackerCommand;
import tuxi.wolfbugs.commands.ModListCommand;
import tuxi.wolfbugs.commands.MorphCommands;
import tuxi.wolfbugs.mixin.BooleanValueAccessor;
import tuxi.wolfbugs.networking.ClientboundMorphPacket;
import tuxi.wolfbugs.networking.ClientboundUnmorphPacket;

import java.net.SocketAddress;
import java.util.*;

@Mod(WolfBugs.MODID)
public class WolfBugs {

    public static final String MODID = "wolfbugs";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final GameRules.Key<GameRules.BooleanValue> RULE_ALLOWCHATTING = GameRules.register("allowChatting", GameRules.Category.CHAT, BooleanValueAccessor.create(false));

    public static final HashMap<SocketAddress, List<String>> scheduleModList = new HashMap<>();
    public static final HashMap<UUID, List<String>> modList = new HashMap<>();

    private static final String PROTOCOL_VERSION = "2";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public WolfBugs() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, WolfBugsConfig.GENERAL_SPEC, "wolfbugs.toml");

        int messageId = 0;
        CHANNEL.messageBuilder(ClientboundMorphPacket.class, messageId)
                .encoder(ClientboundMorphPacket::write)
                .decoder(ClientboundMorphPacket::new)
                .consumerMainThread(ClientboundMorphPacket::handle)
                .add();
        messageId++;
        CHANNEL.messageBuilder(ClientboundUnmorphPacket.class, messageId)
                .encoder(ClientboundUnmorphPacket::write)
                .decoder(ClientboundUnmorphPacket::new)
                .consumerMainThread(ClientboundUnmorphPacket::handle)
                .add();
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent joinEvent) {
        if (joinEvent.getEntity() instanceof ServerPlayer player) {
            MorphCommands.sendAllMorphedToPlayer(player);
            if (player.getTags().contains("MOD")) {
                List<? extends String> savedBlacklistUsers = WolfBugsConfig.savedBlacklistUsers.get();
                if (savedBlacklistUsers.isEmpty()) {
                    player.sendSystemMessage(Component.literal("§aNo Players using blacklisted Mods were saved"));
//                    player.sendSystemMessage(Component.translatable("wolfbugs.modlist.no_saved_blacklist_users"));
                } else {
                    player.sendSystemMessage(Component.literal("Moderators missed these Players using blacklisted Mods:"));
//                    player.sendSystemMessage(Component.translatable("wolfbugs.modlist.following_blacklist_users_saved"));
                    savedBlacklistUsers.forEach(data -> {
                        String[] splitData = data.split("\\|");
                        player.sendSystemMessage(Component.literal(String.format("§c§lThe Player §4§l%s§c§l is using blacklisted Mods!\n§cModList for §4%s§c: %s",
                                splitData[0], splitData[0], splitData[1])));
//                        player.sendSystemMessage(Component.translatable("wolfbugs.modlist.player_using_blacklisted",
//                                        "§4§l" + splitData[0])
//                                .append(Component.literal("\n"))
//                                .append(Component.translatable("wolfbugs.modlist.modlist_display",
//                                        "§4" + splitData[0],
//                                        splitData[1].equals("wolfbugs.modlist.only_allowed") ?
//                                                Component.translatable("wolfbugs.modlist.only_allowed") : splitData[1])));
                    });
                    WolfBugsConfig.savedBlacklistUsers.set(List.of());
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent leaveEvent) {
        if (leaveEvent.getEntity() instanceof ServerPlayer player) {
            MorphCommands.removePlayer(player.getUUID());
        }
    }

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        if (event.getPlayer().getServer() != null && event.getPlayer().getServer().getProfilePermissions(event.getPlayer().getGameProfile()) < 2 && !event.getPlayer().getServer().getGameRules().getBoolean(WolfBugs.RULE_ALLOWCHATTING)) {
            event.getPlayer().sendSystemMessage(Component.translatable("chat.cannotSend").withStyle(ChatFormatting.RED), false);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onCommandsRegister(RegisterCommandsEvent event) {
        CombatTrackerCommand.register(event.getDispatcher());
        ModListCommand.register(event.getDispatcher());
        MorphCommands.register(event.getDispatcher());
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        public static final Ordering<PlayerInfo> PLAYERLIST_ORDERING = Ordering.from(new WolfBugs.ClientModEvents.PlayerInfoComparator());

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
        }

        @OnlyIn(Dist.CLIENT)
        static class PlayerInfoComparator implements Comparator<PlayerInfo> {
            public int compare(PlayerInfo p_94564_, PlayerInfo p_94565_) {
                PlayerTeam playerteam = p_94564_.getTeam();
                PlayerTeam playerteam1 = p_94565_.getTeam();
                return ComparisonChain.start().compare(playerteam != null ? playerteam.getName() : "", playerteam1 != null ? playerteam1.getName() : "").compare(p_94564_.getProfile().getName(), p_94565_.getProfile().getName(), String::compareToIgnoreCase).result();
            }
        }
    }
}
