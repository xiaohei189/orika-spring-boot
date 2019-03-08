

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
```$xslt
    @Autowired
    private MapperFactory mapperFactory
```

## 📦 明确配置需要转换的类

```$xslt
   
@Mapper(destination = Dest.class)
public class Source {
    @MapperField(fieldName = "name")
    private String name;
    private int age;

    public Source(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @MapperField(fieldName = "name")
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
}


public class Dest {
    private String name;
    private int age;
     
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
}
```
