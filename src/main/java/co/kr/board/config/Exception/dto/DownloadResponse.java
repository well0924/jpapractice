package co.kr.board.config.Exception.dto;

import lombok.*;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;


@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DownloadResponse<T> {
    private Integer status;
    private HttpHeaders headers;
    private Resource rs;
}
