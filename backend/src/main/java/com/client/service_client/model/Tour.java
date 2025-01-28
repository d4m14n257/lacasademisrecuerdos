package com.client.service_client.model;

import java.time.LocalDate;
import java.util.Set;

import com.client.service_client.model.enums.TourStatus;
import com.client.service_client.util.CustomIdGenerator;
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
@Table(name = "Tour")
public class Tour {
    
    @Id
    @Column(name = "id", length = 12)
    private String id;

    @Column(name = "name", length = 64)
    private String name;

    @Column(name = "description_es", length = 2048)
    private String description_es;

    @Column(name = "summary_es", length = 512)
    private String summary_es;

    @Column(name = "description_en", length = 2048)
    private String description_en;

    @Column(name = "summary_en", length = 512)
    private String summary_en;

    @Column(name = "url", length = 128)
    private String url;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TourStatus status;

    @Column(name = "created_at")
    private LocalDate created_at;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    Set<File> files;

    public Tour() {
        this.id = CustomIdGenerator.generate(12);
    }

    public Tour(String id) {
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
