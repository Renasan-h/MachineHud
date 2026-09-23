
package com.rsdvlp.machinehud.mekanism.network;

import com.rsdvlp.machinehud.common.network.WatchTargetHandler;
import com.rsdvlp.machinehud.mekanism.data.MekanismMachineHudData;
import com.rsdvlp.machinehud.mekanism.data.MekanismMachineHudDataReader;
import mekanism.common.tile.machine.TileEntityEnergizedSmelter;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * Mekanismの機械をサーバー側で監視するHandler。
 * MachineWatchManagerから5tickごとに呼び出される。
 */
public final class MekanismWatchTargetHandler
        implements WatchTargetHandler {

    @Override
    public boolean supports(BlockEntity blockEntity) {

        // 最初はEnergized Smelterのみ対応する。
        return blockEntity instanceof TileEntityEnergizedSmelter;
    }

    @Override
    public void process(
            ServerPlayer player,
            BlockEntity blockEntity
    ) {

        // サーバー側の最新データを取得する。
        MekanismMachineHudData data =
                MekanismMachineHudDataReader.read(blockEntity);

        if (data == null) {
            return;
        }

        // サーバーで取得した最新の加工進捗と消費量を、この機械を監視しているプレイヤーに送信する。
        MekanismHudSyncPayload payload =
                new MekanismHudSyncPayload(
                        blockEntity.getBlockPos(),
                        data.storedEnergy(),
                        data.energyUsage(),
                        data.progress(),
                        data.maxProgress()
                );

        PacketDistributor.sendToPlayer(player, payload);
    }
}