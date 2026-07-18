package com.bookcorner.shared;

import lombok.*;

import java.util.List;

/**
 * Generic paginated response wrapper.
 * Used across all modules for consistent pagination responses.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> content;
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private boolean empty;

    public static <T> PageResponse<T> of(
            List<T> content,
            int currentPage,
            int pageSize,
            long totalElements,
            int totalPages
    ) {
        return PageResponse.<T>builder()
                .content(content)
                .currentPage(currentPage)
                .pageSize(pageSize)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .first(currentPage == 0)
                .last(currentPage >= totalPages - 1)
                .empty(content.isEmpty())
                .build();
    }
}
