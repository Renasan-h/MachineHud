package com.rsdvlp.machinehud.common.network;

import com.rsdvlp.machinehud.MachineHUD;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * MachineHUDを使用しているPlayerの監視対象を
 * Server側で管理する。
 * Clientから送られたBlockPosをそのまま実流量取得に使うのではなく、
 * Server側でPlayerごとの監視状態として保持するためのクラス。
 */
@EventBusSubscriber(modid = MachineHUD.MODID)
public final class MachineWatchManager {

    /**
     * Player UUID -> 現在監視しているBlockPos
     * ServerPlayerそのものをキーとして保持すると、
     * Player切断後も参照を保持してしまう可能性があるためUUIDを使用する。
     */
    private static final Map<UUID, BlockPos> WATCHED_POSITIONS = new HashMap<>();

    /**
     * ServerからMachineHUD用データを更新する間隔。
     * 20tick = 1秒なので、
     * 5tickなら0.25秒ごとの更新になる。
     */
    private static final int UPDATE_INTERVAL = 5;

    private MachineWatchManager() {
    }

    /**
     * Playerの監視対象を登録・更新する。
     * Clientから送られてきたBlockPosは信用せず、
     * Server側で監視可能な座標か確認してから登録する。
     *
     * @return 監視を開始できた場合true
     */
    public static boolean startWatching(
            ServerPlayer player,
            BlockPos pos
    ) {
        ServerLevel level = player.serverLevel();

        /*
         * Chunkがロードされていない座標は監視しない。
         *
         * ここでBlockEntity取得などを先に行わないことで、
         * HUD通信を理由に遠方Chunkをロードさせることを防ぐ。
         */
        if (!level.hasChunkAt(pos)) {
            return false;
        }

        /*
         * MachineHUDのClient側レイキャストは最大10ブロック。
         *
         * Server側でもそれを大きく超える座標を拒否する。
         * BlockPosの中心までの距離で判定する。
         */
        double maxDistance = 10.0;

        if (player.distanceToSqr(
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5
        ) > maxDistance * maxDistance) {
            return false;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);

        WatchTargetHandler handler =
                WatchTargetHandlers.findHandler(blockEntity);

        if (handler == null) {
            return false;
        }

        WATCHED_POSITIONS.put(
                player.getUUID(),
                pos.immutable()
        );

        return true;
    }

    /*
      Playerの監視を終了する。
     */
    public static void stopWatching(ServerPlayer player) {
        WATCHED_POSITIONS.remove(player.getUUID());
    }

    /*
      Playerが現在監視しているBlockPosを取得する。
      監視していない場合はnullを返す。
     */
    public static BlockPos getWatchedPos(ServerPlayer player) {
        return WATCHED_POSITIONS.get(player.getUUID());
    }

    /**
     * PlayerがServerから切断されたとき、
     * MachineHUDの監視情報も破棄する。
     * 切断時はClientからWatchStopPayloadを送れる保証がないため、
     * Server側でも必ず後始末する。
     */
    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        stopWatching(player);
    }

    /**
     * Server tickごとにMachineHUDの監視対象を処理する。
     * 実際の同期処理は毎tick行わず、
     * UPDATE_INTERVALごとにだけ実行する。
     */
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {

        /*
         * Server全体のtick数を利用して、
         * 指定した間隔以外では処理を終了する。
         */
        if (event.getServer().getTickCount() % UPDATE_INTERVAL != 0) {
            return;
        }

        /*
         * 現段階では監視処理の入口だけ作る。
         * 次の段階で、UUIDからServerPlayerを取得
         *   ↓
         * BlockPosからPumpBlockEntityを取得
         *   ↓
         * Server側データを取得
         *   ↓
         * Clientへ送信
         * という処理を追加する。
         */
        if (WATCHED_POSITIONS.isEmpty()) {
            return;
        }

        /*
         * Playerごとの監視対象を処理する。
         *
         * UUIDだけをMapに保存しているため、
         * 現在Serverに接続しているServerPlayerをここで取得する。
         */
        for (Map.Entry<UUID, BlockPos> entry : WATCHED_POSITIONS.entrySet()) {

            UUID playerId = entry.getKey();
            BlockPos pos = entry.getValue();

            ServerPlayer player =
                    event.getServer()
                            .getPlayerList()
                            .getPlayer(playerId);

            /*
             * Playerが既にServerからいなくなっている場合。
             *
             * 通常はPlayerLoggedOutEventで削除されるが、
             * tick処理側でもServerPlayerの存在を前提にしない。
             */
            if (player == null) {
                continue;
            }

            ServerLevel level = player.serverLevel();

            /*
             * START時にはロード済みだったChunkでも、
             * 監視中に状態が変わる可能性がある。
             *
             * MachineHUDのためだけにChunkをロードしない。
             */
            if (!level.hasChunkAt(pos)) {
                continue;
            }

            BlockEntity blockEntity =
                    level.getBlockEntity(pos);

            /*
             * このBlockEntityを担当するHandlerを
             * 共通レジストリから取得する。
             */
            WatchTargetHandler handler =
                    WatchTargetHandlers.findHandler(blockEntity);

            if (handler == null) {
                continue;
            }

            /*
             * MOD固有の処理はHandler側へ委譲する。
             * MachineWatchManagerはCreate/Mekanismの
             * BlockEntity型を知る必要がない。
             */
            handler.process(
                    player,
                    blockEntity
            );
        }
    }
}
