package com.example.domain;

import com.example.domain.utils.ToCommaSeparatedValuesConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "message")
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@NamedEntityGraph(name = "load.eager.all", includeAllAttributes = true)
public class Message implements Serializable {
    @Id
    private String id;

    @Lob
    private String text;

    @Lob
    private String html;

    @Temporal(TemporalType.TIMESTAMP)
    private Date sent;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    private Boolean unread;

    private Long readBy;

    @Lob
    @Convert(converter = ToCommaSeparatedValuesConverter.class)
    private String[] urls;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "message_id", referencedColumnName = "id")
    private Set<Mention> mentions;

    @Embedded
    @ElementCollection
    @CollectionTable(name = "issue", joinColumns = @JoinColumn(name = "message_id"))
    private Set<Issue> issues;
}
