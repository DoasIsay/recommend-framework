package recommend.framework.rulengine;

public interface RuleEngine {
    <T> T execute(String express, Object obj);
}
