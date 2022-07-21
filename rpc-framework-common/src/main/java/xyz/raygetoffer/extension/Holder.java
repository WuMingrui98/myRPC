package xyz.raygetoffer.extension;

/**
 * @author mingruiwu
 * @create 2022/7/15 16:40
 * @description
 */
public class Holder<T> {
    private volatile T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
