package com.rsdvlp.machinehud.network;

import com.rsdvlp.machinehud.MachineHUD;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * ClientがHUDで機械を見始めたことをServerへ通知するPayload。
 */
public record WatchStartPayload(BlockPos pos) implements CustomPacketPayload {
    public static final Type<WatchStartPayload> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath(
                            MachineHUD.MODID,
                            "watch_start"
                    )
            );

    /**
     * BlockPosをネットワークへ書き込み、
     * 受信側でWatchStartPayloadへ復元するCodec。
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, WatchStartPayload> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    WatchStartPayload::pos,
                    WatchStartPayload::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type(){
        return TYPE;
    }
}
