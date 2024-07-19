package me.andreasmelone.forgelikepackets.client;

import me.andreasmelone.forgelikepackets.common.IWorkEnqueuer;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.ApiStatus;

/**
 * Internal class, used to enqueue work to be done on the main thread.
 * The work will be executed on the main thread of the client.
 * This class shouldn't be used by the user, but if it is, never instantiate it on the server-side.
 */
@ApiStatus.Internal
public class WorkEnqueuerClient implements IWorkEnqueuer {
    @Override
    public void enqueueWork(Runnable runnable) {
        Minecraft.getInstance().execute(runnable);
    }
}
