package team.cqr.cqrepoured.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import meldexun.asmutil.ASMUtil;
import meldexun.asmutil.transformer.clazz.AbstractClassTransformer;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.launchwrapper.IClassTransformer;
import team.cqr.cqrepoured.init.CQRCreatureAttributes;

public class CQRClassTransformer extends AbstractClassTransformer implements IClassTransformer {

	@Override
	protected void registerTransformers() {
		// @formatter:off
		//Potion hook 
		this.registerMethodTransformer("aez", "c", "(Lvp;)Z", "net/minecraft/entity/projectile/EntityPotion", "isWaterSensitiveEntity", "(Lnet/minecraft/entity/EntityLivingBase;)Z", methodNode -> {
			ASMUtil.LOGGER.info("Transforming method: EntityPotion#isWaterSensitiveEntity(EntityLivingBase)");

			AbstractInsnNode popNode1 = new LabelNode();

			methodNode.instructions.insert(ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new MethodInsnNode(Opcodes.INVOKESTATIC, "team/cqr/cqrepoured/asm/hook/EntityPotionHook", "isWaterSensitiveEntity", "(Lnet/minecraft/entity/EntityLivingBase;)Z", false),
				new JumpInsnNode(Opcodes.IFEQ, (LabelNode) popNode1),
				new InsnNode(Opcodes.ICONST_1),
				new InsnNode(Opcodes.IRETURN),
				popNode1
			));
		});
		//Change creature attribute of Enderman to void
		this.changeCreatureAttributeOfEntity("", "net/minecraft/entity/monster/EntityEnderman", CQRCreatureAttributes.VOID);
		//Change creature attribute of Enderdragon to void
		this.changeCreatureAttributeOfEntity("", "net/minecraft/entity/boss/EntityDragon", CQRCreatureAttributes.VOID);
		//Change creature attribute of Shulkers to void
		this.changeCreatureAttributeOfEntity("", "net/minecraft/entity/monster/EntityShulker", CQRCreatureAttributes.VOID);
		//Change creature attribute of Endermites to void
		this.changeCreatureAttributeOfEntity("", "net/minecraft/entity/monster/EntityEndermite", CQRCreatureAttributes.VOID);
		// @formatter:on
	}

	protected void changeCreatureAttributeOfEntity(final String obfuscatedClassName, final String entityClassPath, final EnumCreatureAttribute newCA) {
		// @formatter:off
		this.registerClassTransformer(obfuscatedClassName, entityClassPath, classNode -> {
			MethodNode methodGetCreatureAttribute = new MethodNode(Opcodes.ACC_PUBLIC, "getCreatureAttribute", "()Lnet/minecraft/entity/EnumCreatureAttribute;", null, null);
			methodGetCreatureAttribute.instructions.insert(ASMUtil.listOf(
				new FieldInsnNode(Opcodes.GETSTATIC, "team/cqr/cqrepoured/init/CQRCreatureAttributes", newCA.name(), "Lnet/minecraft/entity/EnumCreatureAttribute;"),
				new InsnNode(Opcodes.ARETURN)
			));
			classNode.methods.add(methodGetCreatureAttribute);
		});
		// @formatter:on
	}
	
}
