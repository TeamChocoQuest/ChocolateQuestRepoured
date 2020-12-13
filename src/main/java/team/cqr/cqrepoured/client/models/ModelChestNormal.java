package team.cqr.cqrepoured.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelChestNormal extends ModelBase {

	public ModelRenderer chestBase;
	public ModelRenderer chestKnob;

	public ModelChestNormal() {
		this.textureWidth = 64;
		this.textureHeight = 32;

		this.chestBase = new ModelRenderer(this);
		this.chestBase.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.chestBase.cubeList.add(new ModelBox(this.chestBase, 0, 0, -7.0F, -14.0F, -7.0F, 14, 14, 14, 0.0F, false));

		this.chestKnob = new ModelRenderer(this);
		this.chestKnob.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.chestKnob.cubeList.add(new ModelBox(this.chestKnob, 0, 0, -1.0F, -11.0F, -8.0F, 2, 4, 1, 0.0F, false));
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.chestBase.render(scale);
		this.chestKnob.render(scale);
	}

	public void renderAll() {
		this.chestBase.render(0.0625F);
		this.chestKnob.render(0.0625F);
	}

}
