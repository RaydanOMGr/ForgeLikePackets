package me.andreasmelone.forgelikepackets.common;

import io.netty.buffer.Unpooled;
import me.andreasmelone.forgelikepackets.PacketContext;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Internal class. Should not be used by the user.
 */
@ApiStatus.Internal
public class CommonConsumerRegistry {
    public static <MSG> void register(
            ResourceLocation id,
            Function<FriendlyByteBuf, MSG> decoder,
            BiConsumer<MSG, Supplier<PacketContext>> consumer
    ) {
        ServerPlayNetworking.registerGlobalReceiver(id, (server, sender, handler, buf, responseSender) -> {
            MSG packet = decoder.apply(buf);
            consumer.accept(packet, () -> new PacketContext(new WorkEnqueuerCommon(server), PacketFlow.SERVERBOUND, sender));
        });
    }

    public static <MSG> void sendTo(ServerPlayer player, ResourceLocation id, MSG packet, BiConsumer<MSG, FriendlyByteBuf> encoder) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        encoder.accept(packet, buf);
        ServerPlayNetworking.send(player, id, buf);
    }
}
