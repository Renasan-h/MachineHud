package com.rsdvlp.machinehud.screen;

import com.rsdvlp.machinehud.hud.element.HudElement;
import com.rsdvlp.machinehud.hud.element.HudElementConfig;
import com.rsdvlp.machinehud.hud.element.HudElements;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

public class MachineHudConfigScreen extends Screen {

    // この設定画面を開く前に表示されていた画面。
    //
    // Mods画面などからMachineHUD設定画面を開いた場合、
    // Doneを押したときに元の画面へ戻るために保持しておく。
    private final Screen parent;
    // 設定一覧の現在のスクロール量。
    // 0が一番上で、値が大きいほど下へスクロールする。
    private int scrollOffset = 0;

    // 1行あたりの高さ。
    private static final int ROW_HEIGHT = 24;

    // 設定一覧を描画し始めるY座標。
    private static final int LIST_TOP = 50;

    // Doneボタンと重ならないようにするため、
    // 設定一覧を描画できる下端位置を保持する。
    private int getListBottom() {
        return this.height - 50;
    }


    public MachineHudConfigScreen(Screen parent) {

        // Screenクラスへ、この画面のタイトルを渡す。
        //
        // Component.literal()は翻訳キーを使用せず、
        // 指定した文字列をそのまま表示する。
        super(Component.literal("MachineHUD Settings"));

        // 設定画面を閉じたときに戻る画面を保存する。
        this.parent = parent;
    }


    @Override
    protected void init() {

        // 画面初期化時に、
        // 現在のスクロール位置に合わせてWidgetを生成する。
        rebuildWidgets();
    }


    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        // Minecraft標準の画面背景を描画する。
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        // 登録されているButtonなどのWidgetを描画する。
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        // 設定画面のタイトルを描画する。
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);

        // スクロール位置を反映した最初の行のY座標。
        int y = LIST_TOP - scrollOffset;

        for (HudElement element : HudElements.getOrderedElements()) {

            int rowY = y;

            // 行全体が表示領域内にある場合だけ描画する。
            boolean visible = isRowVisible(rowY);

            if (visible) {

                // 文字は20pxのボタンの中央付近に合わせるため、
                // rowYから少し下へずらして描画する。
                guiGraphics.drawString(this.font, element.getDisplayName(), this.width / 2 - 100, rowY + 6, 0xFFFFFF);
            }

            y += ROW_HEIGHT;
        }
        // 設定一覧全体の高さを計算する。
        int contentHeight = HudElements.getOrderedElements().size() * ROW_HEIGHT;

        // 画面上で設定一覧を表示できる高さ。
        int visibleHeight = getListBottom() - LIST_TOP;

        // 実際にスクロールが必要な場合だけ
        // スクロールバーを表示する。
        if (contentHeight > visibleHeight) {

            int barX = this.width / 2 + 110;

            // スクロール領域全体の高さ。
            int trackHeight = visibleHeight;

            // 表示領域が全体の何割かに応じて
            // スクロールバー本体の高さを決める。
            int thumbHeight = Math.max(20, visibleHeight * visibleHeight / contentHeight);

            int maxScroll = contentHeight - visibleHeight;

            // 現在のスクロール位置を、
            // スクロールバー上のY位置へ変換する。
            int thumbY = LIST_TOP + (int) ((double) scrollOffset / maxScroll * (trackHeight - thumbHeight));

            // スクロールバーの背景。
            guiGraphics.fill(barX, LIST_TOP, barX + 6, getListBottom(), 0xFF333333);

            // 現在位置を表すつまみ部分。
            guiGraphics.fill(barX, thumbY, barX + 6, thumbY + thumbHeight, 0xFFAAAAAA);
        }
    }

    @Override
    public void onClose() {

        // Minecraftに現在表示するScreenを指定する。
        //
        // parentを指定することで、
        // MachineHUD設定画面を開く前の画面へ戻る。
        if (this.minecraft != null) {
            this.minecraft.setScreen(parent);
        }
    }

    // 現在のscrollOffsetを使って、設定画面上のWidgetをすべて作り直す。
    @Override
    public void rebuildWidgets() {
        // スクロール前のWidgetを削除し、
        // 現在のスクロール位置を基準に再構築する。
        clearWidgets();

        int y = LIST_TOP - scrollOffset;

        for (HudElement element : HudElements.getOrderedElements()) {

            int rowY = y;

            // 表示領域からはみ出している行は
            // Widget自体を生成しない。
            if (isRowVisible(rowY)) {

                // HudElementに対応するConfigを
                // 共通のHudElementConfigから取得する。
                ModConfigSpec.BooleanValue enabledConfig =
                        HudElementConfig.getConfig(element);

                /*
                 * Configがまだ登録されていないHudElementの場合は、ボタンを生成しない。
                 * 将来HudElementだけ追加して、Config登録を忘れた場合などでも
                 * NullPointerExceptionを防げる。
                 */
                if (enabledConfig != null) {

                    addRenderableWidget(
                            Button.builder(
                                            getToggleText(enabledConfig),
                                            button -> {
                                                // 現在のON/OFF状態を反転する。
                                                enabledConfig.set(
                                                        !enabledConfig.get()
                                                );
                                                // ボタン上の表示も更新する。
                                                button.setMessage(
                                                        getToggleText(enabledConfig)
                                                );
                                            }
                                    )
                                    .bounds(
                                            this.width / 2 + 40,
                                            rowY,
                                            50,
                                            20
                                    )
                                    .build()
                    );
                }
            }

            y += ROW_HEIGHT;
        }

        // Doneボタンはスクロール対象ではないため、
        // 常に画面下へ固定する。
        addRenderableWidget(
                Button.builder(
                                Component.literal("Done"),
                                button -> onClose()
                        )
                        .bounds(
                                this.width / 2 - 100,
                                this.height - 30,
                                200,
                                20
                        )
                        .build()
        );
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {

        // HUD項目数から、一覧全体の高さを計算する。
        int contentHeight = HudElements.getOrderedElements().size() * ROW_HEIGHT;

        // 実際に設定一覧を表示できる高さ。
        int visibleHeight = getListBottom() - LIST_TOP;

        // 一覧が画面内に収まる場合は
        // スクロールする必要がない。
        int maxScroll = Math.max(0, contentHeight - visibleHeight);

        // scrollYが正なら上方向、
        // 負なら下方向へスクロールされる。
        //
        // 1回のホイール操作につき1行分移動させる。
        scrollOffset -= (int) (scrollY * ROW_HEIGHT);

        // 一番上より上へ行かないようにする。
        if (scrollOffset < 0) {
            scrollOffset = 0;
        }

        // 一番下を超えてスクロールしないようにする。
        if (scrollOffset > maxScroll) {
            scrollOffset = maxScroll;
        }

        // Widgetの位置もスクロール後の座標へ更新するため、
        // 一覧を作り直す。
        rebuildWidgets();

        return true;
    }

    // Boolean Configの現在値から
    // ON/OFFボタンに表示するComponentを生成する。
    private Component getToggleText(ModConfigSpec.BooleanValue config) {

        // trueならON、falseならOFFと表示する。
        return Component.literal(config.get() ? "ON" : "OFF");
    }

    private boolean isRowVisible(int rowY) {
        return rowY >= LIST_TOP && rowY + ROW_HEIGHT <= getListBottom();
    }
}