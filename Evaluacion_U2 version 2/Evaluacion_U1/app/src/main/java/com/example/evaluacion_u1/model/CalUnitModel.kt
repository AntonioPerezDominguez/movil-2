package com.example.evaluacion_u1.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Envelope", strict = false)
data class EnvelopeCalUnit @JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var BodyCalUnit: BodyCalUnit = BodyCalUnit()
)

@Root(name = "Body", strict = false)
data class BodyCalUnit @JvmOverloads constructor(
    @field:Element(name = "getCalifUnidadesByAlumnoResponse ", required = false)
    var getCalUnitResponse : getCalUnitResponse  = getCalUnitResponse ()
)

@Root(name = "getCalifUnidadesByAlumnoResponse", strict = false)
data class getCalUnitResponse  @JvmOverloads constructor(
    @field:Element(name = "getCalifUnidadesByAlumnoResult", required = false)
    var getCalifUnidadesByAlumnoResult: String = ""
)

@Serializable
data class CalPorUnidad(
    @SerialName("Observaciones")
    val Observaciones: String?,

    @SerialName("C13")
    val C13: String?,

    @SerialName("C12")
    val C12: String?,

    @SerialName("C11")
    val C11: String?,

    @SerialName("C10")
    val C10: String?,

    @SerialName("C9")
    val C9: String?,

    @SerialName("C8")
    val C8: String?,

    @SerialName("C7")
    val C7: String?,

    @SerialName("C6")
    val C6: String?,

    @SerialName("C5")
    val C5: String?,

    @SerialName("C4")
    val C4: String?,

    @SerialName("C3")
    val C3: String?,

    @SerialName("C2")
    val C2: String?,

    @SerialName("C1")
    val C1: String?,

    @SerialName("UnidadesActivas")
    val UnidadesActivas: String?,

    @SerialName("Materia")
    val Materia: String?,

    @SerialName("Grupo")
    val Grupo: String?
)

