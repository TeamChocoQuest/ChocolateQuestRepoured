package mcalibrary.client;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

import mcalibrary.Utils;
import mcalibrary.math.Matrix4f;
import mcalibrary.math.Quaternion;
import mcalibrary.math.Vector3f;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;

public class MCAModelRenderer extends ModelRenderer
{
	/** Custom version, as parent variable is PRIVATE */
	private int DDStextureOffsetX;

	/** Custom version, as parent variable is PRIVATE */
	private int DDStextureOffsetY;

	/** Custom version, as parent variable is PRIVATE */  
	private boolean DDScompiled;

	/** Custom version, as parent variable is PRIVATE */ 
	private int DDSdisplayList;

	private Matrix4f rotationMatrix = new Matrix4f();
	/** Previous value of the matrix */
	private Matrix4f prevRotationMatrix = new Matrix4f();

	private float defaultRotationPointX;

	private float defaultRotationPointY;

	private float defaultRotationPointZ;

	private Matrix4f defaultRotationMatrix = new Matrix4f();
	private Quaternion defaultRotationAsQuaternion;

	public MCAModelRenderer(ModelBase par1ModelBase, String par2Str, int xTextureOffset, int yTextureOffset)
	{
		super(par1ModelBase, par2Str);
		this.setTextureSize(par1ModelBase.textureWidth, par1ModelBase.textureHeight);
		this.setTextureOffset(xTextureOffset, yTextureOffset);
	}

	@Override
	public ModelRenderer setTextureOffset(int par1, int par2)
	{
		this.DDStextureOffsetX = par1;
		this.DDStextureOffsetY = par2;
		return this;
	}

	@Override
	public ModelRenderer addBox(String par1Str, float par2, float par3, float par4, int par5, int par6, int par7)
	{
		par1Str = this.boxName + "." + par1Str;
		this.cubeList.add((new MCAModelBox(this, this.DDStextureOffsetX, this.DDStextureOffsetY, par2, par3, par4, par5, par6, par7, 0.0F)).setBoxName(par1Str));
		return this;
	}

	@Override
	public ModelRenderer addBox(float par1, float par2, float par3, int par4, int par5, int par6)
	{
		this.cubeList.add(new MCAModelBox(this, this.DDStextureOffsetX, this.DDStextureOffsetY, par1, par2, par3, par4, par5, par6, 0.0F));
		return this;
	}

	@Override
	public void addBox(float par1, float par2, float par3, int par4, int par5, int par6, float par7)
	{
		this.cubeList.add(new MCAModelBox(this, this.DDStextureOffsetX, this.DDStextureOffsetY, par1, par2, par3, par4, par5, par6, par7));
	}

	@Override
	public void render(float par1)
	{
		if (!this.isHidden)
		{
			if (this.showModel)
			{
				if (!this.DDScompiled)
				{
					this.DDScompileDisplayList(par1);
				}

				//GL11.glPushMatrix();
				GL11.glTranslatef(this.offsetX, this.offsetY, this.offsetZ);
				int i;

				if (this.rotationMatrix.isEmptyRotationMatrix())
				{
					if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F)
					{
						GL11.glCallList(this.DDSdisplayList);

						if (this.childModels != null)
						{
							for (i = 0; i < this.childModels.size(); ++i)
							{
								((ModelRenderer)this.childModels.get(i)).render(par1);
							}
						}
					}
					else
					{
						//GL11.glPushMatrix();
						GL11.glTranslatef(this.rotationPointX * par1, this.rotationPointY * par1, this.rotationPointZ * par1);
						GL11.glCallList(this.DDSdisplayList);

						if (this.childModels != null)
						{
							for (i = 0; i < this.childModels.size(); ++i)
							{
								((ModelRenderer)this.childModels.get(i)).render(par1);
							}
						}

						GL11.glTranslatef(-this.rotationPointX * par1, -this.rotationPointY * par1, -this.rotationPointZ * par1);
						//GL11.glPopMatrix();
					}
				}
				else
				{
					GL11.glPushMatrix();
					GL11.glTranslatef(this.rotationPointX * par1, this.rotationPointY * par1, this.rotationPointZ * par1);
					FloatBuffer buf = Utils.makeFloatBuffer(this.rotationMatrix.intoArray());
					GL11.glMultMatrix(buf);

					GL11.glCallList(this.DDSdisplayList);

					if (this.childModels != null)
					{
						for (i = 0; i < this.childModels.size(); ++i)
						{
							((ModelRenderer)this.childModels.get(i)).render(par1);
						}
					}

					GL11.glPopMatrix();
				}

				GL11.glTranslatef(-this.offsetX, -this.offsetY, -this.offsetZ);
				//GL11.glPopMatrix();

				this.prevRotationMatrix = this.rotationMatrix;
			}
		}
	}

	@Override
	public void renderWithRotation(float par1)
	{
		//NOTHING AS WE MUSTN'T USE GL ROTATIONS METHODS
	}

	@Override
	public void postRender(float par1)
	{
		if (!this.isHidden)
		{
			if (this.showModel)
			{
				if (!this.DDScompiled)
				{
					this.DDScompileDisplayList(par1);
				}

				if (this.rotationMatrix.equals(prevRotationMatrix))
				{
					if (this.rotationPointX != 0.0F || this.rotationPointY != 0.0F || this.rotationPointZ != 0.0F)
					{
						GL11.glTranslatef(this.rotationPointX * par1, this.rotationPointY * par1, this.rotationPointZ * par1);
					}
				}
				else
				{
					GL11.glTranslatef(this.rotationPointX * par1, this.rotationPointY * par1, this.rotationPointZ * par1);

					GL11.glMultMatrix(FloatBuffer.wrap(this.rotationMatrix.intoArray()));
				}
			}
		}
	}

	/** Set default rotation point (model with no animations) and set the current rotation point. */
	public void setInitialRotationPoint(float par1, float par2, float par3)
	{
		this.defaultRotationPointX = par1;
		this.defaultRotationPointY = par2;
		this.defaultRotationPointZ = par3;
		this.setRotationPoint(par1, par2, par3);
	}

	/** Set the rotation point*/
	public void setRotationPoint(float par1, float par2, float par3) {
		this.rotationPointX = par1;
		this.rotationPointY = par2;
		this.rotationPointZ = par3;
	}

	/** Reset the rotation point to the default values. */
	public void resetRotationPoint(){
		this.rotationPointX = this.defaultRotationPointX;
		this.rotationPointY = this.defaultRotationPointY;
		this.rotationPointZ = this.defaultRotationPointZ;
	}

	public Vector3f getPositionAsVector()
	{
		return new Vector3f(this.rotationPointX, this.rotationPointY, this.rotationPointZ);
	}

	/** Set rotation matrix setting also an initial default value (model with no animations). */
	public void setInitialRotationMatrix(Matrix4f matrix)
	{
		defaultRotationMatrix = matrix;
		setRotationMatrix(matrix);
		this.defaultRotationAsQuaternion = Utils.getQuaternionFromMatrix(rotationMatrix);
	}

	/** Set the rotation matrix values based on the given matrix. */
	public void setRotationMatrix(Matrix4f matrix)
	{
		rotationMatrix.m00 = matrix.m00;
		rotationMatrix.m01 = matrix.m01;
		rotationMatrix.m02 = matrix.m02;
		rotationMatrix.m03 = matrix.m03;
		rotationMatrix.m10 = matrix.m10;
		rotationMatrix.m11 = matrix.m11;
		rotationMatrix.m12 = matrix.m12;
		rotationMatrix.m13 = matrix.m13;
		rotationMatrix.m20 = matrix.m20;
		rotationMatrix.m21 = matrix.m21;
		rotationMatrix.m22 = matrix.m22;
		rotationMatrix.m23 = matrix.m23;
		rotationMatrix.m30 = matrix.m30;
		rotationMatrix.m31 = matrix.m31;
		rotationMatrix.m32 = matrix.m32;
		rotationMatrix.m33 = matrix.m33;
	}

	/** Reset the rotation matrix to the default one. */
	public void resetRotationMatrix() {
		setRotationMatrix(this.defaultRotationMatrix);
	}

	public Matrix4f getRotationMatrix() {
		return this.rotationMatrix;
	}

	public Quaternion getDefaultRotationAsQuaternion() {
		return defaultRotationAsQuaternion;
	}

	/**
	 * Compiles a GL display list for this model.
	 * EDITED VERSION BECAUSE OF THE PRIVATE FIELDS
	 */
	public void DDScompileDisplayList(float par1)
	{
		this.DDSdisplayList = GLAllocation.generateDisplayLists(1);
		GL11.glNewList(this.DDSdisplayList, GL11.GL_COMPILE);
		Tessellator tessellator = Tessellator.getInstance();

		for (int i = 0; i < this.cubeList.size(); ++i)
		{
			((ModelBox)this.cubeList.get(i)).render(tessellator.getBuffer(), par1);
		}

		GL11.glEndList();
		this.DDScompiled = true;
	}
}