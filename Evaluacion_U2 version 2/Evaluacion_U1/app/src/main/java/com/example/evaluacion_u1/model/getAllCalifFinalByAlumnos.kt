package com.example.evaluacion_u1.model

import kotlinx.serialization.Serializable
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Envelope", strict = false)
data class EnvelopeCalFinal @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var bodyCalFinal: BodyCalFinal = BodyCalFinal()
)

@Root(name = "Body", strict = false)
data class BodyCalFinal @JvmOverloads constructor(
    @field:Element(name = "getAllCalifFinalByAlumnosResponse ", required = false)
    var getAllCalifFinalByAlumnosResponse : getAllCalifFinalByAlumnosResponse  = getAllCalifFinalByAlumnosResponse ()
)

@Root(name = "getAllCalifFinalByAlumnosResponse", strict = false)
data class getAllCalifFinalByAlumnosResponse  @JvmOverloads constructor(
    @field:Element(name = "getAllCalifFinalByAlumnosResult", required = false)
    var getAllCalifFinalByAlumnosResult: String = ""
)

@Serializable
data class AllCalificacionFinal(
    val calif: Int,
    val acred: String,
    val grupo: String,
    val materia: String,
    val observaciones: String
    )