package com.rsdvlp.machinehud.hud;

import com.rsdvlp.machinehud.hud.element.HudElement;
import com.rsdvlp.machinehud.hud.element.HudElementConfig;
import com.rsdvlp.machinehud.hud.element.HudElements;
import com.rsdvlp.machinehud.hud.provider.HudProvider;
import com.rsdvlp.machinehud.hud.provider.HudProviders;
import com.rsdvlp.machinehud.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;


/**
 * MachineHUDのメイン描画処理を担当するクラス。
 * <p>
 * MachineHUDClientではHUDレイヤーの登録だけを行い、
 * 実際の照準判定・情報取得・描画はこのクラスで行う。
 */
public final class MachineHudRenderer {

    // HUDを描画するX座標。
    private static final int HUD_X = 10;

    // HUDを描画するY座標。
    private static final int HUD_Y = 10;

    // HUDの各項目同士が重ならないようにするための行間。
    private static final int LINE_HEIGHT = 12;

    // MachineHUDが独自にブロックを検索する最大距離。
    private static final double MAX_DISTANCE = 10.0;

    // HUDパネル内部の余白。
    private static final int PANEL_PADDING = 6;

    // 子項目を右へずらす量。
    private static final int INDENT_WIDTH = 10;

    // HUDパネルの半透明背景色。
    // 先頭のCCが透明度、残りの000000が黒色。
    private static final int PANEL_BACKGROUND = 0x55000000;

    // HUDパネルの枠線色。
    private static final int PANEL_BORDER = 0xFF555555;

    // MinecraftのItemアイコンは通常16x16で描画される。
    private static final int HEADER_ICON_SIZE = 16;

    // アイコンとタイトル文字の間に空ける余白。
    private static final int HEADER_ICON_GAP = 5;

    // ブロック名とMOD名を表示するヘッダー全体の高さ。
// 16pxのアイコンに上下の余裕を持たせて20pxとする。
    private static final int HEADER_HEIGHT = 20;

    // Headerと本文を視覚的に分離する線の上下余白。
    private static final int HEADER_SEPARATOR_GAP = 4;

    // Headerと本文の区切り線。
    private static final int HEADER_SEPARATOR = 0xFF444444;

    // 通常文字。
    private static final int TEXT_PRIMARY = 0xFFFFFF;

    // 補足情報。
    // MOD名などに使用する。
    private static final int TEXT_SECONDARY = 0xFFAAAAAA;

    // グループヘッダー左側に表示する
    // 小型アイコンのサイズ。
    private static final int GROUP_ICON_SIZE = 10;

    // アイコンとグループ名の間に空ける余白。
    private static final int GROUP_ICON_GAP = 4;

    // 項目名と値の間に最低限確保する横方向の余白。
    private static final int COLUMN_GAP = 16;

    // LEVEL_BLOCKS / LEVEL_BARで、
    // Visual表示と値の間に確保する余白。
    private static final int VISUAL_VALUE_GAP = 8;

    // 項目名と値の文字サイズ倍率
    private static final float DRAW_VALUE_SCALE = 0.9F;


    // このクラスはstaticメソッドのみを使用するため、
    // 外部からインスタンスを作成できないようにする。
    private MachineHudRenderer() {
    }


    /**
     * MachineHUD全体を描画する。
     *
     * @param guiGraphics MinecraftのGUI描画に使用するオブジェクト
     */
    public static void render(GuiGraphics guiGraphics) {

        // 現在動作しているMinecraftクライアントを取得する。
        Minecraft minecraft = Minecraft.getInstance();

        // PlayerまたはLevelが存在しない状態では
        // 装備状態も確認できないため描画を終了する。
        if (minecraft.player == null || minecraft.level == null) {
            return;
        }

        // プレイヤーの頭装備スロットにあるItemStackを取得する。
        ItemStack headStack = minecraft.player.getItemBySlot(EquipmentSlot.HEAD);

        // Machine HUD Gogglesを装備していない場合は、
        // HUDを一切表示しない。
        if (!headStack.is(ModItems.MACHINE_HUD_GOGGLES.get())) {
            return;
        }

        // ショートカットキーによるHUD全体のON/OFFを
        // HudStateで実装済みの場合はここで判定する。
        if (!HudState.isEnabled()) {
            return;
        }

        // タイトル画面やワールド読み込み中など、PlayerまたはLevelが存在しない状態では
        // HUD情報を取得できないため描画処理を終了する。
        if (minecraft.player == null || minecraft.level == null) {
            return;
        }

        // MachineHUD独自のレイキャストを行い、
        // 最大距離内でプレイヤーが見ているブロックを取得する。
        BlockHitResult target = getTargetBlock(minecraft, MAX_DISTANCE);

        // 最大距離内に対象ブロックが存在しない場合は
        // HUDを表示する必要がないため終了する。
        if (target == null) {
            return;
        }

        // レイキャストで命中したブロックの座標を取得する。
        BlockPos blockPos = target.getBlockPos();

        // 対象座標に存在するブロックの現在状態を取得する。
        BlockState blockState = minecraft.level.getBlockState(blockPos);

        // 空気ブロックはMachineHUDの表示対象外にする。
        if (blockState.isAir()) {
            return;
        }

        // 対象ブロックに対応するアイテムを取得する。
        // 一部の特殊ブロックには対応するItemが存在しないため、空のItemStackになる可能性がある。
        ItemStack blockIcon = blockState.getBlock().asItem().getDefaultInstance();

        // Minecraftが持っている翻訳済みブロック名を取得する。
        // 日本語環境なら日本語名になる。
        Component blockName = blockState.getBlock().getName();

        // 対象座標にBlockEntityが存在する場合は取得する。
        // Createの機械などではBlockEntityを取得できる。
        BlockEntity blockEntity = minecraft.level.getBlockEntity(blockPos);

        Level level = minecraft.level;

        // 対象がCreateの回転機構を持っている場合のみ、
        // Create専用HUDデータを作成する。
        // Create以外のブロックではnullになる。
        List<HudProvider> providers =
                HudProviders.create(
                        level,
                        blockPos,
                        blockState,
                        blockEntity
                );

        // MachineHUDが対応しているProviderが1つも存在しない場合は、
        // ブロック名やMOD名を含めHUD全体を表示しない。
        if (providers.isEmpty()) {
            return;
        }

        // MinecraftのBlock Registryから
        // 対象ブロックの登録IDを取得する。
        //
        // 例:
        // minecraft:stone
        // create:mechanical_press
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(blockState.getBlock());

        // Registry IDのnamespaceを取得する。
        // create:mechanical_press
        // ↓
        // create
        String modName = blockId.getNamespace();

        /*
         * =========================
         * HUD表示
         * =========================
         */

        // 現在描画しているグループを保持する。
        // 前のHUD項目とグループが変わったときだけ
        // 新しいヘッダーを描画するために使用する。
        HudGroup currentGroup = null;

        // 実際に画面へ表示するHUD行を先に収集する。
        // この段階ではまだ画面への描画は行わない。
        List<HudLine> lines = new ArrayList<>();

        // Configで指定されている順番に
        // HUD項目を1つずつ処理する。
        for (HudElement element : HudElements.getOrderedElements()) {

            // Configが手動編集されるなどして
            // 存在しないIDが入っていた場合は無視する。
            if (element == null) {
                continue;
            }

            // ConfigでこのHUD項目がOFFになっている場合は
            // 描画せず次の項目へ進む。
            if (!HudElementConfig.isEnabled(element)) {
                continue;
            }

            for (HudProvider provider : providers) {

                if (!provider.supports(element)) {
                    continue;
                }

                HudLine line = provider.createLine(element);
                if (line == null) {
                    continue;
                }

                /*
                 * =========================
                 * グループヘッダー
                 * =========================
                 */
                // 前に表示した項目とは異なるグループになった場合だけ、
                // 値を追加する前にグループヘッダーを追加する。
                if (element.getHudGroup() != currentGroup) {
                    currentGroup = element.getHudGroup();
                    lines.add(createGroupHeader(currentGroup));
                }

                /*
                 * =========================
                 * HUD項目
                 * =========================
                 */
                lines.add(line);

                // 1つのHudElementは1つのProviderだけが担当するため、
                // 処理できた時点でProvider検索を終了する。
                break;
            }
        }

        /* Body部の作成 */
        int bodyWidth = getBodyWidth(minecraft, lines);

        /* Header部の作成 */
        // Headerのタイトル部分の横幅を計算する。
        // [16px Icon] [5px Gap] [Block Name]
        int headerTitleWidth = HEADER_ICON_SIZE + HEADER_ICON_GAP + minecraft.font.width(blockName);

        // MOD名がブロック名より長い可能性もあるため、MOD名側の横幅も計算する。
        int headerModWidth = HEADER_ICON_SIZE + HEADER_ICON_GAP + minecraft.font.width(modName);

        // Headerで必要になる最大横幅。
        int headerWidth = Math.max(headerTitleWidth, headerModWidth);

        // HeaderとBodyのうち、横幅が大きい方をパネルのコンテンツ幅として採用する。
        int contentWidth = Math.max(headerWidth, bodyWidth);

        // 左右のPaddingを追加して実際のパネル横幅を決定する。
        int panelWidth = contentWidth + PANEL_PADDING * 2;

        // Header下の区切り線を含めた領域。
        int headerAreaHeight = HEADER_HEIGHT + HEADER_SEPARATOR_GAP;

        int groupCount = 0;

        for (HudLine line : lines) {

            if (line.type() == HudLineType.GROUP_HEADER) {
                groupCount++;
            }
        }

        // グループとグループの間に入る追加余白。
        int groupSpacing = Math.max(0, groupCount - 1) * 4;

        int bodyHeight = lines.size() * LINE_HEIGHT + groupSpacing;

        // パネル全体の高さ。
        int panelHeight = PANEL_PADDING + headerAreaHeight + bodyHeight + PANEL_PADDING;

        // HUD情報の後ろに半透明背景を描画する。
        guiGraphics.fill(
                HUD_X,
                HUD_Y,
                HUD_X + panelWidth,
                HUD_Y + panelHeight,
                PANEL_BACKGROUND
        );

        // パネル上端。
        guiGraphics.fill(
                HUD_X,
                HUD_Y,
                HUD_X + panelWidth,
                HUD_Y + 1,
                PANEL_BORDER
        );

        // パネル下端。
        guiGraphics.fill(
                HUD_X,
                HUD_Y + panelHeight - 1,
                HUD_X + panelWidth,
                HUD_Y + panelHeight,
                PANEL_BORDER
        );

        // パネル左端。
        guiGraphics.fill(
                HUD_X,
                HUD_Y,
                HUD_X + 1,
                HUD_Y + panelHeight,
                PANEL_BORDER
        );

        // パネル右端。
        guiGraphics.fill(
                HUD_X + panelWidth - 1,
                HUD_Y,
                HUD_X + panelWidth,
                HUD_Y + panelHeight,
                PANEL_BORDER
        );

        // HUDパネル内でアイコンを表示する位置。
        // パネル左上からpadding分だけ内側へ配置する。
        int iconX =
                HUD_X + PANEL_PADDING;

        int iconY =
                HUD_Y + PANEL_PADDING;

        // ブロックに対応するアイテムが存在する場合のみ描画する。
        // 一部の特殊なBlockは対応するItemを持たない可能性がある。
        if (!blockIcon.isEmpty()) {

            // Minecraft標準の16x16アイテムアイコンをGUIへ描画する。
            guiGraphics.renderItem(
                    blockIcon,
                    iconX,
                    iconY
            );
        }

        // アイコンの右側へタイトル文字を配置する。
        int headerTextX =
                iconX
                        + HEADER_ICON_SIZE
                        + HEADER_ICON_GAP;

        // アイコン上端から少し下げてブロック名を描画する。
        int blockNameY = iconY;

        guiGraphics.drawString(
                minecraft.font,
                blockName,
                headerTextX,
                blockNameY,
                TEXT_PRIMARY
        );

        // ブロック名の下へMOD名を表示する。
        int modNameY =
                blockNameY + 10;

        guiGraphics.drawString(
                minecraft.font,
                modName,
                headerTextX,
                modNameY,
                TEXT_SECONDARY
        );

        // Header直下に区切り線を描画し、
        // タイトル部分と機械情報部分を視覚的に分離する。
        int separatorY =
                HUD_Y
                        + PANEL_PADDING
                        + HEADER_HEIGHT;

        guiGraphics.fill(
                HUD_X + PANEL_PADDING,
                separatorY,
                HUD_X + panelWidth - PANEL_PADDING,
                separatorY + 1,
                HEADER_SEPARATOR
        );

        // Headerと区切り線の下から本文の描画を開始する。
        int bodyStartY = separatorY + HEADER_SEPARATOR_GAP;

        int textY = bodyStartY;

        // 本文で共通使用するLabelカラムの最大幅。
        // VALUE / LEVEL_BLOCKS / LEVEL_BARで
        // VisualやValueの開始位置を揃えるために使用する。
        int maxLabelWidth = getMaxLabelWidth(minecraft, lines);

        // LEVEL_BLOCKSで使用するVisualカラムの最大幅。
        // 各行のValue開始位置を揃えるために使用する。
        int maxLevelBlocksWidth = getMaxLevelBlocksWidth(minecraft, lines);
        int maxLevelCompareWidth = getMaxLevelCompareWidth(minecraft, lines);
        int maxProgressWidth = getMaxProgressWidth(minecraft, lines);

        for (HudLine line : lines) {
            // 2つ目以降のグループヘッダーでは、前のグループとの間に少し余白を追加する。
            if (line.type() == HudLineType.GROUP_HEADER
                    && textY > bodyStartY) {

                textY += 4;
            }

            // 各行のindentに応じて、子項目を右方向へずらす。
            int textX =
                    HUD_X
                            + PANEL_PADDING
                            + line.indent() * INDENT_WIDTH;

            // グループヘッダーの場合は、文字の左側へ専用アイコンを描画する。
            if (line.type() == HudLineType.GROUP_HEADER) {

                HudGroup group = line.group();
                if (group != null) {
                    ResourceLocation icon = group.getIcon();

                    int headerIconX = textX;

                    // Minecraftの文字は約9px程度の高さなので、10pxアイコンと中央が合うように調整する。
                    int headerIconY = textY - 1;

                    // MachineHUDのassetsからグループ専用アイコンを描画する。
                    guiGraphics.blit(
                            icon,
                            headerIconX,
                            headerIconY,
                            0,
                            0,
                            GROUP_ICON_SIZE,
                            GROUP_ICON_SIZE,
                            GROUP_ICON_SIZE,
                            GROUP_ICON_SIZE
                    );

                    // アイコンの右側へグループ名を表示する。
                    int headerGroupTextX =
                            headerIconX
                                    + GROUP_ICON_SIZE
                                    + GROUP_ICON_GAP;

                    guiGraphics.drawString(
                            minecraft.font,
                            line.label(),
                            headerGroupTextX,
                            textY,
                            line.color()
                    );

                    // グループヘッダー1行分だけ下へ進める。
                    textY += LINE_HEIGHT;

                    // この行はグループヘッダーとして描画済みなので、
                    // VALUE用の処理には進まない。
                    continue;
                }
            }

            if (line.type() == HudLineType.LEVEL_COMPARE) {

                if (line.level() == null) {
                    textY += LINE_HEIGHT;
                    continue;
                }

                Component bar = createLevelCompareBar(line.level());

                /*
                 * [indent][label][gap][visual][gap][value]
                 */
                int labelX = HUD_X + PANEL_PADDING + line.indent() * INDENT_WIDTH;

                int visualX = HUD_X + PANEL_PADDING + INDENT_WIDTH + maxLabelWidth + COLUMN_GAP;

                int valueX = visualX + maxLevelCompareWidth + VISUAL_VALUE_GAP;

                // Label
                drawScaledString(
                        guiGraphics,
                        minecraft,
                        line.label(),
                        labelX,
                        textY,
                        TEXT_PRIMARY,
                        DRAW_VALUE_SCALE
                );

                // Visual
                drawScaledString(
                        guiGraphics,
                        minecraft,
                        bar,
                        visualX,
                        textY,
                        TEXT_PRIMARY,
                        DRAW_VALUE_SCALE
                );

                // Value
                if (line.value() != null) {
                    drawScaledString(
                            guiGraphics,
                            minecraft,
                            line.value(),
                            valueX,
                            textY,
                            line.color(),
                            DRAW_VALUE_SCALE
                    );
                }

                textY += LINE_HEIGHT;

                continue;
            }

            if (line.type() == HudLineType.LEVEL_BLOCKS) {

                // Level情報が存在しない場合は描画できないため、
                // この行をスキップする。
                if (line.level() == null) {
                    textY += LINE_HEIGHT;
                    continue;
                }

                // HudLevelから視覚表示を生成する。
                // current = 2, max = 5 → ■■□□□
                Component blocks = createLevelBlocks(line.level());

                /*
                 * [indent][label][gap][visual][gap][value]
                 */
                int labelX = HUD_X + PANEL_PADDING + line.indent() * INDENT_WIDTH;

                int visualX = HUD_X + PANEL_PADDING + INDENT_WIDTH + maxLabelWidth + COLUMN_GAP;

                int valueX = visualX + maxLevelBlocksWidth + VISUAL_VALUE_GAP;

                // Label
                drawScaledString(
                        guiGraphics,
                        minecraft,
                        line.label(),
                        labelX,
                        textY,
                        TEXT_PRIMARY,
                        DRAW_VALUE_SCALE
                );

                // Visual(□■)
                drawScaledString(
                        guiGraphics,
                        minecraft,
                        blocks,
                        visualX,
                        textY,
                        line.color(),
                        DRAW_VALUE_SCALE
                );

                // Value
                if (line.value() != null) {

                    drawScaledString(
                            guiGraphics,
                            minecraft,
                            line.value(),
                            valueX,
                            textY,
                            line.color(),
                            DRAW_VALUE_SCALE
                    );
                }

                textY += LINE_HEIGHT;

                continue;
            }

            if (line.type() == HudLineType.PROGRESS) {

                // Progress情報が存在しない場合は描画できないため、
                // この行をスキップする。
                if (line.level() == null) {
                    textY += LINE_HEIGHT;
                    continue;
                }

                // HudLevelから加工進捗用のバーを生成する。
                Component bar = createProgressBar(line.level());

                /*
                 * [indent][label][gap][visual][gap][value]
                 */
                int labelX = HUD_X + PANEL_PADDING + line.indent() * INDENT_WIDTH;

                int visualX = HUD_X + PANEL_PADDING + INDENT_WIDTH + maxLabelWidth + COLUMN_GAP;

                /*
                 * ProgressのValue領域は常に "100%" の幅を確保する。
                 *
                 * 5%
                 * 23%
                 * 100%
                 *
                 * のように桁数が変化しても、
                 * 右端が揃うように右寄せして描画する。
                 */
                int maxProgressValueWidth =
                        minecraft.font.width(
                                Component.literal("100%")
                        );

                int currentValueWidth =
                        line.value() != null
                                ? minecraft.font.width(line.value())
                                : 0;

                int valueX =
                        visualX
                                + (int) (maxProgressWidth * DRAW_VALUE_SCALE)
                                + VISUAL_VALUE_GAP
                                + (int) (
                                (maxProgressValueWidth - currentValueWidth)
                                        * DRAW_VALUE_SCALE
                        );

                // Label
                drawScaledString(
                        guiGraphics,
                        minecraft,
                        line.label(),
                        labelX,
                        textY,
                        TEXT_PRIMARY,
                        DRAW_VALUE_SCALE
                );

                // Progress Visual
                drawScaledString(
                        guiGraphics,
                        minecraft,
                        bar,
                        visualX,
                        textY,
                        TEXT_PRIMARY,
                        DRAW_VALUE_SCALE
                );

                // Value
                if (line.value() != null) {
                    drawScaledString(
                            guiGraphics,
                            minecraft,
                            line.value(),
                            valueX,
                            textY,
                            line.color(),
                            DRAW_VALUE_SCALE
                    );
                }

                textY += LINE_HEIGHT;

                continue;
            }

            if (line.type() == HudLineType.VALUE) {

                // インデントを考慮した
                // 左側カラムの開始位置。
                int labelX = HUD_X + PANEL_PADDING + line.indent() * INDENT_WIDTH;

                // 項目名を描画する。
                drawScaledString(
                        guiGraphics,
                        minecraft,
                        line.label(),
                        labelX,
                        textY,
                        0xFFAAAAAA,
                        DRAW_VALUE_SCALE
                );


                // 値カラムは、すべての行で同じX座標から開始する。
                // これによってSpeed、Stress、Impactなどの
                // 項目名の長さが違っても値が縦に揃う。
                int valueX = HUD_X + PANEL_PADDING + INDENT_WIDTH + maxLabelWidth + COLUMN_GAP;

                drawScaledString(
                        guiGraphics,
                        minecraft,
                        line.value(),
                        valueX,
                        textY,
                        line.color(),
                        DRAW_VALUE_SCALE
                );

                // 通常行1行分だけ下へ進める。
                textY += LINE_HEIGHT;
            }
        }
    }


    /**
     * プレイヤーが見ているブロックを取得する。
     * <p>
     * Minecraft標準の照準距離ではなく、
     * MachineHUD独自の最大距離を使用するため
     * Level#clip()によるレイキャストを行う。
     */
    private static BlockHitResult getTargetBlock(
            Minecraft minecraft,
            double maxDistance
    ) {

        // PlayerまたはLevelが存在しない場合は
        // レイキャストできないためnullを返す。
        if (minecraft.player == null || minecraft.level == null) {
            return null;
        }

        // プレイヤーの目の位置を
        // レイキャストの開始地点として取得する。
        Vec3 startPos =
                minecraft.player.getEyePosition();

        // プレイヤーが現在向いている方向を取得する。
        Vec3 viewDirection =
                minecraft.player.getViewVector(1.0F);

        // 「目の位置 + 視線方向 × 最大距離」で
        // レイキャストの終了地点を計算する。
        Vec3 endPos =
                startPos.add(
                        viewDirection.scale(maxDistance)
                );

        // プレイヤーの視線上に存在する
        // 最初のブロックを検索する。
        BlockHitResult result =
                minecraft.level.clip(
                        new ClipContext(
                                startPos,
                                endPos,

                                // プレイヤーがブロックを狙うときの
                                // 選択形状を使用して判定する。
                                ClipContext.Block.OUTLINE,

                                // 水や溶岩などのFluidは
                                // MachineHUDの照準対象にしない。
                                ClipContext.Fluid.NONE,

                                minecraft.player
                        )
                );

        // 最大距離内でブロックに命中しなかった場合は、
        // HUD表示対象がないことを示すためnullを返す。
        if (result.getType() != HitResult.Type.BLOCK) {
            return null;
        }

        // ブロックへ正常に命中した結果を返す。
        return result;
    }

    /**
     * HudGroupに対応するグループヘッダーを生成する。
     */
    private static HudLine createGroupHeader(
            HudGroup group
    ) {
        return new HudLine(
                Component.translatable(group.getDisplayName()),
                null,
                0,
                0xFFFFFF,
                HudLineType.GROUP_HEADER,
                group,
                null
        );
    }

    /**
     * 指定された倍率でHUD文字を描画する。
     *
     * @param guiGraphics GUI描画オブジェクト
     * @param minecraft   Minecraftクライアント
     * @param text        表示する文字列
     * @param x           実際に表示したいX座標
     * @param y           実際に表示したいY座標
     * @param color       文字色
     * @param scale       文字サイズ倍率
     */
    private static void drawScaledString(
            GuiGraphics guiGraphics,
            Minecraft minecraft,
            Component text,
            int x,
            int y,
            int color,
            float scale
    ) {

        // この文字だけに拡大・縮小を適用するため、
        // 現在の描画状態を保存する。
        guiGraphics.pose().pushPose();

        // X/Y方向へ指定された倍率を適用する。
        guiGraphics.pose().scale(
                scale,
                scale,
                1.0F
        );

        // 座標自体もscaleの影響を受けるため、
        // 元の画面座標になるようscaleで割って描画する。
        guiGraphics.drawString(
                minecraft.font,
                text,
                (int) (x / scale),
                (int) (y / scale),
                color
        );

        // 後続の描画へscaleを影響させないため、
        // 描画状態を元へ戻す。
        guiGraphics.pose().popPose();
    }

    /**
     * VALUE行に表示される値の中から、最も横幅の大きい値の描画幅を取得する。
     * GROUP_HEADERやLEVEL系の行は、VALUE行とはカラム構成が異なるため対象外とする。
     */
    private static int getMaxValueWidth(Minecraft minecraft, List<HudLine> lines) {
        // VALUE行の値の中で最も横幅の大きいものを調べる。
        int maxValueWidth = 0;

        for (HudLine line : lines) {

            if (line.type() != HudLineType.VALUE || line.value() == null) {
                continue;
            }

            int valueWidth = minecraft.font.width(line.value());

            maxValueWidth = Math.max(maxValueWidth, valueWidth);
        }
        return maxValueWidth;
    }

    /**
     * VALUE行に表示される項目名の中から、最も横幅の大きいラベルの描画幅を取得する。
     * GROUP_HEADERやLEVEL系の行は、VALUE行とはカラム構成が異なるため対象外とする。
     */
    private static int getMaxLabelWidth(
            Minecraft minecraft,
            List<HudLine> lines
    ) {

        int maxLabelWidth = 0;

        for (HudLine line : lines) {

            // GROUP_HEADERは本文のラベルカラムを使用しないため除外。
            if (line.type() == HudLineType.GROUP_HEADER) {
                continue;
            }

            if (line.label() == null) {
                continue;
            }

            int labelWidth = minecraft.font.width(line.label());

            maxLabelWidth = Math.max(maxLabelWidth, labelWidth);
        }

        return maxLabelWidth;
    }

    /**
     * GROUP_HEADER行の中から、アイコン + グループ名を含めて
     * 最も横幅の大きいヘッダー幅を取得する。
     */
    private static int getMaxGroupHeaderWidth(
            Minecraft minecraft,
            List<HudLine> lines
    ) {

        int maxWidth = 0;

        for (HudLine line : lines) {

            // GROUP_HEADER以外は対象外。
            if (line.type() != HudLineType.GROUP_HEADER) {
                continue;
            }

            // 念のためlabelが存在しない場合は無視する。
            if (line.label() == null) {
                continue;
            }

            int width = GROUP_ICON_SIZE + GROUP_ICON_GAP + minecraft.font.width(line.label());

            maxWidth = Math.max(maxWidth, width);
        }

        return maxWidth;
    }

    /**
     * VALUE行全体で必要になる最大横幅を取得する。
     * VALUE行は、
     * [indent][label][COLUMN_GAP][value]
     * という2カラム構成で描画する。
     */
    private static int getValueRowWidth(
            Minecraft minecraft,
            List<HudLine> lines
    ) {

        // VALUE行の中で最も幅の大きいラベルを取得する。
        int maxLabelWidth = getMaxLabelWidth(minecraft, lines);

        // VALUE行の中で最も幅の大きい値を取得する。
        int maxValueWidth = getMaxValueWidth(minecraft, lines);

        return INDENT_WIDTH + maxLabelWidth + COLUMN_GAP + maxValueWidth;
    }

    /**
     * HUD本文全体で必要になる最大横幅を取得する。
     * 現在は、
     * VALUE
     * GROUP_HEADER
     * の2種類について必要な横幅を比較し、最も大きいものを本文幅として返す。
     * 今後LEVEL_BLOCKS / LEVEL_BARを追加する場合も、
     * このメソッド内で各表示形式の横幅を比較する。
     */
    private static int getBodyWidth(Minecraft minecraft, List<HudLine> lines) {

        // 通常のVALUE行で必要になる最大横幅。
        int valueRowWidth = getValueRowWidth(minecraft, lines);

        int levelBlocksRowWidth = getLevelBlocksRowWidth(minecraft, lines);

        // LEVEL_COMPARE行で必要になる最大横幅。
        int levelCompareRowWidth = getLevelCompareRowWidth(minecraft, lines);

        // グループヘッダーで必要になる最大横幅。
        int groupHeaderWidth = getMaxGroupHeaderWidth(minecraft, lines);

        int progressRowWidth = getProgressRowWidth(minecraft, lines);

        // 現在存在する表示形式の中で、
        // 最も横幅の大きいものを本文幅として使用する。
        return Math.max(
                Math.max(
                        Math.max(valueRowWidth, levelBlocksRowWidth),
                        Math.max(levelCompareRowWidth, progressRowWidth)
                ),
                groupHeaderWidth
        );
    }

    /**
     * LEVEL_BLOCKS行のVisual表示の中から、
     * 最も横幅の大きいものを取得する。
     * 例:
     * ■■□□□
     * ■■■■□
     * ■■■■■■■□□
     * のようにHudLevelごとに最大Level数が異なる可能性があるため、
     * 実際に表示するComponentを生成してFontから幅を取得する。
     */
    private static int getMaxLevelBlocksWidth(Minecraft minecraft, List<HudLine> lines) {

        int maxWidth = 0;

        for (HudLine line : lines) {

            // LEVEL_BLOCKS以外の行は対象外。
            if (line.type() != HudLineType.LEVEL_BLOCKS) {
                continue;
            }

            // Level情報を持っていない場合は
            // Visual表示を生成できないため対象外。
            if (line.level() == null) {
                continue;
            }

            Component blocks = createLevelBlocks(line.level());

            int width = minecraft.font.width(blocks);

            maxWidth = Math.max(maxWidth, width);
        }

        return maxWidth;
    }

    /**
     * LEVEL_COMPARE行で必要になる最大横幅を取得する。
     * Label | Visual | Value
     */
    private static int getLevelCompareRowWidth(Minecraft minecraft, List<HudLine> lines) {

        int maxLabelWidth = getMaxLabelWidth(minecraft, lines);

        int maxVisualWidth = getMaxLevelCompareWidth(minecraft, lines);

        int maxRowWidth = 0;

        for (HudLine line : lines) {

            if (line.type() != HudLineType.LEVEL_COMPARE) {
                continue;
            }

            int indentWidth = line.indent() * INDENT_WIDTH;

            int valueWidth = line.value() != null ? minecraft.font.width(line.value()) : 0;

            int rowWidth = indentWidth + maxLabelWidth + COLUMN_GAP + maxVisualWidth + VISUAL_VALUE_GAP + valueWidth;

            maxRowWidth = Math.max(maxRowWidth, rowWidth);
        }

        return maxRowWidth;
    }

    /**
     * LEVEL_COMPARE行のVisual部分で必要になる最大幅を取得する。
     */
    private static int getMaxLevelCompareWidth(Minecraft minecraft, List<HudLine> lines) {

        int maxWidth = 0;

        for (HudLine line : lines) {

            if (line.type() != HudLineType.LEVEL_COMPARE) {
                continue;
            }

            Component visual = createLevelCompareBar(line.level());

            maxWidth = Math.max(maxWidth, minecraft.font.width(visual));
        }

        return maxWidth;
    }

    /**
     * PROGRESS行のVisual部分で必要になる最大幅を取得する。
     */
    private static int getMaxProgressWidth(
            Minecraft minecraft,
            List<HudLine> lines
    ) {

        int maxWidth = 0;

        for (HudLine line : lines) {

            // PROGRESS以外の行は対象外。
            if (line.type() != HudLineType.PROGRESS) {
                continue;
            }

            // Progress情報が存在しない場合はVisualを生成できない。
            if (line.level() == null) {
                continue;
            }

            Component visual =
                    createProgressBar(line.level());

            maxWidth = Math.max(
                    maxWidth,
                    minecraft.font.width(visual)
            );
        }

        return maxWidth;
    }

    /**
     * PROGRESS行全体で必要になる最大横幅を取得する。
     * <p>
     * [indent][label][COLUMN_GAP][visual][VISUAL_VALUE_GAP][value]
     */
    private static int getProgressRowWidth(
            Minecraft minecraft,
            List<HudLine> lines
    ) {

        // 本文で共通使用する最大Label幅。
        int maxLabelWidth = getMaxLabelWidth(minecraft, lines);

        // Progress Visual部分の最大幅。
        int maxVisualWidth = getMaxProgressWidth(minecraft, lines);

        int maxRowWidth = 0;

        for (HudLine line : lines) {

            if (line.type() != HudLineType.PROGRESS) {
                continue;
            }

            int indentWidth = line.indent() * INDENT_WIDTH;

            /*
             * Progressの値表示は、
             * 1桁・2桁・3桁でHUD幅が変動しないように
             * 最大表示である "100%" を基準に幅を確保する。
             */
            int valueWidth =
                    minecraft.font.width(
                            Component.literal("100%")
                    );

            int rowWidth =
                    indentWidth
                            + maxLabelWidth
                            + COLUMN_GAP
                            + maxVisualWidth
                            + VISUAL_VALUE_GAP
                            + valueWidth;

            maxRowWidth = Math.max(maxRowWidth, rowWidth);
        }

        return maxRowWidth;
    }

    /**
     * LEVEL_BLOCKS行に表示されるValueの中から、
     * 最も横幅の大きいValue幅を取得する。
     */
    private static int getMaxLevelBlocksValueWidth(Minecraft minecraft, List<HudLine> lines) {

        int maxWidth = 0;

        for (HudLine line : lines) {

            // LEVEL_BLOCKS以外は対象外。
            if (line.type() != HudLineType.LEVEL_BLOCKS) {
                continue;
            }

            if (line.value() == null) {
                continue;
            }

            int width = minecraft.font.width(line.value());

            maxWidth = Math.max(maxWidth, width);
        }

        return maxWidth;
    }

    /**
     * LEVEL_BLOCKS行全体で必要になる最大横幅を取得する。
     * LEVEL_BLOCKSは、
     * [indent][label][COLUMN_GAP][visual][VISUAL_VALUE_GAP][value]
     * という3カラム構成で描画する。
     */
    private static int getLevelBlocksRowWidth(Minecraft minecraft, List<HudLine> lines) {

        // 本文で共通使用する最大Label幅。
        int maxLabelWidth = getMaxLabelWidth(minecraft, lines);

        // LEVEL_BLOCKSのVisual部分の最大幅。
        int maxVisualWidth = getMaxLevelBlocksWidth(minecraft, lines);

        // LEVEL_BLOCKSのValue部分の最大幅。
        int maxValueWidth = getMaxLevelBlocksValueWidth(minecraft, lines);

        return INDENT_WIDTH + maxLabelWidth + COLUMN_GAP + maxVisualWidth + VISUAL_VALUE_GAP + maxValueWidth;
    }

    /**
     * HudLevelからLEVEL_BLOCKS用の視覚表示を生成する。
     * 1ブロックを1 Levelとして表示する。<br/>
     * 例:
     * current = 2,  max = 5 →  ■■□□□
     *
     * @param level 表示するLevel情報
     * @return LEVEL_BLOCKS用のComponent
     */
    private static Component createLevelBlocks(HudLevel level) {

        // Level情報が存在しない、
        // または最大値が0以下の場合は何も表示しない。
        if (level == null || level.max() <= 0) {
            return Component.empty();
        }

        // LEVEL_BLOCKSは段階表示なので、
        // maxは表示するブロック数として整数へ変換する。
        int max = Math.max(0, (int) Math.ceil(level.max()));

        // currentもブロック数として整数へ変換する。
        // 0未満やmaxを超える値が渡された場合でも、
        // 表示が壊れないよう0～maxへ制限する。
        int current = Math.clamp((int) Math.floor(level.current()), 0, max);

        // 現在Level分を■、
        // 残りを□として表示する。
        String blocks = "■".repeat(current) + "□".repeat(max - current);

        return Component.literal(blocks);
    }

    /**
     * min / current / max の3要素から、Createゴーグル準拠の比較バーを生成する。
     * HudLevel:
     * min     = 3要素の最小値
     * current = この行自身のLevel
     * max     = 3要素の最大値
     * 例:
     * Size  = 1, Water = 4, Heat  = 2
     * Water行では、
     * min     = 1, current = 4, max     = 4
     * となる。
     */
    private static Component createLevelCompareBar(HudLevel level) {

        if (level == null) {
            return Component.empty();
        }

        int min = Math.max(0, (int) level.min());

        int current = Math.max(0, (int) level.current());

        int max = Math.max(0, (int) level.max());

        MutableComponent bar = Component.empty();

        // minより前の有効範囲。
        bar.append(createBars(Math.max(0, min - 1), ChatFormatting.DARK_GREEN));

        // 3要素の最小値となる位置を強調する。
        bar.append(createBars(min > 0 ? 1 : 0, ChatFormatting.GREEN));

        // minから、この項目自身のLevelまで。
        bar.append(createBars(Math.max(0, current - min), ChatFormatting.DARK_GREEN));

        // この項目のLevelから3要素最大値まで。
        bar.append(createBars(Math.max(0, max - current), ChatFormatting.DARK_RED));

        // Create標準と同様に、
        // 次の5Level区切りまでを灰色で補完する。
        bar.append(createBars(Math.max(0, Math.min(18 - max, ((max / 5 + 1) * 5) - max)), ChatFormatting.DARK_GRAY));

        return bar;
    }

    /**
     * 指定された本数の "|" を、
     * 指定色のComponentとして生成する。
     */
    private static Component createBars(int count, ChatFormatting color) {

        if (count <= 0) {
            return Component.empty();
        }

        return Component.literal("|".repeat(count)).withStyle(color);
    }

    /**
     * HudLevelから加工進捗用のバーを生成する。
     * current / max から進捗率を計算するため、Progressの最大値が100以外でも使用できる。
     * 例:
     * current = 50, max = 100 → ■■■■■□□□□□
     */
    private static Component createProgressBar(HudLevel level) {

        // Progress情報が存在しない、
        // または最大値が0以下の場合は何も表示しない。
        if (level == null || level.max() <= 0) {
            return Component.empty();
        }

        /*
         * current / max から0.0～1.0の進捗率を計算する。
         *
         * 想定外の値が渡された場合でもバーが壊れないよう、
         * 0.0～1.0へ制限する。
         */
        double progress = Math.clamp(
                level.current() / level.max(),
                0.0,
                1.0
        );

        // Progressバーは10段階で表示する。
        final int barCount = 10;

        // 進捗率を表示するブロック数へ変換する。
        int filled = (int) Math.round(
                progress * barCount
        );

        MutableComponent bar = Component.empty();

        // 完了している部分。
        if (filled > 0) {
            bar.append(
                    Component.literal("■".repeat(filled))
                            .withStyle(ChatFormatting.GREEN)
            );
        }

        // まだ完了していない部分。
        if (filled < barCount) {
            bar.append(
                    Component.literal("□".repeat(barCount - filled))
                            .withStyle(ChatFormatting.DARK_GRAY)
            );
        }

        return bar;
    }
}