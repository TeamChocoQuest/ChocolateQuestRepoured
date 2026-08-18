package com.example.chocolatequest.network.packet;

import com.example.chocolatequest.ChocolateQuestReDone;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CPacketDodge() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<CPacketDodge> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "c_dodge"));

    public static final StreamCodec<FriendlyByteBuf, CPacketDodge> STREAM_CODEC = StreamCodec.ofMember(
        CPacketDodge::write,
        CPacketDodge::new
    );

    public CPacketDodge(FriendlyByteBuf buffer) {
        this();
    }

    public void write(FriendlyByteBuf buffer) {
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
