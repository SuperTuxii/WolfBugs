package tuxi.wolfbugs.networking;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import tuxi.wolfbugs.WolfBugs;

import java.util.function.Supplier;

public record ClientboundCosmeticsGamerulePacket(boolean capesDisabled, boolean cosmeticsDisabled) {
    public ClientboundCosmeticsGamerulePacket(FriendlyByteBuf buffer) {
        this(buffer.readBoolean(), buffer.readBoolean());
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(capesDisabled);
        buffer.writeBoolean(cosmeticsDisabled);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getNetworkManager().getPacketListener() instanceof ClientPacketListener) {
            WolfBugs.capesDisabled = capesDisabled;
            WolfBugs.cosmeticsDisabled = cosmeticsDisabled;
        } else {
            WolfBugs.LOGGER.warn("Expected PacketListener to be ClientPacketListener, but is {}", ctx.get().getNetworkManager().getPacketListener().getClass());
        }
    }
}
