package team.cqr.cqrepoured.inventory;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public interface IInteractable {

	void onClickButton(EntityPlayer player, int button, ByteBuf extraData);

}
