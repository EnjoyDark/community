package life.majiang.community.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> {
    private List<T> data;
    /**是否有 向前按钮*/
    private boolean showPrevious;
    /**是否有 第一页按钮*/
    private boolean showFirstPage;
    /**是否有 向后按钮*/
    private boolean showNext;
    /**是否有 最后一页按钮*/
    private boolean showEndPage;
    /**当前页*/
    private Integer page;
    /**页数组*/
    private List<Integer> pages = new ArrayList<>();
    /**总页数*/
    private Integer totalPage;
    /**问题总数量*/
    private Integer totalCount;


    public void setPagination(Integer totalPage,Integer page) {
        this.page = page;
        this.totalPage = totalPage;

        // 添加页数  向前展示三页、向后展示三页
        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page-i);
            }
            if (page + i <= totalPage) {
                pages.add(page+i);
            }
        }

        // 是否显示四个按钮（上、下页， 第一、最后页）
        showPrevious = page != 1;

        showNext = page != totalPage;

        if (pages.contains(1)) {
            showFirstPage = false;
        }else{
            showFirstPage=true;
        }
        showEndPage = !pages.contains(totalPage);

    }
}
