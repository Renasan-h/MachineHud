package com.rsdvlp.machinehud.mekanism.data;

import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Mekanismの機械ごとのHUDデータ取得処理。
 * 各機械に対応するReaderを実装し、
 * 共通Readerに登録する。
 */
public interface MekanismMachineReader {

    /**
     * 指定されたBlockEntityに対応しているか。
     */
    boolean supports(BlockEntity blockEntity);

    /**
     * 対応する機械からHUDデータを取得する。
     */
    MekanismMachineHudData read(BlockEntity blockEntity);
}