package team.cqr.cqrepoured.client.model.armor;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ResourceLocation;

public interface IRenderTypeProvider {

	RenderType getRenderType(ResourceLocation texture);

}
