package cc.ioctl.tmoe.hook.func

import cc.ioctl.tmoe.base.annotation.FunctionHookEntry
import cc.ioctl.tmoe.hook.base.CommonDynamicHook
import com.github.kyuubiran.ezxhelper.utils.*
import de.robv.android.xposed.XposedHelpers

@FunctionHookEntry
object BoostSpeed : CommonDynamicHook() {
    override fun initOnce(): Boolean = tryOrFalse {
        findMethod(loadClass("org.telegram.messenger.FileLoadOperation")) {
            name == "updateParams"
        }.hookAfter { param ->
                if (!isEnabled) return@hookAfter
                XposedHelpers.setIntField(param.thisObject, "downloadChunkSizeBig", 1048576)
                XposedHelpers.setObjectField(param.thisObject, "maxDownloadRequests", 12)
                XposedHelpers.setObjectField(param.thisObject, "maxDownloadRequestsBig", 12)
                XposedHelpers.setObjectField(param.thisObject, "maxCdnParts", 2097152000 / 1048576)
                }
    
        findMethod(loadClass("org.telegram.messenger.FileUploadOperation")) {
            name == "startUploadRequest"
        }.hookAfter { param ->
                if (!isEnabled) return@hookAfter
                XposedHelpers.setIntField(param.thisObject, "uploadChunkSize", 1048576)
                XposedHelpers.setObjectField(param.thisObject, "maxRequestsCount", 8)                            
                }            
    }
}