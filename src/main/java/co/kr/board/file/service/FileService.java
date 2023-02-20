package co.kr.board.file.service;

import co.kr.board.board.domain.Board;
import co.kr.board.board.repsoitory.BoardRepository;
import co.kr.board.file.domain.AttachFile;
import co.kr.board.file.domain.dto.AttachDto;
import co.kr.board.file.repository.AttachRepository;
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
    private final BoardRepository boardRepository;

    //파일 목록
    @Transactional
    public List<AttachDto> filelist(@Param("id")Integer boardId)throws Exception{
        Optional<Board>findBoard = boardRepository.findById(boardId);

        Board board = findBoard.get();

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

    //파일 리소스 가져오기.

}
