package team.cqr.cqrepoured.entity.bases;

import java.util.List;

import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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

	default void tryEquipSummon(Entity summoned, RandomSource random) {
		if (summoned instanceof LivingEntity) {
			LivingEntity living = (LivingEntity) summoned;

			int material = random.nextInt(3); // wood, stone, iron
			int weapon = random.nextInt(4); // sword, pickaxe, axe, shovel
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
			living.setItemInHand(InteractionHand.MAIN_HAND, stack);
		}
	}

}
