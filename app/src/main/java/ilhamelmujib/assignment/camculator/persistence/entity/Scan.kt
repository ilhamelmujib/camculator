package ilhamelmujib.assignment.camculator.persistence.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Scan(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "input") val input: String,
    @ColumnInfo(name = "result") val result: Int,
    @ColumnInfo(name = "created_at") val createdAt: Long,
) : Parcelable