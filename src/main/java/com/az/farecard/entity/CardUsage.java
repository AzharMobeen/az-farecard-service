package com.az.farecard.entity;

import com.az.farecard.dto.CardSwipeType;
import com.az.farecard.dto.TransactionType;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table
@AllArgsConstructor
@ToString
@Getter
@Setter
@NoArgsConstructor
public class CardUsage implements Serializable {

    @GeneratedValue
    @Id
    private Long usageId;
    private TransactionType type;
    private Long station;
    private CardSwipeType swipe;
    private BigDecimal fare;

    @ManyToOne
    @JoinColumn(name = "cardId")
    private Card card;

    @CreatedDate
    private LocalDateTime createdDateTime;
    @LastModifiedDate
    private LocalDateTime modifiedDateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CardUsage cardUsage = (CardUsage) o;
        return usageId != null && Objects.equals(usageId, cardUsage.usageId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
