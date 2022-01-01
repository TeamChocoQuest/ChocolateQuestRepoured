package team.cqr.cqrepoured.inventory;

import io.netty.buffer.ByteBuf;

public interface IInteractable {

	void onClickButton(EntityPlayer player, int button, ByteBuf extraData);

}
