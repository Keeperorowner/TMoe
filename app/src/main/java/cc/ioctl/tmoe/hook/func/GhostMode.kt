package cc.ioctl.tmoe.hook.func;

import cc.ioctl.tmoe.base.annotation.FunctionHookEntry
import cc.ioctl.tmoe.hook.base.CommonDynamicHook
import com.github.kyuubiran.ezxhelper.utils.*

@FunctionHookEntry
object GhostMode : CommonDynamicHook() {
    override fun initOnce(): Boolean = tryOrFalse {
        findMethod(loadClass("org.telegram.messenger.MessagesController")){
            name=="completeReadTask"
        }.hookBefore {
            if (!isEnabled)return@hookBefore
            it.result = null
        }

        findMethod(loadClass("org.telegram.ui.ChatActivity\$ChatActivityEnterViewDelegate")){
            name=="needSendTyping"
        }.hookBefore {
            if (!isEnabled) return@hookBefore
            it.result = null
        }

        findMethod(loadClass("org.telegram.ui.Stories.StoriesController")){
            name=="markStoryAsRead"
        }.hookBefore {
            if (!isEnabled) return@hookBefore
            it.result = false
        }
    }
}