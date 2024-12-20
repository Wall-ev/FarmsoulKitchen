package com.github.wallev.maidsoulkitchen.network.message;

import com.github.wallev.maidsoulkitchen.item.ItemCulinaryHub;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ToggleCookBagGuiSideTabMessage(int tabId) {
    public static void encode(ToggleCookBagGuiSideTabMessage message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.tabId);
    }

    public static ToggleCookBagGuiSideTabMessage decode(FriendlyByteBuf buf) {
        return new ToggleCookBagGuiSideTabMessage(buf.readVarInt());
    }

    public static void handle(ToggleCookBagGuiSideTabMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender == null) {
                    return;
                }
                ItemCulinaryHub.openCookBagGuiFromSideTab(sender, message.tabId);
            });
        }
        context.setPacketHandled(true);
    }
}