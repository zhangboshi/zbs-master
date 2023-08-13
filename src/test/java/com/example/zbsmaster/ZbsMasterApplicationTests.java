package com.example.zbsmaster;

import com.github.pagehelper.PageHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
class ZbsMasterApplicationTests {
    @Autowired
    private DataSource dataSource;
    @Test
    void connectDatabase() throws SQLException {
        System.out.println(dataSource.getConnection());
    }

    @Test
    void contextLoads() {
    }

}
