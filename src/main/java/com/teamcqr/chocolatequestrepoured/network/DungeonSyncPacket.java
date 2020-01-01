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
	private HashMap<Integer, String[]> dependencyMap = new HashMap<>();

	public DungeonSyncPacket() {

	}

	public DungeonSyncPacket(List<DungeonBase> dungeons) {
		this.dungeonList = dungeons;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int size = buf.readInt();
		for (int i = 0; i < size; i++) {
			String name = ByteBufUtils.readUTF8String(buf);
			int id = buf.readInt();
			this.dungeonMap.put(name, id);

			// Dependencies
			int dependencyCount = buf.readInt();
			String[] dep = new String[dependencyCount];
			for (int j = 0; j < dependencyCount; j++) {
				dep[j] = ByteBufUtils.readUTF8String(buf);
			}
			this.dependencyMap.put(id, dep);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.dungeonList.size());
		for (DungeonBase dungeon : this.dungeonList) {
			ByteBufUtils.writeUTF8String(buf, dungeon.getDungeonName());
			buf.writeInt(dungeon.getIconID());

			// Dependencies
			buf.writeInt(dungeon.getDependencies().length);
			for (String dependency : dungeon.getDependencies()) {
				ByteBufUtils.writeUTF8String(buf, dependency);
			}
		}
	}

	public HashMap<String, Integer> getDungeonMap() {
		return this.dungeonMap;
	}

	public HashMap<Integer, String[]> getDependencyMap() {
		return this.dependencyMap;
	}

}
