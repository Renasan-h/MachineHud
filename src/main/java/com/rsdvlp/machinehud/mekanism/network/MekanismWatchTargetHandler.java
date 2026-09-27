
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
        MekanismMachineHudData data = MekanismMachineHudDataReader.read(blockEntity);

        if (data == null) {
            return;
        }

        // 既存の単一化学タンク情報
        MekanismChemicalHudData chemical = MekanismChemicalReader.read(blockEntity);

        /*
         * 複数の化学タンクを持つ機械からデータを取得する。
         * 加圧反応室： input / output
         * 電解分離機： left / right
         */
        MekanismChemicalTanksHudData multiple =
                PressurizedReactionChemicalReader.read(blockEntity);

        if (multiple == null) {
            multiple = ElectrolyticSeparatorChemicalReader.read(blockEntity);
        }

        List<MekanismChemicalTankHudData> tanks =
                multiple != null
                        ? multiple.tanks()
                        : List.of();

        // 加圧反応室の入力液体を取得する。
        MekanismFluidHudData fluid = PressurizedReactionFluidReader.read(blockEntity);

        // 電解分離機の場合は、こちらのReaderで取得する。
        if (fluid == null) {
            fluid = ElectrolyticSeparatorFluidReader.read(blockEntity);
        }

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