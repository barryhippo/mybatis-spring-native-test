package com.example.demo.controller;

import com.example.demo.mapper.DbMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("test")
public class DbTest {

    private DbMapper dbMapper;

    @Autowired
    public void setDbMapper(DbMapper dbMapper) {
        this.dbMapper = dbMapper;
    }


    @GetMapping("db")
    public Object db() {
        Integer size = dbMapper.testJobs();
        Map<String, String> map = new HashMap<>();
        map.put("code", "00000000");
        map.put("message", "success");
        map.put("data", String.valueOf(size));
        return map;
    }
}
