package cn.com.fanyu.config;


import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 切面
 *
 * @author Bird
 */
@Aspect
@Component
public class MyInterceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private String requestPath = null; // 请求地址
    private Map<?, ?> inputParamMap = null; // 传入参数
    private Map<String, Object> outputParamMap = null; // 存放输出结果
    private long startTimeMillis = 0; // 开始时间
    private long endTimeMillis = 0; // 结束时间


    @Pointcut("execution(* cn.com.fanyu.controller.*.*(..))")
    private void anyMethod() {
    }//定义一个切入点

//    @Before(value = "anyMethod()")
//    public void doAccessCheck() {
//        System.out.println("前置通知");
//    }

//    @AfterReturning("anyMethod()")
//    public void doAfter() {
//        System.out.println("后置通知");
//    }

    @After("anyMethod()")
    public void after() {
//        System.out.println("最终通知");
    }

//    @AfterThrowing("anyMethod()")
//    public void doAfterThrow() {
//        System.out.println("例外通知");
//    }

    @Around("anyMethod()")
    public Object doBasicProfiling(ProceedingJoinPoint joinPoint) throws Throwable {
//        System.out.println("进入环绕通知");
        Object args[] = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // join arguments.
        logger.info("========{}.{} : {} ", method.getDeclaringClass().getName(), method.getName(), StringUtils.join(args, " ; "));

        ///////////////////////
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        // 获取输入参数
        inputParamMap = request.getParameterMap();
        // 获取请求地址
        requestPath = request.getRequestURI();
        startTimeMillis = System.currentTimeMillis(); // 记录方法执行完成的时间
        Object object = joinPoint.proceed();//执行该方法
        endTimeMillis = System.currentTimeMillis(); // 记录方法执行完成的时间

//        System.out.println("退出方法");
        outputParamMap = new HashMap<>();
        outputParamMap.put("result", object);
        this.printOptLog();
        return object;
    }

    private void printOptLog() {
        String optTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTimeMillis);
        logger.info("\n  url：" + requestPath + "; op_time：" + optTime + " pro_time：" + (endTimeMillis - startTimeMillis) + "ms ;"
                + " param：" + JSON.toJSONString(inputParamMap) + ";" + "\n result：" + JSON.toJSONString(outputParamMap));
    }
}