package team.cqr.cqrepoured.client.render.entity.layer.equipment;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelParrot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.client.render.entity.RenderPirateParrot;
import team.cqr.cqrepoured.client.render.entity.layer.AbstractLayerCQR;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.boss.EntityCQRPirateParrot;

public class LayerShoulderEntity<T extends AbstractEntityCQR> extends AbstractLayerCQR<T> {

	protected RenderLivingBase<? extends EntityLivingBase> leftRenderer;
	private ModelBase leftModel;
	private ResourceLocation leftResource;
	private UUID leftUniqueId;
	private Class<?> leftEntityClass;

	public LayerShoulderEntity(RenderCQREntity<T> renderer) {
		super(renderer);
	}

	@Override
	public void doRenderLayer(T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch,
			float scale) {
		NBTTagCompound shoulderEntityNBT = entity.getLeftShoulderEntity();

		if (shoulderEntityNBT == null) {
			return;
		}
		if (shoulderEntityNBT.isEmpty()) {
			return;
		}

		GlStateManager.enableRescaleNormal();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		DataHolder dataHolder = this.renderEntityOnShoulder(entity, this.leftUniqueId, shoulderEntityNBT, this.leftRenderer, this.leftModel, this.leftResource,
				this.leftEntityClass, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, true);
		this.leftUniqueId = dataHolder.entityId;
		this.leftRenderer = dataHolder.renderer;
		this.leftResource = dataHolder.texture;
		this.leftModel = dataHolder.model;
		this.leftEntityClass = dataHolder.clazz;

		GlStateManager.disableRescaleNormal();
	}

	private DataHolder renderEntityOnShoulder(AbstractEntityCQR entity, @Nullable UUID shoulderEntityUuid, NBTTagCompound shoulderEntityNBT,
			RenderLivingBase<?> shoulderEntityRenderer, ModelBase shoulderEntityModel, ResourceLocation shoulderEntityTexture, Class<?> shoulderEntityClass,
			float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float newHeadYaw, float headPitch, float scale,
			boolean leftShoulder) {
		if (shoulderEntityUuid == null || !shoulderEntityUuid.equals(shoulderEntityNBT.getUniqueId("UUID"))) {
			shoulderEntityUuid = shoulderEntityNBT.getUniqueId("UUID");
			shoulderEntityClass = EntityList.getClassFromName(shoulderEntityNBT.getString("id"));

			if (shoulderEntityClass == EntityCQRPirateParrot.class) {
				shoulderEntityRenderer = new RenderPirateParrot(this.renderer.getRenderManager());
				shoulderEntityModel = new ModelParrot();
				shoulderEntityTexture = RenderPirateParrot.TEXTURE;
			}
		}

		shoulderEntityRenderer.bindTexture(shoulderEntityTexture);
		GlStateManager.pushMatrix();
		float f = entity.isSneaking() ? -1.3F : -1.5F;
		float f1 = leftShoulder ? 0.4F : -0.4F;
		GlStateManager.translate(f1, f, 0.0F);

		if (shoulderEntityClass == EntityCQRPirateParrot.class) {
			ageInTicks = 0.0F;
		}

		shoulderEntityModel.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
		shoulderEntityModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, newHeadYaw, headPitch, scale, entity);
		shoulderEntityModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, newHeadYaw, headPitch, scale);
		GlStateManager.popMatrix();
		return new DataHolder(shoulderEntityUuid, shoulderEntityRenderer, shoulderEntityModel, shoulderEntityTexture, shoulderEntityClass);
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	private static class DataHolder {

		private final UUID entityId;
		private final RenderLivingBase<?> renderer;
		private final ModelBase model;
		private final ResourceLocation texture;
		private final Class<?> clazz;

		public DataHolder(UUID entityId, RenderLivingBase<?> renderer, ModelBase model, ResourceLocation textureLocation, Class<?> clazz) {
			this.entityId = entityId;
			this.renderer = renderer;
			this.model = model;
			this.texture = textureLocation;
			this.clazz = clazz;
		}

	}

}
