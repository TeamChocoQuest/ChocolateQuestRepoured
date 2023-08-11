//package team.cqr.cqrepoured.network.server.packet;
//
//import net.minecraft.network.PacketBuffer;
//import team.cqr.cqrepoured.config.CQRConfig;
//import de.dertoaster.multihitboxlib.api.network.AbstractPacket;
//
//public class SPacketSyncProtectionConfig extends AbstractPacket<SPacketSyncProtectionConfig> {
//
//	private CQRConfig.DungeonProtection protectionConfig;
//
//	public SPacketSyncProtectionConfig() {
//
//	}
//
//	public SPacketSyncProtectionConfig(CQRConfig.DungeonProtection protectionConfig) {
//		this.protectionConfig = protectionConfig;
//	}
//
//	@Override
//	public SPacketSyncProtectionConfig fromBytes(PacketBuffer buf) {
//		SPacketSyncProtectionConfig result = new SPacketSyncProtectionConfig();
//		
//		result.protectionConfig = new CQRConfig.DungeonProtection();
//		result.protectionConfig.protectionSystemEnabled = buf.readBoolean();
//		result.protectionConfig.enablePreventBlockBreaking = buf.readBoolean();
//		result.protectionConfig.enablePreventBlockPlacing = buf.readBoolean();
//		result.protectionConfig.enablePreventEntitySpawning = buf.readBoolean();
//		result.protectionConfig.enablePreventExplosionOther = buf.readBoolean();
//		result.protectionConfig.enablePreventExplosionTNT = buf.readBoolean();
//		result.protectionConfig.enablePreventFireSpreading = buf.readBoolean();
//		result.protectionConfig.protectionSystemBreakableBlockWhitelist = new String[buf.readInt()];
//		for (int i = 0; i < result.protectionConfig.protectionSystemBreakableBlockWhitelist.length; i++) {
//			result.protectionConfig.protectionSystemBreakableBlockWhitelist[i] = buf.readUtf();
//		}
//		result.protectionConfig.protectionSystemBreakableMaterialWhitelist = new String[buf.readInt()];
//		for (int i = 0; i < result.protectionConfig.protectionSystemBreakableMaterialWhitelist.length; i++) {
//			result.protectionConfig.protectionSystemBreakableMaterialWhitelist[i] = buf.readUtf();
//		}
//		result.protectionConfig.protectionSystemPlaceableBlockWhitelist = new String[buf.readInt()];
//		for (int i = 0; i < result.protectionConfig.protectionSystemPlaceableBlockWhitelist.length; i++) {
//			result.protectionConfig.protectionSystemPlaceableBlockWhitelist[i] = buf.readUtf();
//		}
//		result.protectionConfig.protectionSystemPlaceableMaterialWhitelist = new String[buf.readInt()];
//		for (int i = 0; i < result.protectionConfig.protectionSystemPlaceableMaterialWhitelist.length; i++) {
//			result.protectionConfig.protectionSystemPlaceableMaterialWhitelist[i] = buf.readUtf();
//		}
//		
//		return result;
//	}
//
//	@Override
//	public void toBytes(SPacketSyncProtectionConfig packet, PacketBuffer buf) {
//		buf.writeBoolean(packet.protectionConfig.protectionSystemEnabled);
//		buf.writeBoolean(packet.protectionConfig.enablePreventBlockBreaking);
//		buf.writeBoolean(packet.protectionConfig.enablePreventBlockPlacing);
//		buf.writeBoolean(packet.protectionConfig.enablePreventEntitySpawning);
//		buf.writeBoolean(packet.protectionConfig.enablePreventExplosionOther);
//		buf.writeBoolean(packet.protectionConfig.enablePreventExplosionTNT);
//		buf.writeBoolean(packet.protectionConfig.enablePreventFireSpreading);
//		buf.writeInt(packet.protectionConfig.protectionSystemBreakableBlockWhitelist.length);
//		for (String s : packet.protectionConfig.protectionSystemBreakableBlockWhitelist) {
//			buf.writeUtf(s);
//		}
//		buf.writeInt(packet.protectionConfig.protectionSystemBreakableMaterialWhitelist.length);
//		for (String s : packet.protectionConfig.protectionSystemBreakableMaterialWhitelist) {
//			buf.writeUtf(s);
//		}
//		buf.writeInt(packet.protectionConfig.protectionSystemPlaceableBlockWhitelist.length);
//		for (String s : packet.protectionConfig.protectionSystemPlaceableBlockWhitelist) {
//			buf.writeUtf(s);
//		}
//		buf.writeInt(packet.protectionConfig.protectionSystemPlaceableMaterialWhitelist.length);
//		for (String s : packet.protectionConfig.protectionSystemPlaceableMaterialWhitelist) {
//			buf.writeUtf(s);
//		}
//	}
//
//	public CQRConfig.DungeonProtection getProtectionConfig() {
//		return this.protectionConfig;
//	}
//
//	@Override
//	public Class<SPacketSyncProtectionConfig> getPacketClass() {
//		return SPacketSyncProtectionConfig.class;
//	}
//
//}
