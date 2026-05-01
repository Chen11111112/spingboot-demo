package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
public class Config {

    public static final String DATABASE_NAME = "demo";

    @Bean
    public TransactionInterceptor customTransactionInterceptor(TransactionManager transactionManager) {
        NameMatchTransactionAttributeSource attributeSource = new NameMatchTransactionAttributeSource();

        RuleBasedTransactionAttribute requiredAttribute = new RuleBasedTransactionAttribute();
        requiredAttribute.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(RuntimeException.class)));
        requiredAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        DefaultTransactionAttribute readOnlyAttribute = new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        readOnlyAttribute.setReadOnly(true);

        Map<String, TransactionAttribute> namedMap = new HashMap<>();
        namedMap.put("add*", requiredAttribute);
        namedMap.put("save*", requiredAttribute);
        namedMap.put("create*", requiredAttribute);
        namedMap.put("update*", requiredAttribute);
        namedMap.put("delete*", requiredAttribute);
        namedMap.put("find*", readOnlyAttribute);
        namedMap.put("get*", readOnlyAttribute);
        namedMap.put("*", readOnlyAttribute);

        attributeSource.setNameMap(namedMap);
        return new TransactionInterceptor(transactionManager, attributeSource);
    }
}