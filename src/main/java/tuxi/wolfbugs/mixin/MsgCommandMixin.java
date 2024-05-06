package tuxi.wolfbugs.mixin;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.MsgCommand;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tuxi.wolfbugs.WolfBugs;

import java.util.Collection;

@Mixin(MsgCommand.class)
public abstract class MsgCommandMixin {
    @Shadow
    private static int sendMessage(CommandSourceStack p_214523_, Collection<ServerPlayer> p_214524_, MessageArgument.ChatMessage p_214525_) {
        throw new AssertionError();
    }

    @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;executes(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
    private static ArgumentBuilder<?,?> changeExecuteContent(RequiredArgumentBuilder<CommandSourceStack,?> instance, Command<?> command) {
        return instance.executes((commandContext) -> {
            if (commandContext.getSource().getPlayer() != null && commandContext.getSource().getServer().getProfilePermissions(commandContext.getSource().getPlayer().getGameProfile()) < 2 && !commandContext.getSource().getServer().getGameRules().getBoolean(WolfBugs.RULE_ALLOWCHATTING)) {
                commandContext.getSource().getPlayer().sendSystemMessage(Component.translatable("chat.cannotSend").withStyle(ChatFormatting.RED), false);
                return 0;
            }

            MessageArgument.ChatMessage messageargument$chatmessage = MessageArgument.getChatMessage(commandContext, "message");

            try {
                return sendMessage(commandContext.getSource(), EntityArgument.getPlayers(commandContext, "targets"), messageargument$chatmessage);
            } catch (Exception exception) {
                messageargument$chatmessage.consume(commandContext.getSource());
                throw exception;
            }
        });
    }
}
