package cn.itsource.aigou.mapper;

import cn.itsource.aigou.domain.Brand;
import cn.itsource.aigou.query.BrandQuery;
import cn.itsource.basic.util.PageList;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 品牌信息 Mapper 接口
 * </p>
 *
 * @author solargen
 * @since 2019-07-30
 */
public interface BrandMapper extends BaseMapper<Brand> {

    /**
     * 分页条件查询
     * @param page
     * @param query
     * @return
     */
    IPage<Brand> queryPage(Page page, @Param("query")BrandQuery query);
}
