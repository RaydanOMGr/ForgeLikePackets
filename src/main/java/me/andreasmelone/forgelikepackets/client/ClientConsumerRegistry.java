package me.andreasmelone.forgelikepackets.client;

import io.netty.buffer.Unpooled;
import me.andreasmelone.forgelikepackets.PacketContext;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Internal class. Should not be used by the user.
 * Use {@link me.andreasmelone.forgelikepackets.PacketRegistry} instead.
 * @see me.andreasmelone.forgelikepackets.PacketRegistry
 */
@ApiStatus.Internal
public class ClientConsumerRegistry {
    public static <MSG> void register(
            ResourceLocation id,
            Function<FriendlyByteBuf, MSG> decoder,
            BiConsumer<MSG, PacketContext> consumer
    ) {
        ClientPlayNetworking.registerGlobalReceiver(id, (client, handler, buf, responseSender) -> {
            MSG packet = decoder.apply(buf);
            consumer.accept(packet, new PacketContext(new WorkEnqueuerClient(), PacketFlow.CLIENTBOUND, null));
        });
    }

    public static <MSG> void sendToServer(ResourceLocation id, MSG packet, BiConsumer<MSG, FriendlyByteBuf> encoder) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        encoder.accept(packet, buf);
        ClientPlayNetworking.send(id, buf);
    }
}
