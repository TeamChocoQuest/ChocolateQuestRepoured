package team.cqr.cqrepoured.util.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.proxy.ClientProxy;
import team.cqr.cqrepoured.util.Reference;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(value = Side.CLIENT)
public class KeybindingHandler {

	// Opens repu GUI
	@SubscribeEvent(receiveCanceled = true)
	public static void onKeyPress(KeyInputEvent event) {
		// Repu key
		if (ClientProxy.keybindReputationGUI.isPressed()) {
			EntityPlayerSP player = Minecraft.getMinecraft().player;
			player.openGui(CQRMain.INSTANCE, Reference.REPUTATION_GUI_ID, Minecraft.getMinecraft().world, (int) player.posX, (int) player.posY, (int) player.posZ);
		}
	}

}
