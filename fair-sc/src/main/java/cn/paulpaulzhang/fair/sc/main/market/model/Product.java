package cn.paulpaulzhang.fair.sc.main.market.model;

import cn.paulpaulzhang.fair.sc.database.Entity.ProductCache;

/**
 * 包名：cn.paulpaulzhang.fair.sc.main.market.model
 * 创建时间：8/27/19
 * 创建人： paulpaulzhang
 * 描述：
 */
public class Product {

    private ProductCache productCache;

    public Product(ProductCache productCache) {
        this.productCache = productCache;
    }

    public ProductCache getProductCache() {
        return productCache;
    }
}
