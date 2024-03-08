package com.example.evaluacion_u1.ui.theme.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.evaluacion_u1.data.RetrofitClient
import com.example.evaluacion_u1.model.CalPorUnidad

import com.example.evaluacion_u1.model.EnvelopeCalUnit

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

@Composable
fun CalificacionUnidad(navController: NavController, viewModel: DataViewModel) {
    val CalificacionUnida = viewModel.CalificacionPorUnidad

    Column {
        Text(
            text = "Calificacion por unidad\n ",
            modifier = Modifier
                .padding(horizontal =  60.dp),
            fontSize = 24.sp
        )
    }
    if (CalificacionUnida != null) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(vertical = 30.dp)
        ) {
            items(CalificacionUnida) { CalificacionPUnidad ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .size(1050.dp, 305.dp)
                    )
                    {
                        Text(
                            text = "Materia: ${CalificacionPUnidad.Materia}",
                            modifier = Modifier.weight(1.4f)
                        )
                        /*Text(
                            text = "Grupo: ${CalificacionPUnidad.grupo}",
                            modifier = Modifier.weight(1.4f)
                        )
                        Text(
                            text = "Unidad: ${CalificacionPUnidad.unidadesActivas}",
                            modifier = Modifier.weight(1.4f)
                        )
                        Text(
                            text = "Unidad1: ${CalificacionPUnidad.c1}",
                            modifier = Modifier.weight(1.4f)
                        )
                        Text(
                            text = "Unidad2: ${CalificacionPUnidad.c2}",
                            modifier = Modifier.weight(1.4f)
                        )
                        Text(
                            text = "Unidad3: ${CalificacionPUnidad.c3}",
                            modifier = Modifier.weight(1.4f)
                        )
                        Text(
                            text = "Unidad4: ${CalificacionPUnidad.c4}",
                            modifier = Modifier.weight(1.4f)
                        )
                        Text(
                            text = "Unidad5: ${CalificacionPUnidad.c5}",
                            modifier = Modifier.weight(1.4f)
                        )
                        Text(
                            text = "Unidad6: ${CalificacionPUnidad.c6}",
                            modifier = Modifier.weight(1.4f)
                        )
                        Text(
                            text = "Unidad7: ${CalificacionPUnidad.c7}",
                            modifier = Modifier.weight(1.4f)
                        )
                        Text(
                            text = "Unidad8: ${CalificacionPUnidad.c8}",
                            modifier = Modifier.weight(1.4f)
                        )
                        Text(
                            text = "Unidad9: ${CalificacionPUnidad.c9}",
                            modifier = Modifier.weight(1.4f)
                        )
                        Text(
                            text = "Unidad10: ${CalificacionPUnidad.c10}",
                            modifier = Modifier.weight(1.4f)
                        )

                        Text(
                            text = "Unidad11: ${CalificacionPUnidad.c11}",
                            modifier = Modifier.weight(1.4f)
                        )

                        Text(
                            text = "Unidad12: ${CalificacionPUnidad.c12}",
                            modifier = Modifier.weight(1.4f)
                        )

                        Text(
                            text = "Unidad13: ${CalificacionPUnidad.c13}",
                            modifier = Modifier.weight(1.4f)
                        )*/

                    }
                }
            }
        }
    }
}

fun getCalificacionUnidad2(
    context: Context,
    navController: NavController,
    viewModel: DataViewModel
){
    val service = RetrofitClient(context).retrofitServiceCalUnidad
    val bodyAcademico = cargarRequestBody()
    service.getCalUnidad(bodyAcademico).enqueue(object : Callback<EnvelopeCalUnit> {
        override fun onResponse(call: Call<EnvelopeCalUnit>, response: Response<EnvelopeCalUnit>) {
            if (response.isSuccessful) {
                val envelope = response.body()
                val alumnoResultJson: String? =
                    envelope?.BodyCalUnit?.getCalUnitResponse?.getCalifUnidadesByAlumnoResult

                // Deserializa la cadena JSON a AlumnoAcademicoResult
                val json = Json { ignoreUnknownKeys = true }
                val CalificacionUnidadItem: List<CalPorUnidad>? =
                    alumnoResultJson?.let { json.decodeFromString(it) }

                Log.w("exito", "se obtuvo el perfil 2: ${CalificacionUnidadItem}")
                val alumnoAcademicoResultJson = Json.encodeToString(CalificacionUnidadItem)
                viewModel.CalificacionPorUnidad = CalificacionUnidadItem
                navController.navigate("CalificacionUnidad")
            } else {
                showError(
                    context,
                    "Error al obtener el perfil académico. Código de respuesta: ${response.code()}"
                )
            }
        }
        override fun onFailure(call: Call<EnvelopeCalUnit>, t: Throwable) {
            t.printStackTrace()
            showError(context, "Error en la solicitud del perfil académico")

        }


    })
}








private fun cargarRequestBody(): RequestBody {
    return """
    <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
        <soap:Body>
            <getCalifUnidadesByAlumno xmlns="http://tempuri.org/" />
        </soap:Body>
    </soap:Envelope>
        """.trimIndent().toRequestBody("text/xml; charset=utf-8".toMediaTypeOrNull())
}
private fun showError(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}