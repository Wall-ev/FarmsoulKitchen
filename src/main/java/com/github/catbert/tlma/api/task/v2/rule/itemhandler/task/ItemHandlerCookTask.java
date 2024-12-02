package com.github.catbert.tlma.api.task.v2.rule.itemhandler.task;

import com.github.catbert.tlma.api.task.v1.cook.ICookTask;
import com.github.catbert.tlma.api.task.v2.rule.itemhandler.v2.CheckCondition;
import com.github.catbert.tlma.api.task.v2.rule.itemhandler.v2.ItemHandlerCook;
import com.github.catbert.tlma.task.cook.handler.v2.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ItemHandlerCookTask<B extends BlockEntity, R extends Recipe<? extends Container>> implements ItemHandlerCook<B, R>, ICookTask<B, R> {
    public static final Map<TaskType, List<CheckCondition.ItemHandler>> RULES = new HashMap<>();
    private final TaskType taskType;
    private final List<CheckCondition.ItemHandler> rule;
    private final List<ActionState> actionStates;

    public ItemHandlerCookTask() {
        RULES.put(TaskType.NORMAL, this.getNormalRule());
        RULES.put(TaskType.FARMER_DELIGHT, this.getFdRule());
        this.taskType = getTaskType();
        this.rule = RULES.get(taskType);
        this.actionStates = getStateATypes();
    }

    @Override
    public Map<TaskType, List<CheckCondition.ItemHandler>> getRules() {
        return RULES;
    }

    @Override
    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {
        CombinedInvWrapper maidInv = entityMaid.getAvailableInv(true);
        ItemStackHandler beInv = getItemHandler(blockEntity);
        return canMoveToBe(this.rule, maidInv, beInv, serverLevel, entityMaid, blockEntity, maidRecipesManager);
    }

    @Override
    public void processCookMake(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {

    }

    public abstract TaskType getTaskType();
}
