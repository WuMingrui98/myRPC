package xyz.raygetoffer.extension;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 加载扩展模块
 *
 * @author mingruiwu
 * @create 2022/7/15 16:28
 * @description
 */
@Slf4j
@SuppressWarnings("unchecked")
public class ExtensionLoader<T> {
    private static final String SERVICE_DIRECTORY = "META-INF/extensions/";
    // 存每个接口Class对象对应的ExtensionLoader
    private static final Map<Class<?>, ExtensionLoader<?>> EXTENSION_LOADS = new ConcurrentHashMap<>();
    // 存每个接口Class对象对应的实例对象
//    private static final Map<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>();
    // 目标类
    private final Class<?> type;
    // 每个接口的每个实现类的别名对应一个单例对象
    private final Map<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();
    // 每个接口的每个实现类别名有一个对应的Class对象，从配置文件中导入
    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();

    public ExtensionLoader(Class<?> type) {
        this.type = type;
    }


    /**
     * 获得type对应的ExtensionLoader
     *
     * @param type
     * @param <S>
     * @return
     */
    public static <S> ExtensionLoader<S> getExtensionLoader(Class<S> type) {
        if (type == null) {
            throw new IllegalArgumentException("Extension type should not be null.");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Extension type must be an interface.");
        }
        if (type.getAnnotation(SPI.class) == null) {
            throw new IllegalArgumentException("Extension type must be annotated by @SPI");
        }
        // 先从缓存取，如果取不到，新建一个放入缓存
        ExtensionLoader<S> extensionLoader = (ExtensionLoader<S>) EXTENSION_LOADS.get(type);
        if (extensionLoader == null) {
            EXTENSION_LOADS.putIfAbsent(type, new ExtensionLoader<S>(type));
            extensionLoader = (ExtensionLoader<S>) EXTENSION_LOADS.get(type);
        }
        return extensionLoader;
    }

    /**
     * 根据name获得对应的接口实例
     * @param name
     * @return
     */
    public T getExtension(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Extension name should not be null or empty.");
        }
        // 先从缓存中获取
        Holder<Object> holder = cachedInstances.get(name);
        if (holder == null) {
            cachedInstances.putIfAbsent(name, new Holder<>());
            holder = cachedInstances.get(name);
        }
        Object instance = holder.getValue();
        // 双重检查
        if (instance == null) {
            synchronized (holder) {
                instance = holder.getValue();
                if (instance == null) {
                    instance = createExtension(name);
                    holder.setValue(instance);
                }
            }
        }
        return (T) instance;
    }

    /**
     * 默认从@SPI注解上获取值
     * @return
     */
    public T getExtension() {
        SPI annotation = type.getAnnotation(SPI.class);
        String value = annotation.value();
        return getExtension(value);
    }

    /**
     * 根据别名定位具体实现类，并进行实例化
     *
     * @param name
     * @return
     */
    private T createExtension(String name) {
        Class<?> aClass = getExtensionClasses().get(name);
        if (aClass == null) {
            throw new RuntimeException("No such extension of name " + name);
        }
        // 实例化
        T instance = null;
        try {
            instance = (T) aClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage());
        }
        if (instance == null) {
            throw new RuntimeException("Can not instantiate such extension of name " + name);
        }
        return instance;
    }

    /**
     * 获得接口对应的扩展实现类的map
     *
     * @return
     */
    private Map<String, Class<?>> getExtensionClasses() {
        // 先从缓存中取
        Map<String, Class<?>> classes = cachedClasses.getValue();
        // 双重检查
        if (classes == null) {
            synchronized (cachedClasses) {
                classes = cachedClasses.getValue();
                if (classes == null) {
                    classes = new ConcurrentHashMap<>();
                    loadFile(classes);
                    cachedClasses.setValue(classes);
                }
            }
        }
        return classes;
    }

    /**
     * 加载指定的配置文件
     *
     * @param extensionClasses
     */
    private void loadFile(Map<String, Class<?>> extensionClasses) {
        String fileName = SERVICE_DIRECTORY + type.getCanonicalName();
        try {
            ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
            Enumeration<URL> urls = classLoader.getResources(fileName);
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    loadResource(extensionClasses, classLoader, url);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 从配置文件中读取接口对应的每个实现类的全类名，并加载成Class对象，并保存到extensionClasses中
     *
     * @param extensionClasses
     * @param classLoader
     * @param resourceUrl
     */
    private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, URL resourceUrl) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceUrl.openStream()))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // "#"后的信息为注释，忽略注释
                final int ci = line.indexOf('#');
                if (ci > 0) {
                    line = line.substring(0, ci);
                }
                // 去除前后的空格
                line = line.trim();
                if (line.length() > 0) {
                    final int ei = line.indexOf('=');
                    String name = line.substring(0, ei);
                    name = name.trim();
                    String className = line.substring(ei + 1);
                    className = className.trim();
                    if (name.length() > 0 && className.length() > 0) {
                        try {
                            Class<?> loadClass = classLoader.loadClass(className);
                            extensionClasses.put(name, loadClass);
                        } catch (ClassNotFoundException e) {
                            log.error(e.getMessage());
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


}
