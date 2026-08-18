package com.example.chocolatequest.network.packet;

import com.example.chocolatequest.ChocolateQuestReDone;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SPacketUpdatePlayerReputation(java.util.UUID playerId, String faction, int reputation) implements CustomPacketPayload {
    public static final Type<SPacketUpdatePlayerReputation> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "update_reputation"));

    public static final StreamCodec<FriendlyByteBuf, SPacketUpdatePlayerReputation> STREAM_CODEC = StreamCodec.ofMember(
        SPacketUpdatePlayerReputation::write, SPacketUpdatePlayerReputation::new
    );

    public SPacketUpdatePlayerReputation(FriendlyByteBuf buf) {
        this(buf.readUUID(), buf.readUtf(), buf.readInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(this.playerId);
        buf.writeUtf(this.faction);
        buf.writeInt(this.reputation);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
