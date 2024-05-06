package tuxi.wolfbugs.mixin;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.EmoteCommands;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tuxi.wolfbugs.WolfBugs;

@Mixin(EmoteCommands.class)
public class EmoteCommandsMixin {
    @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;executes(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
    private static ArgumentBuilder<?,?> changeExecuteContent(RequiredArgumentBuilder<CommandSourceStack,?> instance, Command<?> command) {
        return instance.executes((commandContext) -> {
            if (commandContext.getSource().getPlayer() != null && commandContext.getSource().getServer().getProfilePermissions(commandContext.getSource().getPlayer().getGameProfile()) < 2 && !commandContext.getSource().getServer().getGameRules().getBoolean(WolfBugs.RULE_ALLOWCHATTING)) {
                commandContext.getSource().getPlayer().sendSystemMessage(Component.translatable("chat.cannotSend").withStyle(ChatFormatting.RED), false);
                return 0;
            }

            MessageArgument.ChatMessage messageargument$chatmessage = MessageArgument.getChatMessage(commandContext, "action");
            CommandSourceStack commandsourcestack = commandContext.getSource();
            PlayerList playerlist = commandsourcestack.getServer().getPlayerList();
            messageargument$chatmessage.resolve(commandsourcestack, (chatMessage) -> playerlist.broadcastChatMessage(chatMessage, commandsourcestack, ChatType.bind(ChatType.EMOTE_COMMAND, commandsourcestack)));
            return 1;
        });
    }
}
