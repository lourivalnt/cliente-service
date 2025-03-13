package com.clienteservice.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor // Adicionado para gerar um construtor público
public class PageResult<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private int pageSize;
}
