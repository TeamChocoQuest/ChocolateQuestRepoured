package team.cqr.cqrepoured.client.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelBall extends Model {

	private final ModelRenderer bone;
	private final ModelRenderer bone1;
	private final ModelRenderer bone2;
	private final ModelRenderer bone3;
	private final ModelRenderer bone4;
	private final ModelRenderer bone5;
	private final ModelRenderer bone6;
	private final ModelRenderer faceTop;
	private final ModelRenderer faceBottom;
	private final ModelRenderer faceNorth;
	private final ModelRenderer faceSouth;
	private final ModelRenderer faceEast;
	private final ModelRenderer faceWest;

	public ModelBall()
	{
		super(RenderType::entityTranslucent);
		this.texWidth = 128;
		this.texHeight = 128;

		//public ModelBox(ModelRenderer renderer, int texU, int texV, float x, float y, float z, int dx, int dy, int dz, float delta, boolean mirror)

		this.bone = new ModelRenderer(this, 0, 0);
		this.bone.setPos(0.0F, 0.0F, 0.0F);
		this.bone.addBox(-7.0F, -13.0F, -7.0F, 14, 12, 14);
		
		this.bone1 = new ModelRenderer(this, 0, 26);
		this.bone1.setPos(0.0F, 0.0F, 0.0F);
		this.bone1.addBox(-6.0F, -14.0F, -7.0F, 12, 1, 14);
		
		this.bone2 = new ModelRenderer(this, 0, 41);
		this.bone2.setPos(0.0F, 0.0F, 0.0F);
		this.bone2.addBox(-7.0F, -14.0F, -6.0F, 1, 1, 12);
		
		this.bone3 = new ModelRenderer(this, 26, 41);
		this.bone3.setPos(0.0F, 0.0F, 0.0F);
		this.bone3.addBox(6.0F, -14.0F, -6.0F, 1, 1, 12);
		
		this.bone4 = new ModelRenderer(this, 0, 54);
		this.bone4.setPos(0.0F, 0.0F, 0.0F);
		this.bone4.addBox(-6.0F, -1.0F, -7.0F, 12, 1, 14);
		
		this.bone5 = new ModelRenderer(this, 0, 69);
		this.bone5.setPos(0.0F, 0.0F, 0.0F);
		this.bone5.addBox(-7.0F, -1.0F, -6.0F, 1, 1, 12);
		
		this.bone6 = new ModelRenderer(this, 26, 69);
		this.bone6.setPos(0.0F, 0.0F, 0.0F);
		this.bone6.addBox(6.0F, -1.0F, -6.0F, 1, 1, 12);
		//this.bone.cubeList.add(new ModelBox(this.bone, 0, 0, -7.0F, -13.0F, -7.0F, 14, 12, 14, 0.0F, false));

		/*this.bone.cubeList.add(new ModelBox(this.bone, 0, 26, -6.0F, -14.0F, -7.0F, 12, 1, 14, 0.0F, false));
		this.bone.cubeList.add(new ModelBox(this.bone, 0, 41, -7.0F, -14.0F, -6.0F, 1, 1, 12, 0.0F, false));
		this.bone.cubeList.add(new ModelBox(this.bone, 26, 41, 6.0F, -14.0F, -6.0F, 1, 1, 12, 0.0F, false));
		this.bone.cubeList.add(new ModelBox(this.bone, 0, 54, -6.0F, -1.0F, -7.0F, 12, 1, 14, 0.0F, false));
		this.bone.cubeList.add(new ModelBox(this.bone, 0, 69, -7.0F, -1.0F, -6.0F, 1, 1, 12, 0.0F, false));
		this.bone.cubeList.add(new ModelBox(this.bone, 26, 69, 6.0F, -1.0F, -6.0F, 1, 1, 12, 0.0F, false)); */

		this.faceTop = new ModelRenderer(this, 56, 0);
		this.faceTop.setPos(0.0F, 0.0F, 0.0F);
		this.faceTop.addBox(-5.0F, -15.0F, -5.0F, 10, 1, 10);
		//this.faceTop.cubeList.add(new ModelBox(this.faceTop, 56, 0, -5.0F, -15.0F, -5.0F, 10, 1, 10, 0.0F, false));

		this.faceBottom = new ModelRenderer(this, 56, 11);
		this.faceBottom.setPos(0.0F, 0.0F, 0.0F);
		this.faceBottom.addBox(-5.0F, 0.0F, -5.0F, 10, 1, 10);
		//this.faceBottom.cubeList.add(new ModelBox(this.faceBottom, 56, 11, -5.0F, 0.0F, -5.0F, 10, 1, 10, 0.0F, false));

		this.faceNorth = new ModelRenderer(this, 56, 22);
		this.faceNorth.setPos(0.0F, 0.0F, 0.0F);
		this.faceNorth.addBox(-5.0F, -12.0F, -8.0F, 10, 10, 1);
		//this.faceNorth.cubeList.add(new ModelBox(this.faceNorth, 56, 22, -5.0F, -12.0F, -8.0F, 10, 10, 1, 0.0F, false));

		this.faceSouth = new ModelRenderer(this, 78, 22);
		this.faceSouth.setPos(0.0F, 0.0F, 0.0F);
		this.faceSouth.addBox(-5.0F, -12.0F, 7.0F, 10, 10, 1);
		//this.faceSouth.cubeList.add(new ModelBox(this.faceSouth, 78, 22, -5.0F, -12.0F, 7.0F, 10, 10, 1, 0.0F, false));

		this.faceEast = new ModelRenderer(this, 56, 33);
		this.faceEast.setPos(0.0F, 0.0F, 0.0F);
		this.faceEast.addBox(-8.0F, -12.0F, -5.0F, 1, 10, 10);
		//this.faceEast.cubeList.add(new ModelBox(this.faceEast, 56, 33, -8.0F, -12.0F, -5.0F, 1, 10, 10, 0.0F, false));

		this.faceWest = new ModelRenderer(this, 78, 33);
		this.faceWest.setPos(0.0F, 0.0F, 0.0F);
		this.faceWest.addBox(7.0F, -12.0F, -5.0F, 1, 10, 10);
		//this.faceWest.cubeList.add(new ModelBox(this.faceWest, 78, 33, 7.0F, -12.0F, -5.0F, 1, 10, 10, 0.0F, false));
	}

	/*@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.bone.render(scale);
		this.faceTop.render(scale);
		this.faceBottom.render(scale);
		this.faceNorth.render(scale);
		this.faceSouth.render(scale);
		this.faceEast.render(scale);
		this.faceWest.render(scale);
	} */

	@Override
	public void renderToBuffer(MatrixStack pMatrixStack, IVertexBuilder pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha)
	{
		this.bone.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		this.bone1.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		this.bone2.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		this.bone3.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		this.bone4.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		this.bone5.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		this.bone6.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		this.faceTop.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		this.faceBottom.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		this.faceNorth.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		this.faceSouth.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		this.faceEast.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
		this.faceWest.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
	}
}
