package tuxi.wolfbugs;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;
import tuxi.wolfbugs.commands.CombatTrackerCommand;
import tuxi.wolfbugs.mixin.BooleanValueAccessor;

import java.util.Comparator;

@Mod(WolfBugs.MODID)
public class WolfBugs {

    public static final String MODID = "wolfbugs";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final GameRules.Key<GameRules.BooleanValue> RULE_ALLOWCHATTING = GameRules.register("allowChatting", GameRules.Category.CHAT, BooleanValueAccessor.create(false));

    private static final String PROTOCOL_VERSION = "1.3.2";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public WolfBugs() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
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
