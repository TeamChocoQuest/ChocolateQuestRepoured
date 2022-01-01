package team.cqr.cqrepoured.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.proxy.ClientProxy;
import team.cqr.cqrepoured.util.GuiHandler;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(modid = CQRMain.MODID, value = Side.CLIENT)
public class KeybindingHandler {

	// Opens repu GUI
	@SubscribeEvent(receiveCanceled = true)
	public static void onKeyPress(KeyInputEvent event) {
		// Repu key
		if (ClientProxy.keybindReputationGUI.isPressed()) {
			ClientPlayerEntity player = Minecraft.getMinecraft().player;
			player.openGui(CQRMain.INSTANCE, GuiHandler.REPUTATION_GUI_ID, Minecraft.getMinecraft().world, (int) player.posX, (int) player.posY, (int) player.posZ);
		}
	}

}
