package com.github.catbert.tlma.mixin.bnc;

import com.github.catbert.tlma.task.cook.v1.common.cbaccessor.IFdCbeAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;
import umpaz.brewinandchewin.common.crafting.KegRecipe;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;

import java.util.Optional;

@Mixin(value = KegBlockEntity.class, remap = false)
public abstract class MixinKegBlockEntity extends SyncedBlockEntity implements IFdCbeAccessor<KegRecipe> {

    public MixinKegBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Shadow
    protected abstract Optional<KegRecipe> getMatchingRecipe(RecipeWrapper inventoryWrapper);

    @Shadow protected abstract boolean canFerment(KegRecipe recipe, Level level);

    @Override
    public Optional<KegRecipe> getMatchingRecipe$tlma(RecipeWrapper inventoryWrapper) {
        return getMatchingRecipe(inventoryWrapper);
    }

    @Override
    public boolean canCook$tlma(KegRecipe recipe) {
        return canFerment(recipe, this.level);
    }
}
