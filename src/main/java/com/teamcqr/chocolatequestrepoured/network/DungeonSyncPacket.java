package com.teamcqr.chocolatequestrepoured.network;

import java.util.HashMap;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class DungeonSyncPacket implements IMessage {

	private List<DungeonBase> dungeonList;
	private HashMap<String, Integer> dungeonMap = new HashMap<String, Integer>();

	public DungeonSyncPacket() {

	}

	public DungeonSyncPacket(List<DungeonBase> dungeons) {
		this.dungeonList = dungeons;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int size = buf.readInt();
		for (int i = 0; i < size; i++) {
			this.dungeonMap.put(ByteBufUtils.readUTF8String(buf), buf.readInt());
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.dungeonList.size());
		for (DungeonBase dungeon : this.dungeonList) {
			ByteBufUtils.writeUTF8String(buf, dungeon.getDungeonName());
			buf.writeInt(dungeon.getIconID());
		}
	}

	public HashMap<String, Integer> getDungeonMap() {
		return dungeonMap;
	}

}
