package tuxi.wolfbugs.mixin;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.TeamMsgCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tuxi.wolfbugs.WolfBugs;

@Mixin(TeamMsgCommand.class)
public abstract class TeamMsgCommandMixin {
    @Shadow
    private static int sendMessage(CommandSourceStack p_214763_, MessageArgument.ChatMessage p_214764_) {
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
                return sendMessage(commandContext.getSource(), messageargument$chatmessage);
            } catch (Exception exception) {
                messageargument$chatmessage.consume(commandContext.getSource());
                throw exception;
            }
        });
    }
}
