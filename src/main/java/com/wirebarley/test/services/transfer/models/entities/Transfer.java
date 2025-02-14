package com.wirebarley.test.services.transfer.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Entity
@Table(name = "transfers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "quote_id")
    private long quoteId;

    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar transferredAt;

    public Transfer(final long quoteId) {
        this.quoteId = quoteId;
    }

    @PrePersist
    void addTimestamp() {
        transferredAt = Calendar.getInstance();
    }
}
