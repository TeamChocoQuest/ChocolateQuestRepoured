package team.cqr.cqrepoured.entity.bases;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import team.cqr.cqrepoured.entity.misc.EntityFlyingSkullMinion;
import team.cqr.cqrepoured.faction.Faction;

public interface ISummoner {

	Faction getSummonerFaction();

	List<Entity> getSummonedEntities();

	LivingEntity getSummoner();

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
		if (summoned instanceof LivingEntity) {
			LivingEntity living = (LivingEntity) summoned;

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
			living.setItemInHand(Hand.MAIN_HAND, stack);
		}
	}

}
