package cn.stephen12.icecola.component.utils.validaterule;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 校验规则执行器
 *
 * @author ouyangsheng
 * @date 2022-05-17
 **/
public class ValidationRuleExecutor<T> {

    /**
     * {@link #rules} 所有待执行的规则，目前为列表做的队列
     * {@link #result} 成功执行的规则结果
     */
    Queue<Rule<? super T>> rules = new ArrayBlockingQueue<>(16);
    List<String> result = new ArrayList<>(16);

    public ValidationRuleExecutor addRule(Rule<? super T> rule) {
        rules.add(rule);
        return this;
    }

    public ValidationRuleExecutor addRules(List<Rule<? super T>> rules) {
        rules.addAll(rules);
        return this;
    }

    public ValidationRuleExecutor addRule(Predicate<? super T> condition, Supplier<String> action) {
        rules.add(new Rule(condition, action));
        return this;
    }

    /**
     * 运行规则
     *
     * @param fact 被规则校验的参数
     * @return 执行成功结果数据
     */
    public Results run(T fact) {
        for (Rule<? super T> r : rules) {
            r.run(fact, this);
        }
        return new Results();
    }

    /**
     * 执行结果
     * <p>
     * 因为校验结果通常是 Message，所以这里定义 result 为 String 类型，如有必要会提取顶层规则执行器
     * </p>
     */
    public class Results {
        private List<String> results;

        Results() {
            this.results = result;
        }

        /**
         * 有多少规则命中
         * @return
         */
        public int getCount() {
            return results.size();
        }

        /**
         * 获取结果文本信息
         *
         * @return
         */
        @Override
        public String toString() {
            return results.stream().collect(Collectors.joining(","));
        }

    }
}
