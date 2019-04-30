package com.lcl.Util;

import com.lcl.pojo.Product;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductUtil {

    private static List<Product> file2list(String filename) throws IOException {
        File file = new File(filename);
        List<String> lines = FileUtils.readLines(file, "UTF-8");
        List<Product> products = new ArrayList<Product>();
        for (String line : lines) {
            Product product = line2product(line);
            products.add(product);
        }
        return products;
    }
//10001,房屋卫士自流平美缝剂瓷砖地砖专用双组份真瓷胶防水填缝剂镏金色,品质建材,398.00,上海,540785126782
    private static Product line2product(String line) {
        String[] fields = line.split(",");
        Product product = new Product();
        product.setId(Integer.parseInt(fields[0]));
        product.setName(fields[1]);
        product.setCategory(fields[2]);
        product.setPrice(Float.parseFloat(fields[3]));
        product.setPlace(fields[4]);
        product.setCode(fields[5]);
        return product;
    }

    public static List<Product> listCreat() throws IOException {
        String filename = "C:\\java\\data\\140k_products.txt";
        List<Product> products = file2list(filename);
        System.out.println(products.size());
        return products;
    }
}
