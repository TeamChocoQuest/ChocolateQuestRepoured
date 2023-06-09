package team.cqr.cqrepoured.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public interface IInteractable {

	void onClickButton(Player player, int button, FriendlyByteBuf extraData);

}
