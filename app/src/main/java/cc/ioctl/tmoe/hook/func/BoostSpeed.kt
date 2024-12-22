package cc.ioctl.tmoe.hook.func

import cc.ioctl.tmoe.base.annotation.FunctionHookEntry
import cc.ioctl.tmoe.hook.base.CommonDynamicHook
import com.github.kyuubiran.ezxhelper.utils.tryOrLogFalse
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

@FunctionHookEntry
object BoostSpeed : CommonDynamicHook() {
    override fun initOnce(): Boolean = tryOrLogFalse {
        class BoostSpeed : IXposedHookLoadPackage {
            override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
                if (isEnabled) {
                    hookFileLoadOperation()
                    hookFileUploadOperation()
                }
            }

            private fun hookFileLoadOperation() {
                val clazz =
                    XposedHelpers.findClass("org.telegram.messenger.FileLoadOperation", null)
                XposedHelpers.findAndHookMethod(clazz, "updateParams", object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val instance = param.thisObject
                        XposedHelpers.setIntField(instance, "downloadChunkSizeBig", 1024 * 1024)
                        XposedHelpers.setIntField(instance, "maxDownloadRequests", 12)
                        XposedHelpers.setIntField(instance, "maxDownloadRequestsBig", 12)
                        XposedHelpers.setIntField(instance, "maxCdnParts", 2097152000 / 1048576)
                    }
                })
            }

            private fun hookFileUploadOperation() {
                val clazz = XposedHelpers.findClass("org.telegram.tgnet.FileUploadOperation", null)
                XposedHelpers.findAndHookMethod(
                    clazz,
                    "startUploadRequest",
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            val instance = param.thisObject
                            XposedHelpers.setIntField(instance, "uploadChunkSize", 1024 * 1024)
                            XposedHelpers.setIntField(instance, "maxRequestsCount", 8)
                        }
                    })
            }
        }
    }
}