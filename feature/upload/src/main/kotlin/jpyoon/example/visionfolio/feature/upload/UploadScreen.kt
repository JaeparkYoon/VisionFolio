package jpyoon.example.visionfolio.feature.upload

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.component.VfCard
import jpyoon.example.visionfolio.domain.model.UploadStage
import jpyoon.example.visionfolio.designsystem.foundation.LocalAccent
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography
import androidx.compose.ui.res.stringResource
import jpyoon.example.visionfolio.designsystem.R
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import jpyoon.example.visionfolio.feature.upload.component.ConfidenceLabel
import jpyoon.example.visionfolio.feature.upload.component.ModelDownloadProgress
import jpyoon.example.visionfolio.feature.upload.component.ParsedRowEditorSheet
import jpyoon.example.visionfolio.feature.upload.component.ParsedRowList
import jpyoon.example.visionfolio.feature.upload.component.ShotGrid
import jpyoon.example.visionfolio.feature.upload.component.SparkleSpinner
import jpyoon.example.visionfolio.feature.upload.component.UploadHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(
    state: UploadState,
    onIntent: (UploadIntent) -> Unit,
    onPickShots: () -> Unit = {},
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    modifier: Modifier = Modifier,
) {
    var editingIndex by remember { mutableStateOf<Int?>(null) }
    val editorSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VfColors.BgDefault),
    ) {
        Column(Modifier.fillMaxSize()) {
            UploadHeader(onClose = { onIntent(UploadIntent.Dismiss) })

            when (state.stage) {
                UploadStage.PARSING -> {
                    // PARSING 단계는 외부 Box 의 Alignment.Center 에 절대 배치하기 위해
                    // 흐름 상에서는 빈 공간만 확보한다.
                    Spacer(Modifier.weight(1f))
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp),
                    ) {
                        when (state.stage) {
                            UploadStage.PICK -> PickBody(
                                state = state,
                                onIntent = onIntent,
                                onPickShots = onPickShots,
                            )
                            UploadStage.DOWNLOADING_MODEL -> DownloadingModelBody(
                                progress = state.modelDownloadProgress,
                            )
                            UploadStage.REVIEW -> ReviewBody(
                                state = state,
                                onIntent = onIntent,
                                onEditRow = { editingIndex = it },
                            )
                            UploadStage.DONE -> DoneBody(count = state.selectedCount)
                            else -> Unit
                        }
                    }
                }
            }

            if (state.stage != UploadStage.PARSING) {
                Footer(state = state, onIntent = onIntent)
            }
        }

        // PARSING 콘텐츠는 UploadHeader 높이에 영향받지 않도록 외부 Box 의 정중앙에 오버레이.
        if (state.stage == UploadStage.PARSING) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    stringResource(R.string.upload_parsing_title),
                    style = VfTypography.TitleScreen,
                    color = VfColors.InkPrimary,
                )
                Text(
                    if (state.quotaExceeded) {
                        stringResource(R.string.quota_exceeded_message, state.quotaRetryAttempt, state.quotaMaxRetries)
                    } else {
                        stringResource(R.string.upload_parsing_subtitle)
                    },
                    style = VfTypography.MetaSub,
                    color = VfColors.InkTertiary,
                    modifier = Modifier.padding(top = 4.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
                Spacer(Modifier.height(24.dp))
                SparkleSpinner()
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }

    editingIndex?.let { idx ->
        state.parsed.getOrNull(idx)?.let { row ->
            ParsedRowEditorSheet(
                row = row,
                sheetState = editorSheetState,
                onEdit = { field, value ->
                    onIntent(UploadIntent.EditField(idx, field, value))
                },
                onDismiss = { editingIndex = null },
            )
        }
    }
}

@Composable
private fun PickBody(
    state: UploadState,
    onIntent: (UploadIntent) -> Unit,
    onPickShots: () -> Unit,
) {
    Text(stringResource(R.string.label_screenshots), style = VfTypography.LabelCaps, color = VfColors.InkTertiary)
    Spacer(Modifier.height(8.dp))
    ShotGrid(
        shots = state.shots,
        onAdd = onPickShots,
        onRemove = { onIntent(UploadIntent.RemoveShot(it)) },
    )

    Spacer(Modifier.height(16.dp))
    TipCard()
}

@Composable
private fun DownloadingModelBody(progress: Float) {
    ModelDownloadProgress(progress = progress)
}

@Composable
private fun ReviewBody(
    state: UploadState,
    onIntent: (UploadIntent) -> Unit,
    onEditRow: (Int) -> Unit,
) {
    val avgConfidence = state.parsed.map { it.confidence }.average().takeIf { !it.isNaN() }?.toFloat()
        ?: 0f
    ConfidenceLabel(count = state.parsed.size, confidence = avgConfidence)

    Spacer(Modifier.height(12.dp))
    ParsedRowList(
        parsed = state.parsed,
        onToggle = { onIntent(UploadIntent.TogglePick(it)) },
        onEdit = onEditRow,
    )

    Spacer(Modifier.height(12.dp))
    DashedHelperCard()
}

@Composable
private fun DoneBody(count: Int) {
    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp), contentAlignment = Alignment.Center) {
        Text(
            stringResource(R.string.done_count_message, count),
            style = VfTypography.BodyDefault,
            color = VfColors.InkPrimary,
        )
    }
}

@Composable
private fun TipCard() {
    val accent = LocalAccent.current
    VfCard(
        color = accent.wash,
        border = null,
        padding = PaddingValues(16.dp),
    ) {
        Text(
            stringResource(R.string.tip_upload),
            style = VfTypography.BodyItem,
            color = accent.ink,
        )
    }
}

@Composable
private fun DashedHelperCard() {
    VfCard(
        color = VfColors.BgAlt,
        border = null,
        padding = PaddingValues(16.dp),
    ) {
        Text(
            stringResource(R.string.helper_review),
            style = VfTypography.BodyItem,
            color = VfColors.InkSecondary,
        )
    }
}

@Composable
private fun Footer(
    state: UploadState,
    onIntent: (UploadIntent) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        val secondaryLabel = when (state.stage) {
            UploadStage.PICK -> stringResource(R.string.btn_cancel)
            UploadStage.DOWNLOADING_MODEL, UploadStage.PARSING -> stringResource(R.string.footer_background)
            UploadStage.REVIEW -> stringResource(R.string.footer_retake)
            UploadStage.DONE -> stringResource(R.string.btn_close)
        }
        val primaryLabel = when (state.stage) {
            UploadStage.PICK -> stringResource(R.string.footer_analyze)
            UploadStage.DOWNLOADING_MODEL, UploadStage.PARSING -> stringResource(R.string.footer_waiting)
            UploadStage.REVIEW -> stringResource(R.string.footer_add_count, state.selectedCount)
            UploadStage.DONE -> stringResource(R.string.btn_done)
        }
        val primaryEnabled = when (state.stage) {
            UploadStage.PICK -> state.canStartParse
            UploadStage.REVIEW -> state.selectedCount > 0
            UploadStage.DOWNLOADING_MODEL, UploadStage.PARSING, UploadStage.DONE -> false
        }

        if (state.stage != UploadStage.DOWNLOADING_MODEL && state.stage != UploadStage.PARSING) {
            OutlinedButton(
                onClick = {
                    when (state.stage) {
                        UploadStage.PICK, UploadStage.DONE -> onIntent(UploadIntent.Dismiss)
                        UploadStage.DOWNLOADING_MODEL, UploadStage.PARSING -> Unit
                        UploadStage.REVIEW -> onIntent(UploadIntent.BackToPick)
                    }
                },
                modifier = Modifier.weight(1f),
            ) {
                Text(secondaryLabel, color = VfColors.InkSecondary)
            }
        }
        Button(
            onClick = {
                when (state.stage) {
                    UploadStage.PICK -> onIntent(UploadIntent.StartParse)
                    UploadStage.REVIEW -> onIntent(UploadIntent.ConfirmAdd)
                    UploadStage.DOWNLOADING_MODEL, UploadStage.PARSING, UploadStage.DONE -> Unit
                }
            },
            enabled = primaryEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = VfColors.InkPrimary,
                contentColor = VfColors.BgDefault,
                disabledContainerColor = VfColors.InkMuted,
            ),
            modifier = Modifier.weight(1f),
        ) {
            Text(primaryLabel)
        }
    }
}
