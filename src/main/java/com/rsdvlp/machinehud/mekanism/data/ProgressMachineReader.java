package com.rsdvlp.machinehud.mekanism.data;

import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.tile.machine.TileEntityMetallurgicInfuser;
import mekanism.common.tile.prefab.TileEntityAdvancedElectricMachine;
import mekanism.common.tile.prefab.TileEntityProgressMachine;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Mekanismの加工進捗を持つ機械向けReader。
 * 対応機械：
 * - Metallurgic Infuser
 * - AdvancedElectricMachine系
 */
public final class ProgressMachineReader
        implements MekanismMachineReader {

    @Override
    public boolean supports(BlockEntity blockEntity) {
        return blockEntity instanceof TileEntityMetallurgicInfuser
                || blockEntity instanceof TileEntityAdvancedElectricMachine;
    }

    @Override
    public MekanismMachineHudData read(BlockEntity blockEntity) {
        if (!supports(blockEntity)) {
            return null;
        }

        TileEntityProgressMachine<?> machine =
                (TileEntityProgressMachine<?>) blockEntity;

        // 機械系列ごとにエネルギーコンテナを取得する。
        MachineEnergyContainer<?> energy;

        if (blockEntity instanceof TileEntityMetallurgicInfuser infuser) {
            energy = infuser.getEnergyContainer();
        } else if (blockEntity instanceof TileEntityAdvancedElectricMachine advanced) {
            energy = advanced.getEnergyContainer();
        } else {
            return null;
        }

        boolean active = machine.getActive();

        // 稼働していない場合は消費量を0にする。
        long energyUsage = active
                ? energy.getEnergyPerTick()
                : 0L;

        MekanismMachineHudData.State state = active
                ? MekanismMachineHudData.State.RUNNING
                : MekanismMachineHudData.State.IDLE;

        return new MekanismMachineHudData(
                energy.getEnergy(),
                energy.getMaxEnergy(),
                energyUsage,
                machine.getOperatingTicks(),
                machine.getTicksRequired(),
                state
        );
    }
}