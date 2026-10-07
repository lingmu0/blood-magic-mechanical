# Blood Magic Mechanical

血魔法机械是 Blood Magic 的 Forge 附属，提供可自动处理血之祭坛配方的机械祭坛。

## 版本工程

- `forge-1.20.1`：Minecraft 1.20.1、Forge 47.4.0、Blood Magic 3.3.8
- `forge-1.18.2`：Minecraft 1.18.2、Forge 40.2.21、Blood Magic 3.2.6

两个目录是独立的 ForgeGradle 工程。Windows 下分别进入对应目录运行 `.\gradlew.bat build` 可构建 jar；开发运行配置会通过 Curse Maven 提供 Blood Magic 和 Patchouli 依赖。手动安装生成的 jar 时，也需要在整合包中安装对应版本的 Patchouli，因为 Blood Magic 本身要求它。

机械祭坛继承 Blood Magic 的祭坛逻辑，沿用原版祭坛配方、祭坛结构阶级和生命精华流体能力。物品自动化在六个方向均可向输入槽插入物品、从输出槽取出产物；GUI 提供独立输入/输出槽、进度箭头和生命精华竖槽。

机械祭坛在锻造台制作：1.20.1 使用下界合金升级锻造模板、血之祭坛和下界合金锭；1.18.2 使用血之祭坛和下界合金锭（该版本的原版锻造台没有模板槽）。输入槽中的物品逐个消耗，每个物品完成后立即开始下一个合成周期；LP 不足时等待补充。

## 外观模型

机械祭坛直接继承 Minecraft 内置的 minecraft:block/cube 模型，不定义自定义几何。三张 16×16 贴图分别用于顶面、底面和四个侧面，源文件保存在 design/altar_top.png、design/altar_bottom.png、design/altar_side.png。design/blockbench_model_preview.png 仅作贴图预览；两个版本的模型与贴图资源已同步。
