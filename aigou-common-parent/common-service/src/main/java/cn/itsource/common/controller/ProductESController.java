package cn.itsource.common.controller;

import cn.itsource.basic.util.AjaxResult;
import cn.itsource.basic.util.PageList;
import cn.itsource.common.domain.ProductDoc;
import cn.itsource.common.repository.ProductRepository;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortFieldAndFormat;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/es")
public class ProductESController {

    @Autowired
    private ProductRepository productRepository;

    /**
     * 保存
     * @param productDoc
     * @return
     */
    @PostMapping("/save")
    public AjaxResult save(@RequestBody ProductDoc productDoc){
        try {
            productRepository.save(productDoc);
            return AjaxResult.me().setSuccess(true).setMessage("成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("失败!"+e.getMessage());
        }
    }

    /**
     * 批量保存
     * @param list
     * @return
     */
    @PostMapping("/saveBath")
    public AjaxResult saveBath(@RequestBody List<ProductDoc> list){
        try {
            productRepository.saveAll(list);
            return AjaxResult.me().setSuccess(true).setMessage("成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("失败!"+e.getMessage());
        }
    }

    /**
     * 删除
     * @param productId
     * @return
     */
    @GetMapping("/delete")
    public AjaxResult delete(@RequestParam("productId") Long productId){
        try {
            productRepository.deleteById(productId);
            return AjaxResult.me().setSuccess(true).setMessage("成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("失败!"+e.getMessage());
        }
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @PostMapping("/deleteBatch")
    public AjaxResult deleteBatch(@RequestBody List<Long> ids){
        try {
            for (Long id : ids) {
                productRepository.deleteById(id);
            }
            return AjaxResult.me().setSuccess(true).setMessage("成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("失败!"+e.getMessage());
        }
    }

    @PostMapping("/query")
    public AjaxResult query(@RequestBody Map<String,Object> params){
        try {
            /*列出条件*/
            String keyword = (String) params.get("keyword");//关键字
            Long brandId = params.get("brandId")!=null?Long.parseLong((String)params.get("brandId")):null;
            Long productTypeId = params.get("productTypeId")!=null?Long.parseLong((String)params.get("productTypeId")):null;

            /*最大价格和最小价格*/
            Integer minPrice = (Integer) params.get("minPrice");
            Integer maxPrice = (Integer) params.get("maxPrice");
            /*排序*/
            String sortColumn = (String) params.get("sortColumn");// xl xp pl jg rq
            String sortType = (String) params.get("sortType");//asc desc
            /*分页*/
            Integer pageNum = (Integer) params.get("pageNum");
            Integer pageSize = (Integer) params.get("pageSize");

            //es查询与过滤
            NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
            //查询
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            List<QueryBuilder> filter = boolQueryBuilder.filter();
            //关键字
            if(StringUtils.isNotEmpty(keyword))
                filter.add(QueryBuilders.matchQuery("all",keyword));
            if(productTypeId!=null)
                filter.add(QueryBuilders.termQuery("productTypeId",productTypeId));
            if(brandId!=null)
                filter.add(QueryBuilders.termQuery("brandId",brandId));
            //最大最小价格
            BoolQueryBuilder priceQuery = new BoolQueryBuilder();
            if(minPrice!=null)
                priceQuery.must(new RangeQueryBuilder("maxPrice").gte(minPrice));
            if(maxPrice!=null)
                priceQuery.must(new RangeQueryBuilder("minPrice").lte(maxPrice));
            filter.add(priceQuery);

            builder.withQuery(boolQueryBuilder);


            //排序规则
            SortOrder sortOrder = SortOrder.ASC;
            if(StringUtils.isNotEmpty(sortType)&&sortType.equals("desc")){
                sortOrder = SortOrder.DESC;
            }
            //排序字段
            if(StringUtils.isNotEmpty(sortColumn)){
                String sort = "saleCount";
                switch (sortColumn){
                    case "xp":
                        sort = "onSaleTime";
                        break;
                    case "pl":
                        sort = "commentCount";
                        break;
                    case "rq":
                        sort = "viewCount";
                        break;
                    case "jg":
                        if(sortOrder == SortOrder.ASC){
                            sort = "minPrice";
                        }else{
                            sort = "maxPrice";
                        }
                        break;
                }
                //排序    xl xp pl jg rq
                builder.withSort(new FieldSortBuilder(sort).order(sortOrder));
            }

            //分页
            builder.withPageable(PageRequest.of(pageNum-1,pageSize));

            Page<ProductDoc> page = productRepository.search(builder.build());
            PageList<ProductDoc> pageList = new PageList<>(page.getTotalElements(),page.getContent());
            return AjaxResult.me().setSuccess(true).setRestObj(pageList);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("系统异常"+e.getMessage());
        }

    }

}
