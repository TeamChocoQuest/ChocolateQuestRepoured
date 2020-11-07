package com.teamcqr.chocolatequestrepoured.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import com.teamcqr.chocolatequestrepoured.client.gui.IUpdatableGui;
import com.teamcqr.chocolatequestrepoured.client.init.CQREntityRenderers;
import com.teamcqr.chocolatequestrepoured.customtextures.CTResourcepack;
import com.teamcqr.chocolatequestrepoured.init.CQRItems;
import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemArmorDyable;
import com.teamcqr.chocolatequestrepoured.util.Reference;
import com.teamcqr.chocolatequestrepoured.util.reflection.ReflectionField;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.ResourceLocation;
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

	private static final ReflectionField<Map<Advancement, AdvancementProgress>> m = new ReflectionField<>(ClientAdvancementManager.class, "field_192803_d", "advancementToProgress");
	public static final ReflectionField<List<IResourcePack>> DEFAULT_RESOURCE_PACKS = new ReflectionField<>(Minecraft.class, "field_110449_ao", "defaultResourcePacks");

	@Override
	public void preInit() {
		List<IResourcePack> defaultResourcePacks = DEFAULT_RESOURCE_PACKS.get(Minecraft.getMinecraft());
		defaultResourcePacks.add(CTResourcepack.getInstance());
		CQREntityRenderers.registerRenderers();
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
		for (Item item : CQRItems.ItemRegistrationHandler.ITEMS) {
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

	@Override
	public Advancement getAdvancement(EntityPlayer player, ResourceLocation id) {
		if (player instanceof EntityPlayerSP) {
			ClientAdvancementManager manager = ((EntityPlayerSP) player).connection.getAdvancementManager();
			return manager.getAdvancementList().getAdvancement(id);
		}
		return null;
	}

	@Override
	public boolean hasAdvancement(EntityPlayer player, ResourceLocation id) {
		if (player instanceof EntityPlayerSP) {
			ClientAdvancementManager manager = ((EntityPlayerSP) player).connection.getAdvancementManager();
			Advancement advancement = manager.getAdvancementList().getAdvancement(id);
			if (advancement != null) {
				return m.get(manager).get(advancement).isDone();
			}
		}
		return false;
	}

	@Override
	public void updateGui() {
		GuiScreen gui = Minecraft.getMinecraft().currentScreen;
		if (gui instanceof IUpdatableGui) {
			((IUpdatableGui) gui).update();
		}
	}

	@Override
	public boolean isOwnerOfIntegratedServer(EntityPlayer player) {
		IntegratedServer integratedServer = Minecraft.getMinecraft().getIntegratedServer();
		return integratedServer != null && player.getName().equals(integratedServer.getServerOwner());
	}

}
