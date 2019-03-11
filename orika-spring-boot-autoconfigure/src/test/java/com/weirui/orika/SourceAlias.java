package com.weirui.orika;
/**
 *
 *   @author 隗锐(weirui) 2019-03-11 18:02:02
 *
 *
 */
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


