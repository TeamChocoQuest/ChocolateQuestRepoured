package com.teamcqr.chocolatequestrepoured.network.server.packet;

import java.util.ArrayList;
import java.util.List;

import com.teamcqr.chocolatequestrepoured.objects.items.ItemDungeonPlacer.FakeDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonBase;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class DungeonSyncPacket implements IMessage {

	private List<DungeonBase> dungeons;
	private List<FakeDungeon> fakeDungeonSet;

	public DungeonSyncPacket() {

	}

	public DungeonSyncPacket(List<DungeonBase> dungeons) {
		this.dungeons = dungeons;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int dungeonCount = buf.readByte();
		this.fakeDungeonSet = new ArrayList<>(dungeonCount);
		for (int i = 0; i < dungeonCount; i++) {
			String name = ByteBufUtils.readUTF8String(buf);
			int iconID = buf.readByte();
			int dependencyCount = buf.readByte();
			String[] dependencies = new String[dependencyCount];
			for (int j = 0; j < dependencyCount; j++) {
				dependencies[j] = ByteBufUtils.readUTF8String(buf);
			}

			this.fakeDungeonSet.add(new FakeDungeon(name, iconID, dependencies));
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(this.dungeons.size());
		for (DungeonBase dungeon : this.dungeons) {
			ByteBufUtils.writeUTF8String(buf, dungeon.getDungeonName());
			buf.writeByte(dungeon.getIconID());
			buf.writeByte(dungeon.getModDependencies().length);
			for (String dependency : dungeon.getModDependencies()) {
				ByteBufUtils.writeUTF8String(buf, dependency);
			}
		}
	}

	public List<FakeDungeon> getFakeDungeonList() {
		return this.fakeDungeonSet;
	}

}
