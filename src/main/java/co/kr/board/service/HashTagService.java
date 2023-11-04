package co.kr.board.service;

import co.kr.board.domain.HashTag;
import co.kr.board.repository.HashTagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Transactional
@Service
@AllArgsConstructor
public class HashTagService {

    private final HashTagRepository hashTagRepository;

    //해시태그 이름별로 조회
    @Transactional(readOnly = true)
    public Set<HashTag> findHashtagsByNames(Set<String> hashtagNames) {
        return new HashSet<>(hashTagRepository.findByHashtagNameIn(hashtagNames));
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
