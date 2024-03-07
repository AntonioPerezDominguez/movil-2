package com.example.evaluacion_u1.ui.theme.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.evaluacion_u1.model.AllCalificacionFinal
import com.example.evaluacion_u1.model.AlumnoAcademicoResult
import com.example.evaluacion_u1.model.EnvelopeCalFinal
import com.example.evaluacion_u1.model.EnvelopeKardex
import com.example.evaluacion_u1.network.CalFinal
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


fun GetCalFinal(
    context: Context,
    navController: NavController,
    viewModel: DataViewModel
) {
    val service = RetrofitClient(context).retrofitService4
    val bodyCalFinal = CalFinalRequestBody()
    service.getCalFinal(bodyCalFinal).enqueue(object : Callback<EnvelopeCalFinal> {
        override fun onResponse(call: Call<EnvelopeCalFinal>, response: Response<EnvelopeCalFinal>) {
            if (response.isSuccessful) {
                val envelope = response.body()
                val CalFinalResultJson: String? =
                    envelope?.bodyCalFinal?.getAllCalifFinalByAlumnosResponse?.getAllCalifFinalByAlumnosResult

                // Deserializa la cadena JSON a AlumnoAcademicoResult
                val json = Json { ignoreUnknownKeys = true }
                val CalFinalResult: List<AllCalificacionFinal>? =
                    CalFinalResultJson?.let { json.decodeFromString(it) }

                Log.w("exito", "se obtuvo el perfil 2: ${CalFinalResult}")
                val alumnoAcademicoResultJson = Json.encodeToString(CalFinalResult)
                viewModel.AllCalificacionFinal = CalFinalResult
                navController.navigate("CalFinal")
            } else {
                showError(
                    context,
                    "Error al obtener el perfil académico. Código de respuesta: ${response.code()}"
                )
            }
        }

        override fun onFailure(call: Call<EnvelopeCalFinal>, t: Throwable) {
            t.printStackTrace()
            showError(context, "Error en la solicitud del perfil académico")
        }
    })
}
private fun CalFinalRequestBody(): RequestBody {
    return """
        <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <getAllCalifFinalByAlumnosResponse xmlns="http://tempuri.org/">
      <getAllCalifFinalByAlumnosResult>string</getAllCalifFinalByAlumnosResult>
    </getAllCalifFinalByAlumnosResponse>
  </soap:Body>
</soap:Envelope>
    """.trimIndent().toRequestBody("text/xml; charset=utf-8".toMediaTypeOrNull())
}
private fun showError(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
@Composable
fun MostrarCalFinal(navController: NavController, viewModel: DataViewModel){

    /*val Calificacion = viewModel.AllCalificacionFinal
    //val hola=Calificacion?.grupo?:"Esto resulto mal"
    Log.e("Calificacion",hola)
    Image(
        painter = painterResource(id = R.drawable.fondodate),
        contentDescription = "My background image",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    if (Calificacion != null) {
        Column(modifier = Modifier.padding(16.dp)) {
            Card(modifier = Modifier.padding(30.dp).size(600.dp,45.dp)){
                Text(text = "Calificacion: ${Calificacion.calif}",modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
                Text(text = "Acreditada: ${Calificacion.acred}",modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
                Text(text = "Grupo: ${Calificacion.grupo}",modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
                Text(text = "Materia: ${Calificacion.materia}",modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
                Text(text = "Observaciones: ${Calificacion.observaciones}",modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
            }

            // Agrega más campos aquí
            Spacer(modifier = Modifier.padding(30.dp))

            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { navController.popBackStack() }) {
                Text(text = "Home")
            }
        }
    } else {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "No se pudo obtener la Calificacion Final.")
        }
    }*/
}