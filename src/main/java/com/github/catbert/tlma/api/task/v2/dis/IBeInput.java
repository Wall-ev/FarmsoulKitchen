package com.github.catbert.tlma.api.task.v2.dis;

import com.github.catbert.tlma.api.task.v2.IBeInv;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * 用来处理输入物品附加条件的...
 */
public interface IBeInput<B extends BlockEntity, T extends IBeInv<B>> {

    interface INeedInputContainer<B extends BlockEntity, T extends IBeInv<B>> extends IBeInput<B, T> {
        ItemStack getContainer(B be, T beInv);

        int getContainerSlot();

    }

    interface INeedInputFuel<B extends BlockEntity, T extends IBeInv<B>> extends IBeInput<B, T> {

        ItemStack getFuel(B be, T beInv);

        int getFuelSlot();

    }

}
