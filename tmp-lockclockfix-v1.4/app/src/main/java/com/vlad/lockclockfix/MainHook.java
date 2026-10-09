package com.vlad.lockclockfix;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
public class MainHook implements IXposedHookLoadPackage {
  private static final String TAG="[LockClockFix] ";
  public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam){
    if(!"com.android.systemui".equals(lpparam.packageName)) return;
    try{
      final Class<?> clazz=XposedHelpers.findClass("com.android.keyguard.clock.animation.oversize.OverSizeBAnimation",lpparam.classLoader);
      XposedBridge.log(TAG+"v1.4 loaded in SystemUI");
      XposedBridge.hookAllMethods(clazz,"doAnimationToLockScreenWithNotification",new XC_MethodHook(){
        @Override protected void beforeHookedMethod(MethodHookParam param){
          try{
            XposedHelpers.callMethod(param.thisObject,"doAnimationToLockScreen");
            param.setResult(null);
            XposedBridge.log(TAG+"v1.4 redirected notification animation to normal lockscreen path");
          }catch(Throwable t){ XposedBridge.log(TAG+"v1.4 redirect failed: "+t); }
        }
      });
    }catch(Throwable t){ XposedBridge.log(TAG+"v1.4 unsupported SystemUI / init failed: "+t); }
  }
}
