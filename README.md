## Beats 客户端
### 关于 Beats
![beats](http://ofrf20oms.bkt.clouddn.com/beats.jpg)  

[大尺寸原图](http://ofrf20oms.bkt.clouddn.com/beats_origin.jpg)

[app下载地址](http://fir.im/beats)

`Beats` 是一款基于萌否网站api的音乐管理软件，它既可以从萌否网站上获取网络上的音乐资源也可以在本地音乐上进行私人化定制管理。

本来只是想写一个 Material Design 风格的客户端练练手而已，后来在写的过程中想要加的功能越来越多，结果也导致开发时间也越来越长。整个App主要以 **mvp+rxjava+retrofit** 为框架，使用 **Material Design** 为主要设计风格。   
### 关于萌否api
![](http://moefou.org/public/images/mf/logo.png)  
[萌否电台](http://moe.fm/)是一个二次元音乐电台网站（虽然歌曲不是很全），可以自己上传专辑和建造电台与网友分享，旗下的[萌否开放平台](http://open.moefou.org/)提供了 api 供开发者使用。

## 功能和技术点

### 功能
- 萌否用户登陆
- 首页显示热门专辑和电台
- 萌否专辑电台的分类浏览
- 萌否专辑电台搜索
- 萌否音乐下载
- 本地音乐专辑浏览
- 个性化收藏夹
- 最近播放
- 设置的通知栏和仅wifi下载功能
- 每日P站排名浏览（前50）

### 技术点
 技术点 | 简介
  -------- | ------
[RxJava](https://github.com/ReactiveX/RxJava) | RxJava
[RxAndroid](https://github.com/ReactiveX/RxAndroid) | RxAndroid
[Gson](https://github.com/google/gson) | Json 解析库
[Glide](https://github.com/bumptech/glide) | 图片加载库
[Retrofit2](https://github.com/square/retrofit) | Retrofit
[FileDownloader](https://github.com/lingochamp/FileDownloader) | 文件下载器
[scribejava](https://github.com/scribejava/scribejava) | OAuth/OAuth2 验证平台辅助库
[Material-Dialogs](https://github.com/afollestad/material-dialogs) | 一个强大漂亮的Material Dialog
[simpleslider](https://github.com/cpacm/SimpleSlider) | 轮播图
[audiovisualization](https://github.com/Cleveroad/WaveInApp) | 音乐播放器可视化渲染器
[searchview](https://github.com/lapism/SearchView) | 搜索框
[TimelyTextView](https://github.com/adnan-SM/TimelyTextView) | 数字路径动画显示控件
[circularseekbar](https://github.com/devadvance/circularseekbar) | 圆形进度条
[DropDownMenu](https://github.com/dongjunkun/DropDownMenu) | 下拉菜单
Oauth登陆 | WebView 进行 JS 注入，避免登陆时网页的出现
RenderScript 图片渲染 | Android 16以上使用 RenderScript 对图片进行模糊处理，16及以下使用快速模糊算法
Android Transition 动画 | Activity切换时图片的 Transition 动画
FloatingMusicMenu | 显示音乐播放时状态和进度的菜单按钮，打开时向上弹出子按钮
RefreshRecyclerView | 包含 `SwipeLayout` 和 `RecyclerView`，具有下拉刷新，上拉加载，占位图功能


## Note
`Beats` 安装的最低要求是Android 4.0，但为了最好的体验效果最好使用Android5.0及以上。

如果大家在使用过程中出现问题，欢迎提交 issue 或直接联系，我会最快时间处理。

## 更新日志

v1.0.2

- 添加下载整个专辑歌曲的功能
- 歌曲以专辑名称分目录保存

v1.0.1

- 修复混淆后无法打开电台wiki的bug


## License
Copyright 2016 cpacm

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.



