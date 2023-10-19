package co.kr.board.service;

import co.kr.board.domain.AttachFile;
import co.kr.board.domain.Board;
import co.kr.board.repository.BoardRepository;
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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FileService {
    private final AttachRepository attachRepository;
    private final BoardRepository boardRepository;

    /*
    * 파일 전체 목록
    * @Param boardId
    *
    */
    @Transactional
    public List<AttachDto> filelist(@Param("id")Integer boardId)throws Exception{
        Optional<Board>findBoard = Optional.ofNullable(boardRepository.findById(boardId).orElseThrow(() -> new CustomExceptionHandler(ErrorCode.NOT_BOARD_DETAIL)));

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

        AttachDto attachDto = AttachDto
                .builder()
                .filePath(attachFile.get().getFilePath())
                .originFileName(attachFile.get().getOriginFileName())
                .fileSize(attachFile.get().getFileSize())
                .boardId(attachFile.get().getBoard().getId())
                .build();
        return attachDto;
    }

}
