package com.rsdvlp.machinehud.hud.provider;

import com.rsdvlp.machinehud.hud.HudLine;
import com.rsdvlp.machinehud.hud.element.HudElement;

/**
 * 各MODのHUD情報生成処理を統一するためのインターフェース。
 * RendererはCreateの詳細を知らず、
 * HudProviderを通してHudLineを取得する。
 */
public interface HudProvider {

    /**
     * このProviderが指定されたHudElementを担当するか。
     */
    boolean supports(HudElement element);

    /**
     * HudElementから実際に描画するHudLineを生成する。
     * 表示できない場合はnullを返す。
     */
    HudLine createLine(HudElement element);
}
