package cc.ioctl.tmoe.hook.func

import cc.ioctl.tmoe.base.annotation.FunctionHookEntry
import cc.ioctl.tmoe.hook.base.CommonDynamicHook
import com.github.kyuubiran.ezxhelper.utils.*
import de.robv.android.xposed.XposedHelpers

@FunctionHookEntry
object GhostMode : CommonDynamicHook() {
    override fun initOnce(): Boolean = tryOrFalse {
       findMethod(loadClass("org.telegram.messenger.MessagesController")) {
            name == "completeReadTask"
        }.hookBefore {
            if (!isEnabled) return@hookBefore
            it.result = null
        }


        findMethod(loadClass("org.telegram.ui.ChatActivity\$ChatActivityEnterViewDelegate")) {
            name == "needSendTyping"
        }.hookBefore {
            if (!isEnabled) return@hookBefore
            it.result = null
        }


        findMethod(loadClass("org.telegram.ui.Stories.StoriesController")) {
            name == "markStoryAsRead"
        }.hookBefore {
            if (!isEnabled) return@hookBefore
            it.result = false
        }

        findMethod(loadClass("org.telegram.tgnet.ConnectionsManager")) {
            name == "sendRequestInternal" &&
                    parameterTypes.size == 10 &&
                    parameterTypes[0] == loadClass("org.telegram.tgnet.TLObject") &&
                    parameterTypes[1] == loadClass("org.telegram.tgnet.RequestDelegate") &&
                    parameterTypes[2] == loadClass("org.telegram.tgnet.RequestDelegateTimestamp") &&
                    parameterTypes[3] == loadClass("org.telegram.tgnet.QuickAckDelegate") &&
                    parameterTypes[4] == loadClass("org.telegram.tgnet.WriteToSocketDelegate") &&
                    parameterTypes[5] == Integer.TYPE &&
                    parameterTypes[6] == Integer.TYPE &&
                    parameterTypes[7] == Integer.TYPE &&
                    parameterTypes[8] == java.lang.Boolean.TYPE &&
                    parameterTypes[9] == Integer.TYPE
        }.hookBefore { param ->
                if (!isEnabled) return@hookBefore
                val updateStatusClass = loadClass("org.telegram.tgnet.TLRPC\$TL_account_updateStatus")
                val requestObject = param.args[0]

                if (updateStatusClass.isInstance(requestObject)) {
                    XposedHelpers.setBooleanField(requestObject, "offline", true)
                }
        }
    }
}