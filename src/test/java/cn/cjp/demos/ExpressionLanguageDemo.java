package cn.cjp.demos;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class ExpressionLanguageDemo {

	@Test
	public void test1() {
		// 创建SpEL表达式的解析器
		ExpressionParser parser = new SpelExpressionParser();
		Map<String, Object> user = new HashMap<>();
		user.put("id", 9527);
		user.put("name", "Star");
		// 解析表达式需要的上下文，解析时有一个默认的上下文
		EvaluationContext ctx = new StandardEvaluationContext();
		// 在上下文中设置变量，变量名为user，内容为user对象
		ctx.setVariable("user", user);
		// 从用户对象中获得id并+1900，获得解析后的值在ctx上下文中
		int id = (Integer) parser.parseExpression("#user.get('id') + 1900").getValue(ctx);

		System.out.println(id);
	}

}
