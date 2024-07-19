package me.andreasmelone.forgelikepackets;

import me.andreasmelone.forgelikepackets.common.IWorkEnqueuer;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the context of a packet
 * @see PacketRegistry
 */
public class PacketContext {
    private final IWorkEnqueuer workEnqueuer;
    private final PacketFlow direction;
    private final ServerPlayer sender;

    /**
     * Creates a new packet context
     * Internal, shouldn't be used by the user
     * @param workEnqueuer The work enqueuer
     * @param direction The direction of the packet
     * @param sender The sender of the packet, it can be null if the packet is clientbound
     */
    @ApiStatus.Internal
    public PacketContext(IWorkEnqueuer workEnqueuer, PacketFlow direction,
                         ServerPlayer sender) {
        this.workEnqueuer = workEnqueuer;
        this.direction = direction;
        this.sender = sender;
    }

    /**
     * Gets the direction of the packet
     * @return The direction of the packet
     */
    public PacketFlow getDirection() {
        return direction;
    }

    /**
     * Gets the sender of the packet
     * @return The sender of the packet, can be null if the packet is clientbound
     */
    @Nullable
    public ServerPlayer getSender() {
        return sender;
    }

    /**
     * Enqueues work to be done on the main thread
     * @param runnable The work to be done
     */
    public void enqueueWork(Runnable runnable) {
        workEnqueuer.enqueueWork(runnable);
    }
}
