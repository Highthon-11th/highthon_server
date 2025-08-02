package com.highthon.server.domain.post.model.entity

import jakarta.persistence.*

@Entity
class PostTag(

    @EmbeddedId
    val id: PostTagId = PostTagId(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    val post: Post? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    val tag: Tag? = null
) {}