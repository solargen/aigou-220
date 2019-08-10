package cn.itsource.aigou.service.impl;

import cn.itsource.aigou.domain.Brand;
import cn.itsource.aigou.mapper.BrandMapper;
import cn.itsource.aigou.query.BrandQuery;
import cn.itsource.aigou.service.IBrandService;
import cn.itsource.basic.util.PageList;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 品牌信息 服务实现类
 * </p>
 *
 * @author solargen
 * @since 2019-07-30
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements IBrandService {


    @Override
    //当前有事务在，则加入到当前事务中，当前没有事务，以非事务方式执行
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public PageList<Brand> queryPage(BrandQuery query) {
        //查询总数
        //查询当前页的数据
        Page<Brand> page = new Page<>(query.getPageNum(),query.getPageSize());
        IPage<Brand> ip = baseMapper.queryPage(page, query);
        //封装到PageList返回
        return new PageList<>(ip.getTotal(),ip.getRecords());
    }
    /**
     * 根据类型查询品牌
     * @param productTypeId
     * @return
     */
    @Override
    public List<Brand> queryBrandByProductTypeId(Long productTypeId) {
        return baseMapper.selectList(new QueryWrapper<Brand>().eq("product_type_id",productTypeId));
    }

    @Override
    public Set<String> queryBrandFirstLetters(Long productTypeId) {
        Set<String> set = new TreeSet<>();
        //小学问-集合的一个灵活选择
        List<Brand> brands = baseMapper.selectList(new QueryWrapper<Brand>().eq("product_type_id", productTypeId));
        for (Brand brand : brands) {
            set.add(brand.getFirstLetter());
        }
        return set;
    }

    public static void main(String[] args) {

        List<String> list = Arrays.asList("C","D","A","B");
        Collections.sort(list);
        for (String s : list) {
            System.out.println(s);
        }

    }

}
