package me.andreasmelone.forgelikepackets;

import me.andreasmelone.forgelikepackets.client.ClientConsumerRegistry;
import me.andreasmelone.forgelikepackets.common.CommonConsumerRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The packet registry. Used to register and send packets
 * Uses Fabric APIs to register and send packets
 */
public class PacketRegistry {
    public static final PacketRegistry INSTANCE = new PacketRegistry();

    private final Map<ResourceLocation, PacketInstance<?>> packets = new HashMap<>();

    /**
     * Registers a packet
     * @param id The id of the packet
     * @param packetInstance The packet instance
     * @param <MSG> The packet type
     */
    @ApiStatus.Internal
    private <MSG> void register(ResourceLocation id, PacketInstance<MSG> packetInstance) {
        if(packets.containsKey(id)) throw new IllegalArgumentException("Packet with id " + id + " already registered");

        packets.put(id, packetInstance);
        if (canRegister()) {
            ClientConsumerRegistry.register(
                    id,
                    packetInstance.decoder(),
                    packetInstance.consumer()
            );
        }
        CommonConsumerRegistry.register(
                id,
                packetInstance.decoder(),
                packetInstance.consumer()
        );
    }

    /**
     * Registers a packet
     * @param id The id of the packet
     * @param clazz The class of the packet, of type MSG
     * @param encoder The encoder of the packet, encodes packet to ByteBuf
     * @param decoder The decoder of the packet, decodes packet from ByteBuf
     * @param consumer The consumer of the packet, what to do when the packet is received
     * @param <MSG> The packet type
     */
    public <MSG> void register(ResourceLocation id, Class<MSG> clazz,
                               BiConsumer<MSG, FriendlyByteBuf> encoder,
                               Function<FriendlyByteBuf, MSG> decoder,
                               BiConsumer<MSG, PacketContext> consumer) {
        PacketInstance<MSG> packetInstance = new PacketInstance<>(clazz, encoder, decoder, consumer);
        register(id, packetInstance);
    }

    /**
     * Sends a packet to the server
     * @param id The id of the packet, has to be registered with {@link #register(ResourceLocation, Class, BiConsumer, Function, BiConsumer)}
     * @param packet The packet to send
     * @param <MSG> The packet type
     */
    public <MSG> void sendToServer(ResourceLocation id, MSG packet) {
        PacketInstance<MSG> packetInstance = (PacketInstance<MSG>) packets.get(id);
        ClientConsumerRegistry.sendToServer(id, packet, packetInstance.encoder());
    }


    /**
     * Sends a packet to a player
     * @param player The player to send the packet to
     * @param id The id of the packet, has to be registered with {@link #register(ResourceLocation, Class, BiConsumer, Function, BiConsumer)}
     * @param packet The packet to send
     * @param <MSG> The packet type
     */
    public <MSG> void sendTo(ServerPlayer player, ResourceLocation id, MSG packet) {
        PacketInstance<MSG> packetInstance = (PacketInstance<MSG>) packets.get(id);
        CommonConsumerRegistry.sendTo(player, id, packet, packetInstance.encoder());
    }

    /**
     * Internal method to check if the client consumer registry can be registered
     * @return If the client consumer registry can be registered
     */
    @ApiStatus.Internal
    private static boolean canRegister() {
        try {
            Class.forName("net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
