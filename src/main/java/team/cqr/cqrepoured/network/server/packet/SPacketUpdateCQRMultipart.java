package team.cqr.cqrepoured.network.server.packet;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraftforge.entity.PartEntity;
import team.cqr.cqrepoured.entity.CQRPartEntity;
import team.cqr.cqrepoured.network.AbstractPacket;

public class SPacketUpdateCQRMultipart extends AbstractPacket<SPacketUpdateCQRMultipart> {

	private int id;
	private PacketBuffer buffer;
	private Entity entity;
	
	public SPacketUpdateCQRMultipart() {
		
	}

	public SPacketUpdateCQRMultipart(PacketBuffer buf) {
		this.id = buf.readInt();
		this.buffer = buf;
	}

	public SPacketUpdateCQRMultipart(Entity entity) {
		this.entity = entity;
	}
	
	@Override
	public Class<SPacketUpdateCQRMultipart> getPacketClass() {
		return SPacketUpdateCQRMultipart.class;
	}

	@Override
	public SPacketUpdateCQRMultipart fromBytes(PacketBuffer buffer) {
		SPacketUpdateCQRMultipart result = new SPacketUpdateCQRMultipart(buffer);
		return result;
	}

	@Override
	public void toBytes(SPacketUpdateCQRMultipart packet, PacketBuffer packetBuffer) {
		try {
			packetBuffer.writeInt(packet.entity.getId());
			PartEntity<?>[] parts = packet.entity.getParts();
			// We assume the client and server part arrays are identical, else everything will crash and burn. Don't even bother handling it.
			if (parts != null) {
				for (PartEntity<?> part : parts) {
					if (part instanceof CQRPartEntity) {
						CQRPartEntity<?> cqrPart = (CQRPartEntity<?>) part;
						cqrPart.writeData(packetBuffer);
						boolean dirty = cqrPart.getEntityData().isDirty();
						packetBuffer.writeBoolean(dirty);
						if (dirty)
							EntityDataManager.pack(cqrPart.getEntityData().packDirty(), packetBuffer);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getId() {
		return id;
	}

	public PacketBuffer getBuffer() {
		return buffer;
	}

}
