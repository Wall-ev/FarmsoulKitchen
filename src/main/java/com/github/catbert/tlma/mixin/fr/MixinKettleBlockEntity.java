package com.github.catbert.tlma.mixin.fr;

import com.github.catbert.tlma.task.cook.v1.common.cbaccessor.IFdCbeAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import umpaz.farmersrespite.common.block.entity.KettleBlockEntity;
import umpaz.farmersrespite.common.crafting.KettleRecipe;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;

import java.util.Optional;

@Mixin(value = KettleBlockEntity.class, remap = false)
public abstract class MixinKettleBlockEntity extends SyncedBlockEntity implements IFdCbeAccessor<KettleRecipe> {
    @Shadow protected abstract Optional<KettleRecipe> getMatchingRecipe(RecipeWrapper inventoryWrapper);

    @Shadow protected abstract boolean canBrew(KettleRecipe recipe, KettleBlockEntity kettle);

    public MixinKettleBlockEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Override
    public Optional<KettleRecipe> getMatchingRecipe$tlma(RecipeWrapper inventoryWrapper) {
        return getMatchingRecipe(inventoryWrapper);
    }

    @Override
    public boolean canCook$tlma(KettleRecipe recipe) {
        return canBrew(recipe, ((KettleBlockEntity) (Object) this));
    }
}
