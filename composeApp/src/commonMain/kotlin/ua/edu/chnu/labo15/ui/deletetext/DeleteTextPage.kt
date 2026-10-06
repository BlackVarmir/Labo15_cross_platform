package ua.edu.chnu.labo15.ui.deletetext

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import ua.edu.chnu.labo15.data.Post

/**
 * Lab 15 screen.
 *
 * Step 1: the post that will be deleted is loaded with GET and shown on screen.
 * Step 2: the user presses the button, a DELETE request is sent, and the server
 *         response (HTTP status + body) is shown on screen as plain text.
 */
@Composable
fun DeleteTextPage(
    onUpButtonClick: () -> Unit,
    viewModel: DeleteTextViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val id = DeleteTextViewModel.POST_ID

    Column(modifier = Modifier.fillMaxSize()) {
        Toolbar(onUpButtonClick = onUpButtonClick)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            // ---- Step 1: what will be deleted ----
            SectionTitle("Крок 1. Пост, який буде видалено")
            Hint("GET https://jsonplaceholder.typicode.com/posts/$id")

            if (state.isLoadingPost) Progress("Завантаження поста...")
            if (state.postError.isNotBlank()) ErrorCard("Не вдалося завантажити пост: ${state.postError}")
            state.post?.let { PostCard(post = it, deleted = state.isDeleted) }

            // ---- Step 2: the DELETE call ----
            SectionTitle("Крок 2. DELETE-запит", top = 24.dp)
            Hint("DELETE https://jsonplaceholder.typicode.com/posts/$id")

            Button(
                onClick = viewModel::delete,
                enabled = state.post != null && !state.isDeleting && !state.isDeleted,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                ),
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            ) {
                Text(if (state.isDeleted) "Пост №$id видалено" else "Видалити пост №$id")
            }

            if (state.isDeleting) Progress("Надсилаємо DELETE...")
            if (state.deleteError.isNotBlank()) ErrorCard("Помилка видалення: ${state.deleteError}")

            // ---- Result as plain text ----
            if (state.resultText.isNotBlank()) {
                SuccessCard("Сервер підтвердив видалення поста №$id")
                SectionTitle("Відповідь сервера (звичайний текст)", top = 16.dp)
                Text(
                    text = state.resultText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                )
                Text(
                    text = "Примітка: jsonplaceholder - тестовий сервер. Він відповідає так, " +
                        "ніби пост видалено (200 OK, порожнє тіло {}), але насправді дані " +
                        "не змінює, тому після «Почати знову» пост завантажиться знову.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 12.dp),
                )
            }

            OutlinedButton(
                onClick = viewModel::loadPost,
                enabled = !state.isLoadingPost && !state.isDeleting,
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            ) {
                Text("Почати знову")
            }
        }
    }
}

@Composable
private fun PostCard(post: Post, deleted: Boolean) {
    val strike = if (deleted) TextDecoration.LineThrough else TextDecoration.None
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .alpha(if (deleted) 0.5f else 1f),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "id = ${post.id}, userId = ${post.userId}" + if (deleted) "   (видалено)" else "",
                style = MaterialTheme.typography.labelMedium,
            )
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                textDecoration = strike,
                modifier = Modifier.padding(top = 6.dp),
            )
            Text(
                text = post.body,
                style = MaterialTheme.typography.bodyMedium,
                textDecoration = strike,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

@Composable
private fun SuccessCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1B5E20),
            contentColor = Color.White,
        ),
    ) {
        Text(
            text = "✓ $message",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(12.dp),
        )
    }
}

@Composable
private fun ErrorCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
        ),
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(12.dp))
    }
}

@Composable
private fun SectionTitle(text: String, top: androidx.compose.ui.unit.Dp = 0.dp) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = top),
    )
}

@Composable
private fun Hint(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        fontFamily = FontFamily.Monospace,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 2.dp),
    )
}

@Composable
private fun Progress(label: String) {
    Row(
        modifier = Modifier.fillMaxWidth().height(48.dp).padding(top = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircularProgressIndicator(modifier = Modifier.height(28.dp))
        Text(text = label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(start = 12.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Toolbar(onUpButtonClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = "DELETE as text") },
        navigationIcon = {
            IconButton(onClick = onUpButtonClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
        },
    )
}
