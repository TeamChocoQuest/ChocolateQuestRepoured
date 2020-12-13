package team.cqr.cqrepoured.objects.items.staves;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import team.cqr.cqrepoured.init.CQRSounds;
import team.cqr.cqrepoured.util.IRangedWeapon;

public class ItemStaffWind extends Item implements IRangedWeapon {

	public ItemStaffWind() {
		this.setMaxDamage(2048);
		this.setMaxStackSize(1);
	}

	@Override
	public void shoot(World worldIn, EntityLivingBase shooter, Entity target, EnumHand handIn) {
		// TODO Auto-generated method stub

	}

	@Override
	public SoundEvent getShootSound() {
		return CQRSounds.MAGIC;
	}

	@Override
	public double getRange() {
		return 32.0D;
	}

	@Override
	public int getCooldown() {
		return 80;
	}

	@Override
	public int getChargeTicks() {
		return 0;
	}

}
