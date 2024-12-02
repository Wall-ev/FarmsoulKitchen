package com.github.catbert.tlma.api.task.v2.dis;

import com.github.catbert.tlma.api.task.v2.IBeInv;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * 用来处理输出物品附加条件的...
 */
public interface IBeOutput<B extends BlockEntity, T extends IBeInv<B>> {

    interface NeedContainer<B extends BlockEntity, T extends IBeInv<B>> extends IBeOutput<B, T> {

        ItemStack getContainer(B be, T beInv);

        int getContainerSlot();

        ItemStack getDisplayFood(B be, T beInv);

        int getDisplayFoodSlot();

        ItemStack getActualContainer(B be, T beInv);

        int getActualContainerSlot();

    }

}
