package team.cqr.cqrepoured.network.server.packet;

import java.util.Collection;

import net.minecraft.network.FriendlyByteBuf;
import de.dertoaster.multihitboxlib.api.network.AbstractPacket;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;

public class SPacketDungeonSync extends AbstractPacket<SPacketDungeonSync> {

	private Collection<DungeonBase> dungeons;
	//private List<ClientDungeon> fakeDungeonSet;

	public SPacketDungeonSync() {

	}

	public SPacketDungeonSync(Collection<DungeonBase> dungeons) {
		this.dungeons = dungeons;
	}

	@Override
	public SPacketDungeonSync fromBytes(FriendlyByteBuf buf) {
		SPacketDungeonSync result = new SPacketDungeonSync();
		int dungeonCount = buf.readByte();
		/*result.fakeDungeonSet = new ArrayList<>(dungeonCount);
		for (int i = 0; i < dungeonCount; i++) {
			String name = buf.readUtf();
			int iconID = buf.readByte();
			int dependencyCount = buf.readByte();
			String[] dependencies = new String[dependencyCount];
			for (int j = 0; j < dependencyCount; j++) {
				dependencies[j] = buf.readUtf();
			}

			result.fakeDungeonSet.add(new ClientDungeon(name, iconID, dependencies));
		}*/
		return result;
	}

	@Override
	public void toBytes(SPacketDungeonSync packet, FriendlyByteBuf buf) {
		buf.writeByte(packet.dungeons.size());
		for (DungeonBase dungeon : packet.dungeons) {
			buf.writeUtf(dungeon.getDungeonName());
			buf.writeByte(dungeon.getIconID());
			buf.writeByte(dungeon.getModDependencies().length);
			for (String dependency : dungeon.getModDependencies()) {
				buf.writeUtf(dependency);
			}
		}
	}

	/*public List<ClientDungeon> getFakeDungeonList() {
		return this.fakeDungeonSet;
	}*/

	@Override
	public Class<SPacketDungeonSync> getPacketClass() {
		return SPacketDungeonSync.class;
	}

}
