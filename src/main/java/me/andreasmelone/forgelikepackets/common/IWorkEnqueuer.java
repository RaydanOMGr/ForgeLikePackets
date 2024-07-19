package me.andreasmelone.forgelikepackets.common;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface IWorkEnqueuer {
    void enqueueWork(Runnable runnable);
}
