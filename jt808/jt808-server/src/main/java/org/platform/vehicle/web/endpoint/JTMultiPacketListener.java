package org.platform.vehicle.web.endpoint;

import io.github.yezhihao.netmc.session.Session;
import java.util.List;
import org.platform.vehicle.codec.MultiPacket;
import org.platform.vehicle.codec.MultiPacketListener;
import org.platform.vehicle.commons.JT808;
import org.platform.vehicle.t808.T8003;

public class JTMultiPacketListener extends MultiPacketListener {

    public JTMultiPacketListener(int timeout) {
        super(timeout);
    }

    @Override
    public boolean receiveTimeout(MultiPacket multiPacket) {
        int retryCount = multiPacket.getRetryCount();
        if (retryCount > 5)
            return false;

        T8003 request = new T8003();
        request.setMessageId(JT808.服务器补传分包请求);
        request.copyBy(multiPacket.getFirstPacket());
        request.setResponseSerialNo(multiPacket.getSerialNo());
        List<Integer> notArrived = multiPacket.getNotArrived();
        short[] idList = new short[notArrived.size()];
        for (int i = 0; i < idList.length; i++) {
            idList[i] = notArrived.get(i).shortValue();
        }
        request.setId(idList);
        Session session = multiPacket.getFirstPacket().getSession();
        if (session != null) {
            session.notify(request).block();
            multiPacket.addRetryCount(1);
            return true;
        }
        return false;
    }
}
