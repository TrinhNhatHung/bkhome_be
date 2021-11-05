package com.bkhome.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "room_utility")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RoomUtility {

    @EmbeddedId
    private Id id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roomId")
    @JoinColumn(name = "room_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_Room_Util"))
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("utilityId")
    @JoinColumn(name = "utility_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_Utils_Room"))
    private Utility utility;

    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Id implements Serializable {

        @Column(name = "utility_id")
        private Integer utilityId;

        @Column(name = "room_id")
        private Integer roomId;
    }
}
