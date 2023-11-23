package org.platform.vehicle.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import net.coobird.thumbnailator.Thumbnails;

/**
 * @author Kit
 */
public class ImageUtil {

    public static ByteArrayInputStream compressPic(InputStream in, float outputQuality) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(in)
                // 图片尺寸
                .scale(0.8)
                // 图片质量
                .outputQuality(outputQuality)
                .toOutputStream(outputStream);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public static ByteArrayOutputStream streamToStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        int len;
        byte[] buffer = new byte[1024];
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOut.write(buffer, 0, len);
        }
        return byteArrayOut;
    }
}
