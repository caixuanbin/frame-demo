package com.xbcai.file;

import com.xbcai.model.User;
import com.xbcai.result.Result;
import com.xbcai.service.file.StorageFileNotFoundException;
import com.xbcai.service.file.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.stream.Collectors;


@RestController
public class FileUploadController {
    @Autowired
    private  StorageService storageService;

    @GetMapping("/tt")
    public Result<User> tt(){
        User t = new User();
        t.setAddress("gz");
        t.setName("xbcai");
        t.setSex("nv");
        return Result.success(t);
    }


    @GetMapping("/listUploadedFiles")
    public ResponseEntity listUploadedFiles() {
        return ResponseEntity.ok(storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    /**
     * 上传文件
     * @param file 文件
     */
    @PostMapping("/handleFileUpload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) throws Exception{
        storageService.store(file);
        return ResponseEntity.ok("You successfully uploaded " + file.getOriginalFilename() + "!");
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    public static void main(String[] args) {
        System.out.println(StringUtils.getFilename("mypath/myfile.txt"));
        System.out.println(StringUtils.getFilename("D:\\private/其他/老师图片/Joanna.png"));
        System.out.println(StringUtils.cleanPath("D://private/其他/老师图片/Joanna.png"));
        System.out.println(StringUtils.cleanPath("d:/java/wolfcode/../../other/Some.java"));
    }

}
