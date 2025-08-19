package br.senai.sp.jandira.clientesapp.screens

import android.util.Patterns
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.senai.sp.jandira.clientesapp.model.Cliente
import br.senai.sp.jandira.clientesapp.service.RetrofitFactory
import br.senai.sp.jandira.clientesapp.ui.theme.ClientesAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.await

@Composable
fun FormCliente(modifier: Modifier = Modifier) {

    var nomeCliente by remember {
        mutableStateOf(value = "")
    }

    var emailCliente by remember {
        mutableStateOf(value = "")
    }

    //variaveis de estado para validar a entrada do usuario

    var isNomeError by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }

    fun validar(): Boolean {
        isNomeError = nomeCliente.length < 1
        isEmailError = !Patterns.EMAIL_ADDRESS.matcher(emailCliente).matches()
        return !isNomeError && !isEmailError
    }

    //Crair uma intancia do RetrofitFactory
    val clienteApi = RetrofitFactory().getClienteService()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        OutlinedTextField(
            value = nomeCliente,
            onValueChange = { nome ->
                nomeCliente = nome
            },
            label = {
                Text(text = "Nome do Cliente")
            },
            //texto de ajuda
            supportingText = {
                if (isEmailError) {
                Text(text = "Nome do cliente é obrigatório")
                    }
            },
            trailingIcon = {
                if (isNomeError){
                Icon(imageVector = Icons.Default.Info, contentDescription = "erro")
            }
            },
            isError = isNomeError,
            modifier = Modifier
                .fillMaxWidth()
        )
        OutlinedTextField(
            value = emailCliente,
            onValueChange = { email ->
                emailCliente = email
            },
            label = {
                Text(text = "E-mail do Cliente")
            },
            //texto de ajuda
            supportingText = {
                if (isEmailError) {
                    Text(text = "Email do cliente é obrigatório")
                }
            },
            trailingIcon = {
                if (isEmailError){
                    Icon(imageVector = Icons.Default.Info, contentDescription = "erro")
            }
            },

            isError = isEmailError,
            modifier = Modifier
                .fillMaxWidth()
        )
        Button(
            onClick = {
                //Criar Cliente com os dados informados
                if (validar()) {
                    val cliente = Cliente(
                        nome = nomeCliente,
                        email = emailCliente
                    )

                    //Requisicao POST para a API
                    GlobalScope.launch(Dispatchers.IO) {
                        val novoCliente = clienteApi.gravar(cliente).await()
                        println(novoCliente)
                    }
                    } else {
                        print(" ******** os dados estao incorretos ")
                }
            },
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Salvar Cliente")
        }
    }
}



@Preview
@Composable
private fun FormClientePreview() {
    ClientesAppTheme {
        FormCliente()
    }
}