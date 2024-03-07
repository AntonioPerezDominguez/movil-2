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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.evaluacion_u1.R
import com.example.evaluacion_u1.data.RetrofitClient
import com.example.evaluacion_u1.model.AlumnoAcademicoResult
import com.example.evaluacion_u1.model.CalificacionPorUnidad
import com.example.evaluacion_u1.model.EnvelopeCalUnidad
import com.example.evaluacion_u1.model.EnvelopeKardex
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


fun GetCalUnidad(
    context: Context,
    navController: NavController,
    viewModel: DataViewModel
){
    val service = RetrofitClient(context).retrofitService3
    val bodyAcademico = CalUnidadRequestBody()
    service.getCalUnidad(bodyAcademico).enqueue(object : Callback<EnvelopeCalUnidad> {
        override fun onResponse(call: Call<EnvelopeCalUnidad>, response: Response<EnvelopeCalUnidad>) {
            if (response.isSuccessful) {
                Log.w("entro", "si entro al response successfull")
                val envelope = response.body()
                val alumnoResultJson: String? = envelope?.bodyCalUnidad?.getCalifUnidadesByAlumnoResponse?.getCalifUnidadesByAlumnoResult
                Log.w("entro", "no causo error en el alumnos result JSON")

                // Verifica si el JSON no es nulo y no está vacío
                if (alumnoResultJson != null /*&& alumnoResultJson.isNotEmpty()*/) {
                    //try {
                    val listType=object: TypeToken<List<CalificacionPorUnidad>>(){}.type
                        val json = Json { ignoreUnknownKeys = true }
                        val CalificacionUnidadItem: List<CalificacionPorUnidad> = Gson().fromJson("["+alumnoResultJson+"]",listType)

                        Log.w("entro", "si trajo algo en el alumno result JSON")
                        Log.w("exito", "se obtuvo el perfil 2: $CalificacionUnidadItem")

                        // Codifica de nuevo el JSON si es necesario
                        val alumnoAcademicoResultJson = Json.encodeToString(CalificacionUnidadItem)

                        viewModel.CalificacionPorUnidad = CalificacionUnidadItem
                        navController.navigate("CalificacionUnidad")
                    /*} catch (e: Exception) {
                        showError(context, "Error al decodificar el JSON: ${e.message}")
                    }*/
                } else {
                    showError(
                        context,
                        "Error al obtener el perfil académico. El resultado JSON está vacío."
                    )
                }
            } else {
                showError(
                    context,
                    "Error al obtener el perfil académico. Código de respuesta: ${response.code()}"
                )
            }
        }

        override fun onFailure(call: Call<EnvelopeCalUnidad>, t: Throwable) {
            t.printStackTrace()
            showError(context, "Error en la solicitud del perfil académico")
        }
    })
}

private fun CalUnidadRequestBody(): RequestBody {
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
@Composable
fun MostrarCalUnidad(navController: NavController, viewModel: DataViewModel){
    val CalUnidad = viewModel.CalificacionPorUnidad

    Column {
        /*Text(
            text = "Calificacion por unidad\n",
            modifier = Modifier.padding(horizontal = 60.dp),
            fontSize = 24.dp
        )*/
    }

    if (CalUnidad != null) {
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp) ){
            items(CalUnidad){CalificacionPorUnidad->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical=10.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .size(1050.dp,305.dp)
                            .padding(horizontal =20.dp)
                    ){
                        Text(text = "Materia: ${CalificacionPorUnidad.Materia}"
                            ,modifier = Modifier.weight(1.4f)
                        )
                        Text(text = "Grupo: ${CalificacionPorUnidad.Grupo}"
                            ,modifier = Modifier.weight(1.4f)
                        )
                        Text(text = "Unidades Activas: ${CalificacionPorUnidad.UnidadesActivas}"
                            ,modifier = Modifier.weight(1.4f)
                        )
                        Text(text = "C1: ${CalificacionPorUnidad.C1}",modifier = Modifier.weight(1.4f))
                        Text(text = "C2: ${CalificacionPorUnidad.C2}",modifier = Modifier.weight(1.4f))
                        Text(text = "C3: ${CalificacionPorUnidad.C3}",modifier = Modifier.weight(1.4f))
                        Text(text = "C4: ${CalificacionPorUnidad.C4}",modifier = Modifier.weight(1.4f))
                        Text(text = "C5: ${CalificacionPorUnidad.C5}",modifier = Modifier.weight(1.4f))
                        Text(text = "C6: ${CalificacionPorUnidad.C6}",modifier = Modifier.weight(1.4f))
                        Text(text = "C7: ${CalificacionPorUnidad.C7}",modifier = Modifier.weight(1.4f))
                        Text(text = "C8: ${CalificacionPorUnidad.C8}",modifier = Modifier.weight(1.4f))
                        Text(text = "C9: ${CalificacionPorUnidad.C9}",modifier = Modifier.weight(1.4f))
                        Text(text = "C10: ${CalificacionPorUnidad.C10}",modifier = Modifier.weight(1.4f))
                        Text(text = "C11: ${CalificacionPorUnidad.C11}",modifier = Modifier.weight(1.4f))
                        Text(text = "C12: ${CalificacionPorUnidad.C12}",modifier = Modifier.weight(1.4f))
                        Text(text = "C13: ${CalificacionPorUnidad.C13}",modifier = Modifier.weight(1.4f))
                    }
                }
            }
        }
        Column(modifier = Modifier.padding(16.dp)) {
            // Agrega más campos aquí
            Spacer(modifier = Modifier.padding(30.dp))
            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { navController.popBackStack() }) {
                Text(text = "Carga Academica")
            }
            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { navController.popBackStack() }) {
                Text(text = "Cerrar sesion")
            }
        }
    } else {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "No se pudo obtener el perfil académico.")
        }
    }
}