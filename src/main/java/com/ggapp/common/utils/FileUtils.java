package com.ggapp.common.utils;

import com.ggapp.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import static com.ggapp.common.utils.Constant.IMAGE_FILE_PATH;

/**
 * @author Tran Minh Truyen on 19/11/2022
 * Dear maintainer.
 * When I wrote this code, only me and God knew what is was.
 * Now, only God knows!
 * So if you are done trying to 'optimize' this routine (and failed), please increment the
 * following counter as a warning to the next guy
 * TOTAL_HOURS_WASTED_HERE =
 */

@Component
public class FileUtils {
    /**
     * Save file to local drive
     *
     * @param fileName
     * @param fileData
     * @return
     * @throws ApplicationException
     */
    public String saveFile(String fileName, String fileData, String fileDirectory) throws ApplicationException {
        if (StringUtils.hasText(fileData) && StringUtils.hasText(fileData)) {
            String timeStamp = DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDateTime.now());
            fileName = fileName + "_" + timeStamp + ".png";
            String directoryName = IMAGE_FILE_PATH + fileDirectory + "/" + fileName;

            try {
                File directory = new File(IMAGE_FILE_PATH + fileDirectory);

                if (!directory.exists()){
                    if (!directory.mkdirs()) throw new ApplicationException("Created file failed", HttpStatus.INTERNAL_SERVER_ERROR);
                }

                byte[] data = Base64.getDecoder().decode(fileData);
                FileOutputStream fos = new FileOutputStream(new File(directoryName));
                fos.write(data);
                fos.close();
            } catch (IOException exception) {
                throw new ApplicationException("Save file failed", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return directoryName;
        }
        return null;
    }

    /**
     * Get file from local drive
     * @param filePath
     * @return
     * @throws ApplicationException
     */
    public String getFile(String filePath) throws ApplicationException {
        if (StringUtils.hasText(filePath)){
            try {
                File file = ResourceUtils.getFile(filePath);
                try(InputStream in = new FileInputStream(file)) {
                    return Base64.getEncoder().encodeToString(in.readAllBytes());
                } catch (IOException exception) {
                    throw new ApplicationException(exception.getMessage(), exception.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (IOException exception) {
                throw new ApplicationException(exception.getMessage(), exception.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return null;
    }

    /**
     * Update file from local drive
     * @param filePath
     * @param fileData
     * @return
     * @throws ApplicationException
     */
    public String updateFile(String filePath, String fileData) throws ApplicationException {
        if (StringUtils.hasText(filePath)){
            try {
                Path path = Paths.get(filePath);
                Files.deleteIfExists(path);
                byte[] data = Base64.getDecoder().decode(fileData);
                FileOutputStream fos = new FileOutputStream(new File(filePath));
                fos.write(data);
                fos.close();
            } catch (IOException exception) {
                throw new ApplicationException(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return filePath;
        }
        return null;
    }
}
