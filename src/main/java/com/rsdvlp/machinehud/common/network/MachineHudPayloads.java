package com.rsdvlp.machinehud.common.network;

import com.rsdvlp.machinehud.MachineHUD;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * MachineHUDで使用するPayloadを登録するクラス。
 * 今後WatchStopPayloadやMachineDataPayloadも
 * このクラスから登録する。
 */
public class MachineHudPayloads {
    private MachineHudPayloads() {
    }

    /**
     * MachineHUDのPayloadをneoforgeへ登録する。
     */
    public static void register(RegisterPayloadHandlersEvent event) {
        /*
         * MachineHUDのネットワークプロトコルのバージョン。
         * Payloadの互換性を壊す変更を行った場合には、将来的にこの値を経こうする。
         */
        PayloadRegistrar registrar = event.registrar("1");

        /*
         * WatchStartPayloadはClientからServerへだけ送信するので、
         * playToServerを利用。
         */
        registrar.playToServer(
                WatchStartPayload.TYPE,
                WatchStartPayload.STREAM_CODEC,
                MachineHudPayloads::handleWatchStart
        );

        /*
         * WatchStopPayloadもClientからServerへの一方向通信。
         * ClientがHUD対象を見るのをやめたことを通知する。
         */
        registrar.playToServer(
                WatchStopPayload.TYPE,
                WatchStopPayload.STREAM_CODEC,
                MachineHudPayloads::handleWatchStop
        );
    }

    /*
     * Clientが機械を見始めたときにServer側で呼ばれる。
     * 今は通信確認だけを行うため、受け取ったBlockPosをログへ出す。
     */
    private static void handleWatchStart(
            WatchStartPayload payload,
            IPayloadContext context
    ) {
        /*
         * playToServerで登録したPayloadのため、
         * 正常なゲーム通信では送信元はServerPlayerになる。
         */
        if (!(context.player() instanceof ServerPlayer player)) {
            return;
        }

        boolean started = MachineWatchManager.startWatching(
                player,
                payload.pos()
        );

        if (!started) {
            MachineHUD.LOGGER.debug(
                    "Rejected watch target: player={}, pos={}",
                    player.getName().getString(),
                    payload.pos()
            );
            return;
        }

        MachineHUD.LOGGER.info(
                "Started watching: player={}, pos={}",
                player.getName().getString(),
                payload.pos()
        );
    }

    /**
     * Clientが機械を見るのをやめたときにServer側で呼ばれる。
     * 現段階では通信確認のためログ出力だけを行う。
     */
    private static void handleWatchStop(
            WatchStopPayload payload,
            IPayloadContext context
    ) {
        if (!(context.player() instanceof ServerPlayer player)) {
            return;
        }

        // このPlayerの監視対象をServer側から削除する。
        MachineWatchManager.stopWatching(player);

        MachineHUD.LOGGER.info(
                "Stopped watching: player={}",
                player.getName().getString()
        );
    }
}
