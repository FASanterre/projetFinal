package ca.qc.cgodin.projet_final

import android.graphics.Bitmap
import java.io.Serializable

class ModelRestaurant (
    var placeId : String,
    var name : String,
    var adresse : String?,
    var note : String?,
    var totalNote : String?,
    var image : Bitmap?,
    var url : String?,
    var numeroTelephone : String?,
    var openNow : Boolean?,
    var latitude : Double,
    var longitude : Double
) : Serializable
