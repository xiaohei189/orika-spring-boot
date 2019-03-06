package com.weirui.orika;

@Mapper(destination = Dest.class)
public class Source2 {
    @MapperField(fieldName = "name")
    private String name;
    private int age;

    public Source2(String name, int age) {
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