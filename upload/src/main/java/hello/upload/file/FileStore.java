package hello.upload.file;

import hello.upload.domain.UpLoadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public List<UpLoadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UpLoadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }

    public UpLoadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originFilename = multipartFile.getOriginalFilename();
        //image.png

        //서버에 저장하는 파일명
        String storeFilename = createStoreFilename(originFilename);
        multipartFile.transferTo(new File(getFullPath(storeFilename)));
        return new UpLoadFile(originFilename, storeFilename);
    }

    private String createStoreFilename(String originFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originFilename);
        return uuid + "." + ext;
    }

    private String extractExt(String originFilename) {
        int pos = originFilename.lastIndexOf(".");
        return originFilename.substring(pos + 1);
    }


}
