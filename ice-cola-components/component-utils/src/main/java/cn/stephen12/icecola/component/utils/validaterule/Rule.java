package cn.stephen12.icecola.component.utils.validaterule;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author ouyangsheng
 * @date 2022-05-23
 **/
@AllArgsConstructor
@Data
public class Rule<P> {
    private Predicate<P> condition;
    private Supplier<String> message;

    public void run(P param, ValidationRuleExecutor executor) {
        if (this.condition.test(param)) {
            String message = this.message.get();
            executor.result.add(message);
        }
    }
}
