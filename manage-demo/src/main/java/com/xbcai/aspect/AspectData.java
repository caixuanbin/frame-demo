package com.xbcai.aspect;

import com.xbcai.model.StaVO;
import com.xbcai.model.Station;
import com.xbcai.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Aspect
@Component
public class AspectData {
    @Pointcut("@annotation(DataCon)")
    private void insertBefore(){}

    /**
     * 环绕增强，相当于MethodInterceptor
     */
    @Around("insertBefore()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object res = null;
        long time = System.currentTimeMillis();
        Object[] args = joinPoint.getArgs();
        Arrays.asList(args).forEach(item-> System.out.println("class:"+item.getClass()));
        String s = Arrays.toString(args);
        System.out.println("方法执行前-------------传递参数："+s);
        Arrays.asList(args).forEach(item->setValue(item));
        System.out.println("方法执行前-------------改变传递参数："+s);
        res =  joinPoint.proceed();
        System.out.println("方法执行后----------------返回数据："+res);
        time = System.currentTimeMillis() - time;
        return res;
    }

    private void setValue(Object item){
        Field[] fields = item.getClass().getDeclaredFields();
        Arrays.asList(fields).forEach(field->{
            String name = field.getName();
            System.out.println("name:"+name+",type:"+field.getType());
            field.setAccessible(true);
            if(name.equals("lat")||name.equals("lng")||name.equals("staLng")||name.equals("staLat")){
                try {
                    double aDouble = (double) field.get(item);
                    field.set(item,aDouble+100);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    if(field.get(item) instanceof StaVO){
                        setValue(field.get(item));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 后置返回通知
     * 这里需要注意的是:
     * 如果参数中的第一个参数为JoinPoint，则第二个参数为返回值的信息
     * 如果参数中的第一个参数不为JoinPoint，则第一个参数为returning中对应的参数
     * returning 限定了只有目标方法返回值与通知方法相应参数类型时才能执行后置返回通知，否则不执行，对于returning对应的通知方法参数为Object类型将匹配任何目标返回值
     */
    @AfterReturning(value = "insertBefore()", returning = "keys")
    public   void doAfterReturningAdvice1(Object keys) {
        log.info("进入方法获取返回结果为：" + keys.getClass());
        Field[] fields = keys.getClass().getDeclaredFields();
        getInitValue(keys);
        if (keys instanceof List) {
            List list = (List) keys;
            list.forEach(entity->getInitValue(entity));
        }
        log.info("可在方法内修改返回结果："+ keys);
    }
    @SuppressWarnings("all")
    public void getInitValue(Object item){
        Field[] fields = item.getClass().getDeclaredFields();
        Arrays.asList(fields).forEach(field->{
            String name = field.getName();
            System.out.println("name:"+name+",type:"+field.getType());
            field.setAccessible(true);
            if(name.equals("lat")||name.equals("lng")||name.equals("staLng")||name.equals("staLat")){
                try {
                    double aDouble = (double) field.get(item);
                    field.set(item,aDouble-100);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    if(!(field.get(item) instanceof Double)&&!(field.get(item) instanceof Long)&&!(field.get(item) instanceof String)&&!(field.get(item) instanceof Integer)){
                        System.out.println("item type:::"+field.get(item).getClass());
                        getInitValue(field.get(item));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        System.out.println(Integer.class+","+Double.class);
    }

}
