package team.cqr.cqrepoured.particles;

import java.util.Locale;
import java.util.Optional;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.cqr.cqrepoured.init.CQRParticleTypes;

public class BlockHighlightParticleData implements IParticleData {

	public static final int COLOR_ENTITY_HIGHLIGHT = 0xC00000;
	public static final int BLOCK_DEPENDENCY_HIGHLIGHT = 0x4050D0;
	public static final int BLOCK_PROTECTED_HIGHLIGHT = 0xFFFF00;
	public static final int BLOCK_UNPROTECTED_HIGHLIGHT = 0x00FF00;
	
	public static class Type extends ParticleType<BlockHighlightParticleData> {
		
		public Type(boolean ignoreRenderDistance) {
			super(ignoreRenderDistance, DESERIALIZER);
		}

		@Override
		public Codec<BlockHighlightParticleData> codec() {
			return CODEC;
		}
		
	}
	
	public static ParticleType<BlockHighlightParticleData> createData() {
		return new BlockHighlightParticleData.Type(true);
	}

	public static final Codec<BlockHighlightParticleData> CODEC = RecordCodecBuilder.create((objOuter) -> {
		return objOuter.group(Codec.INT.fieldOf("color").forGetter((obj) -> {
			return obj.color;
		}), Codec.INT.fieldOf("lifetime").forGetter((obj) -> {
			return obj.lifetime;
		}), Codec.optionalField("entityid", Codec.INT).forGetter((obj) -> {
			return obj.entityReferenceID;
		}), Codec.optionalField("scale", BlockPos.CODEC).forGetter((obj) -> {
			return obj.blockPos;
		})).apply(objOuter, BlockHighlightParticleData::new);
	});

	public static final IParticleData.IDeserializer<BlockHighlightParticleData> DESERIALIZER = new IParticleData.IDeserializer<BlockHighlightParticleData>() {
		public BlockHighlightParticleData fromCommand(ParticleType<BlockHighlightParticleData> pParticleType, StringReader pReader) throws CommandSyntaxException {
			pReader.expect(' ');
			int color = pReader.readInt();
			pReader.expect(' ');
			int lifetime = pReader.readInt();
			return new BlockHighlightParticleData(color, lifetime);
		}

		public BlockHighlightParticleData fromNetwork(ParticleType<BlockHighlightParticleData> pParticleType, PacketBuffer pBuffer) {
			int color = pBuffer.readInt();
			int lifetime = pBuffer.readInt();
			Optional<Integer> entityReference = Optional.empty();
			if (pBuffer.readBoolean()) {
				entityReference = Optional.of(pBuffer.readInt());
			}
			Optional<BlockPos> blockReference = Optional.empty();
			if (pBuffer.readBoolean()) {
				blockReference = Optional.of(pBuffer.readBlockPos());
			}
			return new BlockHighlightParticleData(color, lifetime, entityReference, blockReference);
		}
	};

	private final int color;
	private final int lifetime;
	private final Optional<Integer> entityReferenceID;
	private final Optional<BlockPos> blockPos;

	public BlockHighlightParticleData(int color, int lifetime) {
		this(color, lifetime, Optional.empty(), Optional.empty());
	}

	public BlockHighlightParticleData(int color, int lifetime, int entityID) {
		this(color, lifetime, Optional.of(entityID), Optional.empty());
	}

	public BlockHighlightParticleData(int color, int lifetime, BlockPos pos) {
		this(color, lifetime, Optional.empty(), Optional.of(pos));
	}

	public BlockHighlightParticleData(int color, int lifetime, Optional<Integer> optEntId, Optional<BlockPos> optBlockPos) {
		super();
		this.color = color;
		this.lifetime = lifetime;
		this.entityReferenceID = optEntId;
		this.blockPos = optBlockPos;
	}

	@Override
	public ParticleType<?> getType() {
		return null;
		//return CQRParticleTypes.BLOCK_HIGHLIGHT.get();
	}

	@Override
	public void writeToNetwork(PacketBuffer buf) {
		buf.writeInt(this.color);
		buf.writeInt(this.lifetime);
		buf.writeBoolean(this.entityReferenceID.isPresent());
		if (this.entityReferenceID.isPresent()) {
			buf.writeInt(this.entityReferenceID.get());
		}
		buf.writeBoolean(this.blockPos.isPresent());
		if (this.blockPos.isPresent()) {
			buf.writeBlockPos(this.blockPos.get());
		}
	}

	@Override
	public String writeToString() {
		return String.format(Locale.ROOT, "%s % %", Registry.PARTICLE_TYPE.getKey(this.getType()), this.color, this.lifetime);
	}

	@OnlyIn(Dist.CLIENT)
	public int getColor() {
		return this.color;
	}

	@OnlyIn(Dist.CLIENT)
	public int getLifetime() {
		return this.lifetime;
	}

	@OnlyIn(Dist.CLIENT)
	public Optional<Integer> getEntityID() {
		return this.entityReferenceID;
	}

	@OnlyIn(Dist.CLIENT)
	public Optional<BlockPos> getBlockPos() {
		return this.blockPos;
	}

}
