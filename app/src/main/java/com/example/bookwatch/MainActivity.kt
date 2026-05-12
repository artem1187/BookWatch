package com.example.bookwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookwatch.presentation.add.AddIntent
import com.example.bookwatch.presentation.add.AddScreen
import com.example.bookwatch.presentation.add.AddViewModel
import com.example.bookwatch.presentation.detail.DetailScreen
import com.example.bookwatch.presentation.detail.DetailViewModel
import com.example.bookwatch.presentation.main.MainScreen
import com.example.bookwatch.presentation.main.MainViewModel
import com.example.bookwatch.presentation.search.SearchScreen
import com.example.bookwatch.presentation.search.SearchViewModel

import com.example.bookwatch.ui.theme.BookWatchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BookWatchTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BookWatchApp()
                }
            }
        }
    }
}

@Composable
fun BookWatchApp() {
    val navController = rememberNavController()

    val mainViewModel: MainViewModel = hiltViewModel()
    val addViewModel: AddViewModel = hiltViewModel()
    val searchViewModel: SearchViewModel = hiltViewModel()
    val detailViewModel: DetailViewModel = hiltViewModel()

    val mainState by mainViewModel.state.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(
                viewModel = mainViewModel,
                onNavigateToAdd = { navController.navigate("add") },
                onNavigateToDetail = { bookId ->
                    navController.navigate("detail/$bookId")
                }
            )
        }

        composable("add") {
            AddScreen(
                viewModel = addViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSearch = { query ->
                    navController.navigate("search/$query")
                }
            )
        }

        composable("search/{query}") { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""

            SearchScreen(
                viewModel = searchViewModel,
                query = query,
                onNavigateBack = { navController.popBackStack() },
                onBookSelected = { book ->
                    addViewModel.handleIntent(AddIntent.SelectBook(book))
                    navController.popBackStack()
                }
            )
        }

        composable("detail/{bookId}") { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId")?.toIntOrNull() ?: 0

            val book = mainState.books.find { it.id == bookId }

            if (book != null) {
                DetailScreen(
                    viewModel = detailViewModel,
                    book = book,
                    onNavigateBack = { navController.popBackStack() }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            }
        }
    }
}