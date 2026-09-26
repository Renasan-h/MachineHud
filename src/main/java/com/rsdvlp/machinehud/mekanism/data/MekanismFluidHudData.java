package com.rsdvlp.machinehud.mekanism.data;

/**
 * Mekanismの液体タンクをHUDに表示するためのデータ。
 *
 * @param fluidId 液体のレジストリID。空の場合は空文字列
 * @param amount 現在の貯蔵量（mB）
 * @param capacity 最大容量（mB）
 */
public record MekanismFluidHudData(
        String fluidId,
        int amount,
        int capacity
) {
}