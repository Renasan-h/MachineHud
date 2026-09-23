
package com.rsdvlp.machinehud.mekanism.network;

import com.rsdvlp.machinehud.MachineHUD;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * Mekanismの機械から取得した最新データをサーバーからクライアントへ送信するPayload。
 * BlockPosを含めることで、別の機械に照準を移した際に
 * 古い機械のデータを誤って表示しないようにする。
 */
public record MekanismHudSyncPayload(
        BlockPos pos,
        long storedEnergy,
        long energyUsage,
        int progress,
        int maxProgress
) implements CustomPacketPayload {

    public static final Type<MekanismHudSyncPayload> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath(
                            MachineHUD.MODID,
                            "mekanism_hud_sync"
                    )
            );

    /**
     * Payloadの各フィールドをネットワークへ書き込み、
     * 受信側で同じ順序で復元する。
     */
    public static final StreamCodec<
            RegistryFriendlyByteBuf,
            MekanismHudSyncPayload
            > STREAM_CODEC = StreamCodec.of(
            (buffer, payload) -> {
                BlockPos.STREAM_CODEC.encode(buffer, payload.pos());
                buffer.writeLong(payload.storedEnergy());
                buffer.writeLong(payload.energyUsage());
                buffer.writeVarInt(payload.progress());
                buffer.writeVarInt(payload.maxProgress());
            },
            buffer -> new MekanismHudSyncPayload(
                    BlockPos.STREAM_CODEC.decode(buffer),
                    // storedEnergy
                    buffer.readLong(),
                    // energyUsage
                    buffer.readLong(),
                    // progress
                    buffer.readVarInt(),
                    // maxProgress
                    buffer.readVarInt()
            )
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}