package tuxi.wolfbugs;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class WolfBugsConfig {
    public static final ForgeConfigSpec GENERAL_SPEC;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> modWhitelist;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> modBlacklist;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> savedBlacklistUsers;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        builder.push("/modlist Command");
        modWhitelist = builder.defineList("mod_whitelist", List.of( "yeetem_potions", "cgm", "beautify", "ellemes_container_lib", "pehkui", "ctm", "placebo", "modernfix", "wolf_hud", "artifacts", "mixinextras", "the_classics", "moa_decor_science", "starlight", "apotheosis", "forge", "embeddium", "rubidium", "corpse", "wolf3companion2", "chipped", "useless_sword", "sanitydim", "minecraft", "entity_texture_features", "handcrafted", "voicechat", "voicechat_api", "swingthroughgrass", "moonlight", "mousetweaks", "wolfbugs", "inventorytotem", "solidspectator", "structureexpansion", "curios", "patchouli", "collective", "camera", "autoreglib", "quark", "supplementaries", "framedblocks", "expandedstorage", "resourcefullib", "morevanillaarmor", "cfm", "nfm", "architectury", "simplyswords", "mysticpotions", "ferritecore", "wolf3companion", "framework", "betterinvisibility", "expandability", "geckolib3"), entry -> true);
        modBlacklist = builder.defineList("mod_blacklist", List.of(), entry -> true);
        savedBlacklistUsers = builder.defineList("saved_blacklist_alerts", List.of(), entry -> true);
        builder.pop();
    }
}
