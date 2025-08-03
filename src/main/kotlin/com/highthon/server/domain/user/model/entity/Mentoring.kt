package com.highthon.server.domain.user.model.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
class Mentoring(

    @EmbeddedId
    val id: MentoringId = MentoringId(),

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("mentorId")
    @JoinColumn(name = "mentor_id")
    val mentor: Mentor,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("menteeId")
    @JoinColumn(name = "mentee_id")
    val mentee: Mentee,

    @Column(nullable = false, unique = true)
    val chatRoomId: UUID,
) {
    init {
        mentor.menteeList.add(this)
        mentee.mentorList.add(this)
    }
}