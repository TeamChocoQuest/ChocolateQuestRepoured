package team.cqr.cqrepoured.proxy;

import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
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

	//public static KeyBinding keybindReputationGUI = new KeyBinding("Reputation GUI", Keyboard.KEY_F4, KEY_CATEGORY_MAIN);

	@Override
	public void preInit() {
		Minecraft mc = Minecraft.getInstance();

		mc.defaultResourcePacks.add(CTResourcepack.getInstance());
		CQREntityRenderers.registerRenderers();
		CQRParticleManager.init();

		// Add custom metadataserializers
		mc.metadataSerializer.registerMetadataSectionType(new GlowingMetadataSectionSerializer(), GlowingMetadataSection.class);
	}

	@Override
	public void init() {
		//ClientRegistry.registerKeyBinding(keybindReputationGUI);
	}

	@Override
	public void postInit() {
		// Add electrocute layer to all entities
		for (EntityRenderer<? extends Entity> renderer : Minecraft.getInstance().getRenderManager().entityRenderMap.values()) {
			try {
				@SuppressWarnings("unchecked")
                EntityRenderer<Entity> render = (EntityRenderer<Entity>) renderer;
				if (render instanceof LivingRenderer) {
					((LivingRenderer<?>) render).addLayer(new LayerElectrocute());
					((LivingRenderer<?>) render).addLayer(new LayerCrownRenderer((LivingRenderer<?>) render));
				}
			} catch (ClassCastException ccex) {
				// Ignore
			}
		}
		// Since for whatever reason the player renderer is not in the entityRenderMap we need to add it manually...
		Minecraft.getInstance().getRenderManager().getSkinMap().values().forEach(t -> {
			t.addLayer(new LayerElectrocute());
			t.addLayer(new LayerCrownRenderer(t));
			}
		);
	}

	@Override
	public PlayerEntity getPlayer(Context context) {
		if (context.side.isClient()) {
			return Minecraft.getInstance().player;
		} else {
			return context.getServerHandler().player;
		}
	}

	@Override
	public World getWorld(Context context) {
		if (context.side.isClient()) {
			return Minecraft.getInstance().level;
		} else {
			return context.getServerHandler().player.world;
		}
	}

	@Override
	public Advancement getAdvancement(PlayerEntity player, ResourceLocation id) {
		if (player instanceof ClientPlayerEntity) {
			ClientAdvancementManager manager = ((ClientPlayerEntity) player).connection.getAdvancements();
			return manager.getAdvancements().get(id);
		}
		return null;
	}

	@Override
	public boolean hasAdvancement(PlayerEntity player, ResourceLocation id) {
		if (player instanceof ClientPlayerEntity) {
			ClientAdvancementManager manager = ((ClientPlayerEntity) player).connection.getAdvancements();
			Advancement advancement = manager.getAdvancements().get(id);
			if (advancement != null) {
				return manager.progress.get(advancement).isDone();
			}
		}
		return false;
	}

	@Override
	public void updateGui() {
		Screen gui = Minecraft.getInstance().screen;
		if (gui instanceof IUpdatableGui) {
			((IUpdatableGui) gui).update();
		}
	}

	@Override
	public boolean isOwnerOfIntegratedServer(PlayerEntity player) {
		IntegratedServer integratedServer = Minecraft.getInstance().getSingleplayerServer();
		return integratedServer != null && player.getName().equals(integratedServer.getSingleplayerName()) && integratedServer.isSingleplayerOwner(player.getGameProfile());
	}

	@Override
	public void openGui(int id, PlayerEntity player, World world, int... args) {
		Minecraft mc = Minecraft.getInstance();

		if (id == GuiHandler.ADD_PATH_NODE_GUI_ID) {
			mc.setScreen(new GuiAddPathNode(Hand.values()[args[0]], args[1], new BlockPos(args[2], args[3], args[4])));
		}
	}

	@Override
	public boolean isPlayerCurrentClientPlayer(PlayerEntity player) {
		if(player != null) {
			return ((PlayerEntity)Minecraft.getInstance().player).equals(player);
		}
		return false;
	}

}
