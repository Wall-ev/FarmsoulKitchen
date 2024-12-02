package com.github.catbert.tlma.api.task.v2.dis;

import com.github.catbert.tlma.api.task.v2.IBaseCook;
import com.github.catbert.tlma.api.task.v2.IBeInv;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface ICookRule {

    static <B extends BlockEntity> boolean output(B be, IBeInv<B> beInv, IBaseCook<B, ?> baseCook) {
        return beInv.getStackInSlot(be, baseCook.getOutputSlot()).isEmpty();
    }

}
