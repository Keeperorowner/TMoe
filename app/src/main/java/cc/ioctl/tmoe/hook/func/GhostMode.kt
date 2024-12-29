package cc.ioctl.tmoe.hook.func

import cc.ioctl.tmoe.base.annotation.FunctionHookEntry
import cc.ioctl.tmoe.hook.base.CommonDynamicHook
import com.github.kyuubiran.ezxhelper.utils.*
import de.robv.android.xposed.XposedHelpers
import android.content.Context
import java.util.Locale

@FunctionHookEntry
object GhostMode : CommonDynamicHook() {
    private fun getLocalizedStatus(context: Context): String {
        return when (Locale.getDefault().language) {
            "zh" -> when (Locale.getDefault().country) {
                "CN" -> "离线 ?"
                "TW", "HK" -> "離線 ?"
                else -> "离线 ?"
            }
            else -> "Offline ?"
        }
    }
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

        findMethod(loadClass("org.telegram.ui.ProfileActivity")) {
            name == "updateProfileData"
        }.hookAfter { param ->
            if (!isEnabled) return@hookAfter

            val profileActivity = param.thisObject

            val applicationLoaderClass = loadClass("org.telegram.messenger.ApplicationLoader")
            val context = XposedHelpers.getStaticObjectField(applicationLoaderClass, "applicationContext") as Context
            val baseFragmentClass = loadClass("org.telegram.ui.ActionBar.BaseFragment")
            val getUserConfigMethod = baseFragmentClass.getDeclaredMethod("getUserConfig")
            getUserConfigMethod.isAccessible = true
            val userConfig = getUserConfigMethod.invoke(profileActivity)
            val getClientUserIdMethod = userConfig.javaClass.getDeclaredMethod("getClientUserId")
            getClientUserIdMethod.isAccessible = true
            val currentUserId = getClientUserIdMethod.invoke(userConfig) as Long
            val profileActivityClass = loadClass("org.telegram.ui.ProfileActivity")
            val userIdField = profileActivityClass.getDeclaredField("userId")
            userIdField.isAccessible = true
            val profileUserId = userIdField.getLong(profileActivity)
            if (currentUserId != 0L && profileUserId != 0L && currentUserId == profileUserId) {
                val onlineTextView = XposedHelpers.getObjectField(profileActivity, "onlineTextView") as Array<*>
                if (onlineTextView.size > 1 && onlineTextView[1] != null) {
                    val localizedStatus = getLocalizedStatus(context)
                    XposedHelpers.callMethod(onlineTextView[1], "setText", localizedStatus)
                }
            }
        }
    }
}