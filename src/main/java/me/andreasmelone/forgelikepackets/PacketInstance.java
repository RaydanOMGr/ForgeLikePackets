package me.andreasmelone.forgelikepackets;

import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@ApiStatus.Internal
public class PacketInstance<MSG> {
    protected Class<MSG> packetClass;
    protected BiConsumer<MSG, FriendlyByteBuf> encoder;
    protected Function<FriendlyByteBuf, MSG> decoder;
    protected BiConsumer<MSG, Supplier<PacketContext>> consumer;

    public PacketInstance(Class<MSG> packetClass,
                          BiConsumer<MSG, FriendlyByteBuf> encoder,
                          Function<FriendlyByteBuf, MSG> decoder,
                          BiConsumer<MSG, Supplier<PacketContext>> consumer) {
        this.packetClass = packetClass;
        this.encoder = encoder;
        this.decoder = decoder;
        this.consumer = consumer;
    }

    public Class<MSG> getPacketClass() {
        return packetClass;
    }

    public BiConsumer<MSG, FriendlyByteBuf> getEncoder() {
        return encoder;
    }

    public Function<FriendlyByteBuf, MSG> getDecoder() {
        return decoder;
    }

    public BiConsumer<MSG, Supplier<PacketContext>> getConsumer() {
        return consumer;
    }
}
