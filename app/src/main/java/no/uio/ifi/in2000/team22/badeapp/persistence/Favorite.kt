package no.uio.ifi.in2000.team22.badeapp.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(
    @PrimaryKey val id: Int,
)