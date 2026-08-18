package com.example.chocolatequest.network.packet;

import com.example.chocolatequest.ChocolateQuestReDone;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CPacketSyncEntity(int entityId, int healthScaling, int dropChanceHelm, int dropChanceChest, int dropChanceLegs, int dropChanceFeet, int dropChanceMainhand, int dropChanceOffhand, int sizeScaling) implements CustomPacketPayload {
    public static final Type<CPacketSyncEntity> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "sync_entity"));

    public static final StreamCodec<FriendlyByteBuf, CPacketSyncEntity> STREAM_CODEC = StreamCodec.ofMember(
        CPacketSyncEntity::write, CPacketSyncEntity::new
    );

    public CPacketSyncEntity(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeInt(this.healthScaling);
        buf.writeInt(this.dropChanceHelm);
        buf.writeInt(this.dropChanceChest);
        buf.writeInt(this.dropChanceLegs);
        buf.writeInt(this.dropChanceFeet);
        buf.writeInt(this.dropChanceMainhand);
        buf.writeInt(this.dropChanceOffhand);
        buf.writeInt(this.sizeScaling);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
