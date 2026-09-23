package com.rsdvlp.machinehud.mekanism.client;

import com.rsdvlp.machinehud.mekanism.network.MekanismHudSyncPayload;
import net.minecraft.core.BlockPos;

/**
 * サーバーから受信したMekanismのHUDデータを保持する。
 * 機械の座標も保存し、別の機械のデータを誤って表示しないようにする。
 */
public final class MekanismHudClientData {

    private static MekanismHudSyncPayload latestData;

    private MekanismHudClientData() {
    }

    /**
     * サーバーから受信した最新データを保存する。
     */
    public static void update(MekanismHudSyncPayload payload) {
        latestData = payload;
    }

    /**
     * 指定した座標の機械について、同期済みデータを取得する。
     * 未受信、または別の機械のデータであればnullを返す。
     */
    public static MekanismHudSyncPayload get(BlockPos pos) {

        if (latestData == null || !latestData.pos().equals(pos)) {
            return null;
        }

        return latestData;
    }

    /**
     * 監視終了時などに古いデータを破棄する。
     */
    public static void clear() {
        latestData = null;
    }
}