package co.kr.board.config.redis;

public class CacheKey {
        public static final int DEFAULT_EXPIRE_SEC = 60; // 1 minutes
        public static final String USER = "user";
        public static final int USER_EXPIRE_SEC = 60 * 5; // 5 minutes
        public static final String BOARD = "board";
        public static final int BOARD_EXPIRE_SEC = 60 * 10; // 10 minutes
}
