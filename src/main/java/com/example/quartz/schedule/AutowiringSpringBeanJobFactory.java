//package com.example.quartz.schedule;
//
//import org.quartz.spi.TriggerFiredBundle;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.scheduling.quartz.SpringBeanJobFactory;
//
///**
// * Job.class 로 인스턴스 생성시에  Job 에서 Spring Bean 을 사용하기 위한 클래스
// * */
//public final class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {
//
//    private transient AutowireCapableBeanFactory beanFactory;
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        beanFactory = applicationContext.getAutowireCapableBeanFactory();
//    }
//
//    @Override
//    protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
//        final Object job = super.createJobInstance(bundle);
//        beanFactory.autowireBean(job);
//        return job;
//    }
//}