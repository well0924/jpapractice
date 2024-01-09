package co.kr.board.service;

import co.kr.board.domain.AttachFile;
import co.kr.board.config.Exception.dto.ErrorCode;
import co.kr.board.config.Exception.handler.CustomExceptionHandler;
import co.kr.board.domain.Dto.AttachDto;
import co.kr.board.repository.AttachRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FileService {
    private final AttachRepository attachRepository;

    /**
     * 파일 전체 목록
     * @param boardId : 게시글 번호
    **/
    @Transactional
    public List<AttachDto> filelist(@Param("id")Integer boardId)throws Exception{

        List<AttachFile>list = attachRepository.findAttachFilesBoardId(boardId);

        List<AttachDto>filelist = new ArrayList<>();

        for(AttachFile file : list){

            AttachDto attachDto = AttachDto
                    .builder()
                    .filePath(file.getFilePath())
                    .originFileName(file.getOriginFileName())
                    .fileSize(file.getFileSize())
                    .boardId(file.getId())
                    .build();

            filelist.add(attachDto);
        }
        return filelist;
    }

    
    //파일 단일 조회하기.
    @Transactional(readOnly = true)
    public AttachDto getFile(String originFileName){
        Optional<AttachFile>attachFile = Optional.ofNullable(attachRepository.findAttachFileByOriginFileName(originFileName)
                .orElseThrow(() -> new CustomExceptionHandler(ErrorCode.NOT_FOUND)));

        return AttachDto
                    .builder()
                    .filePath(attachFile.get().getFilePath())
                    .originFileName(attachFile.get().getOriginFileName())
                    .fileSize(attachFile.get().getFileSize())
                    .boardId(attachFile.get().getBoard().getId())
                    .build();
    }

}
