package com.teamcqr.chocolatequestrepoured.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ExtendedReachAttackPacket implements IMessage
{
    private int entityId;

    public ExtendedReachAttackPacket()
    {

    }

    public ExtendedReachAttackPacket(int entityId)
    {
        this.entityId = entityId;
    }

    @Override
    public void fromBytes(ByteBuf byteBuf)
    {
        entityId = ByteBufUtils.readVarInt(byteBuf, 4);
    }

    @Override
    public void toBytes(ByteBuf byteBuf)
    {
        ByteBufUtils.writeVarInt(byteBuf, entityId, 4);
    }

    public int getEntityId()
    {
        return entityId;
    }
}
