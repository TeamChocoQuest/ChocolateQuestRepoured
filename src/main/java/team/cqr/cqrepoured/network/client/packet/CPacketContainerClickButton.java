package team.cqr.cqrepoured.network.client.packet;

import java.util.function.Consumer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CPacketContainerClickButton implements IMessage {

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
	public void fromBytes(ByteBuf buf) {
		this.button = buf.readInt();
		this.extraData.writeBytes(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.button);
		buf.writeBytes(this.extraData);
	}

	public int getButton() {
		return button;
	}

	public ByteBuf getExtraData() {
		return extraData;
	}

}
