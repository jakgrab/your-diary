package com.example.yourdiary.model

import com.example.yourdiary.util.toRealmInstant
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.time.Instant

open class Diary : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.create()
    var ownerId: String = ""
    var affair: String = Affair.WateringPlants.name
    var title: String = ""
    var description: String = ""
    var images: RealmList<String> = realmListOf()
    var date: RealmInstant = Instant.now().toRealmInstant()
}
