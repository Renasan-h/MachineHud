
package com.rsdvlp.machinehud.mekanism.data;

/**
 * Mekanismの電力機械から取得したHUD表示用データ。
 * 取得処理と描画処理を分離するため、
 * このrecordはMekanismのTileEntityを直接保持しない。
 *
 * @param storedEnergy 現在蓄積されているエネルギー（J）
 * @param maxEnergy 最大エネルギー容量（J）
 * @param energyUsage 1tickあたりのエネルギー消費量（J/t）
 * @param progress 現在の処理進捗
 * @param maxProgress 処理完了までの最大進捗
 * @param state 機械の動作状態
 */
public record MekanismMachineHudData(
        long storedEnergy,
        long maxEnergy,
        long energyUsage,
        int progress,
        int maxProgress,
        State state
) {

    /**
     * HUD上で表示する機械の動作状態。
     */
    public enum State {
        // 加工処理を実行中。
        RUNNING,

        // 加工処理を行っていない。
        IDLE,

        // 出力先が満杯などで加工結果を出力できない。
        OUTPUT_BLOCKED
    }
}