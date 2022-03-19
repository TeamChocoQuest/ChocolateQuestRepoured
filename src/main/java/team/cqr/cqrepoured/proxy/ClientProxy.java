package team.cqr.cqrepoured.proxy;

import org.lwjgl.input.Keyboard;

import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.client.gui.GuiAddPathNode;
import team.cqr.cqrepoured.client.gui.IUpdatableGui;
import team.cqr.cqrepoured.client.init.CQREntityRenderers;
import team.cqr.cqrepoured.client.init.CQRParticleManager;
import team.cqr.cqrepoured.client.render.entity.layer.LayerCrownRenderer;
import team.cqr.cqrepoured.client.render.entity.layer.LayerElectrocute;
import team.cqr.cqrepoured.client.resources.data.GlowingMetadataSection;
import team.cqr.cqrepoured.client.resources.data.GlowingMetadataSectionSerializer;
import team.cqr.cqrepoured.customtextures.CTResourcepack;
import team.cqr.cqrepoured.util.GuiHandler;

public class ClientProxy implements IProxy {

	static final String KEY_CATEGORY_MAIN = "Chocolate Quest Repoured";

	public static KeyBinding keybindReputationGUI = new KeyBinding("Reputation GUI", Keyboard.KEY_F4, KEY_CATEGORY_MAIN);

	@Override
	public void preInit() {
		Minecraft mc = Minecraft.getMinecraft();

		mc.defaultResourcePacks.add(CTResourcepack.getInstance());
		CQREntityRenderers.registerRenderers();
		CQRParticleManager.init();

		// Add custom metadataserializers
		mc.metadataSerializer.registerMetadataSectionType(new GlowingMetadataSectionSerializer(), GlowingMetadataSection.class);
	}

	@Override
	public void init() {
		ClientRegistry.registerKeyBinding(keybindReputationGUI);
	}

	@Override
	public void postInit() {
		// Add electrocute layer to all entities
		for (Render<? extends Entity> renderer : Minecraft.getMinecraft().getRenderManager().entityRenderMap.values()) {
			try {
				@SuppressWarnings("unchecked")
				Render<Entity> render = (Render<Entity>) renderer;
				if (render instanceof RenderLivingBase) {
					((RenderLivingBase<?>) render).addLayer(new LayerElectrocute());
					((RenderLivingBase<?>) render).addLayer(new LayerCrownRenderer((RenderLivingBase<?>) render));
				}
			} catch (ClassCastException ccex) {
				// Ignore
			}
		}
		// Since for whatever reason the player renderer is not in the entityRenderMap we need to add it manually...
		Minecraft.getMinecraft().getRenderManager().getSkinMap().values().forEach(t -> {
			t.addLayer(new LayerElectrocute());
			t.addLayer(new LayerCrownRenderer(t));
			}
		);
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
				return manager.advancementToProgress.get(advancement).isDone();
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

	@Override
	public void openGui(int id, EntityPlayer player, World world, int... args) {
		Minecraft mc = Minecraft.getMinecraft();

		if (id == GuiHandler.ADD_PATH_NODE_GUI_ID) {
			mc.displayGuiScreen(new GuiAddPathNode(EnumHand.values()[args[0]], args[1], new BlockPos(args[2], args[3], args[4])));
		}
	}

	@Override
	public boolean isPlayerCurrentClientPlayer(EntityPlayer player) {
		if(player != null) {
			return ((EntityPlayer)Minecraft.getMinecraft().player).equals(player);
		}
		return false;
	}

}
