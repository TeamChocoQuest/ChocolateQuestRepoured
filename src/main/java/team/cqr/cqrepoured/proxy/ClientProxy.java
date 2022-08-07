package team.cqr.cqrepoured.proxy;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import team.cqr.cqrepoured.client.gui.IUpdatableGui;
import team.cqr.cqrepoured.client.init.CQREntityRenderers;
import team.cqr.cqrepoured.client.init.CQRParticleManager;
import team.cqr.cqrepoured.client.render.entity.layer.LayerCrownRenderer;
import team.cqr.cqrepoured.client.render.entity.layer.LayerElectrocute;
import team.cqr.cqrepoured.customtextures.CTResourcepack;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;

public class ClientProxy implements IProxy {

	static final String KEY_CATEGORY_MAIN = "Chocolate Quest Repoured";

	//public static KeyBinding keybindReputationGUI = new KeyBinding("Reputation GUI", Keyboard.KEY_F4, KEY_CATEGORY_MAIN);

	@Override
	public void preInit() {
		Minecraft mc = Minecraft.getInstance();

		mc.getResourcePackRepository().addPackFinder(CTResourcepack.PACK_FINDER);
		CQREntityRenderers.registerRenderers();
		CQRParticleManager.init();

		// Add custom metadataserializers
		//Now longer needed?
		//mc.registerMetadataSectionType(new GlowingMetadataSectionSerializer(), GlowingMetadataSection.class);
	}

	@Override
	public void init() {
		//ClientRegistry.registerKeyBinding(keybindReputationGUI);
	}

	@SuppressWarnings({ "unchecked", "resource", "rawtypes" })
	@Override
	public void postInit() {
		// Add electrocute layer to all entities
		for (EntityRenderer<? extends Entity> renderer : Minecraft.getInstance().getEntityRenderDispatcher().renderers.values()) {
			try {
                EntityRenderer<Entity> render = (EntityRenderer<Entity>) renderer;
				if (render instanceof LivingRenderer) {
					((LivingRenderer<?, ?>) render).addLayer(new LayerElectrocute((LivingRenderer<?,?>) render));
					((LivingRenderer<?, ?>) render).addLayer(new LayerCrownRenderer((LivingRenderer<?,?>) render));
				}
			} catch (ClassCastException ccex) {
				// Ignore
			}
		}
		// Since for whatever reason the player renderer is not in the entityRenderMap we need to add it manually...
		Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap().values().forEach(t -> {
			t.addLayer(new LayerElectrocute<>(t));
			t.addLayer(new LayerCrownRenderer<>(t));
			}
		);
	}

	@Override
	public PlayerEntity getPlayer(Context context) {
		return DistExecutor.safeRunForDist(() -> () -> Minecraft.getInstance().player, () -> () -> context.getSender());
	}

	@Override
	public World getWorld(Context context) {
		return DistExecutor.safeRunForDist(() -> () -> Minecraft.getInstance().level, () -> () -> context.getSender().level);
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
				AdvancementProgress prog = manager.progress.get(advancement);
				return prog != null && prog.isDone();
			}
		}
		return false;
	}

	@SuppressWarnings("resource")
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
		/*Minecraft mc = Minecraft.getInstance();

		if (id == GuiHandler.ADD_PATH_NODE_GUI_ID) {
			mc.setScreen(new GuiAddPathNode(Hand.values()[args[0]], args[1], new BlockPos(args[2], args[3], args[4])));
		}*/
		//TODO: Re-implement
	}

	@SuppressWarnings("resource")
	@Override
	public boolean isPlayerCurrentClientPlayer(PlayerEntity player) {
		if(player != null) {
			return ((PlayerEntity)Minecraft.getInstance().player).equals(player);
		}
		return false;
	}
	
	private AbstractEntityCQR currentEntity;
	@Override
	public void setCurrentCQREntityInGUI(AbstractEntityCQR abstractEntityCQR) {
		this.currentEntity = abstractEntityCQR;
	}
	@Override
	public AbstractEntityCQR getCurrentCQREntityInGUI() {
		return this.currentEntity;
	}

}
