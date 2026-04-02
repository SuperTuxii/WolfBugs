package tuxi.wolfbugs.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.BiConsumer;

@Mixin(GameRules.BooleanValue.class)
public interface BooleanValueAccessor {
    @Invoker("create")
    static GameRules.Type<GameRules.BooleanValue> create(boolean p_46253_, BiConsumer<MinecraftServer, GameRules.BooleanValue> p_46254_) {
        throw new AssertionError();
    }
}
