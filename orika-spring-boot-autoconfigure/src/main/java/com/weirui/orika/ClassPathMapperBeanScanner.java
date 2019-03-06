package com.weirui.orika;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 *扫描路径下所有{@link com.weirui.orika.Mapper}
 * @author 隗锐
 * @dateTime 2019-01-06 16:18:52
 */
public class ClassPathMapperBeanScanner {
    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
    private final Logger logger = LoggerFactory.getLogger(ClassPathMapperBeanScanner.class);
    private String resourcePattern = DEFAULT_RESOURCE_PATTERN;
    private PathMatchingResourcePatternResolver resourcePatternResolver;
    private CachingMetadataReaderFactory metadataReaderFactory;

    private static final String mapperClassName = Mapper.class.getName();


    /**
     * @param packageNames
     * @return
     */
    public Set<MapperFactoryConfigAttribute> scan(String... packageNames) {
        Set<MapperFactoryConfigAttribute> configAttributes = new HashSet<>();
        for (String packageName : packageNames) {

            Set<MapperFactoryConfigAttribute> attributes = scanCandidateComponents(packageName);
            configAttributes.addAll(attributes);
        }

        return configAttributes;
    }

    private Set<MapperFactoryConfigAttribute> scanCandidateComponents(String basePackage) {
        Set<MapperFactoryConfigAttribute> configAttributes = new LinkedHashSet<>();
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
                            MapperFactoryConfigAttribute sbd = buildOrikaConfigAttribute(metadataReader);
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

    private MapperFactoryConfigAttribute buildOrikaConfigAttribute(MetadataReader metadataReader) {
        MapperFactoryConfigAttribute orikaBeanConfigAttribute = new MapperFactoryConfigAttribute();
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        try {
            Class<?> mapperClass = Class.forName(classMetadata.getClassName());

            orikaBeanConfigAttribute.setSource(mapperClass);
            Mapper mapper = AnnotationUtils.getAnnotation(mapperClass, Mapper.class);
            orikaBeanConfigAttribute.setMapper(mapper);

            Map<String, MapperField> fieldMaps = new HashMap<>();

            Field[] fields = mapperClass.getDeclaredFields();
            for (Field field : fields) {
                MapperField mapperField = AnnotationUtils.getAnnotation(field, MapperField.class);
                if (mapperField!=null) {
                    fieldMaps.put(field.getName(), mapperField);
                }

            }

            orikaBeanConfigAttribute.setMapperFields(fieldMaps);

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


}
