package com.rsdvlp.machinehud.hud.element;

import com.rsdvlp.machinehud.config.ClientConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * MachineHUDで使用するすべてのHudElementを管理するクラス。
 * Common / Create、
 * HudElementの実装が複数に分かれていても
 * RendererやConfig側から一元的に扱えるようにする。
 */
public final class HudElements {

    /**
     * 登録されているすべてのHUD項目。
     */
    private static final List<HudElement> ALL_ELEMENTS;

    static {

        List<HudElement> elements = new ArrayList<>();

        // MachineHUD共通
        Collections.addAll(
                elements,
                CommonHudElement.values()
        );

        // Create
        Collections.addAll(
                elements,
                CreateHudElement.values()
        );

        ALL_ELEMENTS = Collections.unmodifiableList(elements);
    }

    private HudElements() {
    }

    /**
     * MachineHUDに登録されている
     * すべてのHUD項目を取得する。
     */
    public static List<HudElement> getAll() {

        return ALL_ELEMENTS;
    }

    /**
     * Configなどに保存されているIDから
     * HudElementを取得する。
     * 見つからない場合はnullを返す。
     */
    public static HudElement fromId(String id) {

        for (HudElement element : ALL_ELEMENTS) {

            if (element.getId().equals(id)) {
                return element;
            }
        }

        return null;
    }

    /**
     * Configに保存された表示順を基準に、
     * MachineHUDで使用するHUD項目の表示順を生成する。
     * 古いConfigに存在しない新規項目が追加された場合は、
     * 自動的に末尾へ追加する。
     */
    public static List<HudElement> getOrderedElements() {

        List<HudElement> elements =
                new ArrayList<>();

        /*
         * Configに保存されている順番を読み込む。
         */
        for (String id : ClientConfig.DISPLAY_ORDER.get()) {

            HudElement element = fromId(id);

            // 不正なIDを無視する。
            // 同じIDがConfigに複数存在しても重複させない。
            if (element != null && !elements.contains(element)) {
                elements.add(element);
            }
        }

        /*
         * 新しく追加されたHudElementを補完する。
         * 既存Configに保存されている項目の順番は変更しない。
         * 新しい項目についてはALL_ELEMENTSの標準順を基準に、
         * 直前に存在する既存項目の後ろへ挿入する。
         * これにより、MOD更新で新しいHUD項目が追加されても
         * すべて末尾へ移動してしまうことを防ぐ。
         */
        for (int standardIndex = 0; standardIndex < ALL_ELEMENTS.size(); standardIndex++) {

            HudElement newElement = ALL_ELEMENTS.get(standardIndex);

            // すでにConfigから追加されている項目は
            // その位置をそのまま維持する。
            if (elements.contains(newElement)) {
                continue;
            }

            /*
             * 標準順でnewElementより前にある要素のうち、
             * 現在の表示リストにも存在する一番近い要素を探す。
             */
            int insertIndex = 0;

            for (int previousIndex = standardIndex - 1; previousIndex >= 0; previousIndex--) {

                HudElement previousElement = ALL_ELEMENTS.get(previousIndex);

                int currentIndex = elements.indexOf(previousElement);

                if (currentIndex >= 0) {
                    // 見つけた直前要素の後ろへ挿入する。
                    insertIndex = currentIndex + 1;
                    break;
                }
            }

            elements.add(insertIndex, newElement);
        }

        return elements;
    }
}