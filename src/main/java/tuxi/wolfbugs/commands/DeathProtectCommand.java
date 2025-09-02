package tuxi.wolfbugs.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import tuxi.wolfbugs.mixininterface.DeathProtectEntity;

import java.util.Collection;

public class DeathProtectCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("deathprotect")
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.literal("add")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes(ctx -> {
                                    Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "targets");
                                    for (ServerPlayer target : players) {
                                        if (target instanceof DeathProtectEntity deathProtectEntity) {
                                            deathProtectEntity.wolfBugs$enableDeathProtect();
                                        }
                                    }
                                    return players.size();
                                })
                        )
                ).then(Commands.literal("get")
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(ctx -> {
                                    if (EntityArgument.getPlayer(ctx, "target") instanceof DeathProtectEntity deathProtectEntity) {
                                        return deathProtectEntity.wolfBugs$hasDeathProtect() ? 1 : 0;
                                    }
                                    return 0;
                                })
                        )
                ).then(Commands.literal("remove")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes(ctx -> {
                                    Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "targets");
                                    for (ServerPlayer target : players) {
                                        if (target instanceof DeathProtectEntity deathProtectEntity) {
                                            deathProtectEntity.wolfBugs$removeDeathProtect();
                                        }
                                    }
                                    return players.size();
                                })
                        )
                )
        );
    }
}
