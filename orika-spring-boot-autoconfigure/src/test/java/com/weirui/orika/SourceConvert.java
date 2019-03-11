package com.weirui.orika;

/**
 * @author 隗锐(weirui) 2019-03-11 17:47:02
 */
@Mapper(destination = SourceConvert.Dest.class)
public class SourceConvert {
    private String name;
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


