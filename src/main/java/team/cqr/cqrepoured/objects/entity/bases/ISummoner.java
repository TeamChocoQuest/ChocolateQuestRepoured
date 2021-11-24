package team.cqr.cqrepoured.objects.entity.bases;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import team.cqr.cqrepoured.factions.CQRFaction;
import team.cqr.cqrepoured.objects.entity.misc.EntityFlyingSkullMinion;

public interface ISummoner {

	CQRFaction getSummonerFaction();

	List<Entity> getSummonedEntities();

	EntityLivingBase getSummoner();

	default void setSummonedEntityFaction(Entity summoned) {
		if (summoned instanceof AbstractEntityCQR) {
			((AbstractEntityCQR) summoned).setLeader(this.getSummoner());
			((AbstractEntityCQR) summoned).setFaction(this.getSummonerFaction().getName());
		}
		if (summoned instanceof EntityFlyingSkullMinion) {
			((EntityFlyingSkullMinion) summoned).setSummoner(this.getSummoner());
		}
	}

	void addSummonedEntityToList(Entity summoned);
	
	default void tryEquipSummon(Entity summoned, Random rand) {
		if (summoned instanceof EntityLivingBase) {
			EntityLivingBase living = (EntityLivingBase) summoned;

			int material = rand.nextInt(3); // wood, stone, iron
			int weapon = rand.nextInt(4); // sword, pickaxe, axe, shovel
			ItemStack stack = ItemStack.EMPTY;
			if (material == 0) {
				if (weapon == 0) {
					stack = new ItemStack(Items.WOODEN_SWORD);
				} else if (weapon == 1) {
					stack = new ItemStack(Items.WOODEN_PICKAXE);
				} else if (weapon == 2) {
					stack = new ItemStack(Items.WOODEN_AXE);
				} else if (weapon == 3) {
					stack = new ItemStack(Items.WOODEN_SHOVEL);
				}
			} else if (material == 1) {
				if (weapon == 0) {
					stack = new ItemStack(Items.STONE_SWORD);
				} else if (weapon == 1) {
					stack = new ItemStack(Items.STONE_PICKAXE);
				} else if (weapon == 2) {
					stack = new ItemStack(Items.STONE_AXE);
				} else if (weapon == 3) {
					stack = new ItemStack(Items.STONE_SHOVEL);
				}
			} else if (material == 2) {
				if (weapon == 0) {
					stack = new ItemStack(Items.IRON_SWORD);
				} else if (weapon == 1) {
					stack = new ItemStack(Items.IRON_PICKAXE);
				} else if (weapon == 2) {
					stack = new ItemStack(Items.IRON_AXE);
				} else if (weapon == 3) {
					stack = new ItemStack(Items.IRON_SHOVEL);
				}
			}
			living.setHeldItem(EnumHand.MAIN_HAND, stack);
		}
	}

}
