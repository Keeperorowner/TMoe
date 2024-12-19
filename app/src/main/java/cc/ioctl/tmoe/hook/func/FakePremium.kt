package cc.ioctl.tmoe.hook.func

import cc.ioctl.tmoe.base.annotation.FunctionHookEntry
import cc.ioctl.tmoe.hook.base.CommonDynamicHook
import com.github.kyuubiran.ezxhelper.utils.*

@FunctionHookEntry
object FakePremium : CommonDynamicHook() {
    override fun initOnce(): Boolean = tryOrFalse {
        val pr= loadClass("org.telegram.messenger.UserConfig")
        for (method in pr.declaredMethods) {
            when (method.name) {
                "isPremium",
                "hasPremiumOnAccounts"->{
                    method.hookBefore {
                        if (isEnabled) it.result=true
                    }
                }
            }
        }

        findMethod(loadClass("org.telegram.messenger.MessagesController")){
            name=="premiumFeaturesBlocked"
        }.hookBefore {
            if (!isEnabled) return@hookBefore
            it.result = false
        }
    }
}
