package com.rsdvlp.machinehud.screen;

import com.rsdvlp.machinehud.common.config.ClientConfig;
import com.rsdvlp.machinehud.common.config.ConfigTab;
import com.rsdvlp.machinehud.common.config.EnergyDisplayUnit;
import com.rsdvlp.machinehud.common.hud.HudGroup;
import com.rsdvlp.machinehud.common.hud.element.HudElement;
import com.rsdvlp.machinehud.common.hud.element.HudElementConfig;
import com.rsdvlp.machinehud.common.hud.element.HudElements;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MachineHudConfigScreen extends Screen {

    // Doneを押したときに元の画面へ戻るために保持しておく。
    private final Screen parent;
    // 設定一覧の現在のスクロール位置。
    private int scrollOffset = 0;

    // 画面上部のタブ。
    private static final int TAB_TOP = 38;
    // 画面上部のタブ幅。
    private static final int TAB_HEIGHT = 20;

    // タブの下に余白を設けて設定一覧を開始する。
    private static final int LIST_TOP = 76;

    private static final int ROW_HEIGHT = 24;

    // 現在選択されている設定タブ。
    private ConfigTab selectedTab = ConfigTab.MACHINEHUD;

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

        List<ConfigScreenRow> rows = getScreenRows();

        for (int i = 0; i < rows.size(); i++) {
            ConfigScreenRow row = rows.get(i);
            int rowY = getRowY(i);

            if (!isRowVisible(rowY)) {
                continue;
            }

            switch (row.type()) {
                case HEADER -> {
                    // 見出しはrebuildWidgets()で生成したボタンが描画する。
                }

                case HUD_ELEMENT -> guiGraphics.drawString(
                        this.font,
                        Component.translatable(
                                row.element().getDisplayName()
                        ),
                        this.width / 2 - 95,
                        rowY + 6,
                        0xFFFFFF
                );

                case ENERGY_UNIT -> guiGraphics.drawString(
                        this.font,
                        Component.literal("Energy Unit"),
                        this.width / 2 - 95,
                        rowY + 6,
                        0xFFFFFF
                );
            }
        }

        // 設定一覧全体の高さを計算する。
        int contentHeight = rows.size() * ROW_HEIGHT;
        // 画面上で設定一覧を表示できる高さ。
        int visibleHeight = getVisibleRowCount() * ROW_HEIGHT;

        // 実際にスクロールが必要な場合だけスクロールバーを表示する。
        if (contentHeight > visibleHeight) {
            int barX = this.width / 2 + 110;
            int trackHeight = visibleHeight;

            int thumbHeight = Math.max(
                    20,
                    visibleHeight * visibleHeight / contentHeight
            );

            int maxScroll = getMaxScroll();

            int thumbY = LIST_TOP
                    + (int) (
                    (double) scrollOffset / maxScroll
                            * (trackHeight - thumbHeight)
            );

            guiGraphics.fill(
                    barX,
                    LIST_TOP,
                    barX + 6,
                    LIST_TOP + trackHeight,
                    0xFF333333
            );

            guiGraphics.fill(
                    barX,
                    thumbY,
                    barX + 6,
                    thumbY + thumbHeight,
                    0xFFAAAAAA
            );
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

    @Override
    public void rebuildWidgets() {
        clearWidgets();

        // タブはスクロール対象外として常に表示する。
        addTabButtons();

        List<ConfigScreenRow> rows = getScreenRows();

        for (int i = 0; i < rows.size(); i++) {
            ConfigScreenRow row = rows.get(i);
            int rowY = getRowY(i);

            if (!isRowVisible(rowY)) {
                continue;
            }

            switch (row.type()) {
                case HEADER -> {
                    HudGroup group = row.group();

                    String marker = ConfigScreenState.isCollapsed(group)
                            ? "▶ "
                            : "▼ ";

                    addRenderableWidget(
                            Button.builder(
                                            Component.literal(marker).append(
                                                    Component.translatable(
                                                            group.getDisplayName()
                                                    )
                                            ),
                                            button -> toggleGroup(group)
                                    )
                                    .bounds(
                                            this.width / 2 - 105,
                                            rowY,
                                            200,
                                            20
                                    )
                                    .build()
                    );
                }

                case HUD_ELEMENT -> {
                    ModConfigSpec.BooleanValue enabledConfig =
                            HudElementConfig.getConfig(row.element());

                    if (enabledConfig != null) {
                        addRenderableWidget(
                                Button.builder(
                                                getToggleText(enabledConfig),
                                                button -> {
                                                    enabledConfig.set(
                                                            !enabledConfig.get()
                                                    );

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

                case ENERGY_UNIT -> addRenderableWidget(
                        Button.builder(
                                        getEnergyUnitText(),
                                        button -> {
                                            EnergyDisplayUnit current =
                                                    ClientConfig.MEKANISM_ENERGY_UNIT.get();

                                            EnergyDisplayUnit next =
                                                    current == EnergyDisplayUnit.J
                                                            ? EnergyDisplayUnit.FE
                                                            : EnergyDisplayUnit.J;

                                            ClientConfig.MEKANISM_ENERGY_UNIT.set(next);

                                            button.setMessage(
                                                    getEnergyUnitText()
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
    public boolean mouseScrolled(
            double mouseX,
            double mouseY,
            double scrollX,
            double scrollY
    ) {
        if (scrollY == 0) {
            return false;
        }

        // 1行単位でスクロールする。
        int direction = scrollY > 0 ? -1 : 1;

        scrollOffset = Math.max(
                0,
                Math.min(
                        getMaxScroll(),
                        scrollOffset + direction * ROW_HEIGHT
                )
        );

        rebuildWidgets();
        return true;
    }

    /**
     * カテゴリ見出しと追加設定を含む総行数。
     */
    private int getTotalRowCount() {
        return getScreenRows().size();
    }

    // Boolean Configの現在値から
    // ON/OFFボタンに表示するComponentを生成する。
    private Component getToggleText(ModConfigSpec.BooleanValue config) {

        // trueならON、falseならOFFと表示する。
        return Component.literal(config.get() ? "ON" : "OFF");
    }

    /**
     * スクロール領域に完全に表示できる行数。
     */
    private int getVisibleRowCount() {
        return Math.max(
                1,
                (getListBottom() - LIST_TOP) / ROW_HEIGHT
        );
    }

    /**
     * 最後の行までスクロールできる最大位置。
     * ROW_HEIGHT単位にすることで、最終行が途中で切れるのを防ぐ。
     */
    private int getMaxScroll() {
        return Math.max(
                0,
                getTotalRowCount() - getVisibleRowCount()
        ) * ROW_HEIGHT;
    }

    /**
     * 指定した行番号の画面上のY座標。
     */
    private int getRowY(int index) {
        return LIST_TOP + index * ROW_HEIGHT - scrollOffset;
    }

    private boolean isRowVisible(int rowY) {
        return rowY >= LIST_TOP
                && rowY + ROW_HEIGHT <= getListBottom();
    }

    // 現在選択されているエネルギー単位をボタンに表示する。
    private Component getEnergyUnitText() {
        return Component.literal(
                ClientConfig.MEKANISM_ENERGY_UNIT.get().name()
        );
    }

    /**
     * 導入済みMODに対応するタブボタンを生成する。
     */
    private void addTabButtons() {
        var tabs = ConfigTab.getAvailableTabs();

        int tabWidth = 100;
        int gap = 4;

        // タブ全体を画面中央に配置する。
        int totalWidth = tabs.size() * tabWidth
                + (tabs.size() - 1) * gap;

        int startX = (this.width - totalWidth) / 2;

        for (int i = 0; i < tabs.size(); i++) {
            ConfigTab tab = tabs.get(i);

            addRenderableWidget(
                    Button.builder(
                                    Component.literal(
                                            tab == selectedTab
                                                    ? "[" + tab.getDisplayName() + "]"
                                                    : tab.getDisplayName()
                                    ),
                                    button -> {
                                        // タブ変更時はスクロール位置を先頭に戻す。
                                        selectedTab = tab;
                                        scrollOffset = 0;

                                        // 選択状態と設定項目を再描画する。
                                        rebuildWidgets();
                                    }
                            )
                            .bounds(
                                    startX + i * (tabWidth + gap),
                                    TAB_TOP,
                                    tabWidth,
                                    TAB_HEIGHT
                            )
                            .build()
            );
        }
    }

    /**
     * 現在選択されているタブに属するHUD項目を取得する。
     * IDの文字列ではなくHudGroupで判定するため、既存のIDを変更する必要がない。
     */
    private List<HudElement> getVisibleElements() {
        return HudElements.getOrderedElements()
                .stream()
                .filter(element -> {
                    String groupName = element.getHudGroup().name();

                    return switch (selectedTab) {
                        case MACHINEHUD -> !groupName.startsWith("CREATE_")
                                && !groupName.startsWith("MEKANISM_");

                        case CREATE -> groupName.startsWith("CREATE_");

                        case MEKANISM -> groupName.startsWith("MEKANISM_");
                    };
                })
                .toList();
    }

    /**
     * 選択中のタブに表示する行一覧を生成する。
     * HUD項目をグループごとにまとめ、各グループの先頭に見出しを追加する。
     */
    private List<ConfigScreenRow> getScreenRows() {
        Map<HudGroup, List<HudElement>> grouped =
                new LinkedHashMap<>();

        // 現在のHUD表示順を基準にグループ化する。
        for (HudElement element : getVisibleElements()) {
            grouped.computeIfAbsent(
                    element.getHudGroup(),
                    ignored -> new ArrayList<>()
            ).add(element);
        }

        List<ConfigScreenRow> rows = new ArrayList<>();

        for (Map.Entry<HudGroup, List<HudElement>> entry
                : grouped.entrySet()) {

            HudGroup group = entry.getKey();

            // 見出しは折りたたみ中も表示する。
            rows.add(ConfigScreenRow.header(group));

            // 折りたたまれている場合は、設定項目を追加しない。
            if (ConfigScreenState.isCollapsed(group)) {
                continue;
            }

            for (HudElement element : entry.getValue()) {
                rows.add(ConfigScreenRow.hudElement(element));
            }

            // エネルギー単位もEnergyカテゴリと一緒に折りたたむ。
            if (selectedTab == ConfigTab.MEKANISM
                    && group == HudGroup.MEKANISM_ENERGY) {
                rows.add(ConfigScreenRow.energyUnit());
            }
        }

        return rows;
    }

    /**
     * カテゴリの開閉状態を切り替える。
     */
    private void toggleGroup(HudGroup group) {
        ConfigScreenState.toggle(group);

        // 折りたたみによる行数の変化に合わせて補正する。
        scrollOffset = Math.min(scrollOffset, getMaxScroll());

        rebuildWidgets();
    }
}