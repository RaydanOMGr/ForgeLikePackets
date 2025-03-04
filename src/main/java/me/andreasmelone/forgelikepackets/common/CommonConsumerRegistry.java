package me.andreasmelone.forgelikepackets.common;

import io.netty.buffer.ByteBuf;
import me.andreasmelone.forgelikepackets.PacketContext;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Internal class. Should not be used by the user.
 * Use {@link me.andreasmelone.forgelikepackets.PacketRegistry} instead.
 * @see me.andreasmelone.forgelikepackets.PacketRegistry
 */
@ApiStatus.Internal
public class CommonConsumerRegistry {
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
        PayloadTypeRegistry.playC2S().register(wrapper.getParametrizedType(), streamCodec);
        ServerPlayNetworking.registerGlobalReceiver(wrapper.getParametrizedType(), ((payload, context) -> {
            consumer.accept(payload.getMsg(), new PacketContext(new WorkEnqueuerCommon(context.server()), PacketFlow.SERVERBOUND, context.player()));
        }));
    }

    public static <MSG> void sendTo(ServerPlayer player, ResourceLocation id, MSG packet) {
        ServerPlayNetworking.send(player, new CustomPacketPayloadWrapper<>(id, packet));
    }
}
