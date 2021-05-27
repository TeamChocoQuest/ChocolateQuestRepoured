package team.cqr.cqrepoured.objects.entity.ai;

import java.util.Iterator;
import java.util.Set;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.util.reflection.ReflectionField;

public class EntityAITasksProfiled extends EntityAITasks {

	private final Set<EntityAITasks.EntityAITaskEntry> executingTaskEntries;
	/** Instance of Profiler. */
	private final Profiler profiler;
	private int tickCount;
	private int tickRate = 3;

	/** CQR profiling */
	private static final Object2LongMap<Class<? extends EntityAIBase>> AI_TIMES = new Object2LongOpenHashMap<>();
	private static long lastTimeLogged = 0;
	private final World world;

	public EntityAITasksProfiled(Profiler profilerIn, World world) {
		super(profilerIn);
		this.profiler = profilerIn;
		this.world = world;
		this.executingTaskEntries = new ReflectionField<Set<EntityAITasks.EntityAITaskEntry>>(EntityAITasks.class, "", "executingTaskEntries").get(this);
	}

	public void onUpdateTasks() {
		this.profiler.startSection("goalSetup");

		if (this.tickCount++ % this.tickRate == 0) {
			for (EntityAITasks.EntityAITaskEntry entityaitasks$entityaitaskentry : this.taskEntries) {
				long t = System.nanoTime();

				if (entityaitasks$entityaitaskentry.using) {
					if (!this.canUse(entityaitasks$entityaitaskentry) || !this.canContinue(entityaitasks$entityaitaskentry)) {
						entityaitasks$entityaitaskentry.using = false;
						entityaitasks$entityaitaskentry.action.resetTask();
						this.executingTaskEntries.remove(entityaitasks$entityaitaskentry);
					}
				} else if (this.canUse(entityaitasks$entityaitaskentry) && entityaitasks$entityaitaskentry.action.shouldExecute()) {
					entityaitasks$entityaitaskentry.using = true;
					entityaitasks$entityaitaskentry.action.startExecuting();
					this.executingTaskEntries.add(entityaitasks$entityaitaskentry);
				}

				t = System.nanoTime() - t;
				t += AI_TIMES.getLong(entityaitasks$entityaitaskentry.action.getClass());
				AI_TIMES.put(entityaitasks$entityaitaskentry.action.getClass(), t);
			}
		} else {
			Iterator<EntityAITasks.EntityAITaskEntry> iterator = this.executingTaskEntries.iterator();

			while (iterator.hasNext()) {
				EntityAITasks.EntityAITaskEntry entityaitasks$entityaitaskentry1 = iterator.next();

				long t = System.nanoTime();

				if (!this.canContinue(entityaitasks$entityaitaskentry1)) {
					entityaitasks$entityaitaskentry1.using = false;
					entityaitasks$entityaitaskentry1.action.resetTask();
					iterator.remove();
				}

				t = System.nanoTime() - t;
				t += AI_TIMES.getLong(entityaitasks$entityaitaskentry1.action.getClass());
				AI_TIMES.put(entityaitasks$entityaitaskentry1.action.getClass(), t);
			}
		}

		this.profiler.endSection();

		if (!this.executingTaskEntries.isEmpty()) {
			this.profiler.startSection("goalTick");

			for (EntityAITasks.EntityAITaskEntry entityaitasks$entityaitaskentry2 : this.executingTaskEntries) {
				long t = System.nanoTime();

				entityaitasks$entityaitaskentry2.action.updateTask();

				t = System.nanoTime() - t;
				t += AI_TIMES.getLong(entityaitasks$entityaitaskentry2.action.getClass());
				AI_TIMES.put(entityaitasks$entityaitaskentry2.action.getClass(), t);
			}

			this.profiler.endSection();
		}

		if (this.world.getTotalWorldTime() - lastTimeLogged > 200) {
			lastTimeLogged = this.world.getTotalWorldTime();

			StringBuilder sb = new StringBuilder("AI Times: \n");
			for (Object2LongMap.Entry<Class<? extends EntityAIBase>> entry : AI_TIMES.object2LongEntrySet()) {
				String s = entry.getKey().getSimpleName();
				sb.append(s);
				sb.append(':');
				sb.append(' ');
				int j = 40;
				while (s.length() < j) {
					j--;
					sb.append(' ');
				}
				double d = entry.getLongValue() / 1_000_000.0D;
				int i = 10000;
				while (d < i) {
					i /= 10;
					sb.append(' ');
				}
				sb.append(String.format("%.4f", d));
				sb.append('m');
				sb.append('s');
				sb.append('\n');

				entry.setValue(0);
			}
			//CQRMain.logger.info(sb);
		}
	}

	/**
	 * Determine if a specific AI Task should continue being executed.
	 */
	private boolean canContinue(EntityAITasks.EntityAITaskEntry taskEntry) {
		return taskEntry.action.shouldContinueExecuting();
	}

	/**
	 * Determine if a specific AI Task can be executed, which means that all running higher (= lower int value) priority
	 * tasks are compatible with it or all lower priority tasks can be interrupted.
	 */
	private boolean canUse(EntityAITasks.EntityAITaskEntry taskEntry) {
		if (this.executingTaskEntries.isEmpty()) {
			return true;
		} else if (this.isControlFlagDisabled(taskEntry.action.getMutexBits())) {
			return false;
		} else {
			for (EntityAITasks.EntityAITaskEntry entityaitasks$entityaitaskentry : this.executingTaskEntries) {
				if (entityaitasks$entityaitaskentry != taskEntry) {
					if (taskEntry.priority >= entityaitasks$entityaitaskentry.priority) {
						if (!this.areTasksCompatible(taskEntry, entityaitasks$entityaitaskentry)) {
							return false;
						}
					} else if (!entityaitasks$entityaitaskentry.action.isInterruptible()) {
						return false;
					}
				}
			}

			return true;
		}
	}

	/**
	 * Returns whether two EntityAITaskEntries can be executed concurrently
	 */
	private boolean areTasksCompatible(EntityAITasks.EntityAITaskEntry taskEntry1, EntityAITasks.EntityAITaskEntry taskEntry2) {
		return (taskEntry1.action.getMutexBits() & taskEntry2.action.getMutexBits()) == 0;
	}

}
