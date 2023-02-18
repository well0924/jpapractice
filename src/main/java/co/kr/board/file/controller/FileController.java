package co.kr.board.file.controller;

import co.kr.board.config.Exception.dto.DownloadResponse;
import co.kr.board.config.Exception.dto.Response;
import co.kr.board.file.domain.dto.AttachDto;
import co.kr.board.file.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/file")
@AllArgsConstructor
public class FileController {
    private final FileService fileService;

    //파일 전체 조회
    @GetMapping("/list/{id}")
    public Response<List<AttachDto>>fileList(@PathVariable("id")Integer id)throws Exception{
        List<AttachDto>list = fileService.filelist(id);
        return new Response<>(HttpStatus.OK.value(),list);
    }

    //파일 다운로드
    @GetMapping("/download/{id}")
    public DownloadResponse<?> fileDownload(@PathVariable("id")Integer boarid)throws Exception{

        HttpHeaders header = new HttpHeaders();
        Resource res = null;

        List<AttachDto>filelist = fileService.filelist(boarid);
        String filePath = filelist.get(0).getFilePath();
        String originName = filelist.get(0).getOriginFileName();
        String fullPath = filePath;

        File targetFile = new File(fullPath);

        return new DownloadResponse<>(HttpStatus.OK.value(),header,res);
    }
}
