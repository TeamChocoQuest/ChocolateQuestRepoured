package com.teamcqr.chocolatequestrepoured.network;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.objects.items.ItemDungeonPlacer.FakeDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class DungeonSyncPacket implements IMessage {

	private List<DungeonBase> dungeonList = new ArrayList<DungeonBase>();
	private List<FakeDungeon> fakeDungeonList = new ArrayList<FakeDungeon>();

	public DungeonSyncPacket() {

	}

	public DungeonSyncPacket(List<DungeonBase> dungeons) {
		this.dungeonList = dungeons;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int dungeonCount = buf.readByte();
		this.fakeDungeonList = new ArrayList<FakeDungeon>(dungeonCount);
		for (int i = 0; i < dungeonCount; i++) {
			String name = ByteBufUtils.readUTF8String(buf);
			int iconID = buf.readByte();
			int dependencyCount = buf.readByte();
			String[] dependencies = new String[dependencyCount];
			for (int j = 0; j < dependencyCount; j++) {
				dependencies[j] = ByteBufUtils.readUTF8String(buf);
			}

			this.fakeDungeonList.add(new FakeDungeon(name, iconID, dependencies));
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(this.dungeonList.size());
		for (DungeonBase dungeon : this.dungeonList) {
			ByteBufUtils.writeUTF8String(buf, dungeon.getDungeonName());
			buf.writeByte(dungeon.getIconID());
			buf.writeByte(dungeon.getDependencies().length);
			for (String dependency : dungeon.getDependencies()) {
				ByteBufUtils.writeUTF8String(buf, dependency);
			}
		}
	}

	public List<FakeDungeon> getFakeDungeonList() {
		return this.fakeDungeonList;
	}

}
