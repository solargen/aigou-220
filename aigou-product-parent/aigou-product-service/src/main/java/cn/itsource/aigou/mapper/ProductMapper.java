package cn.itsource.aigou.mapper;

import cn.itsource.aigou.domain.Product;
import cn.itsource.aigou.query.ProductQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品 Mapper 接口
 * </p>
 *
 * @author solargen
 * @since 2019-08-04
 */
public interface ProductMapper extends BaseMapper<Product> {

    IPage<Product> queryPage(Page page, @Param("query") ProductQuery query);

    void onSale(@Param("ids") List<Long> idsLong, @Param("time") long time);

    List<Product> selectByIds(List<Long> idsLong);

    void offSale(@Param("ids") List<Long> idsLong, @Param("time") long time);

}
