package tuxi.wolfbugs.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientboundPlayerInfoPacket.PlayerUpdate.class)
public interface PlayerUpdateAccessor {
    @Mutable
    @Accessor("profile")
    void setProfile(GameProfile profile);
}
