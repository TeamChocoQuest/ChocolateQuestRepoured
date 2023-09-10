package team.cqr.cqrepoured.network;

import java.util.function.Consumer;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.NetworkHooks;

public class CQRNetworkHooks {
	
	public static void openGUI(Player pe, Component title, Consumer<FriendlyByteBuf> customDataInstructions, MenuType<? extends AbstractContainerMenu> containerConstructor) {
		if(pe instanceof ServerPlayer) {
			openGUI((ServerPlayer)pe, title, customDataInstructions, containerConstructor);
		} else {
			
		}
	}
	
	public static void openGUI(ServerPlayer spe, Component title, Consumer<FriendlyByteBuf> customDataInstructions, MenuType<? extends AbstractContainerMenu> containerConstructor) {
		FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
		customDataInstructions.accept(buffer);
		NetworkHooks.openScreen(spe, new MenuProvider() {
			
			@Override
			public AbstractContainerMenu createMenu(int windowId, Inventory invPlayer, Player lePlayer) {
				return containerConstructor.create(windowId, invPlayer, buffer);
			}
			
			@Override
			public Component getDisplayName() {
				return title;
			}
		},
		customDataInstructions);
	}

}
