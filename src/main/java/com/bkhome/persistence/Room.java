package com.bkhome.persistence;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "room")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Room {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "price")
    private Integer price;

    @Column(name = "area")
    private Integer area;

    @Column(name = "address")
    private String address;

    @Column(name = "title")
    private String title;

    @Column(name = "detail")
    private String detail;

    @Column(name = "image")
    private String image;

    @Column(name = "createAt")
    @CreationTimestamp
    private LocalDateTime createAt;

    @Column(name = "updateAt")
    @UpdateTimestamp
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RoomUtility> roomUtilities;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_Room_User"))
    private User user;

}
