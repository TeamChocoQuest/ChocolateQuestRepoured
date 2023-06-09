package team.cqr.cqrepoured.network;

import java.util.function.Consumer;

import io.netty.buffer.Unpooled;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.network.NetworkHooks;

public class CQRNetworkHooks {
	
	public static void openGUI(Player pe, TextComponent title, Consumer<FriendlyByteBuf> customDataInstructions, MenuType<? extends Container> containerConstructor) {
		if(pe instanceof ServerPlayer) {
			openGUI((ServerPlayer)pe, title, customDataInstructions, containerConstructor);
		} else {
			
		}
	}
	
	public static void openGUI(ServerPlayer spe, TextComponent title, Consumer<FriendlyByteBuf> customDataInstructions, MenuType<? extends Container> containerConstructor) {
		FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
		customDataInstructions.accept(buffer);
		NetworkHooks.openGui(spe, new INamedContainerProvider() {
			
			@Override
			public Container createMenu(int windowId, Inventory invPlayer, Player lePlayer) {
				return containerConstructor.create(windowId, invPlayer, buffer);
			}
			
			@Override
			public TextComponent getDisplayName() {
				return title;
			}
		},
		customDataInstructions);
	}

}
