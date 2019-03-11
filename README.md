

<div align="center">
orika-spring-boot-starter  整合 [orika](https://github.com/orika-mapper/orika)与spring boot 提供自动配置，以及基于注解配置 [orika](https://github.com/orika-mapper/orika)

</div>



## 🎨 安装依赖
```
        <dependency>
            <groupId>com.github.weirui</groupId>
            <artifactId>orika-spring-boot-starter</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
   
```

## 📦注入 MapperFactory
```java
class Service{
      @Autowired
        private MapperFactory mapperFactory;
}
  
    
    
```
## 📦使用属性拷贝
``` 
       SourceAlias source = new SourceAlias("小黑", 123);
       SourceAlias.Dest dest = mapperFactory.getMapperFacade().map(source, SourceAlias.Dest.class);
    
```
## 📦 使用默认配置，自动映射Dest.class中名称相同的字段


```java

@Mapper(destination = Source.Dest.class)
public class Source {
    private String name;
    private int age;

    public Source(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static class Dest {
        private String name;
        private int age;
        private String alia;

        public Dest(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getAlia() {
            return alia;
        }

        public void setAlia(String alia) {
            this.alia = alia;
        }
    }
}
```
## 📦 别名映射，将name 字段映射到目标类中alia字段
```java

@Mapper(destination = SourceAlias.Dest.class)
public class SourceAlias {
    @MapperField(fieldName = "alia")
    private String name;
    private int age;

    public SourceAlias(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static class Dest {
        private String name;
        private int age;
        private String alia;
        public Dest() {
        }
        public Dest(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getAlia() {
            return alia;
        }

        public void setAlia(String alia) {
            this.alia = alia;
        }
    }
}
```

## 📦 为指定属性设置转换器
```java
// 实现字符转转数字转换器
public class MyConverter extends BidirectionalConverter<String,Integer> {

   @Override
   public Integer convertTo(String source, Type<Integer> destinationType) {
      return new Integer(source);
   }

   @Override
   public String convertFrom(Integer source, Type<String> destinationType) {
      return source.toString();
   }
}
// 转换器作为bean配置，并指bean名称，此处为 string2Number
@Configuration
class Config{
     @Bean(name = "string2Number")
            public BidirectionalConverter bidirectionalConverter() {
                return new MyConverter();
            }
}
 

@Mapper(destination = SourceConvert.Dest.class)
public class SourceConvert {
    private String name;
    //  为该属性指定转换器名称
    @MapperField(fieldName = "age",converterName = "string2Number")
    private String age;

    public SourceConvert(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public static class Dest {
        private String name;
        private int age;
        private String alia;

        public Dest() {
        }

        public Dest(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getAlia() {
            return alia;
        }

        public void setAlia(String alia) {
            this.alia = alia;
        }
    }
}

```
