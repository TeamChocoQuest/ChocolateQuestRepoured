package team.cqr.cqrepoured.generation.world.level.levelgen.structure;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraftforge.fml.ModList;
import team.cqr.cqrepoured.generation.init.CQRStructureTypes;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.generator.StructureGenerator;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.inhabitant.InhabitantSelector;
import team.cqr.cqrepoured.generation.world.level.levelgen.structure.placement.PlacementSettings;
import team.cqr.cqrepoured.protection.ProtectionSettings;

public class CQRStructure extends Structure {

	public static final Codec<CQRStructure> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
        		settingsCodec(instance),
				Codec.BOOL.fieldOf("enabled").forGetter(CQRStructure::enabled),
				Codec.INT.fieldOf("icon").forGetter(CQRStructure::icon),
				Codec.list(Codec.STRING).fieldOf("mod_dependencies").forGetter(CQRStructure::modDependencies),
				PlacementSettings.CODEC.fieldOf("placement_settings").forGetter(CQRStructure::placementSettings),
				InhabitantSelector.CODEC.fieldOf("inhabitant_selector").forGetter(CQRStructure::inhabitantSelector),
				Codec.INT.fieldOf("ground_level_delta").forGetter(CQRStructure::groundLevelDelta),
				ProtectionSettings.CODEC.optionalFieldOf("protection_settings").forGetter(CQRStructure::protectionSettings),
				StructureGenerator.CODEC.fieldOf("generator").forGetter(CQRStructure::generator))
        		.apply(instance, CQRStructure::new);
     });

	private final boolean enabled;
	private final int icon;
	private final List<String> modDependencies;
	private final PlacementSettings placementSettings;
	private final InhabitantSelector inhabitantSelector;
	private final int groundLevelDelta;
	private final Optional<ProtectionSettings> protectionSettings;
	private final StructureGenerator generator;

	public CQRStructure(StructureSettings structureSettings, boolean enabled, int icon, List<String> modDependencies, PlacementSettings placementSettings,
			InhabitantSelector inhabitantSelector, int groundLevelDelta, Optional<ProtectionSettings> protectionSettings, StructureGenerator generator) {
		super(structureSettings);
		this.enabled = enabled;
		this.icon = icon;
		this.modDependencies = modDependencies;
		this.placementSettings = placementSettings;
		this.inhabitantSelector = inhabitantSelector;
		this.groundLevelDelta = groundLevelDelta;
		this.protectionSettings = protectionSettings;
		this.generator = generator;
	}

	@Override
	public StructureType<?> type() {
		return CQRStructureTypes.CQR_STRUCTURE_TYPE.get();
	}

	@Override
	protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		if (!this.enabled) {
			return Optional.empty();
		}
		if (!this.modDependencies.isEmpty() && !this.modDependencies.stream().allMatch(ModList.get()::isLoaded)) {
			return Optional.empty();
		}
		Optional<BlockPos> pos = this.placementSettings.findGenerationPoint(this, context);
		if (pos.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(new GenerationStub(pos.get(), this.createGenerator(context, pos.get())));
	}

	private Consumer<StructurePiecesBuilder> createGenerator(GenerationContext context, BlockPos pos) {
		return structurePiecesBuilder -> {
			CQRStructurePiece.Builder structurePieceBuilder = CQRStructurePiece.Builder.create(context, pos, inhabitantSelector, groundLevelDelta, protectionSettings);
			generator.prepare(context, pos.above(groundLevelDelta), structurePieceBuilder);
			structurePiecesBuilder.addPiece(structurePieceBuilder.build());
		};
	}

	public StructureStart createStructureStart(ServerLevel level, BlockPos pos) {
		ChunkPos chunkPos = new ChunkPos(pos);
		GenerationContext context = new GenerationContext(
				level.registryAccess(),
				level.getChunkSource().getGenerator(),
				level.getChunkSource().getGenerator().getBiomeSource(),
				level.getChunkSource().randomState(),
				level.getStructureManager(),
				level.getSeed(),
				chunkPos,
				level,
				biome -> true);
		BlockPos generationPos = this.placementSettings.applyOffsets(context, pos);
		GenerationStub generationStub = new GenerationStub(generationPos, this.createGenerator(context, generationPos));
		StructureStart structureStart = new StructureStart(this, chunkPos, 0, generationStub.getPiecesBuilder().build());
		if (structureStart.isValid()) {
			return structureStart;
		}
		return StructureStart.INVALID_START;
	}

	public boolean enabled() {
		return enabled;
	}

	public int icon() {
		return icon;
	}

	public List<String> modDependencies() {
		return modDependencies;
	}

	public PlacementSettings placementSettings() {
		return placementSettings;
	}

	public InhabitantSelector inhabitantSelector() {
		return inhabitantSelector;
	}

	public int groundLevelDelta() {
		return groundLevelDelta;
	}

	public Optional<ProtectionSettings> protectionSettings() {
		return protectionSettings;
	}

	public StructureGenerator generator() {
		return generator;
	}

}
