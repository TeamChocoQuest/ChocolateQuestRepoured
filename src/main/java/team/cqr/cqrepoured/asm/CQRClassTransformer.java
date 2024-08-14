package team.cqr.cqrepoured.asm;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import meldexun.asmutil2.ASMUtil;
import meldexun.asmutil2.HashMapClassNodeClassTransformer;
import meldexun.asmutil2.IClassTransformerRegistry;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class CQRClassTransformer extends HashMapClassNodeClassTransformer implements IClassTransformer {

	@Override
	protected void registerTransformers(IClassTransformerRegistry registry) {
		// @formatter:off
		registry.add("net.minecraft.entity.projectile.EntityPotion", "isWaterSensitiveEntity", "(Lnet/minecraft/entity/EntityLivingBase;)Z", "c", "(Lnet/minecraft/entity/EntityLivingBase;)Z", ClassWriter.COMPUTE_FRAMES, methodNode -> {
			AbstractInsnNode popNode1 = new LabelNode();

			methodNode.instructions.insert(ASMUtil.listOf(
					// if (entity instanceof IMechanical) return true;
					new VarInsnNode(Opcodes.ALOAD, 0),
					new TypeInsnNode(Opcodes.INSTANCEOF, "team/cqr/cqrepoured/entity/IMechanical"),
					new JumpInsnNode(Opcodes.IFEQ, (LabelNode) popNode1),
					new InsnNode(Opcodes.ICONST_1),
					new InsnNode(Opcodes.IRETURN),
					popNode1
			));
		});
		// @formatter:on

		this.changeCreatureAttributeOfEntity(registry, "net.minecraft.entity.boss.EntityDragon", "VOID");
		this.changeCreatureAttributeOfEntity(registry, "net.minecraft.entity.monster.EntityEnderman", "VOID");
		this.changeCreatureAttributeOfEntity(registry, "net.minecraft.entity.monster.EntityShulker", "VOID");
	}

	protected void changeCreatureAttributeOfEntity(IClassTransformerRegistry registry, String className, String creatureAttributeName) {
		// @formatter:off
		registry.add(className, ClassWriter.COMPUTE_FRAMES, classNode -> {
			MethodNode m_getCreatureAttribute = CQRClassTransformer.createObfMethod(classNode.name, Opcodes.ACC_PUBLIC, "func_70668_bt", "()Lnet/minecraft/entity/EnumCreatureAttribute;", null, null); // getCreatureAttribute
			m_getCreatureAttribute.instructions.insert(ASMUtil.listOf(
					new FieldInsnNode(Opcodes.GETSTATIC, "team/cqr/cqrepoured/init/CQRCreatureAttributes", creatureAttributeName, "Lnet/minecraft/entity/EnumCreatureAttribute;"),
					new InsnNode(Opcodes.ARETURN)
			));
			classNode.methods.add(m_getCreatureAttribute);
		});
		// @formatter:on
	}

	private static MethodNode createObfMethod(String owner, int access, String name, String desc, String signature, String[] exceptions) {
		return new MethodNode(access, FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc), desc, signature, exceptions);
	}

}
