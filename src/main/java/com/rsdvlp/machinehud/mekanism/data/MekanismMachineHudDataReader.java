
package com.rsdvlp.machinehud.mekanism.data;

import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 登録された機械別ReaderからHUDデータを取得する。
 */
public final class MekanismMachineHudDataReader {

    private static final List<MekanismMachineReader> READERS =
            new ArrayList<>();

    private MekanismMachineHudDataReader() {
    }

    /**
     * 対応機械のReaderを登録する。
     */
    public static void register(MekanismMachineReader reader) {
        READERS.add(reader);
    }

    /**
     * 登録済みReaderが対応している機械か判定する。
     * データの取得は行わない。
     */
    public static boolean supports(BlockEntity blockEntity) {
        if (blockEntity == null) {
            return false;
        }

        for (MekanismMachineReader reader : READERS) {
            if (reader.supports(blockEntity)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 対応するReaderを探してHUDデータを取得する。
     */
    public static MekanismMachineHudData read(BlockEntity blockEntity) {
        if (blockEntity == null) {
            return null;
        }

        for (MekanismMachineReader reader : READERS) {
            if (reader.supports(blockEntity)) {
                return reader.read(blockEntity);
            }
        }

        return null;
    }
}