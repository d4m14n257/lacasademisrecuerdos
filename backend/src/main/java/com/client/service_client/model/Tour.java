package com.client.service_client.model;

import java.time.LocalDate;
import java.util.Set;

import com.client.service_client.model.enums.TourStatus;
import com.client.service_client.util.CustomIdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Tour")
public class Tour {
    
    @Id
    @Column(name = "id", length = 12)
    private String id;

    @Column(name = "name", length = 64)
    private String name;

    @Column(name = "description", length = 2048)
    private String description;

    @Column(name = "summary", length = 512)
    private String summary;

    @Column(name = "url", length = 128)
    private String url;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TourStatus status;

    @Column(name = "created_at")
    private LocalDate created_at;

    @OneToMany(mappedBy = "tour")
    Set<File> files;

    public Tour() {
        this.id = CustomIdGenerator.generate(12);
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    public Set<File> getFiles() {
        return files;
    }

    public void setFiles(Set<File> files) {
        this.files = files;
    }

    public TourStatus getStatus() {
        return status;
    }

    public void setStatus(TourStatus status) {
        this.status = status;
    }
}
