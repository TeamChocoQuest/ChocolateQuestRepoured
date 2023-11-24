package team.cqr.cqrepoured.client.render.armor;

import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import mod.azure.azurelib.animatable.GeoItem;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.azurelib.renderer.GeoArmorRenderer;

public class RenderArmorDyableGeo<T extends Item & GeoItem> extends GeoArmorRenderer<T> {
	
	private static Map<ResourceLocation, ResourceLocation> tex2Overlay = new Object2ObjectArrayMap<>();

	public RenderArmorDyableGeo(GeoModel<T> model) {
		super(model);
	}
	
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Minecraft mc = Minecraft.getInstance();
		MultiBufferSource bufferSource = mc.renderBuffers().bufferSource();

		if (mc.levelRenderer.shouldShowEntityOutlines() && mc.shouldEntityAppearGlowing(this.currentEntity))
			bufferSource = mc.renderBuffers().outlineBufferSource();

		float partialTick = mc.getFrameTime();
		
		// Use dyable texture when the colored part is being rendered => r g b values are not equal to 3
		ResourceLocation rs = this.getTextureLocation(animatable);
		boolean dyedMarker = this.animatable instanceof DyeableLeatherItem && !(red + green + blue == 3.0F);
		if (dyedMarker) {
			rs = tex2Overlay.computeIfAbsent(rs, RenderArmorDyableGeo::generateOverlayResourceLocation);
		}
		
		RenderType renderType = getRenderType(this.animatable, rs, bufferSource, partialTick);
		buffer = ItemRenderer.getArmorFoilBuffer(bufferSource, renderType, false, this.currentStack.hasFoil() && !dyedMarker);

		defaultRender(poseStack, this.animatable, bufferSource, null, buffer,
				0, partialTick, packedLight);
	}
	
	private static ResourceLocation generateOverlayResourceLocation(ResourceLocation resLoc) {
		String[] splitPath = resLoc.getPath().split(".");
		String ending = splitPath[splitPath.length - 1];
		String path = resLoc.getPath().substring(0, resLoc.getPath().length() - ("." + ending).length());
		path = path.concat("_overlay.").concat(ending);
		return new ResourceLocation(resLoc.getNamespace(), path);
	}

}
