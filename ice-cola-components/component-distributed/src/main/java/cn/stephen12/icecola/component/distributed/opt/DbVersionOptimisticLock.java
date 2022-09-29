package cn.stephen12.icecola.component.distributed.opt;

import cn.stephen12.icecola.framework.core.infr.id.IdNoGenerator;
import com.alibaba.cola.domain.ApplicationContextHelper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.util.CollectionUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据库版本型乐观锁
 *
 * @author ouyangsheng
 * @since 2022-09-01
 **/
@NoArgsConstructor
@Data
@Entity
@Table(name = DbVersionOptimisticLock.TABLE_NAME)
public class DbVersionOptimisticLock implements VersionOptimisticLock {
    static final String TABLE_NAME = "ver_opt_lock";

    @Id
    private Long id;

    @Column(name = "lock_group", updatable = false)
    private String group;

    @Column(name = "lock_key", updatable = false)
    private String key;

    @Column(name = "lock_version")
    private Long version;

    @Column(name = "c_t", updatable = false)
    @CreationTimestamp
    private LocalDateTime createTime;

    @Column(name = "u_t")
    @UpdateTimestamp
    private LocalDateTime updateTime;

    private DbVersionOptimisticLock(String group, String key) {
        Long id = ApplicationContextHelper.getBean(IdNoGenerator.class).generateId(TABLE_NAME);
        this.id = id;
        this.group = group;
        this.key = key;
        this.version = 0L;
    }

    @Override
    public boolean computeIfMatch(Long v, Consumer<Long> action) {
        if (this.version <= v) {
            //保存 并强制 Flush，如果失败，说明数据库已存在更高版本
            boolean success = saveAndCompareVersion(this, v);
            if (success) {
                this.version = v;
                action.accept(v);
                return true;
            }
        }
        return false;
    }

    /**
     * 按分组和Key获取锁(如果没有会自动创建）
     *
     * @param group 分组ID
     * @param key   Key
     * @return
     */
    public static VersionOptimisticLock get(String group, String key) {
        DbVersionOptimisticLock lock = ApplicationContextHelper.getBean(DbVersionOptimisticLockRepository.class).findByGroupAndKey(group, key);
        if (lock == null) {
            lock = new DbVersionOptimisticLock(group, key);
            lock = persist(lock);
        }
        return lock;
    }

    /**
     * 按分组和Key获取锁(如果没有会自动创建）
     *
     * @param group 分组ID
     * @param keys  一组Key
     * @return
     */
    public static List<VersionOptimisticLock> get(String group, String... keys) {
        return get(group, CollectionUtils.arrayToList(keys));
    }

    /**
     * 按分组和Key获取锁(如果没有会自动创建）
     *
     * @param group 分组
     * @param keys  一组Key
     * @return
     */
    public static List<VersionOptimisticLock> get(String group, Collection<String> keys) {
        Map<String, DbVersionOptimisticLock> keyToLockMap = ApplicationContextHelper.getBean(DbVersionOptimisticLockRepository.class)
                .findByGroupAndKeyIn(group, keys).stream()
                .collect(Collectors.toMap(DbVersionOptimisticLock::getKey, Function.identity()));

        return keys.stream().map(key -> {
            DbVersionOptimisticLock lock = keyToLockMap.get(key);
            if (lock == null) {
                lock = new DbVersionOptimisticLock(group, key);
                lock = persist(lock);
            }
            return lock;
        }).collect(Collectors.toList());
    }

    /**
     * 保存并刷新
     *
     * @param lock
     * @return
     */
    private static DbVersionOptimisticLock persist(DbVersionOptimisticLock lock) {
        DbVersionOptimisticLockRepository repository = ApplicationContextHelper
                .getBean(DbVersionOptimisticLockRepository.class);
        return repository.save(lock);
    }

    /**
     * 保存并刷新
     *
     * @param lock
     * @param v    要对比的version
     * @return
     */
    private static boolean saveAndCompareVersion(DbVersionOptimisticLock lock, Long v) {
        DbVersionOptimisticLockRepository repository = ApplicationContextHelper
                .getBean(DbVersionOptimisticLockRepository.class);

        //比较更新 version
        int count = repository.updateAndCompareVersion(lock.id, v);
        return count > 0;
    }


}
@ConditionalOnClass(EntityManager.class)
@Configuration
@EnableJpaRepositories(basePackages = "cn.stephen12.icecola.component.distributed.opt")
@EntityScan(basePackages = "cn.stephen12.icecola.component.distributed.opt")
class DbVersionOptimisticLockConfig {
}

/**
 * 锁 repository
 */
interface DbVersionOptimisticLockRepository extends JpaRepository<DbVersionOptimisticLock, Long> {

    /**
     * 按Group 和 Key 获取锁
     *
     * @param group
     * @param key
     * @return
     */
    DbVersionOptimisticLock findByGroupAndKey(String group, String key);

    /**
     * 按Group 和 Keys 获取一批锁
     *
     * @param group
     * @param keys
     * @return
     */
    List<DbVersionOptimisticLock> findByGroupAndKeyIn(String group, Collection<String> keys);

    /**
     * 更新且比较版本
     *
     * @param id
     * @param version
     * @return 更新条数
     * @see @Modifying(flushAutomatically = true)
     */
    @Query(value = "update ver_opt_lock set lock_version=?2 where id=?1 and lock_version<=?2", nativeQuery = true)
    @Modifying(flushAutomatically = true)
    int updateAndCompareVersion(@Param("id") Long id, @Param("version") Long version);
}