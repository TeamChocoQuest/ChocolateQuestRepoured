package team.cqr.cqrepoured.network;

import java.util.function.Consumer;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkHooks;

public class CQRNetworkHooks {
	
	public static void openGUI(PlayerEntity pe, ITextComponent title, Consumer<PacketBuffer> customDataInstructions, ContainerType<? extends Container> containerConstructor) {
		if(pe instanceof ServerPlayerEntity) {
			openGUI((ServerPlayerEntity)pe, title, customDataInstructions, containerConstructor);
		} else {
			
		}
	}
	
	public static void openGUI(ServerPlayerEntity spe, ITextComponent title, Consumer<PacketBuffer> customDataInstructions, ContainerType<? extends Container> containerConstructor) {
		PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
		customDataInstructions.accept(buffer);
		NetworkHooks.openGui(spe, new INamedContainerProvider() {
			
			@Override
			public Container createMenu(int windowId, PlayerInventory invPlayer, PlayerEntity lePlayer) {
				return containerConstructor.create(windowId, invPlayer, buffer);
			}
			
			@Override
			public ITextComponent getDisplayName() {
				return title;
			}
		},
		customDataInstructions);
	}

}
