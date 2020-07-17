package com.teamcqr.chocolatequestrepoured.proxy;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.teamcqr.chocolatequestrepoured.client.init.ModEntityRenderers;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemArmorDyable;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = Reference.MODID, value = Side.CLIENT)
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

	@Override
	public World getWorld(MessageContext context) {
		if (context.side.isClient()) {
			return Minecraft.getMinecraft().world;
		} else {
			return context.getServerHandler().player.world;
		}
	}

	@SubscribeEvent
	public static void colorItemArmors(ColorHandlerEvent.Item event) {
		List<Item> dyables = new ArrayList<>();
		for (Item item : ModItems.ItemRegistrationHandler.ITEMS) {
			if (item instanceof ItemArmorDyable) {
				dyables.add(item);
			}
		}

		event.getItemColors().registerItemColorHandler(new IItemColor() {

			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex) {
				return tintIndex > 0 ? -1 : ((ItemArmorDyable) stack.getItem()).getColor(stack);
			}
		}, dyables.toArray(new Item[dyables.size()]));
	}

}
