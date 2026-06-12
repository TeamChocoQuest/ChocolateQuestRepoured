package team.cqr.cqrepoured.asm;

import java.lang.reflect.Field;

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

import com.google.common.collect.BiMap;

import meldexun.asmutil2.ASMUtil;
import meldexun.asmutil2.HashMapClassNodeClassTransformer;
import meldexun.asmutil2.IClassTransformerRegistry;
import meldexun.asmutil2.NonLoadingClassWriter;
import meldexun.asmutil2.reader.ClassUtil;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import team.cqr.cqrepoured.asm.util.DeobfuscationUtil;

public class CQRClassTransformer extends HashMapClassNodeClassTransformer implements IClassTransformer {

	private static final ClassUtil REMAPPING_CLASS_UTIL;
	static {
		try {
			Class<?> FMLDeobfuscatingRemapper = Class.forName("net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper", true, Launch.classLoader);
			Field _INSTANCE = FMLDeobfuscatingRemapper.getField("INSTANCE");
			Field _classNameBiMap = FMLDeobfuscatingRemapper.getDeclaredField("classNameBiMap");
			_classNameBiMap.setAccessible(true);
			@SuppressWarnings("unchecked")
			BiMap<String, String> deobfuscationMap = (BiMap<String, String>) _classNameBiMap.get(_INSTANCE.get(null));
			REMAPPING_CLASS_UTIL = ClassUtil.getInstance(new ClassUtil.Configuration(Launch.classLoader, deobfuscationMap.inverse(), deobfuscationMap));
		} catch (ReflectiveOperationException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	@Override
	protected void registerTransformers(IClassTransformerRegistry registry) {
		this.changeCreatureAttributeOfEntity(registry, "net.minecraft.entity.boss.EntityDragon", "VOID");
		this.changeCreatureAttributeOfEntity(registry, "net.minecraft.entity.monster.EntityEnderman", "VOID");
		this.changeCreatureAttributeOfEntity(registry, "net.minecraft.entity.monster.EntityShulker", "VOID");
	}

	protected void changeCreatureAttributeOfEntity(IClassTransformerRegistry registry, String className, String creatureAttributeName) {
		// @formatter:off
		registry.add(className, ClassWriter.COMPUTE_FRAMES, classNode -> {
			MethodNode m_getCreatureAttribute = DeobfuscationUtil.createObfMethod(classNode.name, Opcodes.ACC_PUBLIC, "func_70668_bt", "()Lnet/minecraft/entity/EnumCreatureAttribute;", null, null); // getCreatureAttribute
			m_getCreatureAttribute.instructions.insert(ASMUtil.listOf(
					new FieldInsnNode(Opcodes.GETSTATIC, "team/cqr/cqrepoured/init/CQRCreatureAttributes", creatureAttributeName, "Lnet/minecraft/entity/EnumCreatureAttribute;"),
					new InsnNode(Opcodes.ARETURN)
			));
			classNode.methods.add(m_getCreatureAttribute);
		});
		// @formatter:on
	}

	@Override
	protected ClassWriter createClassWriter(int flags) {
		return new NonLoadingClassWriter(flags, REMAPPING_CLASS_UTIL);
	}

}
