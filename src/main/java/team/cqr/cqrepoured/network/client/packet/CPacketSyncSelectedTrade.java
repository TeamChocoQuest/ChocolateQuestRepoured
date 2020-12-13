package team.cqr.cqrepoured.network.client.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CPacketSyncSelectedTrade implements IMessage {

	private int selectedTradeIndex;

	public CPacketSyncSelectedTrade() {

	}

	public CPacketSyncSelectedTrade(int selectedTradeIndex) {
		this.selectedTradeIndex = selectedTradeIndex;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.selectedTradeIndex = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.selectedTradeIndex);
	}

	public int getSelectedTradeIndex() {
		return this.selectedTradeIndex;
	}

}
