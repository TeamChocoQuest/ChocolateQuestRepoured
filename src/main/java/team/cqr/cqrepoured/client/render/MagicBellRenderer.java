package team.cqr.cqrepoured.client.render;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import net.minecraft.block.Blocks;
import org.lwjgl.opengl.GL11;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MagicBellRenderer {

	private static MagicBellRenderer instance;
	private final Set<PositionInfo> highlightedPositions = new HashSet<>();
	private final Set<EntityInfo> highlightedEntities = new HashSet<>();
	private final ShaderGroup cqrOutlineShader;
	private final Framebuffer cqrOutlineFramebuffer;
	public static int outlineColor = -1;

	{
		Minecraft mc = Minecraft.getMinecraft();

		if (ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
			ShaderLinkHelper.setNewStaticShaderLinkHelper();
		}

		ResourceLocation resourcelocation = new ResourceLocation("shaders/post/entity_outline.json");

		ShaderGroup shader = null;
		Framebuffer framebuffer = null;
		try {
			shader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), resourcelocation);
			shader.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
			framebuffer = shader.getFramebufferRaw("final");
		} catch (Exception e) {
			// ignore
		}

		this.cqrOutlineShader = shader;
		this.cqrOutlineFramebuffer = framebuffer;
	}

	public abstract static class HighlightInfo {

		public final int color;
		public int lifetime;

		protected HighlightInfo(int color, int lifetime) {
			this.color = color;
			this.lifetime = lifetime;
		}

	}

	public static class PositionInfo extends HighlightInfo {

		public final BlockPos pos;

		public PositionInfo(int color, int lifetime, BlockPos pos) {
			super(color, lifetime);
			this.pos = pos;
		}

		@Override
		public int hashCode() {
			return this.pos.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof PositionInfo)) {
				return false;
			}
			return this.pos.equals(((PositionInfo) obj).pos);
		}

	}

	public static class EntityInfo extends HighlightInfo {

		public final int entityId;

		public EntityInfo(int color, int lifetime, int entityId) {
			super(color, lifetime);
			this.entityId = entityId;
		}

		@Override
		public int hashCode() {
			return this.entityId;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof EntityInfo)) {
				return false;
			}
			return this.entityId == ((EntityInfo) obj).entityId;
		}

	}

	public static MagicBellRenderer getInstance() {
		if (instance == null) {
			instance = new MagicBellRenderer();
		}
		return instance;
	}

	public void clear() {
		this.highlightedPositions.clear();
		this.highlightedEntities.clear();
	}

	public void add(int color, int lifetime, BlockPos pos) {
		Optional<PositionInfo> existing = this.highlightedPositions.stream().filter(posInfo -> posInfo.pos.equals(pos)).findFirst();
		if (existing.isPresent()) {
			existing.get().lifetime = Math.max(existing.get().lifetime, lifetime);
		} else {
			this.highlightedPositions.add(new PositionInfo(color, lifetime, pos));
		}
	}

	public void add(int color, int lifetime, int entityId) {
		Optional<EntityInfo> existing = this.highlightedEntities.stream().filter(entityInfo -> entityInfo.entityId == entityId).findFirst();
		if (existing.isPresent()) {
			existing.get().lifetime = Math.max(existing.get().lifetime, lifetime);
		} else {
			this.highlightedEntities.add(new EntityInfo(color, lifetime, entityId));
		}
	}

	public void tick() {
		Minecraft mc = Minecraft.getMinecraft();
		World world = mc.world;
		if (world == null) {
			this.clear();
			return;
		}

		for (Iterator<PositionInfo> iterator = this.highlightedPositions.iterator(); iterator.hasNext();) {
			PositionInfo positionInfo = iterator.next();
			if (positionInfo.lifetime-- <= 0 || world.getBlockState(positionInfo.pos).getBlock() == Blocks.AIR) {
				iterator.remove();
			}
		}
		for (Iterator<EntityInfo> iterator = this.highlightedEntities.iterator(); iterator.hasNext();) {
			EntityInfo entityInfo = iterator.next();
			if (entityInfo.lifetime-- <= 0 || world.getEntityByID(entityInfo.entityId) == null) {
				iterator.remove();
			}
		}
	}

	public void render(float partialTicks) {
		if (this.highlightedPositions.isEmpty() && this.highlightedEntities.isEmpty()) {
			return;
		}

		Minecraft mc = Minecraft.getMinecraft();
		World world = mc.world;
		double x = mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * partialTicks;
		double y = mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * partialTicks;
		double z = mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * partialTicks;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		this.cqrOutlineFramebuffer.framebufferClear();
		this.cqrOutlineFramebuffer.bindFramebuffer(false);
		GlStateManager.depthFunc(GL11.GL_ALWAYS);

		buffer.setTranslation(-x, -y, -z);
		this.highlightedPositions.forEach(posInfo -> {
			BlockState state = world.getBlockState(posInfo.pos);
			if (state.getBlock() == Blocks.AIR) {
				return;
			}

			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(posInfo.color);
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			mc.getBlockRendererDispatcher().renderBlock(state, posInfo.pos, mc.world, buffer);
			tessellator.draw();
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		});
		buffer.setTranslation(0.0D, 0.0D, 0.0D);
		TileEntityRendererDispatcher.instance.preDrawBatch();
		this.highlightedPositions.forEach(posInfo -> {
			TileEntity te = world.getTileEntity(posInfo.pos);
			if (te == null) {
				return;
			}

			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(posInfo.color);
			TileEntityRendererDispatcher.instance.render(te, partialTicks, -1);
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		});
		TileEntityRendererDispatcher.instance.drawBatch(0);
		mc.getRenderManager().setRenderOutlines(true);
		this.highlightedEntities.forEach(entityInfo -> {
			Entity entity = world.getEntityByID(entityInfo.entityId);
			if (entity == null) {
				return;
			}

			MagicBellRenderer.outlineColor = entityInfo.color;
			mc.getRenderManager().renderEntityStatic(entity, partialTicks, false);
			MagicBellRenderer.outlineColor = -1;
		});
		mc.getRenderManager().setRenderOutlines(false);

		this.cqrOutlineShader.render(partialTicks);

		GlStateManager.depthFunc(GL11.GL_LEQUAL);
		mc.getFramebuffer().bindFramebuffer(false);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);

		this.cqrOutlineFramebuffer.framebufferRenderExt(mc.displayWidth, mc.displayHeight, false);

		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GlStateManager.enableCull();
		GlStateManager.disableFog();
		GlStateManager.depthFunc(GL11.GL_LEQUAL);
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
		GlStateManager.disableBlend();
		GlStateManager.disableColorMaterial();
		GlStateManager.disableLighting();
		GlStateManager.enableAlpha();
	}

}
