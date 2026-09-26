package com.rsdvlp.machinehud.mekanism.data;

/**
 * Mekanismの化学タンクに格納された素材の情報。
 *
 * @param chemicalId 化学素材の識別子
 * @param amount 現在の貯蔵量
 * @param capacity 最大容量
 */
public record MekanismChemicalHudData(
        String chemicalId,
        long amount,
        long capacity
) {
}