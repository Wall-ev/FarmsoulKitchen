package com.github.catbert.tlma.mixin.tlm;

import com.github.catbert.tlma.api.IAddonMaid;
import com.github.catbert.tlma.init.InitEffects;
import com.github.catbert.tlma.inventory.container.CookSettingContainer;
import com.github.catbert.tlma.util.FakePlayerUtil;
import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.ref.WeakReference;
import java.util.List;

import static com.github.catbert.tlma.item.bauble.BurnDamageManager.getBurnDamageTypes;

@Mixin(value = EntityMaid.class, remap = false)
public abstract class MixinEntityMaid extends TamableAnimal implements CrossbowAttackMob, IMaid, IAddonMaid {
    @Unique
    @SuppressWarnings("all")
    private static final EntityDataAccessor<CompoundTag> MaidAddon_DATA = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.COMPOUND_TAG);

    @Unique
    @SuppressWarnings("all")
    private static final EntityDataAccessor<Integer> SEARCHY_OFFSET_DATA = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.INT);

    @Unique
    @SuppressWarnings("all")
    private static final String MAID_ADDON_TAG = "MaidAddonData";

    @Unique
    @SuppressWarnings("all")
    private static final String SEARCHY_OFFSET_TAG = "SearchYOffset";

    @Unique
    @SuppressWarnings("all")
    private WeakReference<FakePlayer> fakePlayer;

    protected MixinEntityMaid(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(at = @At("TAIL"), remap = true, method = "defineSynchedData()V")
    private void registerData$tlma(CallbackInfo ci) {
//        CompoundTag compoundTag = new CompoundTag();
//        compoundTag.putInt(SEARCHY_OFFSET_TAG, 4);
        entityData.define(MaidAddon_DATA, new CompoundTag());
        entityData.define(SEARCHY_OFFSET_DATA, 4);
    }

    @Inject(at = @At("TAIL"), remap = true, method = "addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V")
    private void writeAdditional$tlma(CompoundTag compoundNBT, CallbackInfo ci) {
        CompoundTag addonMaidDat = getAddonMaidData$tlma();
        if (addonMaidDat != null) {
            compoundNBT.put(MAID_ADDON_TAG, addonMaidDat);
        }

        compoundNBT.putInt(SEARCHY_OFFSET_TAG, this.entityData.get(SEARCHY_OFFSET_DATA));
    }

    @Inject(at = @At("TAIL"), remap = true, method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V")
    private void readAdditional$tlma(CompoundTag compoundNBT, CallbackInfo ci) {
        if (compoundNBT.contains(MAID_ADDON_TAG)) {
            setAddonMaidData$tlma(compoundNBT.getCompound(MAID_ADDON_TAG));
        }

        if (compoundNBT.contains(SEARCHY_OFFSET_TAG, Tag.TAG_INT)) {
            this.entityData.set(SEARCHY_OFFSET_DATA, compoundNBT.getInt(SEARCHY_OFFSET_TAG));
        }
    }

    @Override
    public CompoundTag getAddonMaidData$tlma() {
        return entityData.get(MaidAddon_DATA);
    }

    @Override
    public void setAddonMaidData$tlma(CompoundTag nbt) {
        entityData.set(MaidAddon_DATA, nbt);
    }

    @Override
    public Integer getStartYOffset$tlma() {
        return this.entityData.get(SEARCHY_OFFSET_DATA);
//        ??为啥在MaidOverlay不能实时获取呢？
//        return getAddonMaidData().getInt(SEARCHY_OFFSET_TAG);
    }

    @Override
    public void setStartYOffset$tlma(int offset) {
        this.entityData.set(SEARCHY_OFFSET_DATA, offset);
//        getAddonMaidData().putInt(SEARCHY_OFFSET_TAG, offset);
    }

    @Override
    public @NotNull WeakReference<FakePlayer> getFakePlayer$tlma() {
        return fakePlayer;
    }

    @Override
    public void initFakePlayer$tlma() {
        if (fakePlayer == null) {
            this.fakePlayer = FakePlayerUtil.setupBeforeTrigger((ServerLevel) level(), this.getName().getString(), this);
        }
    }

    public boolean openMaidGuiFromSideTab(Player player, int tabIndex) {
        if (player instanceof ServerPlayer && !this.isSleeping()) {
            this.navigation.stop();
            NetworkHooks.openScreen((ServerPlayer) player, getGuiProviderFromSideTab(tabIndex), (buffer) -> buffer.writeInt(getId()));
        }
        return true;
    }

    public MenuProvider getGuiProviderFromSideTab(int tabIndex) {
        switch (tabIndex) {
            case 0:
                return CookSettingContainer.create(getId());
            default:
                return this.getMaidBackpackType().getGuiProvider(getId());
        }
    }
}
