package jpyoon.example.visionfolio.feature.upload

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jpyoon.example.visionfolio.core.analytics.event.LocalEventTracker
import jpyoon.example.visionfolio.designsystem.di.injectedViewModel
import jpyoon.example.visionfolio.domain.model.ScreenshotRef
import jpyoon.example.visionfolio.feature.upload.event.UploadEvents

private const val MaxShots = 20

@Composable
fun UploadRoute(
    onDismiss: () -> Unit,
    onCommit: (addedCount: Int) -> Unit,
    viewModel: UploadViewModel = injectedViewModel(),
) {
    val context = LocalContext.current
    val eventTracker = LocalEventTracker.current
    val dispatcher = remember(viewModel) {
        UploadEventDispatcher(viewModel = viewModel, eventTracker = eventTracker)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val pickLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(maxItems = MaxShots),
    ) { uris ->
        if (uris.isEmpty()) return@rememberLauncherForActivityResult
        val refs = uris.map { it.toScreenshotRef(context.contentResolver) }
        viewModel.dispatch(UploadIntent.AddShots(refs))
    }

    LaunchedEffect(Unit) {
        dispatcher.dispatchEvent(UploadEvents.ViewedUpload)
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                UploadEffect.Close -> onDismiss()
                is UploadEffect.CommittedAdds -> onCommit(effect.count)
                is UploadEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    UploadScreen(
        state = state,
        onIntent = viewModel::dispatch,
        onPickShots = {
            pickLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
            )
        },
        snackbarHostState = snackbarHostState,
    )
}

private fun Uri.toScreenshotRef(resolver: ContentResolver): ScreenshotRef {
    val size = resolver.query(this, arrayOf(OpenableColumns.SIZE), null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            val idx = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (idx >= 0 && !cursor.isNull(idx)) cursor.getLong(idx) else 0L
        } else 0L
    } ?: 0L
    return ScreenshotRef(uri = toString(), sizeBytes = size)
}
