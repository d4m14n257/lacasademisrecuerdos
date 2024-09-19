package com.client.service_client.model;

import java.time.LocalDate;
import java.util.Set;

import com.client.service_client.model.enums.RoomStatus;
import com.client.service_client.util.CustomIdGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Room")
public class Room {

    @Id
    @Column(name = "id", length = 12)
    private String id;

    @Column(name = "name", length = 32)
    private String name;

    @Column(name = "description", length = 2048)
    private String description;

    @Column(name = "summary", length = 512)
    private String summary;

    @Column(name = "additional", length = 256, nullable = true)
    private String additional;

    @Column(name = "single_price", nullable = true)
    private Double single_price;

    @Column(name = "double_price", nullable = true)
    private Double double_price;

    @Column(name = "created_at")
    private LocalDate created_at;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    Set<File> files;

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    Set<Contact> contacts;

    public Room() {
        this.id = CustomIdGenerator.generate(12);
    }

    public Room(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public Double getSingle_price() {
        return single_price;
    }

    public void setSingle_price(Double single_price) {
        this.single_price = single_price;
    }

    public Double getDouble_price() {
        return double_price;
    }

    public void setDouble_price(Double double_price) {
        this.double_price = double_price;
    }

    public Set<File> getFiles() {
        return files;
    }

    public void setFiles(Set<File> files) {
        this.files = files;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }
}
