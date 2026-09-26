
package com.rsdvlp.machinehud.mekanism.data;

/**
 * 1つの化学タンクのHUD表示データ。
 *
 * @param tankId タンク識別子（input、outputなど）
 * @param chemicalId 化学素材のレジストリID
 * @param amount 現在量
 * @param capacity 最大容量
 */
public record MekanismChemicalTankHudData(
        String tankId,
        String chemicalId,
        long amount,
        long capacity
) {
}