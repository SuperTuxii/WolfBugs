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
    private static final DynamicCommandExceptionType ERROR_NON_LIVING = new DynamicCommandExceptionType(
            (entity) -> Component.literal(String.format("The entity %s is not a LivingEntity", entity.toString()))/*Component.translatable("wolfbugs.combat_tracker.error_non_living", entity.toString())*/);

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
                                                ctx.getSource().sendSuccess(Component.literal(String.format("Removed latest entry by %s in %d combat-trackers of %d entites", toRemove.getName(), i, targets.size())), true);
//                                                ctx.getSource().sendSuccess(Component.translatable("wolfbugs.combat_tracker.remove_latest_success", toRemove.getName(), i, targets.size()), true);
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
                                                ctx.getSource().sendSuccess(Component.literal(String.format("Removed %s in %d entries in %d combat-trackers of %d entites", toRemove.getName().getString(), entries, trackers, targets.size())), true);
//                                                ctx.getSource().sendSuccess(Component.translatable("wolfbugs.combat_tracker.remove_all_success", toRemove.getName().getString(), entries, trackers, targets.size()), true);
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
                                ctx.getSource().sendSuccess(Component.literal(String.format("Cleared %d combat-trackers of %d entites", i, targets.size())), true);
//                                ctx.getSource().sendSuccess(Component.translatable("wolfbugs.combat_tracker.clear_success", i, targets.size()), true);
                                return i;
                            })
                    );
        }
    }
}
