package com.teamcqr.chocolatequestrepoured.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ExtendedReachAttackPacket implements IMessage
{
    private int entityId;
    private int isExtended;

    public ExtendedReachAttackPacket()
    {

    }

    public ExtendedReachAttackPacket(int entityId, boolean isExtended)
    {
        this.entityId = entityId;
        this.isExtended = isExtended ? 1 : 0;
    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        entityId = ByteBufUtils.readVarInt(byteBuf, 4);
        isExtended = ByteBufUtils.readVarInt(byteBuf, 1);
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeVarInt(byteBuf, entityId, 4);
        ByteBufUtils.writeVarInt(byteBuf, isExtended, 1);
    }

    public int getEntityId() {
        return entityId;
    }

    public boolean getIsExtended() {
        return (isExtended == 1);
    }
}
