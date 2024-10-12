package cc.ioctl.tmoe.hook.func

import cc.ioctl.tmoe.hook.base.CommonDynamicHook
import com.github.kyuubiran.ezxhelper.utils.*

object FakePremium : CommonDynamicHook() {
    override fun initOnce(): Boolean = tryOrFalse {
        findMethod(loadClass("org.telegram.messenger.UserConfig")){
            name=="isPremium"
        }.hookBefore {
            if (!isEnabled)return@hookBefore

            it.result = true
        }
    }
}