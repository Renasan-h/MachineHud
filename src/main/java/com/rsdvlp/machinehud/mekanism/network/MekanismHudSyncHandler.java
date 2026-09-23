package com.rsdvlp.machinehud.mekanism.network;

import com.rsdvlp.machinehud.mekanism.client.MekanismHudClientData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * MekanismのHUD同期Payloadを受信するHandler。
 */
public final class MekanismHudSyncHandler {

    private MekanismHudSyncHandler() {
    }

    public static void handle(
            MekanismHudSyncPayload payload,
            IPayloadContext context
    ) {

        /*
         * クライアントのメインスレッドでデータを更新する。
         * HUD描画処理とのスレッド競合を避けるため。
         */
        context.enqueueWork(() -> MekanismHudClientData.update(payload));
    }
}