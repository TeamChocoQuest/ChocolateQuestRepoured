package team.cqr.cqrepoured.client.init;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;

public class CQRRenderTypes extends RenderType {

	private CQRRenderTypes(String name, VertexFormat format, int mode, int buffer, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState,
			Runnable clearState) {
		super(name, format, mode, buffer, affectsCrumbling, sortOnUpload, setupState, clearState);
	}

	public static RenderType emissive(ResourceLocation texture) {
		return RenderType.create("emissive", DefaultVertexFormats.NEW_ENTITY, GL11.GL_QUADS, 256, State.builder()
				.setTextureState(new TextureState(texture, false, false))
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				.setOverlayState(OVERLAY)
				.createCompositeState(true));
	}

}
