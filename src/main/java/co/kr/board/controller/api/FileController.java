package co.kr.board.controller.api;

import co.kr.board.config.exception.dto.Response;
import co.kr.board.domain.Dto.AttachDto;
import co.kr.board.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/file")
@AllArgsConstructor
public class FileController {

    private final FileService fileService;

    //파일 전체 조회
    @GetMapping("/{id}")
    public Response<List<AttachDto>>getAllFiles(@PathVariable("id")Integer id)throws Exception{
        List<AttachDto>list = fileService.filelist(id);
        return new Response<>(HttpStatus.OK.value(),list);
    }

    //파일 다운로드
    @GetMapping("/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("filename")String fileName) throws IOException {
        AttachDto getFile = fileService.getFile(fileName);
        Path path = Paths.get(getFile.getFilePath());
        Resource resource = new InputStreamResource(Files.newInputStream(path));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=\"" + URLEncoder.encode(getFile.getOriginFileName(), StandardCharsets.UTF_8) + "\"")
                .body(resource);
    }
}
