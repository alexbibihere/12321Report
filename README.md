# 12321投诉助手

极简Android工具，一键投诉骚扰电话到12321受理中心。

## 功能

- 复制骚扰电话号码后，点击桌面快捷方式
- 自动提取号码并跳转到12321投诉页面
- 号码自动复制到剪贴板，方便在投诉页面粘贴

## 技术规格

- 语言：Kotlin
- 最低支持：Android 7.0 (API 24)
- 目标SDK：Android 14 (API 34)
- 权限：无
- 体积：约 2-3 MB

## 使用方法

1. 从通话记录中复制骚扰电话号码
2. 点击桌面"12321投诉助手"快捷方式
3. 应用自动打开投诉页面
4. 在页面中粘贴号码（长按输入框）

## 开发

### 构建
```bash
./gradlew build
```

### 生成APK
```bash
./gradlew assembleDebug
```

APK位置：`app/build/outputs/apk/debug/app-debug.apk`

## 项目结构

```
app/
├── src/main/
│   ├── AndroidManifest.xml    # 清单配置
│   ├── java/cn/complaint/helper/
│   │   └── MainActivity.kt   # 核心逻辑（约100行）
│   └── res/
│       ├── mipmap-*/         # 应用图标
│       └── values/           # 资源文件
└── build.gradle              # 构建配置
```

## 投诉平台

- 平台：12321网络不良与垃圾信息举报受理中心
- 链接：https://wechat.12321.cn/harass
