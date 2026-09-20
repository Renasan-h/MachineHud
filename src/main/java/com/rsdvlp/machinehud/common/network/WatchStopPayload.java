package com.rsdvlp.machinehud.common.network;

import com.rsdvlp.machinehud.MachineHUD;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * ClientがMachineHUDの監視を終了したことをServerへ通知するPayload。
 * Server側ではPlayerごとに監視対象を管理する予定なので、
 * STOP時にはBlockPosを送る必要はない。
 */
public record WatchStopPayload() implements CustomPacketPayload {

    public static final Type<WatchStopPayload> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath(
                            MachineHUD.MODID,
                            "watch_stop"
                    )
            );

    /**
     * WatchStopPayloadはデータを持たないため、
     * unit()で空のPayloadを表現する。
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, WatchStopPayload> STREAM_CODEC =
            StreamCodec.unit(
                    new WatchStopPayload()
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}