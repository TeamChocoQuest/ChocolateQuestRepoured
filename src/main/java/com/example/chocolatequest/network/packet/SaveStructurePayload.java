package com.example.chocolatequest.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import com.example.chocolatequest.ChocolateQuestReDone;

public record SaveStructurePayload(
        BlockPos pos,
        String structureName,
        int startX, int startY, int startZ,
        int endX, int endY, int endZ,
        boolean relativeMode, boolean ignoreEntities
) implements CustomPacketPayload {

    public static final Type<SaveStructurePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "save_structure"));

    public static final StreamCodec<FriendlyByteBuf, SaveStructurePayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> payload.write(buf),
            SaveStructurePayload::new
    );

    public SaveStructurePayload(FriendlyByteBuf buf) {
        this(
                buf.readBlockPos(),
                buf.readUtf(),
                buf.readInt(), buf.readInt(), buf.readInt(),
                buf.readInt(), buf.readInt(), buf.readInt(),
                buf.readBoolean(), buf.readBoolean()
        );
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeUtf(structureName);
        buf.writeInt(startX);
        buf.writeInt(startY);
        buf.writeInt(startZ);
        buf.writeInt(endX);
        buf.writeInt(endY);
        buf.writeInt(endZ);
        buf.writeBoolean(relativeMode);
        buf.writeBoolean(ignoreEntities);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
