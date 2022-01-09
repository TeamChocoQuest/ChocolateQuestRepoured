package team.cqr.cqrepoured.network.client.packet;

import it.unimi.dsi.fastutil.ints.IntCollection;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import team.cqr.cqrepoured.network.AbstractPacket;

public class CPacketAddPathNode extends AbstractPacket<CPacketAddPathNode>{

	private Hand hand;
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

	public CPacketAddPathNode(Hand hand, int rootNode, BlockPos pos, int waitingTimeMin, int waitingTimeMax, float waitingRotation, int weight, int timeMin, int timeMax, boolean bidirectional, IntCollection blacklistedPrevNodes) {
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

	public CPacketAddPathNode fromBytes(PacketBuffer buf) {
		CPacketAddPathNode result = new CPacketAddPathNode();
		result.hand = Hand.values()[buf.readByte()];
		result.rootNode = buf.readInt();
		result.pos = buf.readBlockPos();
		result.waitingTimeMin = buf.readShort();
		result.waitingTimeMax = buf.readShort();
		result.waitingRotation = buf.readFloat();
		result.weight = buf.readShort();
		result.timeMin = buf.readShort();
		result.timeMax = buf.readShort();
		result.bidirectional = buf.readBoolean();
		result.blacklistedPrevNodes = new int[buf.readInt()];
		for (int i = 0; i < result.blacklistedPrevNodes.length; i++) {
			result.blacklistedPrevNodes[i] = buf.readInt();
		}
		
		return result;
	}

	public void toBytes(CPacketAddPathNode packet, PacketBuffer buf) {
		buf.writeByte(packet.hand.ordinal());
		buf.writeInt(packet.rootNode);
		buf.writeBlockPos(packet.pos);
		buf.writeShort(packet.waitingTimeMin);
		buf.writeShort(packet.waitingTimeMax);
		buf.writeFloat(packet.waitingRotation);
		buf.writeShort(packet.weight);
		buf.writeShort(packet.timeMin);
		buf.writeShort(packet.timeMax);
		buf.writeBoolean(packet.bidirectional);
		buf.writeInt(packet.blacklistedPrevNodes.length);
		for (int i = 0; i < packet.blacklistedPrevNodes.length; i++) {
			buf.writeInt(packet.blacklistedPrevNodes[i]);
		}
	}

	public Hand getHand() {
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

	@Override
	public Class<CPacketAddPathNode> getPacketClass() {
		return CPacketAddPathNode.class;
	}

}
