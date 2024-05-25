package co.kr.board.service;

import co.kr.board.repository.HashTagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@AllArgsConstructor
public class HashTagService {

    private final HashTagRepository hashTagRepository;
    
    /**
     *  해시태그 목록(전체)
     **/
    @Transactional(readOnly = true)
    public List<String> findAllHashTagNames(){
        return hashTagRepository.findAllHashtagNames();
    }

}
