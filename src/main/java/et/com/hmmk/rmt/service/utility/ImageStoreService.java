package et.com.hmmk.rmt.service.utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ImageStoreService {

    private static String UPLOADED_FOLDER = "/var/www/html/uploads/";

    public String store(byte[] image, String imageContentType) {
        try {
            if (imageContentType != null && image != null && image.length > 0) {
                String randomName = UUID.randomUUID().toString();
                String orginalFileName = "orginal" + randomName + "." + getExtention(imageContentType);
                Path path = Paths.get(UPLOADED_FOLDER + orginalFileName);
                Files.write(path, image);
                String fileName = randomName + ".jpeg";
                Boolean converted = ImageConvertor.convertFormat(UPLOADED_FOLDER + orginalFileName, UPLOADED_FOLDER + fileName, "JPEG");
                if (converted) {
                    return fileName;
                } else {
                    return orginalFileName;
                }
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getExtention(String imageContentType) {
        if (imageContentType.equals("image/x-jg")) {
            return "art";
        }
        if (imageContentType.equals("image/bmp")) {
            return "bmp";
        }
        //        if (imageContentType.equals("image/bmp")) {return "dib";}
        if (imageContentType.equals("image/gif")) {
            return "gif";
        }
        if (imageContentType.equals("image/ief")) {
            return "ief";
        }
        if (imageContentType.equals("image/jpeg")) {
            return "jpe";
        }
        //        ("jpeg", "image/jpeg");
        //        ("jpg", "image/jpeg");
        if (imageContentType.equals("image/pict")) {
            return "pct";
        }
        //        ("pic", "image/pict");
        //        ("pict", "image/pict");
        if (imageContentType.equals("image/png")) {
            return "png";
        }
        if (imageContentType.equals("image/x-portable-graymap")) {
            return "pgm";
        }
        if (imageContentType.equals("image/x-portable-anymap")) {
            return "pnm";
        }
        if (imageContentType.equals("image/x-portable-pixmap")) {
            return "ppm";
        }
        if (imageContentType.equals("image/x-macpaint")) {
            return "mac";
        }
        //        ("pnt", "image/x-macpaint");
        if (imageContentType.equals("image/x-portable-bitmap")) {
            return "pbm";
        }
        if (imageContentType.equals("image/vnd.adobe.photoshop")) {
            return "psd";
        }
        if (imageContentType.equals("image/vnd.wap.wbmp")) {
            return "wbmp";
        }
        if (imageContentType.equals("image/x-quicktime")) {
            return "qti";
        }
        if (imageContentType.equals("image/x-cmu-raster")) {
            return "ras";
        }
        if (imageContentType.equals("image/x-rgb")) {
            return "rgb";
        }
        if (imageContentType.equals("image/svg+xml")) {
            return "svg";
        }
        //        ("svgz", "image/svg+xml");
        if (imageContentType.equals("image/tiff")) {
            return "tif";
        }
        //        ("tiff", "image/tiff");
        if (imageContentType.equals("image/x-xbitmap")) {
            return "xbm";
        }
        //        ("xpm", "image/x-xpixmap");
        if (imageContentType.equals("image/x-xwindowdump")) {
            return "xwd";
        }
        return "";
    }
}
