
package com.rsdvlp.machinehud.mekanism.data;

import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.tile.prefab.TileEntityElectricMachine;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Mekanismの単一入力・単一出力の電動加工機械に対応するReader。
 * 対応機械：
 * - Energized Smelter
 * - Enrichment Chamber
 * - Crusher
 */
public final class ElectricMachineReader
        implements MekanismMachineReader {

    @Override
    public boolean supports(BlockEntity blockEntity) {
        return blockEntity instanceof TileEntityElectricMachine;
    }

    @Override
    public MekanismMachineHudData read(BlockEntity blockEntity) {
        if (!supports(blockEntity)) {
            return null;
        }

        // 上記3機種はいずれもこの共通基底クラスを継承する。
        TileEntityElectricMachine machine =
                (TileEntityElectricMachine) blockEntity;

        MachineEnergyContainer<TileEntityElectricMachine> energy =
                machine.getEnergyContainer();

        boolean active = machine.getActive();

        // 稼働中のみ消費エネルギーを表示する。
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