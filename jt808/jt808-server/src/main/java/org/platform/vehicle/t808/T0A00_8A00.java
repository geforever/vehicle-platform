package org.platform.vehicle.t808;

import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.annotation.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Base64;

import org.platform.vehicle.basics.JTMessage;
import org.platform.vehicle.commons.JT808;
import org.platform.vehicle.model.APICodes;
import org.platform.vehicle.model.APIException;



/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
@Message({JT808.平台RSA公钥, JT808.终端RSA公钥})
public class T0A00_8A00 extends JTMessage {

    @Field(length = 4, desc = "RSA公钥{e,n}中的e")
    private int e;
    @Field(length = 128, desc = "RSA公钥{e,n}中的n")
    private byte[] n;

    public T0A00_8A00() {
    }

    public T0A00_8A00(int e, byte[] n) {
        this.e = e;
        this.n = n;
    }

    public int getE() {
        return e;
    }

    public void setE(int e) {
        this.e = e;
    }

    public byte[] getN() {
        return n;
    }

    public void setN(byte[] n) {
        this.n = n;
    }

    @Schema(description = "n(BASE64编码)")
    private String nBase64;

    public String getnBase64() {
        return nBase64;
    }

    public void setnBase64(String nBase64) {
        this.nBase64 = nBase64;
    }

    public T0A00_8A00 build() {
        byte[] src = Base64.getDecoder().decode(n);
        if (src.length == 129) {
            byte[] dest = new byte[128];
            System.arraycopy(src, 1, dest, 0, 128);
            src = dest;
        }
        if (src.length != 128) {
            throw new APIException(APICodes.InvalidParameter, "e length is not 128");
        }
        this.n = src;
        return this;
    }
}
