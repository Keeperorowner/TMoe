package cc.ioctl.tmoe.hook.func

import cc.ioctl.tmoe.base.annotation.FunctionHookEntry
import cc.ioctl.tmoe.hook.base.CommonDynamicHook
import com.github.kyuubiran.ezxhelper.utils.*

@FunctionHookEntry
object BoostSpeed : CommonDynamicHook() {
    override fun initOnce(): Boolean = tryOrFalse {
        findMethod(loadClass("org.telegram.messenger.FileLoadOperation")) {
            name == "updateParams"
        }.hookBefore {
            if (!isEnabled) return@hookBefore
            val instance = it.thisObject
            setField("downloadChunkSizeBig", instance, 1024 * 1024)
            setField("maxDownloadRequests", instance, 12)
            setField("maxDownloadRequestsBig", instance, 12)
            setField("maxCdnParts", instance, 2097152000 / 1048576)
        }
        findMethod(loadClass("org.telegram.tgnet.FileUploadOperation")) {
            name == "startUploadRequest"
        }.hookBefore {
            if (!isEnabled) return@hookBefore
            val instance = it.thisObject
            setField("forceSmallFile", instance, false)
            setField("minUploadChunkSize", instance, 512)
            setField("minUploadChunkSizeBoost", instance, 1024)
            setField("maxUploadingKBytes", instance, 1024)
        }
    }
}

private fun setField(fieldName: String, obj: Any, value: Any?) {
    findField(obj::class.java) {
        name == fieldName
    }.set(obj, value)
}