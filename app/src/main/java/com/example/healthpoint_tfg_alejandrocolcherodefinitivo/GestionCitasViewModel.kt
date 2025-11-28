package com.example.healthpoint_tfg_alejandrocolcherodefinitivo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class GestionCitasViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    var citas by mutableStateOf<List<Cita>>(emptyList())
        private set

    fun cargarCitasMedico(idMedico: String) {
        db.collection("Cita")
            .whereEqualTo("Id_medico", idMedico)
            .addSnapshotListener{ value, _ ->
                if(value != null) {
                    citas = value.documents.map { doc ->
                        Cita(
                            Id_Cita = doc.id,
                            Id_usuario = doc.getString("Id_usuario")?: "",
                            Id_medico = idMedico,
                            Id_centroMedico = doc.getString("Id_centroMedico")?: "",
                            fecha = doc.getString("fecha")?: "",
                            hora = doc.getString("hora")?: "",
                            motivo = doc.getString("motivo")?: "",
                            estado = doc.getString("estado")?: "",
                            notasMedico = doc.getString("notasMedico")?: ""
                        )
                    }
                }
            }
    }

    fun crearCita(cita: Cita){
        val ref = db.collection("Cita").document()
        val data= cita.copy(Id_Cita = ref.id)
        ref.set(data)
    }

    fun actualizarCita(id: String, data: Map<String, Any>) {
        db.collection("Cita").document(id).update(data)
    }

    fun eliminarCita(id: String){
        db.collection("Cita").document(id).delete()
    }
}