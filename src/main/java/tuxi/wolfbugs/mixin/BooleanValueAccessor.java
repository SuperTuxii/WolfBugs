package tuxi.wolfbugs.mixin;

import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRules.BooleanValue.class)
public interface BooleanValueAccessor {
    @Invoker("create")
    static GameRules.Type<GameRules.BooleanValue> create(boolean p_46251_) {
        throw new AssertionError();
    }
}
