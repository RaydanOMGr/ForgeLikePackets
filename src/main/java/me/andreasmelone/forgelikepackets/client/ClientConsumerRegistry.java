package me.andreasmelone.forgelikepackets.client;

import io.netty.buffer.ByteBuf;
import me.andreasmelone.forgelikepackets.PacketContext;
import me.andreasmelone.forgelikepackets.common.CustomPacketPayloadWrapper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
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
            BiConsumer<MSG, FriendlyByteBuf> encoder,
            BiConsumer<MSG, PacketContext> consumer
    ) {
        CustomPacketPayloadWrapper<MSG> wrapper = new CustomPacketPayloadWrapper<>(id, null);
        StreamCodec<ByteBuf, CustomPacketPayloadWrapper<MSG>> streamCodec = new StreamCodec<>() {
            @Override
            public CustomPacketPayloadWrapper<MSG> decode(ByteBuf buf) {
                return new CustomPacketPayloadWrapper<>(id, decoder.apply(new FriendlyByteBuf(buf)));
            }

            @Override
            public void encode(ByteBuf buf, CustomPacketPayloadWrapper<MSG> o) {
                encoder.accept(o.getMsg(), new FriendlyByteBuf(buf));
            }
        };
        PayloadTypeRegistry.playS2C().register(wrapper.getParametrizedType(), streamCodec);
        ClientPlayNetworking.registerGlobalReceiver(wrapper.getParametrizedType(), ((payload, context) -> {
            consumer.accept(payload.getMsg(), new PacketContext(new WorkEnqueuerClient(), PacketFlow.CLIENTBOUND, null));
        }));
    }

    public static <MSG> void sendToServer(ResourceLocation id, MSG packet) {
        ClientPlayNetworking.send(new CustomPacketPayloadWrapper<>(id, packet));
    }
}
