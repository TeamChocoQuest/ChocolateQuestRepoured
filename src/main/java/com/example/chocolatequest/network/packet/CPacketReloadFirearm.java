package com.example.chocolatequest.network.packet;

import com.example.chocolatequest.ChocolateQuestReDone;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CPacketReloadFirearm() implements CustomPacketPayload {

    public static final Type<CPacketReloadFirearm> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "c_reload_firearm")
    );

    public static final StreamCodec<FriendlyByteBuf, CPacketReloadFirearm> STREAM_CODEC = StreamCodec.ofMember(
            CPacketReloadFirearm::write,
            CPacketReloadFirearm::new
    );

    public CPacketReloadFirearm(FriendlyByteBuf buffer) {
        this();
    }

    private void write(FriendlyByteBuf buffer) {
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
