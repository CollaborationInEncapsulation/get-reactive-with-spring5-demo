package com.example.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Entity
@Table(name = "mention")
@Accessors(chain = true)
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
public class Mention implements Serializable {
    @NotNull
    @NonNull
    @EmbeddedId
    private Key id;

    @Getter
    @ManyToOne
    @JoinColumn(name = "user_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Embeddable
    @Data
    @Accessors(chain = true)
    @RequiredArgsConstructor(staticName = "of")
    @NoArgsConstructor
    public static class Key implements Serializable {
        @NotNull
        @NonNull
        @Column(name = "message_id")
        private String messageId;

        @NotNull
        @NonNull
        @Column(name = "user_id")
        private String userId;
    }
}
