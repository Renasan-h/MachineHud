package com.rsdvlp.machinehud.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

/**
 * Machine HUD Gogglesを頭へ装備したときに使用する3Dモデル。
 *
 * UVの位置とCubeサイズを整数ベースで揃え、
 * 64x32テクスチャ内で各部品の領域が重ならないようにしている。
 */
public class MachineHudGogglesModel
        extends HumanoidModel<LivingEntity> {

    public MachineHudGogglesModel(ModelPart root) {
        super(root);
    }


    public static LayerDefinition createBodyLayer() {

        // HumanoidModelとして必要なhead/body/arms/legs等の
        // 基本パーツを生成する。
        MeshDefinition mesh =
                HumanoidModel.createMesh(
                        CubeDeformation.NONE,
                        0.0F
                );

        PartDefinition root =
                mesh.getRoot();

        // ゴーグルは頭へ追従させるため、
        // HumanoidModelのhead配下へ追加する。
        PartDefinition head =
                root.getChild("head");


        // 左側の金属フレーム。
        // UV占有サイズ: 10x4
        head.addOrReplaceChild(
                "left_frame",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                0.0F,
                                -5.5F,
                                -5.0F,
                                4.0F,
                                3.0F,
                                1.0F
                        ),
                PartPose.ZERO
        );


        // 右側の金属フレーム。
        // UV占有サイズ: 10x4
        head.addOrReplaceChild(
                "right_frame",
                CubeListBuilder.create()
                        .texOffs(10, 0)
                        .addBox(
                                -4.0F,
                                -5.5F,
                                -5.0F,
                                4.0F,
                                3.0F,
                                1.0F
                        ),
                PartPose.ZERO
        );


        // 左HUDレンズ。
        // フレームより少し前へ出して、
        // Z-fightingを防ぐ。
        // UV占有サイズ: 8x3
        head.addOrReplaceChild(
                "left_lens",
                CubeListBuilder.create()
                        .texOffs(20, 0)
                        .addBox(
                                0.5F,
                                -5.0F,
                                -5.15F,
                                3.0F,
                                2.0F,
                                1.0F
                        ),
                PartPose.ZERO
        );


        // 右HUDレンズ。
        // UV占有サイズ: 8x3
        head.addOrReplaceChild(
                "right_lens",
                CubeListBuilder.create()
                        .texOffs(28, 0)
                        .addBox(
                                -3.5F,
                                -5.0F,
                                -5.15F,
                                3.0F,
                                2.0F,
                                1.0F
                        ),
                PartPose.ZERO
        );


        // 左右のフレームをつなぐ中央ブリッジ。
        // UV占有サイズ: 4x2
        head.addOrReplaceChild(
                "bridge",
                CubeListBuilder.create()
                        .texOffs(36, 0)
                        .addBox(
                                -0.5F,
                                -4.5F,
                                -5.1F,
                                1.0F,
                                1.0F,
                                1.0F
                        ),
                PartPose.ZERO
        );


        // 中央HUDセンサー。
        // 将来、レンズやセンサーだけ発光させる場合に
        // 独立して扱えるよう別Partにしている。
        // UV占有サイズ: 4x2
        head.addOrReplaceChild(
                "center_sensor",
                CubeListBuilder.create()
                        .texOffs(40, 0)
                        .addBox(
                                -0.5F,
                                -6.0F,
                                -5.15F,
                                1.0F,
                                1.0F,
                                1.0F
                        ),
                PartPose.ZERO
        );


        // 左側の解析ユニット。
        // UV占有サイズ: 4x4
        head.addOrReplaceChild(
                "left_module",
                CubeListBuilder.create()
                        .texOffs(44, 0)
                        .addBox(
                                4.0F,
                                -5.5F,
                                -4.75F,
                                1.0F,
                                3.0F,
                                1.0F
                        ),
                PartPose.ZERO
        );


        // 右側の解析ユニット。
        // UV占有サイズ: 4x4
        head.addOrReplaceChild(
                "right_module",
                CubeListBuilder.create()
                        .texOffs(48, 0)
                        .addBox(
                                -5.0F,
                                -5.5F,
                                -4.75F,
                                1.0F,
                                3.0F,
                                1.0F
                        ),
                PartPose.ZERO
        );


        // 後頭部の金属バックル。
        // UV占有サイズ: 6x2
        head.addOrReplaceChild(
                "rear_buckle",
                CubeListBuilder.create()
                        .texOffs(52, 0)
                        .addBox(
                                -1.0F,
                                -6.5F,
                                4.0F,
                                2.0F,
                                1.0F,
                                1.0F
                        ),
                PartPose.ZERO
        );


        // レンズ上部の補強プレート。
        // 幅が長いため、UVの2段目を使用する。
        // UV占有サイズ: 18x2
        head.addOrReplaceChild(
                "top_plate",
                CubeListBuilder.create()
                        .texOffs(0, 5)
                        .addBox(
                                -4.0F,
                                -6.25F,
                                -4.75F,
                                8.0F,
                                1.0F,
                                1.0F
                        ),
                PartPose.ZERO
        );


        // 頭部へ固定する革バンド。
        // 頭部へめり込まないよう、
        // CubeDeformationで少し外側へ膨らませる。
        // UV占有サイズ: 32x9
        head.addOrReplaceChild(
                "strap",
                CubeListBuilder.create()
                        .texOffs(0, 8)
                        .addBox(
                                -4.0F,
                                -6.5F,
                                -4.0F,
                                8.0F,
                                1.0F,
                                8.0F,
                                new CubeDeformation(0.15F)
                        ),
                PartPose.ZERO
        );


        // 今回作成したテクスチャサイズ。
        return LayerDefinition.create(
                mesh,
                64,
                32
        );
    }
}