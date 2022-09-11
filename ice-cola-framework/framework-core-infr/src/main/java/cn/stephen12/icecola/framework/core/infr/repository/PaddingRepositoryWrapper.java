package cn.stephen12.icecola.framework.core.infr.repository;

/**
 * DataPadding功能的增强
 *
 * @param <E> 统一的承载对象
 * @author ouyangsheng
 * @date 2022-05-23
 **/
//public interface PaddingRepositoryWrapper<E> extends RepositoryWrapper<E, Long>, QueryCache<Long, E> {
//
//    /**
//     * 默认获取Id的策略
//     *
//     * @param entity
//     * @return
//     */
//    @Override
//    default Long getIdOf(E entity) {
//        if (entity instanceof IdV) {
//            return ((IdV) entity).obtainId();
//        } else if (entity instanceof BaseV) {
//            return ((BaseV) entity).getId();
//        } else {
//            return BeanUtil.getProperty(entity, "id");
//        }
//    }
//
//    /**
//     * 自动填充函数
//     *
//     * @param ids
//     * @return
//     */
//    @Override
//    default Map<Long, E> getCacheById(Set<Long> ids) {
//        if (CollectionUtils.isEmpty(ids)) {
//            return Collections.emptyMap();
//        }
//        return this.findByIds(ids).stream().collect(Collectors.toMap(e -> getIdOf(e), Function.identity(), (pre, curr) -> pre));
//    }
//}
