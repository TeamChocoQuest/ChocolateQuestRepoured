package team.cqr.cqrepoured.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;

public interface IInteractable {

	void onClickButton(PlayerEntity player, int button, PacketBuffer extraData);

}
