package neatlogic.framework.rdm.app;

import java.util.Map;
import java.util.Set;

/** 由拥有应用的模块声明该应用支持的业务能力。 */
public interface IRdmAppCapabilityProvider {
    /** 返回应用类型到能力集合的映射。 */
    Map<String, Set<RdmAppCapability>> getCapabilities();
}
