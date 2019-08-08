package cn.itsource.aigou.service.impl;

import cn.itsource.aigou.domain.Product;
import cn.itsource.aigou.domain.ProductExt;
import cn.itsource.aigou.domain.Sku;
import cn.itsource.aigou.domain.Specification;
import cn.itsource.aigou.mapper.ProductExtMapper;
import cn.itsource.aigou.mapper.ProductMapper;
import cn.itsource.aigou.mapper.SkuMapper;
import cn.itsource.aigou.mapper.SpecificationMapper;
import cn.itsource.aigou.query.ProductQuery;
import cn.itsource.aigou.service.IProductService;
import cn.itsource.basic.util.PageList;
import cn.itsource.common.client.ProductESClient;
import cn.itsource.common.domain.ProductDoc;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.FacesRequestAttributes;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private SpecificationMapper specificationMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private ProductESClient productESClient;


    /**
     * 上架
     * @param idsLong
     */
    @Override
    @Transactional
    public void onSale(List<Long> idsLong) {
        //修改数据库的状态和上架时间
        baseMapper.onSale(idsLong,new Date().getTime());
        //根据ids查询要上架的商品信息
        List<Product> products = baseMapper.selectByIds(idsLong);
        //调用公共服务的接口将商品信息保存到es中
        productESClient.saveBath(products2Docs(products));
    }

    /**
     * 下架
     * @param idsLong
     */
    @Override
    public void offSale(List<Long> idsLong) {
        //修改数据库
        baseMapper.offSale(idsLong,new Date().getTime());
        //删除es
        productESClient.deleteBatch(idsLong);
    }

    /**
     * 集合转换
     * @param products
     * @return
     */
    private List<ProductDoc> products2Docs(List<Product> products) {
        List<ProductDoc> docList = new ArrayList<>();
        ProductDoc productDoc = null;
        for (Product product : products) {
            productDoc = product2Doc(product);
            docList.add(productDoc);
        }
        return docList;
    }

    /**
     * 对象转换
     * @param product
     * @return
     */
    private ProductDoc product2Doc(Product product) {
        ProductDoc productDoc = new ProductDoc();
        productDoc.setId(product.getId());

        String all = product.getName()+" "+product.getSubName()+" "
                +product.getBrand().getName()+" "+product.getProductType().getName();
        productDoc.setAll(all);

        productDoc.setBrandId(product.getBrandId());
        productDoc.setProductTypeId(product.getProductTypeId());


        //查询所有的sku，获取最大的价格和最下的价格
        List<Sku> skus = skuMapper.selectList(new QueryWrapper<Sku>().eq("productId", product.getId()));
        if(skus!=null&&skus.size()>0){
            Integer maxPrice = skus.get(0).getPrice();
            Integer minPrice = skus.get(0).getPrice();
            for (Sku sku : skus) {
                if(sku.getPrice()>=maxPrice){
                    maxPrice = sku.getPrice();
                }
                if(sku.getPrice()<=minPrice){
                    minPrice = sku.getPrice();
                }
            }
            productDoc.setMaxPrice(maxPrice);
            productDoc.setMinPrice(minPrice);
        }
        productDoc.setSaleCount(product.getSaleCount());
        productDoc.setOnSaleTime(product.getOnSaleTime());
        productDoc.setCommentCount(product.getCommentCount());
        productDoc.setViewCount(product.getViewCount());
        productDoc.setName(product.getName());
        productDoc.setSubName(product.getSubName());
        productDoc.setMedias(product.getMedias());

        productDoc.setViewProperties(product.getProductExt().getViewProperties());
        productDoc.setSkuProperties(product.getProductExt().getSkuProperties());
        return productDoc;
    }


    @Override
    public PageList<Product> queryPage(ProductQuery query) {
        IPage<Product> iPage = baseMapper.queryPage(new Page(query.getPageNum(), query.getPageSize()), query);
        return new PageList<>(iPage.getTotal(),iPage.getRecords());
    }

    @Override
    public List<Specification> getViewProperties(Long productId) {
        //先到product_ext中查询
        ProductExt productExt = productExtMapper.selectOne(new QueryWrapper<ProductExt>().eq("productId", productId));
        //如果viewProperties属性为null
        if(productExt.getViewProperties()!=null){
            List<Specification> viewPropertiesList = JSON.parseArray(productExt.getViewProperties(), Specification.class);
            return viewPropertiesList;
        }
        //再查询属性表
        Product product = baseMapper.selectById(productId);
        Long productTypeId = product.getProductTypeId();
        List<Specification> specifications = specificationMapper.selectList(new QueryWrapper<Specification>().eq("product_type_id", productTypeId).eq("isSku",0));
        return specifications;
    }

    /**
     * 修改商品的显示属性
     * @param productId
     * @param viewProperties
     */
    @Override
    public void updateViewProperties(long productId, String viewProperties) {
        productExtMapper.updateViewProperties(productId,viewProperties);
    }

    @Override
    public List<Specification> getSkuProperties(Long productId) {
        //先到product_ext中查询
        ProductExt productExt = productExtMapper.selectOne(new QueryWrapper<ProductExt>().eq("productId", productId));
        //如果skuProperties属性不为null
        if(productExt.getSkuProperties()!=null){
            List<Specification> viewPropertiesList = JSON.parseArray(productExt.getSkuProperties(), Specification.class);
            return viewPropertiesList;
        }
        //再查询属性表
        Product product = baseMapper.selectById(productId);
        Long productTypeId = product.getProductTypeId();
        List<Specification> specifications = specificationMapper.selectList(new QueryWrapper<Specification>().eq("product_type_id", productTypeId).eq("isSku",1));
        return specifications;
    }

    /**
     * sku属性的维护
     * @param productId
     * @param skus
     * @param skuProperties
     */
    @Override
    public void updateSkuProperties(long productId, List<Map<String, String>> skus, List<Map<String, String>> skuProperties) {
        //修改商品详情表中的skuProperties
        String skuPropertiesStr = JSON.toJSONString(skuProperties);
        productExtMapper.updateSkuProperties(productId,skuPropertiesStr);
        //修改sku
        //(1)根据商品id删除所有的sku
        skuMapper.delete(new QueryWrapper<Sku>().eq("productId",productId));
        //(2)添加
        List<Sku> skuList = toSkuList(skus,productId);
        skuList.forEach(sku->{
            skuMapper.insert(sku);
        });
    }

    private List<Sku> toSkuList(List<Map<String, String>> skus, long productId) {
        List<Sku> list = new ArrayList<>();
        Sku sku = null;
        for (Map<String, String> map : skus) {
            sku = new Sku();
            sku.setCreateTime(new Date().getTime());
            sku.setProductId(productId);

            String skuName = "";//萝莉土豪金
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if(entry.getKey().equals("price")||entry.getKey().equals("store")||entry.getKey().equals("indexes")){
                    continue;
                }
                skuName+=entry.getValue();
            }
            sku.setSkuName(skuName);

            sku.setPrice(Integer.parseInt(map.get("price")));
            sku.setAvailableStock(Integer.parseInt(map.get("store")));
            sku.setIndexs(map.get("indexes"));

            list.add(sku);
        }

        return list;
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
