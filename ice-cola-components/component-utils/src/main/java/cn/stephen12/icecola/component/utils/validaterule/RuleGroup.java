package cn.stephen12.icecola.component.utils.validaterule;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @author ouyangsheng
 * @date 2022-05-23
 **/
public class RuleGroup<P> extends Rule<P> {
    @Getter
    private final List<Rule<P>> rules;
    public RuleGroup(List<Rule<P>> rules) {
        super(null, null);
        this.rules = rules;
    }
    public RuleGroup(Rule<P>... rules) {
        this(Arrays.asList(rules));
    }

    @Override
    public void run(P param, ValidationRuleExecutor executor) {
        for (Rule<P> r : rules) {
            r.run(param, executor);
        }
    }
}
