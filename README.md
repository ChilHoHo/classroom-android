# Classroom-Android

使用 Jetpack Compose 和 Material 3 Expressive 的 Android 客户端首版，配合 classroom-web 后端开发。

## 当前页面

- 首页：沿用 CRTicket 的静态产品图标及 Google Sans Rounded 标题，名称为 Classroom。账号卡片采用圆形空头像，未登录时显示“登录”，登录后显示真实账号名。
- 设置：二级页面，仅包含“后端地址”。点击弹窗编辑，保存到本机，重启后保留。取消不保存，留空保存可清除地址。
- 登录：账号、密码、密码可见切换、原生图形验证码及登录按钮；登录成功返回首页。已登录账号入口展示身份、姓名、学号和班级，并提供退出登录。

页面遵循系统深浅色与动态配色，支持边到边布局、滚动及输入法避让。界面字符串统一维护在 res/values/strings.xml。

## 后端地址

填写服务器根地址，例如 http://192.168.1.100:3001，包含 http 或 https 协议，不包含 /api、查询参数、片段或账号密码。API 路径由后续网络层统一追加。

保存地址时只校验格式；进入登录页后实际连接后端并读取验证码。真机访问开发机应填写同一网络内的局域网地址。classroom-web 默认监听 127.0.0.1，真机联调时需在后端配置 API_HOST=0.0.0.0，并允许对应端口通过防火墙。本应用支持局域网 HTTP 联调，正式部署建议使用 HTTPS；生产后端的 Secure Cookie 要求 HTTPS。

## 原生登录与会话

Android 使用 HttpURLConnection 和协程访问接口，没有 WebView。验证码由 AndroidSVG 渲染到原生 Bitmap，再交给 Compose 显示。请求路径：

- GET /api/captcha：保存验证码会话 Cookie，验证码一次性使用，失败后自动更新，可手动换一张。
- POST /api/android/auth/login：提交 username、password、code，由后端决定 android 会话类型。
- GET /api/auth/me：启动、返回首页和已有登录用户恢复前台时校验登录态。
- GET /api/profile：登录及恢复会话时读取当前用户资料；学生展示姓名、学号和班级，空班级显示“未分配”。
- POST /api/auth/logout：销毁本次会话，成功后清除本机 Cookie。

同账号一个 Web 和一个 Android 可同时在线，同端新登录替换旧登录；改密、停用、移除、密码重置会撤销全部会话。更新后需重启 classroom-web API，再安装新的 Android APK。

密码只留在登录页内存，不保存。Cookie 按后端根地址隔离，采用 Android Keystore AES/GCM 加密保存，并排除系统云备份与设备迁移；重启后通过 /auth/me 恢复用户，切换后端不会发送其他服务器的 Cookie。网络失败保留现有用户信息，收到有效 401 后回到未登录状态。请求禁止自动重定向，采用系统默认 TLS 校验。

## 构建

使用 JDK 25 与项目自带 Gradle Wrapper：

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
```

产物：app/build/outputs/apk/debug/app-debug.apk。

## OPPO 真机验证（2026-11-04）

仅使用 OPPO PDYM20（Android 12 / API 31），后端地址为 `http://172.16.0.6:3001`。

- 构建、单元测试和 Lint：`:app:assembleDebug :app:testDebugUnitTest :app:lintDebug` 全部通过。
- 使用 ADB 安装 APK 后，在 OPPO 系统安装程序中确认安装，安装成功，无需关闭系统安装保护。
- 在手机设置页输入并保存后端地址，通过原生图形验证码和学生账号完成真实登录。
- 账号页面显示后端资料：学生身份、姓名“学生测试”、学号 `20261104001`、班级“软件工程测试班”。
- 强制关闭并重新打开应用后成功恢复学生登录态，资料仍可读取。
- 测试账号为 `oppo_student_1104`；密码不写入仓库。Web 前端和后端代码无需改动。

## Android 17 局域网权限

目标 SDK 37 的应用在 Android 17 上访问局域网后端，需要声明并动态申请 `android.permission.ACCESS_LOCAL_NETWORK`。应用启动时通过系统授权对话框申请权限；未授权时提供“允许局域网访问”入口。Android 12 等旧版本直接进入应用。

2026-11-04 在 Pixel 8 Pro（Android 17）验证：保存的地址为 `http://172.16.0.6:3001`，手机可 ping 通服务器，但旧客户端未声明局域网权限。补齐权限后安装修复版，确认权限已授权，登录页成功加载真实后端验证码。此次验证覆盖连接及验证码读取，没有在 Pixel 提交学生登录。构建、单元测试和 Lint 均通过。
