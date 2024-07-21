package tuxi.wolfbugs;

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.api.distmarker.Dist;
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

@Mod(WolfBugs.MODID)
public class WolfBugs {

    public static final String MODID = "wolfbugs";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final GameRules.Key<GameRules.BooleanValue> RULE_ALLOWCHATTING = GameRules.register("allowChatting", GameRules.Category.CHAT, BooleanValueAccessor.create(false));

    private static final String PROTOCOL_VERSION = "1";
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

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
        }
    }
}
