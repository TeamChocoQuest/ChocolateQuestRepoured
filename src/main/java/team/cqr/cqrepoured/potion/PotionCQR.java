package team.cqr.cqrepoured.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.client.util.GuiHelper;

public class PotionCQR extends Effect {

	private final ResourceLocation texture;

	public PotionCQR(String name, boolean bad, int color) {
		super(bad, color);
		this.setRegistryName(CQRMain.MODID, name);
		this.setPotionName("effect." + CQRMain.MODID + "." + name);
		if (!bad) {
			this.setBeneficial();
		}
		this.texture = new ResourceLocation(CQRMain.MODID, "textures/mob_effect/" + name + ".png");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(EffectInstance effect, AbstractGui gui, int x, int y, float z) {
		Minecraft mc = Minecraft.getMinecraft();
		mc.renderEngine.bindTexture(this.texture);
		GuiHelper.drawTexture(x + 6, y + 7, 0.0D, 0.0D, 18.0D, 18.0D, 1.0D, 1.0D);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderHUDEffect(EffectInstance effect, AbstractGui gui, int x, int y, float z, float alpha) {
		Minecraft mc = Minecraft.getMinecraft();
		mc.renderEngine.bindTexture(this.texture);
		GuiHelper.drawTexture(x + 3, y + 3, 0.0D, 0.0D, 18.0D, 18.0D, 1.0D, 1.0D);
	}

}
