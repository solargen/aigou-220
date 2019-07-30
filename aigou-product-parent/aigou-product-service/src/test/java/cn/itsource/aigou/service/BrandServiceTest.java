package cn.itsource.aigou.service;

import cn.itsource.aigou.domain.Brand;
import cn.itsource.aigou.query.BrandQuery;
import cn.itsource.basic.util.PageList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BrandServiceTest {

    @Autowired
    private IBrandService brandService;

    @Test
    public void test(){
        brandService.list().forEach(e->{
            System.out.println(e);
        });
    }

    @Test
    public void queryPage() {
        BrandQuery query = new BrandQuery();
        query.setKeyword("狼");
        PageList<Brand> pageList = brandService.queryPage(query);
        System.out.println(pageList.getTotal());
        pageList.getRows().forEach(e->{
            System.out.println(e);
        });

    }
}