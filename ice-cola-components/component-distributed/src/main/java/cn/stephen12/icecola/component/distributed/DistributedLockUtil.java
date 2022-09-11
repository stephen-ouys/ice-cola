package cn.stephen12.icecola.component.distributed;

import com.alibaba.cola.exception.BizException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * <p>
 * 分布式锁工具类 Redisson 实现。
 * </p>
 *
 * @author ouyangsheng
 * @date 2022-03-14
 */
@Slf4j
@Component
public class DistributedLockUtil {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private Redisson redisson;

    /**
     * 获取锁，带返回结果
     *
     * @param processFunction 操作代码函数 ()-> { return obj; }
     * @param waitTime 超时时间
     * @param lockName 锁名称函数 ()-> str
     * @return
     */
    public <T> T lock(Supplier<T> processFunction, Integer waitTime, Supplier<String> lockName) {
        AbstractLockTemplateWithResult<T> lockTemplateWithResult = new AbstractLockTemplateWithResult<T>() {
            @Override
            public T processWithResult() {
                return processFunction.get();
            }
        };
        getLock(lockTemplateWithResult, waitTime, lockName.get());
        return lockTemplateWithResult.getResult();
    }

    /**
     * 获取锁
     *
     * @param processFunction 操作代码函数 ()-> { }
     * @param waitTime 等待时间
     * @param lockName 锁名称函数 ()-> str
     */
    public void lock(LockTemplate processFunction, Integer waitTime, Supplier<String> lockName) {
        getLock(processFunction, waitTime, lockName.get());
    }

    /**
     * 获取锁，默认无返回结果
     *
     * @param lockTemplate
     * @param time
     */
    private void getLock(LockTemplate lockTemplate, Integer time, String lockName) {
        RLock lock = null;
        try {
            //上锁
            lock = redisson.getLock(lockName);
            try {
                if (!lock.tryLock(time, TimeUnit.SECONDS)) {
                    log.error("无法获取锁{}", lockName);
                    throw new BizException("无法获取锁" + lockName);
                }
            } catch (InterruptedException e) {
                throw new BizException("无法获取锁" + lockName, e);
            }
            lockTemplate.process();
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }
    }

    @Getter
    private abstract class AbstractLockTemplateWithResult<T> implements LockTemplate {
        private T result;

        /**
         * 业务代码
         *
         * @return
         */
        public abstract T processWithResult();

        @Override
        public void process() {
            this.result = processWithResult();
        }
    }

    @FunctionalInterface
    public interface LockTemplate {
        /**
         * 业务代码
         */
        void process();
    }


}