package team.cqr.cqrepoured.integration.jei.trade;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import mezz.jei.api.IModRegistry;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.LoaderException;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableEntityInfo;
import team.cqr.cqrepoured.world.structure.generation.generation.preparable.PreparableSpawnerInfo;
import team.cqr.cqrepoured.world.structure.generation.structurefile.CQStructure;

public class JEITradeIntegration {

	public static void register(IModRegistry registry) {
		World world = new WorldClient(null, new WorldSettings(0L, GameType.SURVIVAL, true, false, WorldType.DEFAULT), 0, EnumDifficulty.NORMAL, new Profiler()) {
			@Override
			public void playSound(double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch, boolean distanceDelay) {
				// nop
			}
		};

		try {
			Path structureDir = CQRMain.CQ_STRUCTURE_FILES_FOLDER.toPath();
			Files.find(structureDir, Integer.MAX_VALUE, (p, a) -> {
				if (!a.isRegularFile()) return false;
				if (!p.getFileName().toString().endsWith(".nbt")) return false;
				if (structureDir.relativize(p).toString().contains("DISABLED")) return false;
				return true;
			})
					.collect(Collectors.toList())
					.parallelStream()
					.map(file -> {
						List<TradeWrapper> trades = new ArrayList<>();
						CQStructure structure = CQStructure.createFromFile(file.toFile());

						// parse spawners
						structure.forEachBlock((posInfo, pos) -> {
							if (!(posInfo instanceof PreparableSpawnerInfo)) return;
							NBTTagList itemTags = ((PreparableSpawnerInfo) posInfo).getTileEntityData().getCompoundTag("inventory").getTagList("Items", NBT.TAG_COMPOUND);
							IntStream.range(0, itemTags.tagCount())
									.mapToObj(itemTags::getCompoundTagAt)
									.map(itemTag -> itemTag.getCompoundTag("tag"))
									.map(itemTagTag -> itemTagTag.getCompoundTag("EntityIn"))
									.flatMap(entityTag -> stream(entityTag, world, AbstractEntityCQR.class))
									.peek(entity -> {
										entity.posX = pos.getX() + 0.5;
										entity.posY = pos.getY();
										entity.posZ = pos.getZ() + 0.5;
									})
									.flatMap(trader -> trader.getTrades().getTrades().stream().map(trade -> new TradeWrapper(file, trader, trade)))
									.forEach(trades::add);
						});

						// parse entities
						structure.getEntityInfoList()
								.stream()
								.map(PreparableEntityInfo::getEntityData)
								.flatMap(entityTag -> stream(entityTag, world, AbstractEntityCQR.class))
								.flatMap(trader -> trader.getTrades().getTrades().stream().map(trade -> new TradeWrapper(file, trader, trade)))
								.forEach(trades::add);

						return trades;
					})
					.collect(Collectors.toList())
					.forEach(recipes -> registry.addRecipes(recipes, TradeCategory.ID));
		} catch (IOException e) {
			throw new LoaderException("Failed loading trading info from structure templates", e);
		}
	}

	private static <T extends Entity> Stream<T> stream(NBTTagCompound entityTag, World world, Class<T> entityClass) {
		Stream.Builder<T> builder = Stream.builder();
		stream(entityTag, world, entityClass, builder);
		return builder.build();
	}

	@SuppressWarnings("unchecked")
	private static <T extends Entity> void stream(NBTTagCompound entityTag, World world, Class<T> entityClass, Stream.Builder<T> builder) {
		if (entityClass.isAssignableFrom(EntityList.getClassFromName(entityTag.getString("id")))) {
			Entity entity = EntityList.createEntityFromNBT(entityTag, world);
			NBTTagList tagList = entityTag.getTagList("Pos", Constants.NBT.TAG_DOUBLE);
			entity.posX = tagList.getDoubleAt(0);
			entity.posY = tagList.getDoubleAt(1);
			entity.posZ = tagList.getDoubleAt(2);
			builder.add((T) entity);
		}
		for (NBTBase passengerNBT : entityTag.getTagList("Passengers", NBT.TAG_COMPOUND)) {
			stream((NBTTagCompound) passengerNBT, world, entityClass, builder);
		}
	}

}
