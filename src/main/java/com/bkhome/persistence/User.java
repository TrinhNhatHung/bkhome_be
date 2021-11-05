package com.bkhome.persistence;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class User {

    @Id
    @Column(name = "id", columnDefinition = "varchar(50)")
    private String id;

    @Column(name = "password")
    private String password;

    @Column(name = "fullname ")
    private String fullname;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "createAt")
    @CreationTimestamp
    private LocalDate createAt;

    @Column(name = "updateAt")
    @UpdateTimestamp
    private LocalDate updateAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Room> rooms;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
