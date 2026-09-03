# Machine HUD

Createのエンジニアのゴーグルだけでは情報が足りない人向けのHUD拡張MODです。

**Machine HUD Goggles** を装備してCreateの回転機械を見ると、
機械単体の情報や、接続されている回転ネットワーク全体の情報をHUD上に表示します。

現在は **Minecraft 1.21.1 + NeoForge + Create** 向けに開発しています。

---

## 特徴

Machine HUDでは、照準を合わせたCreateの機械について、
以下のような情報を表示できます。

### 機械情報

- **Speed**
  - 現在の回転速度（RPM）

- **Stress Impact**
  - 1 RPMあたりにかかる応力（SU/RPM）

- **Stress**
  - 対象機械単体の現在の応力

- **Status**
  - 対象機械の動作状態

- **Theoretical Speed**
  - 理論上の回転速度

- **Position**
  - 対象機械の座標

### ネットワーク情報

- **Network Stress**
  - 回転ネットワーク全体の応力

- **Network Capacity**
  - 回転ネットワーク全体の許容応力

- **Network Usage**
  - ネットワーク容量の使用率

- **Network Size**
  - 回転ネットワーク全体のサイズ

- **Network Status**
  - 回転ネットワーク全体の状態

---

## Machine HUD Goggles

Machine HUDでは専用装備として、

**Machine HUD Goggles**

を追加します。

Createのエンジニアのゴーグルをベースに、
より詳細な機械・ネットワーク情報を確認できる上位情報端末という位置付けです。

### クラフトレシピ

| | | |
|---|---|---|
| Brass Ingot | Glass Pane | Brass Ingot |
| Electron Tube | Precision Mechanism | Electron Tube |
| Leather | Engineer's Goggles | Leather |

![Machine HUD Goggles Recipe](https://github.com/user-attachments/assets/829be22e-0276-47cc-b3f8-188d14e99f29)

---

## HUD表示

Machine HUD Gogglesを装備した状態でCreateの回転機械を見ると、
画面左上に情報が表示されます。

![Machine HUD](https://github.com/user-attachments/assets/8481c7b2-b94f-45ba-8ae0-462ca4af3bd9)

機械単体の情報と、
接続されている回転ネットワークの情報を分けて確認できます。

---

## 設定

表示する項目は、

**Mods → Machine HUD → Config**

から変更できます。

![Machine HUD Config](https://github.com/user-attachments/assets/2be5ed6a-2c28-424d-869b-09d758905983)

各表示項目は個別にON/OFFできます。

---

## キーバインド

HUD表示の初期キーは、

**Endキー**

です。

押すことでMachine HUD全体の表示 / 非表示を切り替えられます。

Minecraft標準の操作設定から変更可能です。

---

## 必須MOD

| 項目 | バージョン |
|---|---|
| Minecraft | 1.21.1 |
| NeoForge | 21.1.x |
| Create | 6.0.x |

現在、Machine HUDは **Create必須** です。

---

## インストール方法

1. Minecraft 1.21.1を用意します
2. NeoForgeを導入します
3. Createと必要な依存MODを導入します
4. Machine HUDのjarファイルをダウンロードします
5. Minecraftの `mods` フォルダーへjarファイルを入れます
6. Minecraftを起動します

---

## 開発状況

Machine HUDは現在開発中です。

現時点ではCreateの回転機構に関する情報表示を中心に実装しています。

今後、他の工業MODへの対応や表示機能の追加を行う可能性があります。

---

## ソースからビルド

Machine HUDはGradleを使用しています。

### Windows

```bat
gradlew.bat build