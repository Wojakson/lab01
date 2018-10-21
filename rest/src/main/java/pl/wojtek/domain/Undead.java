package pl.wojtek.domain;

public class Undead {
    private int id;
    private String name;
    private int age;
    private int numberOfLegs;

    public Undead(int id, String name, int age, int numberOfLegs) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.numberOfLegs = numberOfLegs;
    }

    public Undead(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getNumberOfLegs() {
        return numberOfLegs;
    }

    public void setNumberOfLegs(int numberOfLegs) {
        this.numberOfLegs = numberOfLegs;
    }

}
