
package com.rsdvlp.machinehud.mekanism.data;

import java.util.List;

/**
 * 1台の機械が持つ化学タンクの一覧。
 */
public record MekanismChemicalTanksHudData(
        List<MekanismChemicalTankHudData> tanks
) {

    public MekanismChemicalTanksHudData {
        // 呼び出し元によるリストの変更を防ぐ。
        tanks = List.copyOf(tanks);
    }
}