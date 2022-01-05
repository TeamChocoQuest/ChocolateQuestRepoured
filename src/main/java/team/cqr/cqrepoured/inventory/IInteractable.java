package team.cqr.cqrepoured.inventory;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.PlayerEntity;

public interface IInteractable {

	void onClickButton(PlayerEntity player, int button, ByteBuf extraData);

}
