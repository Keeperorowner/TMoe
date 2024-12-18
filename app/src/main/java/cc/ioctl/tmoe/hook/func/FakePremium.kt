package cc.ioctl.tmoe.hook.func

import cc.ioctl.tmoe.base.annotation.FunctionHookEntry
import cc.ioctl.tmoe.hook.base.CommonDynamicHook
import com.github.kyuubiran.ezxhelper.utils.*

@FunctionHookEntry
object FakePremium : CommonDynamicHook() {
    override fun initOnce(): Boolean = tryOrFalse {
        findMethod(loadClass("org.telegram.messenger.UserConfig")){
            name=="isPremium"
        }.hookBefore {
            if (!isEnabled)return@hookBefore
            it.result = true
        }
        
        findMethod(loadClass("org.telegram.messenger.UserConfig")){ 
            name=="hasPremiumOnAccounts" 
        }.hookBefore { 
            if (!isEnabled) return@hookBefore 
            it.result = true
        }

        findMethod(loadClass("org.telegram.messenger.MessagesController")){
            name=="premiumFeaturesBlocked"
        }.hookBefore {
            if (!isEnabled) return@hookBefore
            it.result = false
        }
    }
}
