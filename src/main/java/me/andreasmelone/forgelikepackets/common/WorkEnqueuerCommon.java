package me.andreasmelone.forgelikepackets.common;

import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.ApiStatus;

/**
 * Internal class, used to enqueue work to be done on the main thread.
 * Takes a {@link MinecraftServer} as a parameter.
 * The work will be executed on the main thread of the server.
 */
@ApiStatus.Internal
public class WorkEnqueuerCommon implements IWorkEnqueuer {
    private final MinecraftServer server;

    public WorkEnqueuerCommon(MinecraftServer server) {
        this.server = server;
    }

    public void enqueueWork(Runnable runnable) {
        server.execute(runnable);
    }
}
