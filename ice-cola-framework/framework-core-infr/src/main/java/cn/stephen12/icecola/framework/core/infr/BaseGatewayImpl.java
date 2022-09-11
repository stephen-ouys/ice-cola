package cn.stephen12.icecola.framework.core.infr;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.stephen12.icecola.framework.core.domain.BaseGatewayI;
import cn.stephen12.icecola.framework.core.domain.IdGeneratorGateway;
import cn.stephen12.icecola.framework.core.domain.model.BaseAggregateRoot;
import cn.stephen12.icecola.framework.core.domain.model.DomainEvent;
import cn.stephen12.icecola.framework.core.infr.api.FindAggregateRootInterceptor;
import cn.stephen12.icecola.framework.core.infr.model.IJpaEntity;
import cn.stephen12.icecola.framework.core.infr.repository.BaseJpaRepository;
import com.alibaba.cola.exception.SysException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 基础的gateway实现
 *
 * @author ouyangsheng
 * @date 2022-03-22
 * @see BaseGatewayI
 * @see BaseJpaRepository
 * @see org.springframework.context.ApplicationEventPublisher
 **/
@Slf4j
public abstract class BaseGatewayImpl<D extends BaseAggregateRoot<D>, E extends IJpaEntity> implements BaseGatewayI<D> {
    protected final Class<D> domainClass = (Class<D>) ClassUtil.getSuperClassGenericType(this.getClass(), BaseGatewayImpl.class, 0);
    protected final Class<E> entityClass = (Class<E>) ClassUtil.getSuperClassGenericType(this.getClass(), BaseGatewayImpl.class, 1);

    @Autowired
    protected ApplicationContext context;
    @Autowired
    protected BaseJpaRepository<E> baseJpaRepository;
    @Autowired(required = false)
    protected IEntityConverter<D, E> entityConvertor;
    @Autowired
    protected IdGeneratorGateway idGeneratorGateway;
    @Autowired
    protected List<FindAggregateRootInterceptor> postHandlers;

    @Override
    public Optional<D> findDomainById(Long id) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start(StrUtil.format("Start congesting aggregate root{} with id:{} ", domainClass, id));

        if (id == null) {
            return Optional.empty();
        }

        Optional<D> result = baseJpaRepository.findById(id).map(convertAndHandle());

        //Finished congesting aggregate root
        stopWatch.stop();
        log.info(stopWatch.prettyPrint());
        return result;
    }

    @Override
    public D save(D domainE) {
        try {
            E entity = convertDomainToEntity(domainE);
            E savedEntity = baseJpaRepository.save(entity);
            return this.convertEntityToDomain(savedEntity);
        } catch (Throwable e) {
            throw new SysException("Persistence Exception, cause: ", e);
        }
    }

    @Override
    public <DE extends DomainEvent> void publishEvent(DE event) {
        context.publishEvent(event);
    }

    @Override
    public <DE extends DomainEvent> void publishAndIgnoreEvent(DE event) {
        try {
            context.publishEvent(event);
        } catch (Throwable e) {
            log.warn("Error occurred at listeners when publish domain event({}), cause: {}", event, e);
        }
    }

    /**
     * 批量按ID获取聚合根
     *
     * @param ids
     * @return
     */
    @Override
    public List<D> findAllDomainById(Collection<Long> ids) {
        return baseJpaRepository.findAllById(ids).stream()
                .map(convertAndHandle()).collect(Collectors.toList());
    }

    /**
     * 获取值对象
     *
     * @param id
     * @param clazz
     * @param <V>   值对象类型
     * @return
     */
    @Override
    public <V> Optional<V> findValObjById(Long id, Class<V> clazz) {
        if (id == null) {
            return Optional.empty();
        }
        return findDomainById(id).map(e -> BeanUtil.toBean(e, clazz));
    }

    /**
     * 批量按ID获取值对象
     *
     * @param ids
     * @param clazz
     * @param <V>
     * @return
     */
    @Override
    public <V> List<V> findAllValObjById(Collection<Long> ids, Class<V> clazz) {
        return findAllDomainById(ids).stream().map(item -> BeanUtil.toBean(item, clazz)).collect(Collectors.toList());
    }

    /**
     * 移除领域对象
     * @param id
     */
    @Override
    public void removeById(Long id) {
        this.baseJpaRepository.deleteById(id);
    }

    /**
     * 由Entity转换为聚合根（默认实现）
     *
     * @param entity
     * @return
     */
    protected D convertEntityToDomain(E entity) {
        if (entityConvertor != null) {
            D domainBean = entityConvertor.convertEntityToDomain(entity);
            this.context.getAutowireCapableBeanFactory().autowireBean(domainBean);
            return domainBean;
        }

        D domainBean = context.getBean(domainClass);
        BeanUtil.copyProperties(entity, domainBean);
        return domainBean;
    }

    /**
     * 由聚合根转换为Entity（默认实现）
     *
     * @param domainE
     * @return
     */
    protected E convertDomainToEntity(D domainE) {
        if (entityConvertor != null) {
            return entityConvertor.convertDomainToEntity(domainE);
        }
        E entity = BeanUtil.toBean(domainE, this.entityClass);
        return entity;
    }

    /**
     * 转换并且处理
     *
     * @return
     */
    protected Function<E, D> convertAndHandle() {
        return e -> {
            D d = this.convertEntityToDomain(e);
            if (postHandlers != null) {
                for (FindAggregateRootInterceptor handler : postHandlers) {
                    handler.postHandle(d);
                }
            }
            return d;
        };
    }

    /**
     * 获取分布式ID
     *
     * @return
     */
    @Override
    public Long generateId() {
        return generateId(this.domainClass);
    }

    /**
     * 生成唯一业务编号
     *
     * @return
     */
    @Override
    public String generateNo() {
        return generateNo(this.domainClass);
    }

    /**
     * 获取分布式ID
     *
     * @return
     */
    @Override
    public Long generateId(Class<?> clazz) {
        return idGeneratorGateway.generateId(clazz);
    }

    /**
     * 生成唯一业务编号
     *
     * @return
     */
    @Override
    public String generateNo(Class<?> clazz) {
        return idGeneratorGateway.generateNo(clazz);
    }

}


/**
 * 反射工具类，提供反射相关的快捷操作
 *
 * @author Caratacus
 * @author hcl
 * @since 2016-09-22
 */
 class ClassUtil  {


    /**
     * 代理 class 的名称
     */
    private static final List<String> PROXY_CLASS_NAMES = Arrays.asList("net.sf.cglib.proxy.Factory"
            // cglib
            , "org.springframework.cglib.proxy.Factory"
            , "javassist.util.proxy.ProxyObject"
            // javassist
            , "org.apache.ibatis.javassist.util.proxy.ProxyObject");

    /**
     * <p>
     * 反射对象获取泛型
     * </p>
     *
     * @param clazz      对象
     * @param genericIfc 所属泛型父类
     * @param index      泛型所在位置
     * @return Class
     */
    public static Class<?> getSuperClassGenericType(final Class<?> clazz, final Class<?> genericIfc, final int index) {
        Class<?>[] typeArguments = GenericTypeResolver.resolveTypeArguments(getUserClass(clazz), genericIfc);
        return null == typeArguments ? null : typeArguments[index];
    }

    /**
     * 判断是否为代理对象
     *
     * @param clazz 传入 class 对象
     * @return 如果对象class是代理 class，返回 true
     */
    public static boolean isProxy(Class<?> clazz) {
        if (clazz != null) {
            for (Class<?> cls : clazz.getInterfaces()) {
                if (PROXY_CLASS_NAMES.contains(cls.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <p>
     * 获取当前对象的 class
     * </p>
     *
     * @param clazz 传入
     * @return 如果是代理的class，返回父 class，否则返回自身
     */
    public static Class<?> getUserClass(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return isProxy(clazz) ? clazz.getSuperclass() : clazz;
    }

}