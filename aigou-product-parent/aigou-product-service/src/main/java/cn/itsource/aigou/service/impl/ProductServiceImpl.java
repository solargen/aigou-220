package cn.itsource.aigou.service.impl;

import cn.itsource.aigou.domain.Product;
import cn.itsource.aigou.domain.ProductExt;
import cn.itsource.aigou.mapper.ProductExtMapper;
import cn.itsource.aigou.mapper.ProductMapper;
import cn.itsource.aigou.query.ProductQuery;
import cn.itsource.aigou.service.IProductService;
import cn.itsource.basic.util.PageList;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 商品 服务实现类
 * </p>
 *
 * @author solargen
 * @since 2019-08-04
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Autowired
    private ProductExtMapper productExtMapper;

    @Override
    public PageList<Product> queryPage(ProductQuery query) {
        IPage<Product> iPage = baseMapper.queryPage(new Page(query.getPageNum(), query.getPageSize()), query);
        return new PageList<>(iPage.getTotal(),iPage.getRecords());
    }

    /**
     * 重写save方法，同时向product表和productExt表中插入数据
     * @param entity
     * @return
     */
    @Override
    @Transactional
    public boolean save(Product entity) {
        //初始化部分字段的值
        entity.setCreateTime(new Date().getTime());
        entity.setState(0);
        baseMapper.insert(entity);//插入后要获取到id
        System.out.println(entity.getId()+"====================================");
        ProductExt productExt = entity.getProductExt();
        //设置productId
        productExt.setProductId(entity.getId());
        productExtMapper.insert(productExt);
        return true;
    }
}
