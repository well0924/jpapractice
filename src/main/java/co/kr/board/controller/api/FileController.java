package co.kr.board.controller.api;

import co.kr.board.config.Exception.dto.Response;
import co.kr.board.domain.Dto.AttachDto;
import co.kr.board.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
