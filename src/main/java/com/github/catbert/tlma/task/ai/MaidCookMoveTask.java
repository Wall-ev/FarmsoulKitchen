package com.github.catbert.tlma.task.ai;

import com.github.catbert.tlma.api.task.v1.cook.ICookTask;
import com.github.catbert.tlma.task.cook.handler.v2.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidCheckRateTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public class MaidCookMoveTask<B extends BlockEntity, R extends Recipe<? extends Container>> extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 120;
    private final float movementSpeed;
    private final int verticalSearchRange;
    private final ICookTask<B, R> task;
    private final boolean single;
    protected int verticalSearchStart;
    private MaidRecipesManager<R> maidRecipesManager;

    public MaidCookMoveTask(EntityMaid maid, ICookTask<B, R> task) {
        this(maid, task, 0.5f, 2, false);
    }

    public MaidCookMoveTask(EntityMaid maid, ICookTask<B, R> task, MaidRecipesManager<R> maidRecipesManager) {
        this(maid, task, 0.5f, 2, false);
        this.maidRecipesManager = maidRecipesManager;
    }

    public MaidCookMoveTask(EntityMaid maid, ICookTask<B, R> task, float movementSpeed, boolean single) {
        this(maid, task, movementSpeed, 2, single);
    }

    public MaidCookMoveTask(EntityMaid maid, ICookTask<B, R> task, float movementSpeed, int verticalSearchRange, boolean single) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
                InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_ABSENT));
        this.task = task;
        this.movementSpeed = movementSpeed;
        this.verticalSearchRange = verticalSearchRange;
        this.single = single;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    private static void setWalkAndLookTargetMemories(LivingEntity pLivingEntity, BlockPos walkPos, BlockPos lookPos, float pSpeed, int pDistance) {
        pLivingEntity.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(walkPos, pSpeed, pDistance));
        pLivingEntity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(lookPos));
    }

    public MaidRecipesManager<R> getMaidRecipesManager() {
        return maidRecipesManager;
    }

    private boolean checkOwnerPos(EntityMaid maid, BlockPos mutableBlockPos) {
        if (maid.isHomeModeEnable()) {
            return true;
        }
        return maid.getOwner() != null && mutableBlockPos.closerToCenterThan(maid.getOwner().position(), 8);
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
//        this.processRecipeManager(maid);
        this.searchForDestination(worldIn, maid);
    }

    private void processRecipeManager(EntityMaid maid) {
        if (maidRecipesManager == null) {
            maidRecipesManager = new MaidRecipesManager<>(maid, task, single, true);
        } else {
            this.maidRecipesManager.checkAndCreateRecipesIngredients(maid);
        }
    }

    @SuppressWarnings("unchecked")
    protected boolean shouldMoveTo(ServerLevel worldIn, EntityMaid maid, BlockPos blockPos) {
        BlockEntity blockEntity = worldIn.getBlockEntity(blockPos);
        if (blockEntity == null) {
            return false;
        }
        if (task.isCookBE(blockEntity) && maidCanIntractBlock(maid, blockPos)) {
            this.processRecipeManager(maid);
            return task.shouldMoveTo(worldIn, maid, (B) blockEntity, maidRecipesManager);
        }
        return false;
    }

//    @todo 女仆有无权限与该方块交互： 领地、殖民地
    protected boolean maidCanIntractBlock(EntityMaid maid, BlockPos pos) {
        return true;
    }

    protected boolean checkPathReach(EntityMaid maid, BlockPos pos) {
        return maid.canPathReach(pos);
    }

    private static BlockPos getSearchPos(EntityMaid maid) {
        return maid.hasRestriction() ? maid.getRestrictCenter() : maid.blockPosition().below();
    }

    protected final void searchForDestination(ServerLevel worldIn, EntityMaid maid) {
        BlockPos centrePos = getSearchPos(maid);
        int searchRange = (int) maid.getRestrictRadius();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int y = this.verticalSearchStart; y <= this.verticalSearchRange; y = y > 0 ? -y : 1 - y) {
            for (int i = 0; i < searchRange; ++i) {
                for (int x = 0; x <= i; x = x > 0 ? -x : 1 - x) {
                    for (int z = x < i && x > -i ? i : 0; z <= i; z = z > 0 ? -z : 1 - z) {
                        mutableBlockPos.setWithOffset(centrePos, x, y + 1, z);

                        if (maid.isWithinRestriction(mutableBlockPos) && shouldMoveTo(worldIn, maid, mutableBlockPos)
//                                && checkPathReach(maid, mutableBlockPos)
                                && checkOwnerPos(maid, mutableBlockPos)) {
                            setWalkAndLookTargetMemories(maid, mutableBlockPos, mutableBlockPos, this.movementSpeed, 0);
                            maid.getBrain().setMemory(InitEntities.TARGET_POS.get(), new BlockPosTracker(mutableBlockPos));
                            this.setNextCheckTickCount(5);
                            return;
                        }
                    }
                }
            }
        }
    }
}
