package neatlogic.framework.rdm.app;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/** 汇总各模块声明的应用能力；未注册应用默认没有能力。 */
@RootComponent
public class RdmAppCapabilityRegistry extends ModuleInitializedListenerBase {
    private static final Map<String, Set<RdmAppCapability>> CAPABILITIES = new ConcurrentHashMap<>();

    /** 一个应用类型只允许所属模块注册一次，避免跨模块覆盖。 */
    public static void register(String appType, Set<RdmAppCapability> capabilities) {
        if (appType == null || appType.trim().isEmpty() || capabilities == null) {
            throw new IllegalArgumentException("应用类型和能力集合不能为空");
        }
        Set<RdmAppCapability> values;
        if (capabilities.isEmpty()) {
            values = Collections.emptySet();
        } else {
            values = Collections.unmodifiableSet(EnumSet.copyOf(capabilities));
        }
        if (CAPABILITIES.putIfAbsent(appType, values) != null) {
            throw new IllegalArgumentException("重复应用能力声明：" + appType);
        }
    }

    /** 判断应用是否显式声明了指定能力。 */
    public static boolean has(String appType, RdmAppCapability capability) {
        if (appType == null || capability == null) { return false; }
        Set<RdmAppCapability> values = CAPABILITIES.get(appType);
        return values != null && values.contains(capability);
    }

    /** 返回可直接用于应用接口的稳定能力名称。 */
    public static List<String> getNames(String appType) {
        List<String> result = new ArrayList<>();
        if (appType == null) { return result; }
        Set<RdmAppCapability> values = CAPABILITIES.get(appType);
        if (values != null) {
            for (RdmAppCapability capability : RdmAppCapability.values()) {
                if (values.contains(capability)) { result.add(capability.name()); }
            }
        }
        return result;
    }

    /** 模块初始化后发现所属模块的能力提供者。 */
    @Override
    protected void onInitialized(NeatLogicWebApplicationContext context) {
        for (IRdmAppCapabilityProvider provider : context.getBeansOfType(IRdmAppCapabilityProvider.class).values()) {
            for (Map.Entry<String, Set<RdmAppCapability>> entry : provider.getCapabilities().entrySet()) {
                register(entry.getKey(), entry.getValue());
            }
        }
    }

    /** 注册工作由模块初始化回调完成。 */
    @Override
    protected void myInit() { }
}
