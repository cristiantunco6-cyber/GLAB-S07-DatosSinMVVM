package com.example.datossinmvvm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import kotlinx.coroutines.launch

val FondoUmbra = Color(0xFF101512)
val TarjetaUmbra = Color(0xFF1B211D)
val TextoUmbra = Color(0xFFE1E9E0)
val DescripcionUmbra = Color(0xFFB8C9BE)
val MentaUmbra = Color(0xFF8EE5C2)
val AmbarUmbra = Color(0xFFEBC078)
val CoralUmbra = Color(0xFFFFB4AB)

@Composable
fun ScreenMapa() {
    val context = LocalContext.current
    val db = remember {
        Room.databaseBuilder(context, UmbraDatabase::class.java, "umbra_db").build()
    }
    val dao = db.mapaDao()
    val coroutineScope = rememberCoroutineScope()

    var mapas by remember { mutableStateOf(listOf<Mapa>()) }
    var idEditando by remember { mutableStateOf(0) }
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var niveles by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        mapas = dao.getAll()
    }

    val colores = TextFieldDefaults.colors(
        focusedContainerColor = TarjetaUmbra,
        unfocusedContainerColor = TarjetaUmbra,
        focusedTextColor = TextoUmbra,
        unfocusedTextColor = TextoUmbra,
        focusedLabelColor = MentaUmbra,
        unfocusedLabelColor = DescripcionUmbra
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoUmbra)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Text(
            text = "Mapas",
            color = TextoUmbra,
            fontSize = 32.sp
        )
        Spacer(Modifier.height(12.dp))
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            singleLine = true,
            colors = colores,
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            singleLine = true,
            colors = colores,
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = niveles,
            onValueChange = { niveles = it },
            label = { Text("Niveles") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = colores,
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = precio,
            onValueChange = { precio = it },
            label = { Text("Precio en monedas (0 = Gratis)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = colores,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                val n = niveles.toIntOrNull() ?: 0
                val p = precio.toIntOrNull() ?: 0
                coroutineScope.launch {
                    if (idEditando == 0) {
                        dao.insert(Mapa(nombre = nombre, descripcion = descripcion, niveles = n, precio = p))
                    } else {
                        dao.update(Mapa(idEditando, nombre, descripcion, n, p))
                    }
                    mapas = dao.getAll()
                }
                idEditando = 0
                nombre = ""
                descripcion = ""
                niveles = ""
                precio = ""
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MentaUmbra,
                contentColor = FondoUmbra
            )
        ) {
            Text(if (idEditando == 0) "Agregar Mapa" else "Actualizar Mapa", fontSize = 16.sp)
        }
        Spacer(Modifier.height(8.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(mapas) { mapa ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = TarjetaUmbra),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(mapa.nombre, color = TextoUmbra, fontSize = 20.sp)
                        Text(mapa.descripcion, color = DescripcionUmbra)
                        Text("${mapa.niveles} niveles", color = DescripcionUmbra)
                        Text(
                            text = if (mapa.precio == 0) "Gratis" else "${mapa.precio} monedas",
                            color = AmbarUmbra
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    idEditando = mapa.id
                                    nombre = mapa.nombre
                                    descripcion = mapa.descripcion
                                    niveles = mapa.niveles.toString()
                                    precio = mapa.precio.toString()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MentaUmbra,
                                    contentColor = FondoUmbra
                                )
                            ) {
                                Text("Editar")
                            }
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        dao.delete(mapa)
                                        mapas = dao.getAll()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CoralUmbra,
                                    contentColor = FondoUmbra
                                )
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }
}