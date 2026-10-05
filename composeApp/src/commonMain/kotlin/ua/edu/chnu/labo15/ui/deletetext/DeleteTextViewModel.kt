package ua.edu.chnu.labo15.ui.deletetext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.edu.chnu.labo15.data.NetworkResult
import ua.edu.chnu.labo15.data.PostRepository

/** UI state for the Lab 15 "DELETE as plain text" screen. */
data class DeleteTextUiState(
    val isLoading: Boolean = false,
    val text: String = "",
    val errorText: String = "",
)

/**
 * Lab 15 ViewModel: sends a single DELETE request and exposes the response
 * (HTTP status + body) as plain text. It reuses [PostRepository], which wraps
 * every call in a [NetworkResult] so success and failure are handled uniformly.
 */
class DeleteTextViewModel(
    private val repository: PostRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(DeleteTextUiState())
    val state: StateFlow<DeleteTextUiState> = _state.asStateFlow()

    /** Performs DELETE /posts/{id} and stores the raw response text in the UI state. */
    fun send() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, text = "", errorText = "") }
            when (val result = repository.deletePostText(id = POST_ID)) {
                is NetworkResult.Success ->
                    _state.update { it.copy(isLoading = false, text = result.data) }
                is NetworkResult.Error ->
                    _state.update { it.copy(isLoading = false, errorText = result.message) }
            }
        }
    }

    companion object {
        /** Id of the post deleted by this screen. */
        const val POST_ID = 1
    }
}
