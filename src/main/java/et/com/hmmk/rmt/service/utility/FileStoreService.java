package et.com.hmmk.rmt.service.utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class FileStoreService {

    private static String UPLOADED_FOLDER = "/var/www/html/uploads/";

    public String store(byte[] data, String contentType, String fileName) {
        try {
            if (data != null && data.length > 0) {
                String randomName = UUID.randomUUID().toString();
                fileName = randomName + fileName;
                Path path = Paths.get(UPLOADED_FOLDER + fileName);
                Files.write(path, data);
                return fileName;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
