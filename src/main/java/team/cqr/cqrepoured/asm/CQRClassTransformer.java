package team.cqr.cqrepoured.asm;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
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
		registry.addObf("net.minecraft.entity.projectile.EntityPotion", "isWaterSensitiveEntity", "func_190544_c", "(Lnet/minecraft/entity/EntityLivingBase;)Z", ClassWriter.COMPUTE_FRAMES, methodNode -> {
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

		registry.addObf("net.minecraft.pathfinding.Path", "getVectorFromIndex", "func_75881_a", "(Lnet/minecraft/entity/Entity;I)Lnet/minecraft/util/math/Vec3d;", ClassWriter.COMPUTE_FRAMES, methodNode -> {
			methodNode.instructions.insert(ASMUtil.listOf(
					// PathPoint point = this.points[index];
					new VarInsnNode(Opcodes.ALOAD, 0),
					CQRClassTransformer.createObfFieldInsn(Opcodes.GETFIELD, "net/minecraft/pathfinding/Path", "field_75884_a", "[Lnet/minecraft/pathfinding/PathPoint;"), // points
					new VarInsnNode(Opcodes.ILOAD, 2),
					new InsnNode(Opcodes.AALOAD),
					new VarInsnNode(Opcodes.ASTORE, 3),
					
					// return new Vec3d(point.x + 0.5, point.y, point.z + 0.5);
					new TypeInsnNode(Opcodes.NEW, "net/minecraft/util/math/Vec3d"),
					new InsnNode(Opcodes.DUP),
					
					new VarInsnNode(Opcodes.ALOAD, 3),
					CQRClassTransformer.createObfFieldInsn(Opcodes.GETFIELD, "net/minecraft/pathfinding/PathPoint", "field_75839_a", "I"), // x
					new InsnNode(Opcodes.I2D),
					new LdcInsnNode(0.5D),
					new InsnNode(Opcodes.DADD),
					
					new VarInsnNode(Opcodes.ALOAD, 3),
					CQRClassTransformer.createObfFieldInsn(Opcodes.GETFIELD, "net/minecraft/pathfinding/PathPoint", "field_75837_b", "I"), // y
					new InsnNode(Opcodes.I2D),
					
					new VarInsnNode(Opcodes.ALOAD, 3),
					CQRClassTransformer.createObfFieldInsn(Opcodes.GETFIELD, "net/minecraft/pathfinding/PathPoint", "field_75838_c", "I"), // z
					new InsnNode(Opcodes.I2D),
					new LdcInsnNode(0.5D),
					new InsnNode(Opcodes.DADD),
					
					new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/util/math/Vec3d", "<init>", "(DDD)V", false),
					new InsnNode(Opcodes.ARETURN)
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

	public static FieldInsnNode createObfFieldInsn(int opcode, String owner, String name, String desc) {
		return new FieldInsnNode(opcode, owner, FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(owner, name, desc), desc);
	}

}
