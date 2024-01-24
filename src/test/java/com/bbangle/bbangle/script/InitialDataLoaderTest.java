package com.bbangle.bbangle.script;

import com.bbangle.bbangle.scripts.InitialDataLoader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InitialDataLoaderTest {
    @Autowired
    InitialDataLoader initialDataLoader;

    @Test
    public void loadData(){
        initialDataLoader.loadData();
    }
}