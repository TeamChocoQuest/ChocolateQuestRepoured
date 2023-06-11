package team.cqr.cqrepoured.client.init;

import java.util.OptionalDouble;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Display.RenderState;

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

	public static RenderType laser() {
		return RenderType.create("cqrepoured_laser", DefaultVertexFormats.NEW_ENTITY, GL11.GL_QUADS, 256, State.builder()
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				.setWriteMaskState(COLOR_WRITE)
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
	
	public static RenderType emissiveColorable() {
		return RenderType.create("cqrepoured_emissive_colorable", DefaultVertexFormats.POSITION_COLOR, GL11.GL_QUADS, 256, State.builder()
				.setAlphaState(DEFAULT_ALPHA)
				.setCullState(NO_CULL)
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				.setOverlayState(OVERLAY)
				.createCompositeState(true)
				);
	}
	
	public static RenderType sphere() {
		return RenderType.create("cqrepoured_sphere", DefaultVertexFormats.POSITION, GL11.GL_TRIANGLES, 256, State.builder()
				.setTransparencyState(LIGHTNING_TRANSPARENCY)
				.setWriteMaskState(COLOR_WRITE)
				.createCompositeState(true));
	}

	public static RenderType speechbubble(ResourceLocation texture) {
		return RenderType.create("cqrepoured_speechbubble", CQRVertexFormats.POSITION_TEX_LIGHTMAP, GL11.GL_QUADS, 256, State.builder()
				.setTextureState(new TextureState(texture, false, false))
				.setDiffuseLightingState(DIFFUSE_LIGHTING)
				.setAlphaState(DEFAULT_ALPHA)
				.setCullState(NO_CULL)
				.setLightmapState(LIGHTMAP)
				.createCompositeState(false));
	}

	public static RenderType overlayQuads() {
		return RenderType.create("cqrepoured_overlay_quads", DefaultVertexFormats.POSITION_COLOR, GL11.GL_QUADS, 256, State.builder()
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				.setCullState(NO_CULL)
				.setWriteMaskState(COLOR_WRITE)
				.createCompositeState(false));
	}

	public static RenderType overlayLines() {
		return RenderType.create("cqrepoured_overlay_lines", DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 256, State.builder()
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				.setCullState(NO_CULL)
				.setWriteMaskState(COLOR_WRITE)
				.createCompositeState(false));
	}

}
