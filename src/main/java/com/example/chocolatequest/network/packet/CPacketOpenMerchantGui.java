package com.example.chocolatequest.network.packet;

import com.example.chocolatequest.ChocolateQuestReDone;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CPacketOpenMerchantGui(int entityId) implements CustomPacketPayload {
    public static final Type<CPacketOpenMerchantGui> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "open_merchant"));

    public static final StreamCodec<FriendlyByteBuf, CPacketOpenMerchantGui> STREAM_CODEC = StreamCodec.ofMember(
        CPacketOpenMerchantGui::write, CPacketOpenMerchantGui::new
    );

    public CPacketOpenMerchantGui(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
