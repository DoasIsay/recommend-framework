package recommend.framework.annotation;

/**
 * @author xiewenwu
 * @date 2022/4/7 18:07
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {
    String name() default "";
}
