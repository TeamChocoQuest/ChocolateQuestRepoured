package team.cqr.cqrepoured.client.models;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelChestRight extends ModelChestNormal {

	public ModelChestRight() {
		this.textureWidth = 64;
		this.textureHeight = 32;

		this.chestBase = new ModelRenderer(this);
		this.chestBase.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.chestBase.cubeList.add(new ModelBox(this.chestBase, 0, 0, -8.0F, -14.0F, -7.0F, 15, 14, 14, 0.0F, false));

		this.chestKnob = new ModelRenderer(this);
		this.chestKnob.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.chestKnob.cubeList.add(new ModelBox(this.chestKnob, 0, 0, -8.0F, -11.0F, -8.0F, 1, 4, 1, 0.0F, false));
	}

}
