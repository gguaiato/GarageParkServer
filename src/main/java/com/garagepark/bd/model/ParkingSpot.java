package com.garagepark.bd.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

@Entity
@Table(name="parking_spot")
public class ParkingSpot {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="name")
	@Expose
	private String name;

    @OneToOne(optional = true, fetch = FetchType.LAZY, orphanRemoval = false)
    @JoinColumn(name="owner_id")
    private Person owner;

	public ParkingSpot() {}

	public ParkingSpot(String name) {
		this.name = name;
	}

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParkingSpot person = (ParkingSpot) o;

        if (!id.equals(person.id)) return false;
        return !name.equals(person.name);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
