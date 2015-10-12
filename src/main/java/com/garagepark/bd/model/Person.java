package com.garagepark.bd.model;

import javax.persistence.*;
import javax.persistence.criteria.Predicate;

import com.google.gson.annotations.Expose;

@Entity
@Table(name="person")
public class Person {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="nusp")
	@Expose
	private String nusp;
	
	@Column(name="name")
	@Expose
	private String name;

	@Column(name="gender")
	@Expose
	private String gender;

    @Column(name = "valid")
    @Expose
    private Boolean valid;

	public Person() {}

	public Person(String nusp, String name, String gender) {
		this.nusp = nusp;
		this.name = name;
		this.gender = gender;
        this.valid = true;
	}

    public Long getId() {
        return id;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNusp() {
        return nusp;
    }

    public void setNusp(String nusp) {
        this.nusp = nusp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (!id.equals(person.id)) return false;
        if (!nusp.equals(person.nusp)) return false;
        if (!name.equals(person.name)) return false;
        if (!valid.equals(person.valid)) return false;
        return gender.equals(person.gender);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + nusp.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + gender.hashCode();
        result = 31 * result + valid.hashCode();
        return result;
    }
}
