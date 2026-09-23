package com.rsdvlp.machinehud.create;

import com.rsdvlp.machinehud.common.hud.element.HudElements;
import com.rsdvlp.machinehud.common.hud.provider.HudProviders;
import com.rsdvlp.machinehud.common.network.WatchTargetHandlers;
import com.rsdvlp.machinehud.create.config.CreateHudElementConfig;
import com.rsdvlp.machinehud.create.element.CreateHudElement;
import com.rsdvlp.machinehud.create.network.CreateWatchTargetHandler;
import com.rsdvlp.machinehud.create.provider.CreateHudProviders;

/**
 * MachineHUDのCreate連携を初期化する。
 * Create固有クラスへの参照をこのクラスへ集約し、
 * MachineHUD本体とCreate実装の境界を明確にする。
 */
public final class CreateInitialize {

    private CreateInitialize() {
    }

    /**
     * Create連携で使用する各実装をcommon側へ登録する。
     * このメソッドはCreateが導入されている場合にのみ呼び出す。
     */
    public static void initialize() {

        // Create固有のHUD要素を登録する。
        HudElements.register(CreateHudElement.values());

        // Create用HudProvider Factoryを登録する。
        HudProviders.register(CreateHudProviders::create);

        // Create用Server監視Handlerを登録する。
        WatchTargetHandlers.register(new CreateWatchTargetHandler());
    }

    /**
     * CreateのHUD要素とClientConfigを対応付ける。
     * すべてのMODのHUD要素を登録した後に呼び出す。
     */
    public static void registerConfig() {
        CreateHudElementConfig.register();
    }
}