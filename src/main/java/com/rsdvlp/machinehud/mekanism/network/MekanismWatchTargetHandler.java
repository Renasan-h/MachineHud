
package com.rsdvlp.machinehud.mekanism.network;

import com.rsdvlp.machinehud.common.network.WatchTargetHandler;
import com.rsdvlp.machinehud.mekanism.data.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

/**
 * Mekanismの機械をサーバー側で監視するHandler。
 * MachineWatchManagerから5tickごとに呼び出される。
 */
public final class MekanismWatchTargetHandler
        implements WatchTargetHandler {

    @Override
    public boolean supports(BlockEntity blockEntity) {
        return blockEntity != null
                && MekanismMachineHudDataReader.supports(blockEntity);
    }

    @Override
    public void process(
            ServerPlayer player,
            BlockEntity blockEntity
    ) {
        // 既存のエネルギー・加工進捗を取得する。
        MekanismMachineHudData data =
                MekanismMachineHudDataReader.read(blockEntity);

        if (data == null) {
            return;
        }

        // 既存の単一化学タンク情報
        MekanismChemicalHudData chemical =
                MekanismChemicalReader.read(blockEntity);

        // 新規：複数化学タンク情報
        MekanismChemicalTanksHudData multiple =
                PressurizedReactionChemicalReader.read(blockEntity);

        List<MekanismChemicalTankHudData> tanks =
                multiple != null
                        ? multiple.tanks()
                        : List.of();
        // 加圧反応室の入力液体タンクを取得する。
        // それ以外の機械ではnullになる。
        MekanismFluidHudData fluid =
                PressurizedReactionFluidReader.read(blockEntity);

        MekanismHudSyncPayload payload =
                new MekanismHudSyncPayload(
                        // 基礎情報
                        blockEntity.getBlockPos(),
                        data.storedEnergy(),
                        data.energyUsage(),
                        data.progress(),
                        data.maxProgress(),

                        // 単一化学タンク
                        chemical != null ? chemical.chemicalId() : "",
                        chemical != null ? chemical.amount() : 0L,
                        chemical != null ? chemical.capacity() : 0L,
                        // 複数化学タンク
                        tanks,
                        // 液体タンク
                        fluid
                );

        PacketDistributor.sendToPlayer(player, payload);
    }
}