package com.example.demo.aot;

import com.example.demo.mapper.DbMapper;
import org.apache.ibatis.executor.loader.ProxyFactory;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.scripting.defaults.RawLanguageDriver;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class DemoRuntimeHints implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        if (ClassUtils.isPresent("ch.qos.logback.classic.Logger", classLoader)) {
            hints.reflection().registerType(TypeReference.of("ch.qos.logback.classic.Logger"), hint -> hint.withMethod("log",
                    Arrays.asList(TypeReference.of("org.slf4j.Marker"), TypeReference.of(String.class), TypeReference.of("int"),
                            TypeReference.of(String.class), TypeReference.of(Object[].class), TypeReference.of(Throwable.class)),
                    ExecutableMode.INVOKE));
        }
        hints.reflection().registerType(Slf4jImpl.class,
                hint -> hint.withMembers(MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS));

        hints.proxies().registerJdkProxy(Interceptor.class);
        hints.proxies().registerJdkProxy(ProxyFactory.class);
        hints.reflection().registerType(ArrayList.class, hint -> hint.withMembers(MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS));

        // TODO define all mapper
        hints.proxies().registerJdkProxy(DbMapper.class);
        hints.reflection().registerType(DbMapper.class, hint ->
                hint.withMembers(MemberCategory.INVOKE_PUBLIC_METHODS, MemberCategory.INVOKE_DECLARED_METHODS));

//        hints.resources().registerType(org.apache.ibatis.javassist.util.proxy.ProxyFactory.class);
//        hints.resources().registerType(javassist.util.proxy.ProxyFactory.class);
        hints.reflection().registerType(org.apache.ibatis.javassist.util.proxy.ProxyFactory.class, hint ->
                hint.withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS));
        hints.reflection().registerType(javassist.util.proxy.ProxyFactory.class, hint ->
                hint.withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS));
        hints.reflection().registerType(XMLLanguageDriver.class, hint ->
                hint.withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS));
        hints.reflection().registerType(RawLanguageDriver.class, hint ->
                hint.withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS));
        hints.reflection().registerType(SqlSessionFactoryBean.class, hint ->
                hint.withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS));

        hints.resources().registerPattern("com/example/**/mapper/*.xml");
        hints.resources().registerPattern("com/mysql/cj/*.properties");
    }
}
