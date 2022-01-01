package team.cqr.cqrepoured.entity.ai;

import java.util.Iterator;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import team.cqr.cqrepoured.CQRMain;

public class EntityAITasksProfiled extends GoalSelector {

	/** Instance of Profiler. */
	private final Profiler profiler;
	private int tickCount;
	private int tickRate = 3;

	/** CQR profiling */
	private static final Object2LongMap<Class<? extends Goal>> AI_TIMES = new Object2LongOpenHashMap<>();
	private static long lastTimeLogged = 0;
	private final World world;

	public EntityAITasksProfiled(Profiler profilerIn, World world) {
		super(profilerIn);
		this.profiler = profilerIn;
		this.world = world;
	}

	@Override
	public void onUpdateTasks() {
		this.profiler.startSection("goalSetup");

		if (this.tickCount++ % this.tickRate == 0) {
			for (GoalSelector.EntityAITaskEntry entityaitasks$entityaitaskentry : this.taskEntries) {
				long t = System.nanoTime();

				if (entityaitasks$entityaitaskentry.using) {
					if (!this.canUseCQR(entityaitasks$entityaitaskentry) || !this.canContinueCQR(entityaitasks$entityaitaskentry)) {
						entityaitasks$entityaitaskentry.using = false;
						entityaitasks$entityaitaskentry.action.resetTask();
						this.executingTaskEntries.remove(entityaitasks$entityaitaskentry);
					}
				} else if (this.canUseCQR(entityaitasks$entityaitaskentry) && entityaitasks$entityaitaskentry.action.shouldExecute()) {
					entityaitasks$entityaitaskentry.using = true;
					entityaitasks$entityaitaskentry.action.startExecuting();
					this.executingTaskEntries.add(entityaitasks$entityaitaskentry);
				}

				t = System.nanoTime() - t;
				t += AI_TIMES.getLong(entityaitasks$entityaitaskentry.action.getClass());
				AI_TIMES.put(entityaitasks$entityaitaskentry.action.getClass(), t);
			}
		} else {
			Iterator<GoalSelector.EntityAITaskEntry> iterator = this.executingTaskEntries.iterator();

			while (iterator.hasNext()) {
				GoalSelector.EntityAITaskEntry entityaitasks$entityaitaskentry1 = iterator.next();

				long t = System.nanoTime();

				if (!this.canContinueCQR(entityaitasks$entityaitaskentry1)) {
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

			for (GoalSelector.EntityAITaskEntry entityaitasks$entityaitaskentry2 : this.executingTaskEntries) {
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
			for (Object2LongMap.Entry<Class<? extends Goal>> entry : AI_TIMES.object2LongEntrySet()) {
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
				for (int i = 10; i <= 10_000; i *= 10) {
					if (d < i) {
						sb.append(' ');
					}
				}
				sb.append(String.format("%.4f", d));
				sb.append('m');
				sb.append('s');
				sb.append('\n');

				entry.setValue(0);
			}
			CQRMain.logger.info(sb);
		}
	}

	/**
	 * Determine if a specific AI Task should continue being executed.
	 */
	private boolean canContinueCQR(GoalSelector.EntityAITaskEntry taskEntry) {
		return taskEntry.action.shouldContinueExecuting();
	}

	/**
	 * Determine if a specific AI Task can be executed, which means that all running higher (= lower int value) priority
	 * tasks are compatible with it or all lower priority tasks can be interrupted.
	 */
	private boolean canUseCQR(GoalSelector.EntityAITaskEntry taskEntry) {
		if (this.executingTaskEntries.isEmpty()) {
			return true;
		} else if (this.isControlFlagDisabled(taskEntry.action.getMutexBits())) {
			return false;
		} else {
			for (GoalSelector.EntityAITaskEntry entityaitasks$entityaitaskentry : this.executingTaskEntries) {
				if (entityaitasks$entityaitaskentry != taskEntry) {
					if (taskEntry.priority >= entityaitasks$entityaitaskentry.priority) {
						if (!this.areTasksCompatibleCQR(taskEntry, entityaitasks$entityaitaskentry)) {
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
	private boolean areTasksCompatibleCQR(GoalSelector.EntityAITaskEntry taskEntry1, GoalSelector.EntityAITaskEntry taskEntry2) {
		return (taskEntry1.action.getMutexBits() & taskEntry2.action.getMutexBits()) == 0;
	}

}
