# TMoe_mod

## 使用非官方版本请勿去官方群聊反馈问题

TMoe 是一个兼容若干第三方开源 Telegram 客户端的开源 Xposed 模块

TMoe is an open source Xposed module compatible with several third-party open source Telegram clients.

## 使用方法 / Usage

激活本模块后，在 Telegram 客户端的设置中点击 "TMoe 设置" 即可开关对应功能。

After activating this module, click "TMoe Settings" in the Telegram client settings to turn on or off the corresponding function.

## 新功能介绍 / NEW Features

1. 本地会员 / Fake Premium
2. 隐身模式 / Ghost Mode
3. 加速上传和下载速度 / Boost Upload and Download Speed
4. 永远显示下载管理器 / Always Show Downloads icon

## 支持的客户端 / Supported clients

- 任何基于 Telegram Android 官方 [TMessagesProj](https://github.com/DrKLO/Telegram) 的无混淆客户端。

  Any official Telegram Android [TMessagesProj](https://github.com/DrKLO/Telegram) based client without obfuscation.

- 完整的列表请参考 [HookEntry.java](app/src/main/java/cc/ioctl/tmoe/startup/HookEntry.java)
  以及 [模块作用域](app/src/main/res/values/arrays.xml).

  Please refer to [HookEntry.java](app/src/main/java/cc/ioctl/tmoe/startup/HookEntry.java)
  and [Xposed scope](app/src/main/res/values/arrays.xml) for the complete list.

- 如果您的客户端满足兼容性要求但不在列表中，请在 [HookEntry.java](app/src/main/java/cc/ioctl/tmoe/startup/HookEntry.java)
  和 [模块作用域](app/src/main/res/values/arrays.xml) 中添加对应的值。

  If your client meets the compatibility requirements but is not in the list, please add the corresponding value
  in [HookEntry.java](app/src/main/java/cc/ioctl/tmoe/startup/HookEntry.java) and [Xposed scope](app/src/main/res/values/arrays.xml).

## License

- [GPL-3.0](https://www.gnu.org/licenses/gpl-3.0.html)

```
Copyright (C) 2021-2022 xenonhydride@gmail.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
```
