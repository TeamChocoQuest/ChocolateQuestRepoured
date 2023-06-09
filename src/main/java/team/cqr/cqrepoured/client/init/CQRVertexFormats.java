package team.cqr.cqrepoured.client.init;

import com.google.common.collect.ImmutableList;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;

public class CQRVertexFormats {

	public static final VertexFormat POSITION_TEX_LIGHTMAP = new VertexFormat(ImmutableList.<VertexFormatElement>builder()
			.add(DefaultVertexFormat.ELEMENT_POSITION)
			.add(DefaultVertexFormat.ELEMENT_UV0)
			.add(DefaultVertexFormat.ELEMENT_UV2)
			.build());

}
