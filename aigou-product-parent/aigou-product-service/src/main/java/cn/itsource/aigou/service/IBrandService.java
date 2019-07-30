package cn.itsource.aigou.service;

import cn.itsource.aigou.domain.Brand;
import cn.itsource.aigou.query.BrandQuery;
import cn.itsource.basic.util.PageList;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 品牌信息 服务类
 * </p>
 *
 * @author solargen
 * @since 2019-07-30
 */
public interface IBrandService extends IService<Brand> {

    /**
     * 分页查询
     * @param query
     * @return
     */
    PageList<Brand> queryPage(BrandQuery query);
}
