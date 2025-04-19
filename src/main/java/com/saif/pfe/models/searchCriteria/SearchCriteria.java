package com.saif.pfe.models.searchCriteria;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class SearchCriteria extends CommonBaseDTO {
    private int page;
    private int size;
    private String sort;
    private String order;

    public SearchCriteria() {
        this.size = 500;
        this.page = 0;
    }

    public Pageable getPageable() {
        Sort sort = null;
        if (this.getSort() != null) {
            if (this.getOrder().equalsIgnoreCase("desc")) {
                sort = Sort.by(Sort.Order.desc(this.getSort()).ignoreCase());
            } else {
                sort = Sort.by(Sort.Order.asc(this.getSort()).ignoreCase());
            }
        }
        Pageable pageable = Pageable.unpaged();
        if (this.getSize() == 0)
            this.size = Integer.MAX_VALUE;
        if (sort != null) {
            pageable = PageRequest.of(this.getPage() >= 1 ? this.getPage() - 1 : 0,
                    this.getSize() > 0 ? this.getSize() : 10, sort);
        } else {
            pageable = PageRequest.of(this.getPage() >= 1 ? this.getPage() - 1 : 0,
                    this.getSize() > 0 ? this.getSize() : 10);
        }
        return pageable;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getStart() {
        if (page > 0) {
            return (page - 1) * size;
        }
        return 0;
    }
}
