package team.cqr.cqrepoured.client.init;

import java.util.OptionalDouble;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.RenderState;
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
		return RenderType.create("cqrepoured_emissive", DefaultVertexFormats.NEW_ENTITY, GL11.GL_QUADS, 256, State.builder()
				.setAlphaState(DEFAULT_ALPHA)
				.setCullState(NO_CULL)
				.setTextureState(new TextureState(texture, false, false))
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				.setOverlayState(OVERLAY)
				.createCompositeState(true));
	}
	
	public static RenderType lineStrip(double lineWidth) {
		return RenderType.create("cqrepoured_line_strip", DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINE_STRIP, 256, State.builder()
				.setLineState(new RenderState.LineState(OptionalDouble.of(lineWidth)))
				.setLayeringState(VIEW_OFFSET_Z_LAYERING)
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				.setOutputState(ITEM_ENTITY_TARGET)
				.setWriteMaskState(COLOR_DEPTH_WRITE)
				.createCompositeState(false));
	}
	
	public static RenderType emissiveSolid() {
		return RenderType.create("cqrepoured_emissive_solid", DefaultVertexFormats.POSITION_COLOR, GL11.GL_QUADS, 256, State.builder()
				.createCompositeState(true));
	}

}
