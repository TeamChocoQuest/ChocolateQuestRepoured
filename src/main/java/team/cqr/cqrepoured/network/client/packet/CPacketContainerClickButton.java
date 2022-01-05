package team.cqr.cqrepoured.network.client.packet;

import java.util.function.Consumer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import team.cqr.cqrepoured.network.AbstractPacket;

public class CPacketContainerClickButton extends AbstractPacket<CPacketContainerClickButton> {

	private int button;
	private final ByteBuf extraData = Unpooled.buffer();

	public CPacketContainerClickButton() {

	}

	public CPacketContainerClickButton(int button) {
		this.button = button;
	}

	public CPacketContainerClickButton(int button, Consumer<ByteBuf> extraDataSupplier) {
		this.button = button;
		extraDataSupplier.accept(this.extraData);
	}

	@Override
	public CPacketContainerClickButton fromBytes(PacketBuffer buf) {
		CPacketContainerClickButton result = new CPacketContainerClickButton();
		result.button = buf.readInt();
		result.extraData.writeBytes(buf);
		return result;
	}

	@Override
	public void toBytes(CPacketContainerClickButton packet, PacketBuffer buf) {
		buf.writeInt(packet.button);
		buf.writeBytes(packet.extraData);
	}

	public int getButton() {
		return button;
	}

	public ByteBuf getExtraData() {
		return extraData;
	}

	@Override
	public Class<CPacketContainerClickButton> getPacketClass() {
		return CPacketContainerClickButton.class;
	}

}
