package me.andreasmelone.forgelikepackets.client;

import me.andreasmelone.forgelikepackets.common.IWorkEnqueuer;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class WorkEnqueuerClient implements IWorkEnqueuer {
    @Override
    public void enqueueWork(Runnable runnable) {
        Minecraft.getInstance().execute(runnable);
    }
}
