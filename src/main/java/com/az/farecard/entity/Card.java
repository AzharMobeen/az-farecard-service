package com.az.farecard.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table
@AllArgsConstructor
@ToString
@Getter
@Setter
@NoArgsConstructor
public class Card implements Serializable {

    @GeneratedValue
    @Id
    private Long cardId;
    private BigDecimal balance;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private List<CardUsage> cardUsageList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Card card = (Card) o;
        return cardId != null && Objects.equals(cardId, card.cardId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void addCardUsage(CardUsage cardUsage) {
        if(CollectionUtils.isEmpty(cardUsageList))
            this.cardUsageList = new ArrayList<>();
        this.cardUsageList.add(cardUsage);
        cardUsage.setCard(this);
    }
}
