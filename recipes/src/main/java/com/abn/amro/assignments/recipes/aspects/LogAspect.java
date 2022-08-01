package com.abn.amro.assignments.recipes.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
@Order(value = 2)
public class LogAspect {

	@Around("execution(* com.abn.amro.assignments.recipes.controllers..*.*(..))")
	public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable{
		 final Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass().getName());
	        Object retVal = null;

	        try {
	            StringBuffer startMessageStringBuffer = new StringBuffer();

	            startMessageStringBuffer.append("Start method ");
	            startMessageStringBuffer.append(joinPoint.getSignature().getName());
	            startMessageStringBuffer.append("(");

	            Object[] args = joinPoint.getArgs();
	            for (int i = 0; i < args.length; i++) {
	                startMessageStringBuffer.append(args[i]).append(",");
	            }
	            if (args.length > 0) {
	                startMessageStringBuffer.deleteCharAt(startMessageStringBuffer.length() - 1);
	            }

	            startMessageStringBuffer.append(")");

	            logger.info(startMessageStringBuffer.toString());

	            StopWatch stopWatch = new StopWatch();
	            stopWatch.start();

	            retVal = joinPoint.proceed();

	            stopWatch.stop();

	            StringBuffer endMessageStringBuffer = new StringBuffer();
	            endMessageStringBuffer.append("Finish method ");
	            endMessageStringBuffer.append(joinPoint.getSignature().getName());
	            endMessageStringBuffer.append("(..); execution time: ");
	            endMessageStringBuffer.append(stopWatch.getTotalTimeMillis());
	            endMessageStringBuffer.append(" ms;");

	            logger.info(endMessageStringBuffer.toString());
	        } catch (Throwable ex) {
	            StringBuffer errorMessageStringBuffer = new StringBuffer();

	             // Create error message with exception  
	             logger.error(errorMessageStringBuffer.toString(), ex);    
	            throw ex;
	        }

	        return retVal;
	}
}
