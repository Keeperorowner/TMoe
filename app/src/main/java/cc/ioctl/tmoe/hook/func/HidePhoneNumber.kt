package cc.ioctl.tmoe.hook.func

import cc.ioctl.tmoe.base.annotation.FunctionHookEntry
import cc.ioctl.tmoe.hook.base.CommonDynamicHook
import com.github.kyuubiran.ezxhelper.utils.*
import de.robv.android.xposed.XposedHelpers

@FunctionHookEntry
object HidePhoneNumber : CommonDynamicHook() {
    override fun initOnce(): Boolean = tryOrLogFalse {
        findMethod(loadClass("org.telegram.messenger.UserConfig"), false) {
            name == "getCurrentUser"
        }.hookAfter { param ->
            if (!isEnabled) return@hookAfter
            val result = param.result
            XposedHelpers.setObjectField(result, "phone", null)
        }
    }
}