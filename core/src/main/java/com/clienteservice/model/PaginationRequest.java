package com.clienteservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationRequest {
    private int page;
    private int pageSize;
    private String sortBy;
    private boolean ascending;
}
