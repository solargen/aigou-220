package cn.itsource.aigou.service;

import cn.itsource.aigou.domain.Product;
import cn.itsource.aigou.domain.Specification;
import cn.itsource.aigou.query.ProductQuery;
import cn.itsource.basic.util.AjaxResult;
import cn.itsource.basic.util.PageList;
import cn.itsource.common.domain.ProductDoc;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品 服务类
 * </p>
 *
 * @author solargen
 * @since 2019-08-04
 */
public interface IProductService extends IService<Product> {

    PageList<Product> queryPage(ProductQuery query);


    List<Specification> getViewProperties(Long productId);

    void updateViewProperties(long productId, String viewProperties);

    List<Specification> getSkuProperties(Long productId);

    void updateSkuProperties(long productId, List<Map<String, String>> skus, List<Map<String, String>> skuProperties);

    //上架
    void onSale(List<Long> idsLong);
    //下架
    void offSale(List<Long> idsLong);

    /**
     * 在线商城搜索上架的商品
     * @param params
     * @return
     */
    PageList<ProductDoc> queryOnSaleProduct(Map<String, Object> params);
}
