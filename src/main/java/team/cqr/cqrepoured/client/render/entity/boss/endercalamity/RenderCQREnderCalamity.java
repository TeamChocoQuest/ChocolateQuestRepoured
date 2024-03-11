package team.cqr.cqrepoured.client.render.entity.boss.endercalamity;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nullable;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import software.bernie.geckolib.renderer.layer.ItemArmorGeoLayer;
import team.cqr.cqrepoured.CQRConstants;
import team.cqr.cqrepoured.client.init.CQRRenderTypes;
import team.cqr.cqrepoured.client.model.geo.entity.boss.ModelEnderCalamityGeo;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntityGeo;
import team.cqr.cqrepoured.client.render.entity.StandardBipedBones;
import team.cqr.cqrepoured.client.util.SphereRenderer;
import team.cqr.cqrepoured.client.util.SphereRenderer.Triangle;
import team.cqr.cqrepoured.client.util.SphereRenderer.Vertex;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.entity.boss.endercalamity.EntityCQREnderCalamity.E_CALAMITY_HAND;
import team.cqr.cqrepoured.util.ArrayUtil;

public class RenderCQREnderCalamity extends RenderCQREntityGeo<EntityCQREnderCalamity> {

	private static final VertexBuffer SPHERE_VBO = new VertexBuffer(VertexBuffer.Usage.DYNAMIC);
	static {
		BufferBuilder buffer = Tesselator.getInstance().getBuilder();
		buffer.begin(Mode.TRIANGLES, DefaultVertexFormat.POSITION);

		AtomicInteger index = new AtomicInteger();
		SphereRenderer.getIcoSphere().flatMap(SphereRenderer.splitter(3, true)).forEach((Triangle triangle) -> {
			int ind = index.getAndIncrement();
			Vertex[] outer = triangle.vertices().toArray(Vertex[]::new);
			Vertex[] outerDir = ArrayUtil.createArray(outer.length, Vertex[]::new, (int i) -> ArrayUtil.next(outer, i).subtract(outer[i]).normalize());
			Vertex[] inner = ArrayUtil.createArray(outer.length, Vertex[]::new, (int i) -> {
				Vertex v = outerDir[i].subtract(ArrayUtil.prev(outerDir, i)).normalize();
				double d = outerDir[i].dot(v);
				v = v.scale(0.01D / Math.sqrt(1.0D - d * d));
				outer[i] = outer[i].subtract(v).normalize();
				return outer[i].add(v.scale(2.0D)).normalize();
			});

			if (ind % 16 < 12) {
				if (ind % 4 == 0) {
					buffer.vertex(inner[0].x, inner[0].y, inner[0].z).endVertex();
					buffer.vertex(outer[0].x, outer[0].y, outer[0].z).endVertex();
					buffer.vertex(outer[1].x, outer[1].y, outer[1].z).endVertex();
				}
				if (ind % 4 == 2) {
					buffer.vertex(inner[0].x, inner[0].y, inner[0].z).endVertex();
					buffer.vertex(outer[1].x, outer[1].y, outer[1].z).endVertex();
					buffer.vertex(inner[1].x, inner[1].y, inner[1].z).endVertex();
				}
				if (ind % 4 == 3) {
					buffer.vertex(outer[0].x, outer[0].y, outer[0].z).endVertex();
					buffer.vertex(outer[1].x, outer[1].y, outer[1].z).endVertex();
					buffer.vertex(inner[1].x, inner[1].y, inner[1].z).endVertex();
				}
			}
		});

		SPHERE_VBO.upload(buffer.end());
	}
	private static final Axis SPHERE_ROT_AXIS = Axis.of(new Vector3f(1.0F, 1.0F, 0.0F).normalize());

	private static final ResourceLocation TEXTURE = new ResourceLocation(CQRConstants.MODID, "textures/entity/boss/ender_calamity.png");
	private static final ResourceLocation MODEL_RESLOC = new ResourceLocation(CQRConstants.MODID, "geo/entity/boss/ender_calamity.geo.json");

	public RenderCQREnderCalamity(Context renderManager) {
		super(renderManager, new ModelEnderCalamityGeo(MODEL_RESLOC, TEXTURE, "boss/ender_calamity"));

		//this.addLayer(new LayerGlowingAreasGeo<EntityCQREnderCalamity>(this, this.TEXTURE_GETTER, this.MODEL_ID_GETTER));
		this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
	}
	
	@Override
	protected Optional<ItemArmorGeoLayer<EntityCQREnderCalamity>> createArmorLayer(RenderCQREntityGeo<EntityCQREnderCalamity> renderCQREntityGeo) {
		return Optional.empty();
	}
	
	@Override
	protected Optional<BlockAndItemGeoLayer<EntityCQREnderCalamity>> createBlockAndItemLayer(RenderCQREntityGeo<EntityCQREnderCalamity> renderCQREntityGeo) {
		BlockAndItemGeoLayer<EntityCQREnderCalamity> layer = new BlockAndItemGeoLayer<>(this, (b, a) -> null, this::getHeldBlockForBone);
		return Optional.of(layer);
	}

	@Override
	public void render(EntityCQREnderCalamity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);

		if (entity.isShieldActive()) {
			float width = this.getWidthScale(entity);
			float height = this.getHeightScale(entity);

			stack.pushPose();
			stack.translate(0.0D, entity.getBbHeight() * 0.5D, 0.0D);
			stack.scale(width, height, width);
			stack.scale(1.25F, 1.25F, 1.25F);
			stack.mulPose(SPHERE_ROT_AXIS.rotationDegrees((entity.tickCount + partialTicks) * 4.0F));

			float f = 0.7F + 0.15F * Mth.sin((entity.tickCount + partialTicks) * 0.1F);
			GL11.glColor4f(0.6F, 0.2F, 0.7F, f);
			SphereRenderer.renderSphere(stack, CQRRenderTypes.sphere(), SPHERE_VBO, GL11.GL_TRIANGLES, null, true, false);
			GL11.glColor4f(0.6F, 0.2F, 0.7F, f * 0.35F);
			SphereRenderer.renderSphere(stack, CQRRenderTypes.sphere(), SPHERE_VBO, GL11.GL_TRIANGLES, null, false, true);

			stack.popPose();
		}
	}


	@Nullable
	protected BlockState getHeldBlockForBone(GeoBone bone, EntityCQREnderCalamity currentEntity) {
		Optional<BlockState> optional = currentEntity.getBlockFromHand(E_CALAMITY_HAND.getFromBoneName(bone.getName()));
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	protected ResourceLocation getTextureOverrideForBone(GeoBone bone, EntityCQREnderCalamity animatable, float partialTick) {
		if(bone.getName().equalsIgnoreCase(StandardBipedBones.CAPE_BONE) && animatable.hasCape()) {
			return animatable.getResourceLocationOfCape();
		}
		return super.getTextureOverrideForBone(bone, animatable, partialTick);
	}

	@Override
	protected float getDeathMaxRotation(EntityCQREnderCalamity entityLivingBaseIn) {
		return 0;
	}

}
