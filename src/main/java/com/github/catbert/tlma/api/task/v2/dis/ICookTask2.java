package com.github.catbert.tlma.api.task.v2.dis;

import com.github.catbert.tlma.api.TaskBookEntryType;
import com.github.catbert.tlma.api.task.v1.cook.ICookTask;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface ICookTask2<B extends BlockEntity, R extends Recipe<? extends Container>> extends ICookTask<B, R> {
    @Override
    default TaskBookEntryType getBookEntryType() {
        return TaskBookEntryType.COOK;
    }
}
