package team.cqr.cqrepoured.mixin.command;

import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.command.impl.LocateCommand;
import team.cqr.cqrepoured.world.structure.generation.dungeons.DungeonBase;

@Mixin(LocateCommand.class)
public class MixinLocateCommand {

	// Mixin into re-assignment of literalargumentbuilder
	// When name is CQR-any-structure => Set it to be the "same"
	// But before, add our re-routing to our method for our cqr structures
	// TODO: Change to inject, then if it is for the cqr-any structure => remove the last added children from arguments
	/*@Inject(
			method = "register(Lcom/mojang/brigadier/CommandDispatcher;)V",
			locals = LocalCapture.CAPTURE_FAILHARD,
			cancellable = true,
			at = @At(
					value = "TAIL",
					ordinal = 1
			)
	)*/
	@ModifyVariable(
			method = "register(Lcom/mojang/brigadier/CommandDispatcher;)V",
			at = @At(
					value = "STORE"
			),
			ordinal = 3
	)
	private static LiteralArgumentBuilder<CommandSourceStack> mixinArgumentBuilder(LiteralArgumentBuilder<CommandSourceStack> lab) {
		List<CommandNode<?>> nodes = lab.getArguments().stream().collect(Collectors.toList());
		CommandNode<?> node = nodes.get(nodes.size());
		if(node instanceof LiteralCommandNode<?>) {
			String literal = ((LiteralCommandNode) node).getLiteral();
			//if(literal.equalsIgnoreCase(CQRStructures.CQR_ANY_DUNGEON.getId().toString())) {
				lab = lab.then(Commands.literal("cqrepoured:yooooo" + nodes.size()).executes(ctx -> 0));
			//}
		}
		lab = lab.then(Commands.literal("cqrepoured:yooooo" + nodes.size()).executes(ctx -> 0));
		return lab;
	}

	private static int locateCQR(CommandSourceStack sender, DungeonBase dungeon) {
		return 0;
	}

}
