package ru.ostritsov.projectTSM.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    private String text;

    @OneToOne
    @JoinColumn(name = "author_id")
    private User author;

    @OneToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
