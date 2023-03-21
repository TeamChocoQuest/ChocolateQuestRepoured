package team.cqr.cqrepoured.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.client.model.armor.IRenderTypeProvider;

@Mixin(BipedArmorLayer.class)
public class MixinBipedArmorLayer<T extends LivingEntity, M extends BipedModel<T>, A extends BipedModel<T>> {

	@Unique
	private A model;
	@Unique
	private ResourceLocation armorResource;

	@Inject(remap = false, method = "renderModel(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;IZLnet/minecraft/client/renderer/entity/model/BipedModel;FFFLnet/minecraft/util/ResourceLocation;)V", at = @At("HEAD"))
	public void preRenderModel(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight, boolean glint, A model, float r, float g, float b,
			ResourceLocation armorResource, CallbackInfo info) {
		this.model = model;
		this.armorResource = armorResource;
	}

	@Redirect(
			method = "renderModel(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;IZLnet/minecraft/client/renderer/entity/model/BipedModel;FFFLnet/minecraft/util/ResourceLocation;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/RenderType;armorCutoutNoCull(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"
			)
	)
	public RenderType getRenderType(ResourceLocation armorResource) {
		if (model instanceof IRenderTypeProvider) {
			return ((IRenderTypeProvider) model).getRenderType(armorResource);
		}
		return RenderType.armorCutoutNoCull(armorResource);
	}

	@Inject(remap = false, method = "renderModel(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;IZLnet/minecraft/client/renderer/entity/model/BipedModel;FFFLnet/minecraft/util/ResourceLocation;)V", at = @At("RETURN"))
	public void postRenderModel(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight, boolean glint, A model, float r, float g, float b,
			ResourceLocation armorResource, CallbackInfo info) {
		this.model = null;
		this.armorResource = null;
	}

}
