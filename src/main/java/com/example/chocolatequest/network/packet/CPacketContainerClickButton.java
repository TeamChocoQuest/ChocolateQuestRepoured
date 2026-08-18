package com.example.chocolatequest.network.packet;

import com.example.chocolatequest.ChocolateQuestReDone;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.function.Consumer;

public record CPacketContainerClickButton(int button, FriendlyByteBuf extraData) implements CustomPacketPayload {
    public static final Type<CPacketContainerClickButton> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ChocolateQuestReDone.MODID, "container_click"));

    public static final StreamCodec<FriendlyByteBuf, CPacketContainerClickButton> STREAM_CODEC = StreamCodec.ofMember(
        CPacketContainerClickButton::write, CPacketContainerClickButton::new
    );

    public CPacketContainerClickButton(FriendlyByteBuf buf) {
        this(buf.readInt(), readExtraData(buf));
    }

    private static FriendlyByteBuf readExtraData(FriendlyByteBuf buf) {
        FriendlyByteBuf extra = new FriendlyByteBuf(Unpooled.buffer());
        if (buf.isReadable()) {
            extra.writeBytes(buf);
        }
        return extra;
    }

    public CPacketContainerClickButton(int button) {
        this(button, new FriendlyByteBuf(Unpooled.buffer()));
    }

    public CPacketContainerClickButton(int button, Consumer<ByteBuf> extraDataSupplier) {
        this(button, createExtraData(extraDataSupplier));
    }

    private static FriendlyByteBuf createExtraData(Consumer<ByteBuf> extraDataSupplier) {
        FriendlyByteBuf extra = new FriendlyByteBuf(Unpooled.buffer());
        extraDataSupplier.accept(extra);
        return extra;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.button);
        if (this.extraData != null && this.extraData.isReadable()) {
            buf.writeBytes(this.extraData.copy());
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
