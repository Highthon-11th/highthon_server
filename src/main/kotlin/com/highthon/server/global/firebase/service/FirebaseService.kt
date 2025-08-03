package com.highthon.server.global.firebase.service

import com.google.cloud.firestore.Firestore
import org.springframework.stereotype.Service


@Service
class FirestoreService(
    private val firestore: Firestore,
) {
    fun saveWithRandomId(collection: String, data: Map<String, Any>): String {
        val docRef = firestore.collection(collection).document() // 랜덤 ID 생성
        docRef.set(data)  // 비동기 Future 반환
        return docRef.id  // 생성된 랜덤 ID 반환
    }


    fun saveData(collection: String, document: String, data: Map<String, Any>) {
        firestore.collection(collection).document(document).set(data)
    }

    fun getData(collection: String, document: String): Map<String, Any>? {
        val snapshot = firestore.collection(collection).document(document).get().get()
        return if (snapshot.exists()) snapshot.data else null
    }
}