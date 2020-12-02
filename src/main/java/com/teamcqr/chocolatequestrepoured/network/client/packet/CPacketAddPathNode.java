package com.teamcqr.chocolatequestrepoured.network.client.packet;

import com.teamcqr.chocolatequestrepoured.util.ByteBufUtil;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.IntCollection;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CPacketAddPathNode implements IMessage {

	private EnumHand hand;
	private int rootNode;
	private BlockPos pos;
	private int waitingTimeMin;
	private int waitingTimeMax;
	private float waitingRotation;
	private int weight;
	private int timeMin;
	private int timeMax;
	private boolean bidirectional;
	private int[] blacklistedPrevNodes;

	public CPacketAddPathNode() {

	}

	public CPacketAddPathNode(EnumHand hand, int rootNode, BlockPos pos, int waitingTimeMin, int waitingTimeMax, float waitingRotation, int weight, int timeMin, int timeMax, boolean bidirectional, IntCollection blacklistedPrevNodes) {
		this.hand = hand;
		this.rootNode = rootNode;
		this.pos = pos;
		this.waitingTimeMin = waitingTimeMin;
		this.waitingTimeMax = waitingTimeMax;
		this.waitingRotation = waitingRotation;
		this.weight = weight;
		this.timeMin = timeMin;
		this.timeMax = timeMax;
		this.bidirectional = bidirectional;
		this.blacklistedPrevNodes = blacklistedPrevNodes.toIntArray();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.hand = EnumHand.values()[buf.readByte()];
		this.rootNode = buf.readInt();
		this.pos = ByteBufUtil.readBlockPos(buf);
		this.waitingTimeMin = buf.readShort();
		this.waitingTimeMax = buf.readShort();
		this.waitingRotation = buf.readFloat();
		this.weight = buf.readShort();
		this.timeMin = buf.readShort();
		this.timeMax = buf.readShort();
		this.bidirectional = buf.readBoolean();
		this.blacklistedPrevNodes = new int[buf.readInt()];
		for (int i = 0; i < this.blacklistedPrevNodes.length; i++) {
			this.blacklistedPrevNodes[i] = buf.readInt();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(this.hand.ordinal());
		buf.writeInt(this.rootNode);
		ByteBufUtil.writeBlockPos(buf, this.pos);
		buf.writeShort(this.waitingTimeMin);
		buf.writeShort(this.waitingTimeMax);
		buf.writeFloat(this.waitingRotation);
		buf.writeShort(this.weight);
		buf.writeShort(this.timeMin);
		buf.writeShort(this.timeMax);
		buf.writeBoolean(this.bidirectional);
		buf.writeInt(this.blacklistedPrevNodes.length);
		for (int i = 0; i < this.blacklistedPrevNodes.length; i++) {
			buf.writeInt(this.blacklistedPrevNodes[i]);
		}
	}

	public EnumHand getHand() {
		return this.hand;
	}

	public int getRootNode() {
		return this.rootNode;
	}

	public BlockPos getPos() {
		return this.pos;
	}

	public int getWaitingTimeMin() {
		return this.waitingTimeMin;
	}

	public int getWaitingTimeMax() {
		return this.waitingTimeMax;
	}

	public float getWaitingRotation() {
		return this.waitingRotation;
	}

	public int getWeight() {
		return this.weight;
	}

	public int getTimeMin() {
		return this.timeMin;
	}

	public int getTimeMax() {
		return this.timeMax;
	}

	public boolean isBidirectional() {
		return this.bidirectional;
	}

	public int[] getBlacklistedPrevNodes() {
		return this.blacklistedPrevNodes;
	}

}
