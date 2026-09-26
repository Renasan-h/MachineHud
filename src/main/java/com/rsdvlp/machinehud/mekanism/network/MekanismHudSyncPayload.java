
package com.rsdvlp.machinehud.mekanism.network;

import com.rsdvlp.machinehud.MachineHUD;
import com.rsdvlp.machinehud.mekanism.data.MekanismChemicalTankHudData;
import com.rsdvlp.machinehud.mekanism.data.MekanismFluidHudData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
        int maxProgress,
        String chemicalId,
        long chemicalAmount,
        long chemicalCapacity,
        List<MekanismChemicalTankHudData> chemicalTanks,
        MekanismFluidHudData fluid
) implements CustomPacketPayload {

    private static final int MAX_CHEMICAL_TANKS = 16;

    public MekanismHudSyncPayload {
        chemicalTanks = List.copyOf(chemicalTanks);

        if (chemicalTanks.size() > MAX_CHEMICAL_TANKS) {
            throw new IllegalArgumentException(
                    "Too many chemical tanks: " + chemicalTanks.size()
            );
        }
    }

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
                // 化学タンクの同期情報
                buffer.writeUtf(payload.chemicalId());
                buffer.writeVarLong(payload.chemicalAmount());
                buffer.writeVarLong(payload.chemicalCapacity());
                // 新規フィールド：複数化学タンク
                buffer.writeVarInt(payload.chemicalTanks().size());
                for (MekanismChemicalTankHudData tank
                        : payload.chemicalTanks()) {
                    buffer.writeUtf(tank.tankId());
                    buffer.writeUtf(tank.chemicalId());
                    buffer.writeVarLong(tank.amount());
                    buffer.writeVarLong(tank.capacity());
                }

                // 液体タンクが存在するかを先に送信する。
                buffer.writeBoolean(payload.fluid() != null);

                if (payload.fluid() != null) {
                    MekanismFluidHudData fluid = payload.fluid();

                    buffer.writeUtf(fluid.fluidId());
                    buffer.writeVarInt(fluid.amount());
                    buffer.writeVarInt(fluid.capacity());
                }
            },
            buffer -> {
                BlockPos pos = BlockPos.STREAM_CODEC.decode(buffer);
                // storedEnergy
                long storedEnergy = buffer.readLong();
                // energyUsage
                long energyUsage = buffer.readLong();
                // progress
                int progress = buffer.readVarInt();
                // maxProgress
                int maxProgress = buffer.readVarInt();
                // chemicalId
                String chemicalId = buffer.readUtf();
                // chemicalAmount
                long chemicalAmount = buffer.readVarLong();
                // chemicalCapacity
                long chemicalCapacity = buffer.readVarLong();

                // 新規フィールド：複数化学タンク
                int tankCount = buffer.readVarInt();

                if (tankCount < 0
                        || tankCount > MAX_CHEMICAL_TANKS) {
                    throw new IllegalArgumentException(
                            "Invalid chemical tank count: " + tankCount
                    );
                }

                List<MekanismChemicalTankHudData> tanks =
                        new ArrayList<>(tankCount);

                for (int i = 0; i < tankCount; i++) {
                    tanks.add(
                            new MekanismChemicalTankHudData(
                                    buffer.readUtf(),
                                    buffer.readUtf(),
                                    buffer.readVarLong(),
                                    buffer.readVarLong()
                            )
                    );
                }

                MekanismFluidHudData fluid = null;

                boolean hasFluid = buffer.readBoolean();

                if (hasFluid) {
                    fluid = new MekanismFluidHudData(
                            buffer.readUtf(),
                            buffer.readVarInt(),
                            buffer.readVarInt()
                    );
                }

                return new MekanismHudSyncPayload(
                        pos,
                        storedEnergy,
                        energyUsage,
                        progress,
                        maxProgress,
                        chemicalId,
                        chemicalAmount,
                        chemicalCapacity,
                        tanks,
                        fluid
                );
            }
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}