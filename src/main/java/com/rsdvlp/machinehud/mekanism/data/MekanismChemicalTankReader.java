
package com.rsdvlp.machinehud.mekanism.data;

import mekanism.api.MekanismAPI;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalTank;

/**
 * Mekanismの化学タンクをHUD用データに変換する。
 * 機械の種類には依存せず、IChemicalTankを受け取る。
 */
public final class MekanismChemicalTankReader {

    private MekanismChemicalTankReader() {
    }

    public static MekanismChemicalTankHudData read(
            String tankId,
            IChemicalTank tank
    ) {
        ChemicalStack stack = tank.getStack();

        if (stack.isEmpty()) {
            return new MekanismChemicalTankHudData(
                    tankId,
                    "",
                    0L,
                    tank.getCapacity()
            );
        }

        String chemicalId = MekanismAPI.CHEMICAL_REGISTRY
                .getKey(stack.getChemical())
                .toString();

        return new MekanismChemicalTankHudData(
                tankId,
                chemicalId,
                stack.getAmount(),
                tank.getCapacity()
        );
    }
}