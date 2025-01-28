package com.client.service_client.model;

import java.time.LocalDateTime;
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

    @Column(name = "description_es", length = 2048)
    private String description_es;

    @Column(name = "description_en", length = 2048)
    private String description_en;

    @Column(name = "summary_es", length = 512)
    private String summary_es;

    @Column(name = "summary_en", length = 512)
    private String summary_en;

    @Column(name = "single_price", nullable = true)
    private Double single_price;

    @Column(name = "double_price", nullable = true)
    private Double double_price;

    @Column(name = "created_at")
    private LocalDateTime created_at;

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
        this.status = RoomStatus.hidden;
        this.created_at = LocalDateTime.now();
    }

    public Room(String id) {
        this.id = id;
        this.status = RoomStatus.hidden;
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

    public String getDescription_es() {
        return description_es;
    }

    public void setDescription_es(String description_es) {
        this.description_es = description_es;
    }

    public String getSummary_es() {
        return summary_es;
    }

    public void setSummary_es(String summary_es) {
        this.summary_es = summary_es;
    }

    public String getDescription_en() {
        return description_en;
    }

    public void setDescription_en(String description_en) {
        this.description_en = description_en;
    }

    public String getSummary_en() {
        return summary_en;
    }

    public void setSummary_en(String summary_en) {
        this.summary_en = summary_en;
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

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }
}
