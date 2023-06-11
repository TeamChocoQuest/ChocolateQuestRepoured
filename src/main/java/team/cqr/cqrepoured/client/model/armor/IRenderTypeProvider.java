package team.cqr.cqrepoured.client.model.armor;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public interface IRenderTypeProvider {

	RenderType getRenderType(ResourceLocation texture);

}
