package com.example.evaluacion_u1.ui.theme.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.evaluacion_u1.R
import com.example.evaluacion_u1.data.RetrofitClient
import com.example.evaluacion_u1.navigation.AppNavigation
import com.example.evaluacion_u1.navigation.AppScreens.CalFinalScreen.route
import com.example.evaluacion_u1.network.CargaAcademica
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun mostrarDatos(navController: NavController, viewModel: DataViewModel) {
    val alumnoAcademicoResult = viewModel.alumnoAcademicoResult
    val context = LocalContext.current
    var user by remember {
        mutableStateOf("")
    }
    var pass by remember {
        mutableStateOf("")
    }

    Image(
        painter = painterResource(id = R.drawable.fondodate),
        contentDescription = "My background image",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    if (alumnoAcademicoResult != null) {
        Column(modifier = Modifier.padding(16.dp)) {
            Card(modifier = Modifier
                .padding(30.dp)
                .size(600.dp, 45.dp)){
                Text(text = "Nombre: ${alumnoAcademicoResult.nombre}",modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
            }
            Card(modifier = Modifier
                .padding(30.dp)
                .size(600.dp, 45.dp)){
                Text(text = "Matrícula: ${alumnoAcademicoResult.matricula}",modifier = Modifier
                    .padding(10.dp)
                    .align(alignment = Alignment.CenterHorizontally))
            }

            Card(modifier = Modifier
                .padding(30.dp)
                .size(600.dp, 45.dp)){
                Text(text = "Carrera: ${alumnoAcademicoResult.carrera}",modifier = Modifier
                    .padding(10.dp)
                    .align(alignment = Alignment.CenterHorizontally))
            }
            Card(modifier = Modifier
                .padding(30.dp)
                .size(600.dp, 45.dp)){
                Text(text = "Semestre Actual: ${alumnoAcademicoResult.semActual}",modifier = Modifier
                    .padding(10.dp)
                    .align(alignment = Alignment.CenterHorizontally))
            }
            Card(modifier = Modifier
                .padding(30.dp)
                .size(600.dp, 65.dp)){
                Text(text = "Especialidad: ${alumnoAcademicoResult.especialidad}",modifier = Modifier
                    .padding(10.dp)
                    .align(alignment = Alignment.CenterHorizontally))
            }
            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    iniciarCalFinal(context, user, pass, navController,viewModel)
                    navController.navigate("CalFinal") }) {
                Text(text = "Calificacion Final")
            }
            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { navController.navigate("kardex") }) {
                Text(text = "kardex")
            }
            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { navController.navigate("CargaAcademica") }) {
                Text(text = "Carga Academica")
            }
            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    iniciarCalUnidad(context, user, pass, navController,viewModel)
                    navController.navigate("CalUnidad") }) {
                Text(text = "Calificaciones Por unidad")
            }
            // Agrega más campos aquí
            Spacer(modifier = Modifier.padding(30.dp))

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



private fun iniciarCalFinal(
    context: Context,
    matricula: String,
    contrasenia: String,
    navController: NavController,
    viewModel: DataViewModel
) {
    val bodyLogin = loginRequestBody(matricula, contrasenia)
    val service = RetrofitClient(context).retrofitService4
    service.login(bodyLogin).enqueue(object : Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            if (response.isSuccessful) {
                Log.w("exito", "se obtuvo el perfil")
                GetCalFinal(context, navController, viewModel)
            } else {
                showError(
                    context,
                    "Error en la autenticación. Código de respuesta: ${response.code()}"
                )
            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            t.printStackTrace()
            showError(context, "Error en la solicitud")
        }
    })
}

private fun iniciarCalUnidad(
    context: Context,
    matricula: String,
    contrasenia: String,
    navController: NavController,
    viewModel: DataViewModel
) {
    val bodyLogin = loginRequestBody(matricula, contrasenia)
    val service = RetrofitClient(context).retrofitService
    service.login(bodyLogin).enqueue(object : Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            if (response.isSuccessful) {
                Log.w("exito", "se obtuvo el la calificacion o")
                getCalificacionUnidad2(context, navController, viewModel)
            } else {
                showError(
                    context,
                    "Error en la autenticación. Código de respuesta: ${response.code()}"
                )
            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            t.printStackTrace()
            showError(context, "Error en la solicitud")
        }
    })
}

private fun showError(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


