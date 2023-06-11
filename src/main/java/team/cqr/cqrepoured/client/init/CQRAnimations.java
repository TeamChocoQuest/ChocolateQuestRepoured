package team.cqr.cqrepoured.client.init;

import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.CQRMain;

public class CQRAnimations {
	
	public static class Armor {
		public static final ResourceLocation KING_CROWN = CQRMain.prefixArmorAnimation("crown");
	}
	
	public static class Block {
		public static final ResourceLocation NEXUS_CORE = CQRMain.prefixBlockAnimation("force_field_nexus");
	}
	
	public static class Entity {
		private static ResourceLocation humanoid(final String path) {
			return CQRMain.prefixEntityAnimation("humanoid/" + path);
		}
		
		// Misc
		public static final ResourceLocation FLYING_SKULL = CQRMain.prefixEntityAnimation("flying_skull");
		
		// Humanoid
		public static final ResourceLocation _GENERIC_HUMANOID = CQRMain.prefixEntityAnimation("_generic_humanoid");
		
		public static final ResourceLocation BOARMAN = humanoid("boarman");
		public static final ResourceLocation DUMMY = humanoid("dummy");
		public static final ResourceLocation DWARF = humanoid("dwarf");
		public static final ResourceLocation ENDERMAN = humanoid("enderman");
		public static final ResourceLocation GOBLIN = humanoid("goblin");
		public static final ResourceLocation GOLEM = humanoid("golem");
		public static final ResourceLocation GREMLIN = humanoid("gremlin");
		public static final ResourceLocation HUMAN = humanoid("human");
		public static final ResourceLocation ILLAGER = humanoid("illager");
		public static final ResourceLocation MANDRIL = humanoid("mandril");
		public static final ResourceLocation MINOTAUR = humanoid("minotaur");
		public static final ResourceLocation MUMMY = humanoid("mummy");
		public static final ResourceLocation NPC = humanoid("npc");
		public static final ResourceLocation OGRE = humanoid("ogre");
		public static final ResourceLocation ORC = humanoid("orc");
		public static final ResourceLocation PIRATE = humanoid("pirate");
		public static final ResourceLocation SKELETON = humanoid("skeleton");
		public static final ResourceLocation SPECTRE = humanoid("spectre");
		public static final ResourceLocation TRITON = humanoid("triton");
		public static final ResourceLocation WALKER = humanoid("walker");
		public static final ResourceLocation ZOMBIE = humanoid("zombie");
		
		
		// Boss
		public static final ResourceLocation BOAR_MAGE = CQRMain.prefixEntityAnimation("humanoid/boss/mage/boar_mage");
		public static final ResourceLocation ENDER_CALAMITY = CQRMain.prefixEntityAnimation("boss/ender_calamity");
		public static final ResourceLocation EXTERMINATOR = CQRMain.prefixEntityAnimation("humanoid/boss/exterminator");
		public static final ResourceLocation GIANT_TORTOISE = CQRMain.prefixEntityAnimation("boss/giant_tortoise");
		public static final ResourceLocation LICH = CQRMain.prefixEntityAnimation("humanoid/boss/mage/lich");
		public static final ResourceLocation MAGE_HIDDEN = CQRMain.prefixEntityAnimation("humanoid/boss/mage/_hidden");
		public static final ResourceLocation NECROMANCER = CQRMain.prefixEntityAnimation("humanoid/boss/mage/necromancer");
		public static final ResourceLocation NETHER_DRAGON = CQRMain.prefixEntityAnimation("boss/nether_dragon");
		public static final ResourceLocation PIRATE_CAPTAIN = CQRMain.prefixEntityAnimation("humanoid/boss/pirate_captain");
		public static final ResourceLocation SHELOB = CQRMain.prefixEntityAnimation("boss/shelob");
		public static final ResourceLocation SPECTRE_LORD = CQRMain.prefixEntityAnimation("humanoid/boss/spectre_lord");
		public static final ResourceLocation WALKER_KING = CQRMain.prefixEntityAnimation("humanoid/boss/walker_king");
	}
	
	public static class Item {
		
	}

	public static final ResourceLocation _EMPTY = CQRMain.prefixAnimation("_empty");

}
