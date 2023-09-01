package team.cqr.cqrepoured.potion;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import team.cqr.cqrepoured.CQRConstants;

public class PotionCQR extends MobEffect {

	private final ResourceLocation texture;

	public PotionCQR(String name, MobEffectCategory type, int color) {
		super(type, color);
		//Finally no longer needed?
		//this.setRegistryName(CQRConstants.MODID, name);
		
		//Unnecessary?
		//this.setPotionName("effect." + CQRConstants.MODID + "." + name);
		this.texture = new ResourceLocation(CQRConstants.MODID, "textures/mob_effect/" + name + ".png");
	}

	//No longer needed?
	/*@Override
	@OnlyIn(Dist.CLIENT)
	public void renderInventoryEffect(EffectInstance effect, AbstractGui gui, int x, int y, float z) {
		Minecraft mc = Minecraft.getInstance();
		mc.renderEngine.bindTexture(this.texture);
		GuiHelper.drawTexture(x + 6, y + 7, 0.0D, 0.0D, 18.0D, 18.0D, 1.0D, 1.0D);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderHUDEffect(EffectInstance effect, AbstractGui gui, int x, int y, float z, float alpha) {
		Minecraft mc = Minecraft.getInstance();
		mc.renderrenderEngine.bindTexture(this.texture);
		GuiHelper.drawTexture(x + 3, y + 3, 0.0D, 0.0D, 18.0D, 18.0D, 1.0D, 1.0D);
	}

	
	@Override
	public void renderHUDEffect(EffectInstance effect, AbstractGui gui, MatrixStack mStack, int x, int y, float z, float alpha) {
		// TODO Auto-generated method stub
		super.renderHUDEffect(effect, gui, mStack, x, y, z, alpha);
	}
	
	@Override
	public void renderInventoryEffect(EffectInstance effect, DisplayEffectsScreen<?> gui, MatrixStack mStack, int x, int y, float z) {
		// TODO Auto-generated method stub
		super.renderInventoryEffect(effect, gui, mStack, x, y, z);
	}*/
}
