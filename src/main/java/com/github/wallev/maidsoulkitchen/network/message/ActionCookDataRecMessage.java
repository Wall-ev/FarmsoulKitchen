package com.github.wallev.maidsoulkitchen.network.message;

import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.data.TaskDataRegister;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ActionCookDataRecMessage(int entityId, ResourceLocation dataKey, String rec, String mode) {

    public static void encode(ActionCookDataRecMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        buf.writeResourceLocation(message.dataKey);
        buf.writeUtf(message.rec);
        buf.writeUtf(message.mode);
    }

    public static ActionCookDataRecMessage decode(FriendlyByteBuf buf) {
        return new ActionCookDataRecMessage(buf.readInt(), buf.readResourceLocation(), buf.readUtf(), buf.readUtf());
    }

    public static void handle(ActionCookDataRecMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                Entity entity = sender.level.getEntity(message.entityId);
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                    TaskDataKey<CookData> value = TaskDataRegister.getValue(message.dataKey);
                    CookData cookData = maid.getOrCreateData(value, new CookData());
                    cookData.addOrRemoveRec(message.rec, message.mode);
                    maid.setAndSyncData(value, cookData);
                }
            });
        }
        context.setPacketHandled(true);
    }
}
