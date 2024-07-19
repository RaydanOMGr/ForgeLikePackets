package me.andreasmelone.forgelikepackets.common;

import org.jetbrains.annotations.ApiStatus;

/**
 * Internal class, used to enqueue work to be done on the main thread.
 */
@ApiStatus.Internal
public interface IWorkEnqueuer {
    void enqueueWork(Runnable runnable);
}
