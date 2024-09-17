package com.example.requestenrichingproxy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Entity
@Table(name = "enriched_data_form")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrichedDataForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Map to store field names and their presentations
    @ElementCollection
    @CollectionTable(name = "field_presentation_names", joinColumns = @JoinColumn(name = "field_id"))
    @MapKeyColumn(name = "field_name")
    @Column(name = "field_presentation")
    private Map<String, String> presentations;

    // Map to store field names and their values
    @ElementCollection
    @CollectionTable(name = "field_values", joinColumns = @JoinColumn(name = "field_id"))
    @MapKeyColumn(name = "field_name")
    @Column(name = "field_value")
    private Map<String, String> values;



    // Constructor for filling values and presentations
    public EnrichedDataForm(Map<String, String> formPresentations, Map<String, String> formValues) {
        this.values = formValues;
        this.presentations = formPresentations;
    }
}
