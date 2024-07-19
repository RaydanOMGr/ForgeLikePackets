package me.andreasmelone.forgelikepackets.common;

import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.ApiStatus;

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
