package tuxi.wolfbugs.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import tuxi.wolfbugs.WolfBugs;
import tuxi.wolfbugs.WolfBugsConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModListCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("modlist")
                .requires(cs -> cs.isPlayer() && cs.getEntity() != null && cs.getEntity().getTags().contains("MOD"))
                .then(Commands.literal("show")
                        .then(Commands.argument("target", EntityArgument.player())
                                .then(Commands.argument("fullList", BoolArgumentType.bool())
                                        .executes(ctx -> modList(ctx.getSource(), EntityArgument.getPlayer(ctx, "target"), BoolArgumentType.getBool(ctx, "fullList")))))
                ).then(Commands.literal("allow")
                        .then(Commands.argument("modId", StringArgumentType.word())
                                .executes(ctx -> allowMod(ctx.getSource(), StringArgumentType.getString(ctx, "modId"))))
                ).then(Commands.literal("deny")
                        .then(Commands.argument("modId", StringArgumentType.word())
                                .executes(ctx -> denyMod(ctx.getSource(), StringArgumentType.getString(ctx, "modId"))))
                ).then(Commands.literal("neutralize")
                        .then(Commands.argument("modId", StringArgumentType.word())
                                .executes(ctx ->  neutralizeMod(ctx.getSource(), StringArgumentType.getString(ctx, "modId"))))
                )
        );
    }

    private static int modList(CommandSourceStack ctx, ServerPlayer player, boolean fullList) {
        if (WolfBugs.modList.containsKey(player.getUUID())) {
            ctx.sendSystemMessage(Component.literal("ModList for " + player.getScoreboardName() + ": " + getColoredModList(WolfBugs.modList.get(player.getUUID()), fullList)));
            return WolfBugs.modList.get(player.getUUID()).size();
        }
        return 0;
    }

    private static int allowMod(CommandSourceStack ctx, String modId) {
        List<String> allowedMods = new ArrayList<>(WolfBugsConfig.modWhitelist.get());
        List<String> deniedMods = new ArrayList<>(WolfBugsConfig.modBlacklist.get());
        if (allowedMods.contains(modId)) {
            ctx.sendSystemMessage(Component.literal("§cModId is already in Mod-WhiteList"));
            return 0;
        }
        allowedMods.add(modId);
        deniedMods.remove(modId);
        WolfBugsConfig.modWhitelist.set(allowedMods);
        WolfBugsConfig.modWhitelist.save();
        WolfBugsConfig.modBlacklist.set(deniedMods);
        WolfBugsConfig.modBlacklist.save();
        ctx.sendSystemMessage(Component.literal("§aSuccessfully added to Mod-WhiteList. Current Mod-WhiteList is:\n§f" + Arrays.toString(allowedMods.toArray())));
        return 1;
    }

    private static int denyMod(CommandSourceStack ctx, String modId) {
        List<String> allowedMods = new ArrayList<>(WolfBugsConfig.modWhitelist.get());
        List<String> deniedMods = new ArrayList<>(WolfBugsConfig.modBlacklist.get());
        if (deniedMods.contains(modId)) {
            ctx.sendSystemMessage(Component.literal("§cModId is already in Mod-BlackList"));
            return 0;
        }
        allowedMods.remove(modId);
        deniedMods.add(modId);
        WolfBugsConfig.modWhitelist.set(allowedMods);
        WolfBugsConfig.modWhitelist.save();
        WolfBugsConfig.modBlacklist.set(deniedMods);
        WolfBugsConfig.modBlacklist.save();
        ctx.sendSystemMessage(Component.literal("§aSuccessfully added to Mod-BlackList. Current Mod-BlackList is:\n§f" + Arrays.toString(deniedMods.toArray())));
        return 1;
    }

    private static int neutralizeMod(CommandSourceStack ctx, String modId) {
        List<String> allowedMods = new ArrayList<>(WolfBugsConfig.modWhitelist.get());
        List<String> deniedMods = new ArrayList<>(WolfBugsConfig.modBlacklist.get());
        allowedMods.remove(modId);
        deniedMods.remove(modId);
        WolfBugsConfig.modWhitelist.set(allowedMods);
        WolfBugsConfig.modWhitelist.save();
        WolfBugsConfig.modBlacklist.set(deniedMods);
        WolfBugsConfig.modBlacklist.save();
        ctx.sendSystemMessage(Component.literal("§aSuccessfully removed from Mod-WhiteList and Mod-BlackList"));
        return 1;
    }

    private static String getColoredModList(List<String> mods, boolean fullList) {
        List<? extends String> allowedMods = WolfBugsConfig.modWhitelist.get();
        List<? extends String> deniedMods = WolfBugsConfig.modBlacklist.get();
        boolean onlyAllowed = true;
        StringBuilder list = new StringBuilder();
        for (String mod : mods) {
            if (allowedMods.contains(mod)) {
                if (fullList) {
                    list.append("§a");
                    list.append(mod);
                    list.append("§f, ");
                }
            }else if (deniedMods.contains(mod)) {
                onlyAllowed = false;
                list.append("§c");
                list.append(mod);
                list.append("§f, ");
            }else {
                onlyAllowed = false;
                list.append("§e");
                list.append(mod);
                list.append("§f, ");
            }
        }
        list.replace(list.lastIndexOf("§f, "), list.length(), "");
        return onlyAllowed && !fullList ? "§2Only allowed mods have been found" : list.toString();
    }
}
