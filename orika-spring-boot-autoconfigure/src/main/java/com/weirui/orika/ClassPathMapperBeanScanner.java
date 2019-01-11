package com.weirui.orika;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 扫描orika bean映射文件
 *
 * @author 隗锐
 * @dateTime 2019-01-06 16:18:52
 */
public class ClassPathMapperBeanScanner {
    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";


    private final Logger logger = LoggerFactory.getLogger(ClassPathMapperBeanScanner.class);


    private String resourcePattern = DEFAULT_RESOURCE_PATTERN;
    private List<String> pacageName;
    private PathMatchingResourcePatternResolver resourcePatternResolver;
    private CachingMetadataReaderFactory metadataReaderFactory;

    private final String mapperClassName = Mapper.class.getName();


    /**
     * 扫描路径下所有orika Mapper bean
     *
     * @param packageNames
     * @return
     */
    public Set<OrikaBeanConfigAttribute> scan(String... packageNames) {

        Set<OrikaBeanConfigAttribute> configAttributes = new HashSet<>();

        for (String packageName : packageNames) {

            Set<OrikaBeanConfigAttribute> attributes = scanCandidateComponents(packageName);
            configAttributes.addAll(attributes);
        }

        return configAttributes;
    }

    private Set<OrikaBeanConfigAttribute> scanCandidateComponents(String basePackage) {
        Set<OrikaBeanConfigAttribute> configAttributes = new LinkedHashSet<>();
        try {
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    ClassUtils.convertClassNameToResourcePath(basePackage) + '/' + this.resourcePattern;
            Resource[] resources = getResourcePatternResolver().getResources(packageSearchPath);
            boolean traceEnabled = logger.isTraceEnabled();
            boolean debugEnabled = logger.isDebugEnabled();
            for (Resource resource : resources) {
                if (traceEnabled) {
                    logger.trace("Scanning " + resource);
                }
                if (resource.isReadable()) {
                    try {
                        MetadataReader metadataReader = getMetadataReaderFactory().getMetadataReader(resource);

                        if (isOrikaMapper(metadataReader)) {
                            OrikaBeanConfigAttribute sbd = buildOrikaConfigAttribute(metadataReader);
                            if (true) {
                                if (debugEnabled) {
                                    logger.debug("Identified candidate component class: " + resource);
                                }
                                configAttributes.add(sbd);
                            } else {
                                if (debugEnabled) {
                                    logger.debug("Ignored because not a concrete top-level class: " + resource);
                                }
                            }
                        } else {
                            if (traceEnabled) {
                                logger.trace("Ignored because not matching any filter: " + resource);
                            }
                        }
                    } catch (Throwable ex) {
                        throw new BeanDefinitionStoreException(
                                "Failed to read candidate component class: " + resource, ex);
                    }
                } else {
                    if (traceEnabled) {
                        logger.trace("Ignored because not readable: " + resource);
                    }
                }
            }
        } catch (IOException ex) {
            throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
        }
        return configAttributes;
    }

    private OrikaBeanConfigAttribute buildOrikaConfigAttribute(MetadataReader metadataReader) {
        OrikaBeanConfigAttribute orikaBeanConfigAttribute = new OrikaBeanConfigAttribute();
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        try {
            Class<?> mapperClass = Class.forName(classMetadata.getClassName());

            Mapper mapper = AnnotationUtils.getAnnotation(mapperClass, Mapper.class);
            orikaBeanConfigAttribute.setMapper(mapper);

            Map<String, MapperField> fields = new HashMap<>();
            recursionDetectAllFields(mapperClass, fields);
            orikaBeanConfigAttribute.setMapperFields(fields);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return orikaBeanConfigAttribute;
    }

    private boolean isOrikaMapper(MetadataReader metadataReader) {

        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        if (annotationMetadata.getAnnotationTypes().contains(mapperClassName)) {
            return true;
        }


        return false;
    }

    private ResourcePatternResolver getResourcePatternResolver() {
        if (this.resourcePatternResolver == null) {
            this.resourcePatternResolver = new PathMatchingResourcePatternResolver();
        }
        return this.resourcePatternResolver;
    }

    public final MetadataReaderFactory getMetadataReaderFactory() {
        if (this.metadataReaderFactory == null) {
            this.metadataReaderFactory = new CachingMetadataReaderFactory();
        }
        return this.metadataReaderFactory;
    }

    /**
     * 递归查找该类所MapperField标注的字段,子类字段上注解属性会覆盖父类
     *
     * @param mapperClass
     * @param mapperFields
     */

    private void recursionDetectAllFields(Class<?> mapperClass, Map<String, MapperField> mapperFields) {
        String canonicalName = mapperClass.getCanonicalName();
        if ("java.lang.Object".equals(canonicalName)) {
            return;
        } else {
            Class<?> superclass = mapperClass.getSuperclass();
            recursionDetectAllFields(superclass, mapperFields);
        }

        Field[] fields = mapperClass.getDeclaredFields();

        for (Field field : fields) {
            String name = field.getName();
            MapperField parrentField = mapperFields.get(name);
            if (parrentField == null) {
                mapperFields.put(name, parrentField);
            } else {
                MapperField childField = AnnotationUtils.getAnnotation(field, MapperField.class);
                if (childField != null) {
                    Map<String, Object> parrentAttributes = AnnotationUtils.getAnnotationAttributes(parrentField);
                    Map<String, Object> childAttributes = AnnotationUtils.getAnnotationAttributes(childField);
                    parrentAttributes.putAll(childAttributes);

                    MapperField synthesizeMapperField = AnnotationUtils.synthesizeAnnotation(parrentAttributes, MapperField.class, null);
                    mapperFields.put(name, synthesizeMapperField);

                } else {
                    mapperFields.remove(name);
                }

            }
        }

    }
}
