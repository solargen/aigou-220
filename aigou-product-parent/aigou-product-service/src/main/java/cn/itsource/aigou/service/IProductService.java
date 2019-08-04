package cn.itsource.aigou.service;

import cn.itsource.aigou.domain.Product;
import cn.itsource.aigou.query.ProductQuery;
import cn.itsource.basic.util.PageList;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

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



}
