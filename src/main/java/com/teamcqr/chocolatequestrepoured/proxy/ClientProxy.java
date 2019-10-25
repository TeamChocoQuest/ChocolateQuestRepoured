package com.teamcqr.chocolatequestrepoured.proxy;

import org.lwjgl.input.Keyboard;

import com.teamcqr.chocolatequestrepoured.client.init.ModEntityRenderers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy implements IProxy {
	
	static final String KEY_CATEGORY_MAIN = "Chocolate Quest Repoured";
	
	public static KeyBinding keybindReputationGUI = new KeyBinding("Reputation GUI", Keyboard.KEY_F4, KEY_CATEGORY_MAIN);

	@Override
	public void preInit() {
		ModEntityRenderers.registerRenderers();
	}

	@Override
	public void init() {
		ClientRegistry.registerKeyBinding(keybindReputationGUI);
	}

	@Override
	public void postInit() {

	}

	@Override
	public EntityPlayer getPlayer(MessageContext context) {
		if (context.side.isClient()) {
			return Minecraft.getMinecraft().player;
		} else {
			return context.getServerHandler().player;
		}
	}

}
