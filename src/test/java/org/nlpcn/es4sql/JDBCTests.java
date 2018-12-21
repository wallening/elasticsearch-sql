package org.nlpcn.es4sql;


import com.alibaba.druid.pool.DruidDataSource;

import com.alibaba.druid.pool.ElasticSearchDruidDataSourceFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.junit.Assert;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by allwefantasy on 8/26/16.
 */
public class JDBCTests {
    @Test
    public void testJDBC() throws Exception {
        String sql = "SELECT  gender,lastname,age from  " + TestsConstants.TEST_INDEX + " where lastname='Heath'";
        Properties properties = new Properties();
        properties.put("url", "jdbc:elasticsearch://127.0.0.1:9300/");
        DruidDataSource dds = (DruidDataSource) ElasticSearchDruidDataSourceFactory.createDataSource(properties);
        Connection connection = dds.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int colSize = metaData.getColumnCount();
        JSONArray arr = new JSONArray();
        
        while (resultSet.next()) {
            JSONObject obj = new JSONObject();
            for (int i = 1; i <= colSize; i++) {
                obj.put(metaData.getColumnLabel(i), resultSet.getObject(i));
            }
            arr.add(obj);
        }

        ps.close();
        connection.close();
        dds.close();
        
        System.out.println(arr.size());
        System.out.println(arr);
    }

}


