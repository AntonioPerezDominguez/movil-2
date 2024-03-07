package com.example.evaluacion_u1.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Envelope", strict = false)
data class EnvelopeCarga@JvmOverloads constructor(
    @field:Element(name = "Body", required = false)
    var bodyCarga: BodyCarga = BodyCarga()
)

@Root(name = "Body", strict = false)
data class BodyCarga @JvmOverloads constructor(
    @field:Element(name = "getCargaAcademicaByAlumnoResponse", required = false)
    var getCargaAcademicaByAlumnoResponse: getCargaAcademicaByAlumnoResponse = getCargaAcademicaByAlumnoResponse()
)

@Root(name = "getCargaAcademicaByAlumnoResponse", strict = false)
data class getCargaAcademicaByAlumnoResponse @JvmOverloads constructor(
    @field:Element(name = "getCargaAcademicaByAlumnoResult", required = false)
    var getCargaAcademicaByAlumnoResult: String = ""
)
@Serializable
data class CargaAcademicaItem(
    @SerialName("Semipresencial")
    val semipresencial: String?,

    @SerialName("Observaciones")
    val observaciones: String?,

    @SerialName("docente")
    val docente: String?,

    @SerialName("clvOficial")
    val clvOficial: String?,

    @SerialName("Sabado")
    val sabado: String?,

    @SerialName("Viernes")
    val viernes: String?,

    @SerialName("Jueves")
    val jueves: String?,

    @SerialName("Miercoles")
    val miercoles: String?,

    @SerialName("Martes")
    val martes: String?,

    @SerialName("Lunes")
    val lunes: String?,

    @SerialName("EstadoMateria")
    val estadoMateria: String?,
    @SerialName("CreditosMateria")
    val creditosMateria: Int?,
    @SerialName("materia")
    val materia: String?,
    @SerialName("grupo")
    val grupo: String?,

    )
