package org.platform.vehicle.response;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Data
@Root(name = "xml", strict = false)
public class WxPaySandboxKeyResponse {

    @Element(name = "return_code")
    private String returnCode;
    @Element(name = "return_msg")
    private String returnMSG;
    @Element(name = "sandbox_signkey")
    private String sandboxSignkey;
}
