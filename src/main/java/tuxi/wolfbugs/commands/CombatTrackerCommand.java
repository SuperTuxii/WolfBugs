package tuxi.wolfbugs.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import tuxi.wolfbugs.CombatTrackerContainer;

import java.util.Collection;

public class CombatTrackerCommand {
    private static final DynamicCommandExceptionType ERROR_NON_LIVING = new DynamicCommandExceptionType((entity) -> Component.literal("The entity " + entity.toString() + " "));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("combattracker")
                .requires(cs -> cs.hasPermission(2))
                .then(RemoveCommand.register())
                .then(ClearCommand.register()));
    }

    private static class RemoveCommand {
        static ArgumentBuilder<CommandSourceStack, ?> register() {
            return Commands.literal("remove")
                    .requires(cs -> cs.hasPermission(2))
                    .then(Commands.literal("latest")
                            .then(Commands.argument("targets", EntityArgument.entities())
                                    .then(Commands.argument("entityToRemove", EntityArgument.entity())
                                            .executes(ctx -> {
                                                Collection<? extends Entity> targets = EntityArgument.getEntities(ctx, "targets");
                                                Entity toRemove = EntityArgument.getEntity(ctx, "entityToRemove");
                                                if (!(toRemove instanceof LivingEntity)) throw ERROR_NON_LIVING.create(toRemove);
                                                for (Entity entity : targets) {
                                                    if (!(entity instanceof LivingEntity)) throw ERROR_NON_LIVING.create(entity);
                                                }
                                                int i = 0;
                                                for (Entity entity : targets) {
                                                    if (((CombatTrackerContainer) ((LivingEntity) entity).getCombatTracker()).wolfBugs$removeLatest((LivingEntity) toRemove)) i++;
                                                }
                                                ctx.getSource().sendSuccess(Component.literal("Removed latest entry by " + toRemove.getName().getString() + " in " + i + " combat-trackers of " + targets.size() + " entites"), true);
                                                return i;
                                            })
                                    )
                            )
                    )
                    .then(Commands.literal("all")
                            .then(Commands.argument("targets", EntityArgument.entities())
                                    .then(Commands.argument("entityToRemove", EntityArgument.entity())
                                            .executes(ctx -> {
                                                Collection<? extends Entity> targets = EntityArgument.getEntities(ctx, "targets");
                                                Entity toRemove = EntityArgument.getEntity(ctx, "entityToRemove");
                                                if (!(toRemove instanceof LivingEntity)) throw ERROR_NON_LIVING.create(toRemove);
                                                for (Entity entity : targets) {
                                                    if (!(entity instanceof LivingEntity)) throw ERROR_NON_LIVING.create(entity);
                                                }
                                                int entries = 0;
                                                int trackers = 0;
                                                for (Entity entity : targets) {
                                                    int found = ((CombatTrackerContainer) ((LivingEntity) entity).getCombatTracker()).wolfBugs$remove((LivingEntity) toRemove);
                                                    entries += found;
                                                    if (found > 0) trackers++;
                                                }
                                                ctx.getSource().sendSuccess(Component.literal("Removed " + toRemove.getName().getString() + " in " + entries + " entries in " + trackers + " combat-trackers of " + targets.size() + " entites"), true);
                                                return entries;
                                            })
                                    )
                            )
                    );
        }
    }

    private static class ClearCommand {
        static ArgumentBuilder<CommandSourceStack, ?> register() {
            return Commands.literal("clear")
                    .requires(cs -> cs.hasPermission(2))
                    .then(Commands.argument("targets", EntityArgument.entities())
                            .executes(ctx -> {
                                Collection<? extends Entity> targets = EntityArgument.getEntities(ctx, "targets");
                                for (Entity entity : targets) {
                                    if (!(entity instanceof LivingEntity)) throw ERROR_NON_LIVING.create(entity);
                                }
                                int i = 0;
                                for (Entity entity : targets) {
                                    if (((CombatTrackerContainer) ((LivingEntity) entity).getCombatTracker()).wolfBugs$clear()) i++;
                                }
                                ctx.getSource().sendSuccess(Component.literal("Cleared " + i + " combat-trackers of " + targets.size() + " entites"), true);
                                return i;
                            })
                    );
        }
    }
}
