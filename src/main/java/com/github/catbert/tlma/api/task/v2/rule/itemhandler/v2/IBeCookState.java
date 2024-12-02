package com.github.catbert.tlma.api.task.v2.rule.itemhandler.v2;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * 额外的烹饪条件
 */
public interface IBeCookState<B extends BlockEntity> {

    default boolean beIsSHeated(B be, EntityMaid maid) {
        return true;
    }

    default boolean beIsSEnoughTempera(B be, EntityMaid maid) {
        return true;
    }



    default boolean beIsAEnoughWater(B be, EntityMaid maid) {
        return true;
    }

    default boolean beIsAEnoughFuel(B be, EntityMaid maid) {
        return true;
    }



    enum StaticState {
        HEATED,
        TEMPERA,
        NONE;
    }

    enum ActionState {
        WATER,
        FUEL,
        NONE;
    }

}
