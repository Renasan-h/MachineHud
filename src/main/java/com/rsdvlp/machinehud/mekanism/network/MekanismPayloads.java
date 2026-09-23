package com.rsdvlp.machinehud.mekanism.network;

import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * Mekanism専用Payloadの登録を担当する。
 */
public final class MekanismPayloads {

    private MekanismPayloads() {
    }

    public static void register(PayloadRegistrar registrar) {
        registrar.playToClient(
                MekanismHudSyncPayload.TYPE,
                MekanismHudSyncPayload.STREAM_CODEC,
                MekanismHudSyncHandler::handle
        );
    }
}