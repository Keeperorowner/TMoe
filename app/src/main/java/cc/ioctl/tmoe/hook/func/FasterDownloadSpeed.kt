package cc.ioctl.tmoe.hook.func

import cc.ioctl.tmoe.base.annotation.FunctionHookEntry
import cc.ioctl.tmoe.hook.base.CommonDynamicHook
import com.github.kyuubiran.ezxhelper.utils.*

@FunctionHookEntry
object FasterDownloadSpeed : CommonDynamicHook() {
    override fun initOnce(): Boolean = tryOrFalse {
        findMethod(loadClass("org.telegram.messenger.FileLoadOperation")){
            name=="updateParams"
        }.hookBefore {
            if (!isEnabled) return@hookBefore
            setField("downloadChunkSizeBig", it.thisObject, 1024 * 1024)
            setField("maxDownloadRequests", it.thisObject, 12)
            setField("maxDownloadRequestsBig", it.thisObject, 12)
            setField("maxCdnParts", it.thisObject, 2097152000 / 1048576)
        }
    }
}

private fun setField(fieldName: String, obj: Any, value: Any?) {
    findField(obj::class.java) {
        name == fieldName
    }.set(obj, value)
}
