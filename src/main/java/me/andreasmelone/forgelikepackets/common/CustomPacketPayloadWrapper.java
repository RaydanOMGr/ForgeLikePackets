package me.andreasmelone.forgelikepackets.common;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class CustomPacketPayloadWrapper<MSG> implements CustomPacketPayload {
    private MSG msg;
    private final Type<CustomPacketPayloadWrapper<MSG>> type;
    public CustomPacketPayloadWrapper(ResourceLocation id, MSG msg) {
        this.type = new Type<>(id);
        this.msg = msg;
    }

    public MSG getMsg() {
        return msg;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return type;
    }

    public Type<CustomPacketPayloadWrapper<MSG>> getParametrizedType() {
        return type;
    }
}
