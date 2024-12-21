package cc.ioctl.tmoe.hook.func

import cc.ioctl.tmoe.base.annotation.FunctionHookEntry
import cc.ioctl.tmoe.hook.base.CommonDynamicHook
import com.github.kyuubiran.ezxhelper.utils.*

@FunctionHookEntry
object AlwaysShowDownloads : CommonDynamicHook() {
    override fun initOnce(): Boolean = tryOrFalse {
        findMethod(loadClass("org.telegram.messenger.DownloadController")){
            name=="hasUnviewedDownloads"
        }.hookBefore {
            if (!isEnabled) return@hookBefore
            it.result = true
        }
    }
}