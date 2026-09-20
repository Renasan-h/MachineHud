package com.rsdvlp.machinehud.common.network;

import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * MachineHUDで使用するWatchTargetHandlerを管理するレジストリ。
 * common側はCreateやMekanismの具体的なHandlerを知らず、
 * 各MOD側から登録されたHandlerだけを保持する。
 */
public final class WatchTargetHandlers {

    private static final List<WatchTargetHandler> HANDLERS =
            new ArrayList<>();

    private WatchTargetHandlers() {
    }

    /**
     * MOD固有のWatchTargetHandlerを登録する。
     *
     * @param handler 登録するHandler
     */
    public static void register(WatchTargetHandler handler) {
        HANDLERS.add(handler);
    }

    /**
     * 登録されているHandler一覧を取得する。
     * 呼び出し側から一覧そのものを変更されないよう、
     * 読み取り専用のListとして返す。
     */
    public static List<WatchTargetHandler> getHandlers() {
        return Collections.unmodifiableList(HANDLERS);
    }

    /**
     * 指定されたBlockEntityを処理できるHandlerを取得する。
     * 対応Handlerが存在しない場合はnullを返す。
     */
    public static WatchTargetHandler findHandler(BlockEntity blockEntity) {

        for (WatchTargetHandler handler : HANDLERS) {

            if (handler.supports(blockEntity)) {
                return handler;
            }
        }

        return null;
    }
}