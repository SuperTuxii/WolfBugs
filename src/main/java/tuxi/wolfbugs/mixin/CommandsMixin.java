package tuxi.wolfbugs.mixin;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Commands.class)
public class CommandsMixin {
    @Inject(method = "literal", at = @At("RETURN"))
    private static void changePermissionLevels(String string, CallbackInfoReturnable<LiteralArgumentBuilder<CommandSourceStack>> cir) {
        if (string.equalsIgnoreCase("me") || string.equalsIgnoreCase("msg") || string.equalsIgnoreCase("tell") || string.equalsIgnoreCase("w") || string.equalsIgnoreCase("teammsg") || string.equalsIgnoreCase("tm")) {
            cir.getReturnValue().requires(commandSourceStack -> commandSourceStack.hasPermission(2));
        }
    }
}
