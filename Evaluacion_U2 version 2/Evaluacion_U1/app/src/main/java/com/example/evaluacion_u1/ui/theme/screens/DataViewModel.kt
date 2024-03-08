package com.example.evaluacion_u1.ui.theme.screens

import androidx.lifecycle.ViewModel
import com.example.evaluacion_u1.model.AllCalificacionFinal
import com.example.evaluacion_u1.model.AlumnoAcademicoResult
import com.example.evaluacion_u1.model.CalPorUnidad
import com.example.evaluacion_u1.model.CalificacionPorUnidad
import com.example.evaluacion_u1.model.CargaAcademicaItem
import com.example.evaluacion_u1.model.KardexItem

class DataViewModel:ViewModel(){
    var alumnoAcademicoResult:AlumnoAcademicoResult?= null
    var AllCalificacionFinal: List<AllCalificacionFinal>?= null
    var CargaAcademicaItem: List<CargaAcademicaItem>?= null
    var CalificacionPorUnidad: List<CalPorUnidad>?= null
    var KardexItem: KardexItem?= null
}