package com.teamcqr.chocolatequestrepoured.network.packets.toClient;

import java.util.HashSet;
import java.util.Set;

import com.teamcqr.chocolatequestrepoured.objects.items.ItemDungeonPlacer.FakeDungeon;
import com.teamcqr.chocolatequestrepoured.structuregen.DungeonBase;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class DungeonSyncPacket implements IMessage {

	private Set<DungeonBase> dungeonSet = new HashSet<DungeonBase>();
	private Set<FakeDungeon> fakeDungeonSet = new HashSet<FakeDungeon>();

	public DungeonSyncPacket() {

	}

	public DungeonSyncPacket(Set<DungeonBase> dungeons) {
		this.dungeonSet = dungeons;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int dungeonCount = buf.readByte();
		this.fakeDungeonSet = new HashSet<FakeDungeon>(dungeonCount);
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
		buf.writeByte(this.dungeonSet.size());
		for (DungeonBase dungeon : this.dungeonSet) {
			ByteBufUtils.writeUTF8String(buf, dungeon.getDungeonName());
			buf.writeByte(dungeon.getIconID());
			buf.writeByte(dungeon.getDependencies().length);
			for (String dependency : dungeon.getDependencies()) {
				ByteBufUtils.writeUTF8String(buf, dependency);
			}
		}
	}

	public Set<FakeDungeon> getFakeDungeonList() {
		return this.fakeDungeonSet;
	}

}
