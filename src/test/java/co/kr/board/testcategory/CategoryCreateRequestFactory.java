package co.kr.board.testcategory;

import co.kr.board.domain.Dto.CategoryCreateRequest;

public class CategoryCreateRequestFactory {
    public static CategoryCreateRequest createCategoryCreateRequest() {
        return new CategoryCreateRequest("category", null);
    }

    public static CategoryCreateRequest createCategoryCreateRequestWithName(String name) {
        return new CategoryCreateRequest(name, null);
    }
}
