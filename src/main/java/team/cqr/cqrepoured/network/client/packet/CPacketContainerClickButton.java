package team.cqr.cqrepoured.network.client.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import de.dertoaster.multihitboxlib.api.network.AbstractPacket;

import java.util.function.Consumer;

public class CPacketContainerClickButton extends AbstractPacket<CPacketContainerClickButton> {

	private int button;
	private final FriendlyByteBuf extraData = new FriendlyByteBuf(Unpooled.buffer());

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
	public CPacketContainerClickButton fromBytes(FriendlyByteBuf buf) {
		CPacketContainerClickButton result = new CPacketContainerClickButton();
		result.button = buf.readInt();
		result.extraData.writeBytes(buf);
		return result;
	}

	@Override
	public void toBytes(CPacketContainerClickButton packet, FriendlyByteBuf buf) {
		buf.writeInt(packet.button);
		buf.writeBytes(packet.extraData);
	}

	public int getButton() {
		return button;
	}

	public FriendlyByteBuf getExtraData() {
		return extraData;
	}

	@Override
	public Class<CPacketContainerClickButton> getPacketClass() {
		return CPacketContainerClickButton.class;
	}

}
