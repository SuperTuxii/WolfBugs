package tuxi.wolfbugs;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class WolfBugsConfig {
    public static final ForgeConfigSpec GENERAL_SPEC;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> modWhitelist;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> modBlacklist;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        builder.push("/modlist Command");
        modWhitelist = builder.defineList("mod_whitelist", List.of( "cgm", "beautify", "ellemes_container_lib", "ctm", "placebo", "modernfix", "artifacts", "mixinextras", "the_classics", "moa_decor_science", "apotheosis", "starlight", "luckperms", "forge", "pfm", "embeddium", "rubidium", "corpse", "wolf3companion2", "useless_sword", "minecraft", "handcrafted", "voicechat", "moonlight", "mousetweaks", "wolfbugs", "inventorytotem", "solidspectator", "chisel_chipped_integration", "chipped", "structureexpansion", "curios", "patchouli", "tempban", "collective", "autoreglib", "quark", "supplementaries", "framedblocks", "expandedstorage", "resourcefullib", "worldedit", "morevanillaarmor", "architectury", "mysticpotions", "ferritecore", "wolf3companion", "framework", "betterinvisibility", "expandability", "openloader", "essential"), entry -> true);
        modBlacklist = builder.defineList("mod_blacklist", List.of(), entry -> true);
        builder.pop();
    }
}
