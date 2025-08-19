package br.senai.sp.jandira.clientesapp.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.senai.sp.jandira.clientesapp.R
import br.senai.sp.jandira.clientesapp.model.Cliente
import br.senai.sp.jandira.clientesapp.service.ClienteService
import br.senai.sp.jandira.clientesapp.service.RetrofitFactory
import br.senai.sp.jandira.clientesapp.ui.theme.ClientesAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.await

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    var navController = rememberNavController()

    Scaffold(
        topBar = {
            BarraDeTitulo() //desenhamos abaixo
        },
        bottomBar = {
            BarraDeNavegacao(navController) //desenhamos abaixo
        },
        floatingActionButton = {
            BotaoFlutuante(navController)
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background) //cor do fundo
            ) {
                NavHost(
                    navController = navController,
                    startDestination = "Home"
                ) {
                    composable(route = "Home") { TelaHome(paddingValues) }
                    composable(route = "Form") { FormCliente(navController) }
                }
            }
        })
}
//-------------------------------------
@Composable
fun TelaHome(paddingValues: PaddingValues) {

    //Criar uma instancia de RetroFitFactory
    val clienteApi = RetrofitFactory().getClienteService()

    //Criar uma variavel de estado para armazenar a lista de clientes da Api
    var clientes by remember {
        mutableStateOf(listOf<Cliente>())
    }

    //Executa assim que executar a funcao
    LaunchedEffect(Dispatchers.IO) {       // espera a lista voltar
        clientes = clienteApi.exibirTodos().await()
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
    ){
        Row( //conteudo
            modifier = Modifier
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AccountBox,
                contentDescription = "Icone da Lista de Clientes",
                tint = MaterialTheme.colorScheme.onBackground
            )
            Spacer(
                modifier = Modifier
                    .width(8.dp)
            )
            Text(text = "Lista de Clientes")
        }
        LazyColumn(
            contentPadding = PaddingValues(
                bottom = 88.dp)
        ) {
            items(clientes){ cliente ->
                ClienteCard(cliente, clienteApi)
            }
        }
    }
}
//-------------------------------------
@Composable
fun ClienteCard(cliente: Cliente, clienteApi: ClienteService?) {

    var mostrarConfirmacaoDeExclusao by remember {
        mutableStateOf(false)
    }
    if (mostrarConfirmacaoDeExclusao){
        AlertDialog(
            onDismissRequest = {
                mostrarConfirmacaoDeExclusao = false
            },
            title = { Text(text = "Excluir") },
            text = { Text(text = "Confirma a Exclusao do cliente ${cliente.nome}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        GlobalScope.launch(Dispatchers.IO) {
                            clienteApi!!.excluir(cliente).await()
                        }
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacaoDeExclusao = false
                    }
                ) { Text("cancelar")
                }
            }
        )
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(
                start = 8.dp,
                end = 8.dp,
                bottom = 4.dp
            ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary) // cor do card
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {    //passa o nome
                Text(text = cliente.nome,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )       //passa o email
                Text(text = cliente.email,
                    fontSize = 12.sp
                )
            }
            IconButton(
                onClick = {
                    mostrarConfirmacaoDeExclusao = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Excluir"
                )
            }
        }
    }
}
//-------------------------------------
@Preview
@Composable
private fun ClienteCardPreview() {
    ClientesAppTheme {
        ClienteCard(Cliente(), null)
    }
}
//-------------------------------------
@OptIn(ExperimentalMaterial3Api::class) //importa do TopAppBar
@Composable
fun BarraDeTitulo(modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        colors = TopAppBarDefaults
            .topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
        title = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, //espacar itens
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Lara Horacio",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimary //cor para o texto
                    )
                    Text(
                        text = "lara@email.com",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimary //cor para o texto
                    )
                }
                Card(
                    modifier = Modifier
                        .size(60.dp)
                        .padding(4.dp),
                    shape = CircleShape
                ){
                    Image(
                        painter = painterResource(R.drawable.medico),
                        contentDescription = "Foto de Perfil"
                    )
                }
            }
        }
    )
}
//-------------------------------------
@Composable // deixa nulo usando ?
fun BarraDeNavegacao(navController: NavController?) { //barra inferior
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary, //mudar a cor
        contentColor = MaterialTheme.colorScheme.primary //mudar a cor
    ) { //composta por itens de barra de navigacao
        NavigationBarItem(
            selected = false,
            onClick = { // quando clica no icone home, volta para tela home
                navController!!.navigate(route = "Home")
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = MaterialTheme.colorScheme.onPrimary //mudar a cor do icon
                )
            },
            label = { //texto para o icon
                Text(text = "Home",
                    color = MaterialTheme.colorScheme.onPrimary) //mudar a cor do texto
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = {

            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            },
            label = { //texto para o icon
                Text(text = "Favorite",
                    color = MaterialTheme.colorScheme.onPrimary)
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = {

            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            },
            label = { //texto para o icon
                Text(text = "Menu",
                    color = MaterialTheme.colorScheme.onPrimary)
            }
        )
    }
}
//-------------------------------------
@Composable
fun BotaoFlutuante(navController: NavController?) {
    FloatingActionButton(
        onClick = {
            //pode ser nulo mas nao tem problema // !!
            navController!!.navigate(route = "Form")
        },
        containerColor = MaterialTheme.colorScheme.tertiary // cor do botao flutuante
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Botão Adicionar",
            tint = MaterialTheme.colorScheme.onTertiary
        )
    }
}
//-------------------------------------
@Preview
@Composable
private fun BotaoFlutuantePreview() {
    ClientesAppTheme {
        BotaoFlutuante(navController = null)
    }
}
//-------------------------------------
@Preview
@Composable
private fun BarraDeNavegacaoPreview() {
    ClientesAppTheme {
        BarraDeNavegacao(navController = null)
    }
}
//-------------------------------------
@Preview
@Composable
private fun BarraDeTituloPreview() {
    ClientesAppTheme {
        BarraDeTitulo()
    }
}
//-------------------------------------
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES) //configuracoes para temas
@Composable
private fun HomeScreenPreview() {
    ClientesAppTheme {
        HomeScreen()
    }
}