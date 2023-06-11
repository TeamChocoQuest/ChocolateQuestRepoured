package team.cqr.cqrepoured.client.init;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;

public class CQRVertexFormats {

	public static final VertexFormat POSITION_TEX_LIGHTMAP = new VertexFormat(ImmutableMap.<String, VertexFormatElement>builder()
			.put("Position", DefaultVertexFormat.ELEMENT_POSITION)
			.put("UV", DefaultVertexFormat.ELEMENT_UV0)
			.put("UV2", DefaultVertexFormat.ELEMENT_UV2)
			.build());

}
