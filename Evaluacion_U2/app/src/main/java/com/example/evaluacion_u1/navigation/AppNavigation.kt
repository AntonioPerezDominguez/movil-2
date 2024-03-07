package com.example.evaluacion_u1.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.evaluacion_u1.ui.theme.screens.DataViewModel
import com.example.evaluacion_u1.ui.theme.screens.MostrarCalFinal
import com.example.evaluacion_u1.ui.theme.screens.MostrarCalUnidad
import com.example.evaluacion_u1.ui.theme.screens.MostrarCargaAcademica
import com.example.evaluacion_u1.ui.theme.screens.MostrarKardex
import com.example.evaluacion_u1.ui.theme.screens.PantallaPrincipal
import com.example.evaluacion_u1.ui.theme.screens.mostrarDatos

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    val viewModel = DataViewModel()
    NavHost(navController = navController, startDestination = "login"){
        composable(route ="login"){ PantallaPrincipal(navController = navController, viewModel = viewModel) }
        composable(route="data"){ mostrarDatos(navController = navController, viewModel= viewModel)}
        composable(route="kardex"){ MostrarKardex(navController = navController, viewModel= viewModel)}
        composable(route="CalUnidad"){ MostrarCalUnidad(navController = navController, viewModel= viewModel)}
        composable(route="CalFinal"){ MostrarCalFinal(navController = navController, viewModel= viewModel) }
        composable(route="CargaAcademica"){ MostrarCargaAcademica(navController = navController, viewModel= viewModel)}
    }

}