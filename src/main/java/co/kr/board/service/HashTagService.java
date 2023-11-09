package co.kr.board.service;

import co.kr.board.domain.Dto.HashTagDto;
import co.kr.board.domain.HashTag;
import co.kr.board.repository.HashTagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Transactional
@Service
@AllArgsConstructor
public class HashTagService {

    private final HashTagRepository hashTagRepository;
    
    //해시태그 목록(전체)
    @Transactional(readOnly = true)
    public List<String> findAllHashTagNames(){
        return hashTagRepository.findAllHashtagNames();
    }

    //게시글에 관련된 해시태그 목록
    @Transactional(readOnly = true)
    public Set<HashTag>findHashTagsByArticles(Integer boardId){
        return hashTagRepository.findHashTagsByArticles(boardId);
    }

    //해시태그 구별
    public Set<String> parseHashtagNames(String content) {
        if (content == null) {
            return Set.of();
        }

        Pattern pattern = Pattern.compile("#[\\w가-힣]+");
        Matcher matcher = pattern.matcher(content.strip());
        Set<String> result = new HashSet<>();

        while (matcher.find()) {
            result.add(matcher.group().replace("#", ""));
        }

        return Set.copyOf(result);
    }

    //해시태그 삭제
    public void deleteHashtagWithoutArticles(Integer hashtagId) {
        Optional<HashTag>hashtag = hashTagRepository.findById(hashtagId);
        if (hashtag.get().getArticles().isEmpty()) {
            hashTagRepository.delete(hashtag.get());
        }
    }
}
