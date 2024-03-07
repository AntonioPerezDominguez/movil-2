package com.example.evaluacion_u1.navigation

sealed class AppScreens (val route: String){
    object loginSice: AppScreens(route = "login_screen")
    object dataScreen: AppScreens(route= "data_screen")
    object kardexScreen: AppScreens(route= "kardex_screen")
    object CalUnidadScreen: AppScreens(route= "calUnidad_screen")
    object CalFinalScreen: AppScreens(route= "calFinal_screen")
    object CargaAcademicaScreen: AppScreens(route= "cargaAcademica_screen")

}