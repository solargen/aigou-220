package cn.itsource.aigou.mapper;

import cn.itsource.aigou.domain.ProductExt;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 商品扩展 Mapper 接口
 * </p>
 *
 * @author solargen
 * @since 2019-08-04
 */
@Component
public interface ProductExtMapper extends BaseMapper<ProductExt> {

    void updateViewProperties(@Param("productId") long productId, @Param("viewProperties") String viewProperties);
}
