package cn.itsource.aigou.controller;

import cn.itsource.aigou.domain.Specification;
import cn.itsource.aigou.service.IProductService;
import cn.itsource.aigou.domain.Product;
import cn.itsource.aigou.query.ProductQuery;
import cn.itsource.basic.util.AjaxResult;
import cn.itsource.basic.util.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    public IProductService productService;

    /**
    * 保存和修改公用的
    * @param product  传递的实体
    * @return Ajaxresult转换结果
    */
    @RequestMapping(value="/add",method= RequestMethod.POST)
    public AjaxResult save(@RequestBody Product product){
        try {
            if(product.getId()!=null){
                productService.updateById(product);
            }else{
                productService.save(product);
            }
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setMessage("保存对象失败！"+e.getMessage());
        }
    }

    /**
    * 删除对象信息
    * @param id
    * @return
    */
    @RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
    public AjaxResult delete(@PathVariable("id") Integer id){
        try {
            productService.removeById(id);
            return AjaxResult.me();
        } catch (Exception e) {
        e.printStackTrace();
            return AjaxResult.me().setMessage("删除对象失败！"+e.getMessage());
        }
    }

    //获取用户
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Product get(@RequestParam(value="id",required=true) Long id)
    {
        return productService.getById(id);
    }


    /**
    * 查看所有的员工信息
    * @return
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<Product> list(){

        return productService.list(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/json",method = RequestMethod.POST)
    public PageList<Product> json(@RequestBody ProductQuery query)
    {
        return productService.queryPage(query);
    }

    /**
     * 查询商品的显示属性
     * @param productId
     * @return
     */
    @GetMapping("/getViewProperties")
    public List<Specification> getViewProperties(@RequestParam("productId")Long productId){
        return productService.getViewProperties(productId);
    }

    /**
     * 查询商品的SKU属性
     * @param productId
     * @return
     */
    @GetMapping("/getSkuProperties")
    public List<Specification> getSkuProperties(@RequestParam("productId")Long productId){
        return productService.getSkuProperties(productId);
    }

    /**
     * 修改商品的显示属性
     * @param para
     * @return
     */
    @PostMapping("/updateViewProperties")
    public AjaxResult updateViewProperties(@RequestBody Map<String,Object> para){
        int productId = (Integer)para.get("productId");
        String viewProperties = (String) para.get("viewProperties");
        try {
            productService.updateViewProperties(productId,viewProperties);
            return AjaxResult.me().setSuccess(true).setMessage("修改成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("操作失败!"+e.getMessage());
        }
    }

    @PostMapping("/updateSkuProperties")
    public AjaxResult updateSkuProperties(@RequestBody Map<String,Object> param){
        //获取参数 -- debug查看参数类型
        long productId = (Integer)param.get("productId");
        List<Map<String,String>> skus = (List<Map<String, String>>) param.get("skus");
        List<Map<String,String>> skuProperties = (List<Map<String, String>>) param.get("skuProperties");

        //调用service的一个方法（保证同一个事务）
        try {
            productService.updateSkuProperties(productId,skus,skuProperties);
            return AjaxResult.me().setSuccess(true).setMessage("成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("失败!"+e.getMessage());
        }
    }



}
