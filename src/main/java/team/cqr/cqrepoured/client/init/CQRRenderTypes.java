package team.cqr.cqrepoured.client.init;

import java.util.OptionalDouble;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class CQRRenderTypes extends RenderType {

	private CQRRenderTypes(String name, VertexFormat format, Mode mode, int buffer, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState,
			Runnable clearState) {
		super(name, format, mode, buffer, affectsCrumbling, sortOnUpload, setupState, clearState);
	}

	public static RenderType emissive(ResourceLocation texture) {
		return RenderType.create("cqrepoured_emissive", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, false, CompositeState.builder()
				.setShaderState(RENDERTYPE_ENTITY_ALPHA_SHADER)
				//.setAlphaState(DEFAULT_ALPHA)
				.setCullState(NO_CULL)
				.setTextureState(new TextureStateShard(texture, false, false))
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				.setOverlayState(OVERLAY)
				.createCompositeState(true));
	}

	public static RenderType laser() {
		return RenderType.create("cqrepoured_laser", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, false, CompositeState.builder()
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				.setWriteMaskState(COLOR_WRITE)
				.createCompositeState(true));
	}
	
	public static RenderType lineStrip(double lineWidth) {
		return RenderType.create("cqrepoured_line_strip", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.LINE_STRIP, 256, false, false, CompositeState.builder()
				.setLineState(new RenderStateShard.LineStateShard(OptionalDouble.of(lineWidth)))
				.setLayeringState(VIEW_OFFSET_Z_LAYERING)
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				.setOutputState(ITEM_ENTITY_TARGET)
				.setWriteMaskState(COLOR_DEPTH_WRITE)
				.createCompositeState(false));
	}
	
	public static RenderType emissiveSolid() {
		return RenderType.create("cqrepoured_emissive_solid", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, false, CompositeState.builder()
				.createCompositeState(true));
	}
	
	public static RenderType emissiveColorable() {
		return RenderType.create("cqrepoured_emissive_colorable", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, false, CompositeState.builder()
				.setShaderState(RENDERTYPE_ENTITY_ALPHA_SHADER)
				//.setAlphaState(DEFAULT_ALPHA)
				.setCullState(NO_CULL)
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				.setOverlayState(OVERLAY)
				.createCompositeState(true)
				);
	}
	
	public static RenderType sphere() {
		return RenderType.create("cqrepoured_sphere", DefaultVertexFormat.POSITION, VertexFormat.Mode.TRIANGLES, 256, false, false, CompositeState.builder()
				.setTransparencyState(LIGHTNING_TRANSPARENCY)
				.setWriteMaskState(COLOR_WRITE)
				.createCompositeState(true));
	}

	public static RenderType speechbubble(ResourceLocation texture) {
		return RenderType.create("cqrepoured_speechbubble", CQRVertexFormats.POSITION_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, false, CompositeState.builder()
				.setTextureState(new TextureStateShard(texture, false, false))
				//.setDiffuseLightingState(DIFFUSE_LIGHTING) //TODO: Replace
				.setShaderState(RENDERTYPE_ENTITY_ALPHA_SHADER)
				//.setAlphaState(DEFAULT_ALPHA)
				.setCullState(NO_CULL)
				.setLightmapState(LIGHTMAP)
				.createCompositeState(false));
	}

	public static RenderType overlayQuads() {
		return RenderType.create("cqrepoured_overlay_quads", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, false, CompositeState.builder()
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				.setCullState(NO_CULL)
				.setWriteMaskState(COLOR_WRITE)
				.createCompositeState(false));
	}

	public static RenderType overlayLines() {
		return RenderType.create("cqrepoured_overlay_lines", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.LINES, 256, false, false, CompositeState.builder()
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				.setCullState(NO_CULL)
				.setWriteMaskState(COLOR_WRITE)
				.createCompositeState(false));
	}

}
