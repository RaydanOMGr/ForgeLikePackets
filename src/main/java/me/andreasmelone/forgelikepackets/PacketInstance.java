package me.andreasmelone.forgelikepackets;

import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.BiConsumer;
import java.util.function.Function;

@ApiStatus.Internal
public record PacketInstance<MSG>(Class<MSG> packetClass,
                                  BiConsumer<MSG, FriendlyByteBuf> encoder,
                                  Function<FriendlyByteBuf, MSG> decoder,
                                  BiConsumer<MSG, PacketContext> consumer) {
}
