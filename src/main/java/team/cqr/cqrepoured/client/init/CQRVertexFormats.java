package team.cqr.cqrepoured.client.init;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;

public class CQRVertexFormats {

	public static final VertexFormat POSITION_TEX_LIGHTMAP = new VertexFormat(ImmutableList.<VertexFormatElement>builder()
			.add(DefaultVertexFormats.ELEMENT_POSITION)
			.add(DefaultVertexFormats.ELEMENT_UV0)
			.add(DefaultVertexFormats.ELEMENT_UV2)
			.build());

}
